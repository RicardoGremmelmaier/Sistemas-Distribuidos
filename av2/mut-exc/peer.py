import time
import Pyro5.api
import threading
from utils import now, start_ns
from enum import Enum
from apscheduler.schedulers.background import BackgroundScheduler

class PeerState(Enum):
    RELEASED = 1
    WANTED = 2
    HELD = 3

class Peer:
    ## ================================== Inicialização ================================== ##
    """
    Inicializa um Peer na rede Pyro.
    Esse peer deve conter um nome, um dicionário de peers ativos com seu ultimo heartbeat,
    uma fila de requisições, um conjunto de replies pendentes,
    um estado (RELEASED, WANTED, HELD) e um timestamp.

    Além disso, deve conter duas threads, uma para enviar heartbeats e
    outra para monitorar os heartbeats recebidos.

    :param name: Nome do peer.
    """
    def __init__(self, name):
        self.name = name
        self.request_queue = []
        self.waiting_replies = set()
        self.state = PeerState.RELEASED
        self.timestamp = 0
        self.stop_critical = False
        self.lock = threading.Lock()
        self.scheduler = BackgroundScheduler()

        ns = start_ns()

        self.daemon = Pyro5.api.Daemon()
        self.uri = self.daemon.register(self)
        ns.register(self.name, self.uri)
        all_peers = ns.list()
        self.active_peers = {
            name: {"uri": uri, "last_heartbeat": now()}
            for name, uri in all_peers.items()
            if name not in {self.name, "Pyro.NameServer"}
        }

        threading.Thread(target=self.daemon.requestLoop, daemon=True).start()
        threading.Thread(target=self.send_heartbeat, daemon=True).start()
        threading.Thread(target=self.check_heartbeats, daemon=True).start()



    ## ============================ Algoritmo de Ricati-Agrawala ============================ ##
    """
    Realiza o algoritmo de Ricati-Agrawala para entrar na seção crítica.

    :params: None
    :return: None
    """
    def request_critical_section(self):
        self.state = PeerState.WANTED
        self.timestamp = now()
        self.waiting_replies = set(self.active_peers.keys())
        print(f"\n[{self.name}] Requisitando seção crítica com timestamp {self.timestamp}.")

        for peer in list(self.waiting_replies):
            try:
                proxy = Pyro5.api.Proxy(f"PYRONAME:{peer}")
                granted = proxy.receive_request(self.name, self.timestamp)

                if granted:
                    self.waiting_replies.discard(peer)
                    print(f"\n[{self.name}] Recebeu permissão de {peer}.")
                else:
                    print(f"\n[{self.name}] Permissão negada por {peer}.")
            except:
                print(f"\n[{self.name}] Não foi possível enviar request para {peer}.")
                with self.lock:
                    if peer in self.active_peers:
                        del self.active_peers[peer]
                self.waiting_replies.discard(peer)

        while self.waiting_replies:
            time.sleep(0.1)

        self.state = PeerState.HELD
        print(f"\n[{self.name}] Entrou na seção crítica.")

        self.use_critical_section()

    """
    Simula uso da seção crítica por um tempo.

    :params: None
    :return: None
    """
    def use_critical_section(self):
        print(f"[{self.name}] Executando na seção crítica (pressione 2 no menu para sair).")
        for _ in range(100):  # ~10s (100 * 0.1)
            if self.stop_critical:
                print(f"\n[{self.name}] Seção crítica interrompida manualmente.")
                break
            time.sleep(0.1)
        self.release_critical_section()

    """
    Realiza o algoritmo de Ricati-Agrawala para sair da seção crítica.

    :params: None
    :return: None
    """
    def release_critical_section(self):
        self.state = PeerState.RELEASED
        print(f"\n[{self.name}] Saindo da seção crítica.")

        while self.request_queue:
            peer, timestamp = self.request_queue.pop(0)
            try:
                proxy = Pyro5.api.Proxy(f"PYRONAME:{peer}")
                proxy.receive_release(self.name)
                print(f"\n[{self.name}] Enviou release para {peer} da fila.")
            except:
                print(f"\n[{self.name}] Não foi possível enviar release para {peer} da fila.")

    """
    Realiza o algoritmo de Ricati-Agrawala para responder pedidos da seção crítica.

    :params: None
    :return: None
    """
    @Pyro5.api.expose
    def receive_request(self, peer, timestamp):
        if self.state == PeerState.HELD or (self.state == PeerState.WANTED and (self.timestamp, self.name) < (timestamp, peer)):
            self.request_queue.append((peer, timestamp))
            print(f"\n[{self.name}] Adicionou {peer} à fila de requisições.")
            return False
        else:
            with self.lock:
                if peer in self.active_peers:
                    print(f"\n[{self.name}] Concedeu permissão para {peer}.")
                    return True
                else:
                    print(f"\n[{self.name}] Peer {peer} não está mais ativo.")
                    return False

    """
    Recebe a liberação da seção crítica de outro peer.

    :param peer: O nome do peer que está liberando a seção crítica.
    :return: None
    """
    @Pyro5.api.expose
    def receive_release(self, peer):
        if peer in self.waiting_replies:
            self.waiting_replies.remove(peer)
            print(f"\n[{self.name}] Recebeu release de {peer}.")

    
    ## ============================ Monitoramento de Heartbeats ============================ ##
    """
    Monitora os heartbeats recebidos dos outros peers.
    Se um peer não enviar um heartbeat dentro de um intervalo
    de tempo, ele é removido da lista de peers ativos.

    :params: None
    :return: None
    """
    def check_heartbeats(self):
        time.sleep(10) 
        while True:
            current_time = now()
            to_remove = []

            with self.lock:
                for peer, data in self.active_peers.items():
                    if current_time - data["last_heartbeat"] > 16000:
                        to_remove.append(peer)
                for peer in to_remove:
                    del self.active_peers[peer]
                    if peer in self.waiting_replies:
                        self.waiting_replies.remove(peer)

            time.sleep(5)
    """
    Envia heartbeats para todos os peers ativos a cada segundo.
    
    :params: None
    :return: None
    """
    def send_heartbeat(self):
        while True:
            with self.lock:
                peers = list(self.active_peers.items())
            for peer, data in peers:
                try:
                    proxy = Pyro5.api.Proxy(data["uri"])
                    proxy.listen_heartbeat(self.name)
                except Exception:
                    pass
            time.sleep(1)

    """
    Recebe um heartbeat de outro peer e atualiza seu timestamp.

    :param peer: O nome do peer que enviou o heartbeat.
    :return: None
    """
    @Pyro5.api.expose
    def listen_heartbeat(self, peer):
        with self.lock:
            if peer in self.active_peers:
                self.active_peers[peer]["last_heartbeat"] = now()
            else:
                self.active_peers[peer] = {
                    "uri": f"PYRONAME:{peer}",
                    "last_heartbeat": now()
                }
                print(f"\n[{self.name}] Novo peer {peer} adicionado à lista de ativos.")
