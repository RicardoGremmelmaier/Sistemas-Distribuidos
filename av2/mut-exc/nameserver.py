import Pyro5.api
import Pyro5.nameserver

def main():
    """
    Função principal que verifica se um servidor de nomes Pyro está em execução.
    Se não estiver, inicia um novo servidor de nomes.
    Deve ser inicializado antes de inicializar os Peers.

    :params: None
    :return: None
    """
    try:
        Pyro5.api.locate_ns()
        print("[NameServer] Já existe um servidor em execução.")
    except:
        print("[NameServer] Nenhum servidor encontrado, criando um novo...")
        Pyro5.nameserver.start_ns_loop()

if __name__ == "__main__":
    main()
