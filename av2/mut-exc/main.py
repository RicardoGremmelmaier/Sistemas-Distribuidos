import sys
from unittest import case
import Pyro5.api
from peer import Peer
from utils import parse_args

def run(peer):
    print("1. Requisitar recurso")
    print("2. Liberar recurso")
    print("3. Listar peers ativos")
    print("0. Sair")
    
    escolha = input("Escolha uma opção:")

    if escolha == "1":
        peer.request_critical_section()
    elif escolha == "2":
        peer.release_critical_section()
    elif escolha == "3":
        print(f"[MAIN] Peers ativos: {list(peer.active_peers.keys())}")
    elif escolha == "0":
        print("Saindo...")
        exit(0)
    else:
        print("[MAIN] Opção inválida.")

def main():
    args = parse_args()
    peer_name = args.name
    peer = Peer(peer_name)

    print(f"[MAIN] Peer {peer_name} registrado no NameServer.")
    
    while(1):
        try:
            run(peer)
        except KeyboardInterrupt as e:
            print(f"\n[MAIN] Encerrando peer {peer_name}.")
            sys.exit(0)

if __name__ == "__main__":
    main()