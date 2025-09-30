import sys
from unittest import case
import Pyro5.api
from peer import Peer
from utils import parse_args

def run(peer):
    print("[MAIN] Pressione Ctrl+C para sair.")
    print("1. Requisitar recurso")
    print("2. Liberar recurso")
    print("3. Listar peers ativos")
    
    escolha = input("Escolha uma opção:")

    if escolha == "1":
        peer.request_critical_section()
    elif escolha == "2":
        peer.release_critical_section()
    elif escolha == "3":
        print(f"[MAIN] Peers ativos: {list(peer.active_peers.keys())}")
    else:
        print("[MAIN] Opção inválida.")

def main():
    args = parse_args()
    peer_name = args.name

    peer = Peer(peer_name)

    print(f"[MAIN] Peer {peer_name} registrado no NameServer.")
    run(peer)

if __name__ == "__main__":
    main()