import time
import threading
import Pyro5.api
from utils import now, start_ns
from enum import Enum

class PeerState(Enum):
    RELEASED = 1
    WANTED = 2
    HELD = 3

class Peer:
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
        self.active_peers = {}  
        self.request_queue = []
        self.waiting_replies = set()
        self.state = PeerState.RELEASED
        self.timestamp = 0

        ns = start_ns

        self.daemon = Pyro5.api.Daemon()
        self.uri = self.daemon.register(self)
        ns().register(self.name, self.uri)

        threading.Thread(target=self.daemon.requestLoop, daemon=True).start()
        threading.Thread(target=self.send_heartbeats, daemon=True).start()
        threading.Thread(target=self.check_heartbeats, daemon=True).start()

    """
    Realiza o algoritmo de Ricati-Agrawala para entrar na seção crítica.

    :params: None
    :return: None
    """
    def request_critical_section(self):
        self.state = PeerState.WANTED
        self.timestamp = now()
        self.waiting_replies = set(self.active_peers.keys())
        print(f"[{self.name}] Requisitando seção crítica com timestamp {self.timestamp}.")

        for peer in self.active_peers.keys():
            try:
                proxy = Pyro5.api.Proxy(f"PYRONAME:{peer}")
                proxy.reply(self.name, self.timestamp)
            except:
                print(f"[{self.name}] Não foi possível enviar request para {peer}.")

        while self.waiting_replies:
            time.sleep(0.1)

        self.state = PeerState.HELD
        print(f"[{self.name}] Entrou na seção crítica.")
        # Provavelmente esperar um certo tempo aqui para simular o uso da seção crítica

    """
    Realiza o algoritmo de Ricati-Agrawala para responder pedidos da seção crítica.

    :params: None
    :return: None
    """
    
    """
    Monitora os heartbeats recebidos dos outros peers.
    Se um peer não enviar um heartbeat dentro de um intervalo
    de tempo, ele é removido da lista de peers ativos.

    :params: None
    :return: None
    """
    def check_heartbeats(self):
        while True:
            current_time = now()
            to_remove = []
            for peer, last_heartbeat in self.active_peers.items():
                if current_time - last_heartbeat > 5000: 
                    to_remove.append(peer)
            for peer in to_remove:
                print(f"[{self.name}] Peer {peer} removido por timeout.")
                del self.active_peers[peer]
            time.sleep(1)

    """
    Envia heartbeats para todos os peers ativos a cada segundo.
    
    :params: None
    :return: None
    """
    def send_heartbeats(self):
        while True:
            for peer in list(self.active_peers.keys()):
                try:
                    proxy = Pyro5.api.Proxy(f"PYRONAME:{peer}")
                    proxy.heartbeat(self.name)
                except:
                    print(f"[{self.name}] Não foi possível enviar heartbeat para {peer}.")
            time.sleep(1)

    """
    Recebe um heartbeat de outro peer e atualiza seu timestamp.

    :param peer: O nome do peer que enviou o heartbeat.
    :return: None
    """
    def listen_heartbeat(self, peer):
        self.active_peers[peer] = now()
        print(f"[{self.name}] Heartbeat recebido de {peer}.")