import sys
import threading
from peer import Peer
from utils import parse_args


def run(peer):
    """Loop do menu CLI"""
    while True:
        try:
            print("\n1. Requisitar recurso")
            print("2. Liberar recurso")
            print("3. Listar peers ativos")
            print("0. Sair")

            escolha = input("Escolha uma opção: ")

            if escolha == "1":
                peer.stop_critical = False
                threading.Thread(target=peer.request_critical_section, daemon=True).start()

            elif escolha == "2":
                peer.stop_critical = True

            elif escolha == "3":
                print(f"\n[MAIN] Peers ativos: {list(peer.active_peers.keys())}")

            elif escolha == "0":
                print(f"\n[MAIN] Encerrando peer {peer.name}...")
                sys.exit(0)

            else:
                print("\n[MAIN] Opção inválida.")

        except KeyboardInterrupt:
            print(f"\n[MAIN] Encerrando peer {peer.name}.")
            sys.exit(0)


def main():
    args = parse_args()
    peer_name = args.name
    peer = Peer(peer_name)
    print(f"[MAIN] Peer {peer_name} registrado no NameServer.")

    threading.Thread(target=run, args=(peer,), daemon=False).start()


if __name__ == "__main__":
    main()
