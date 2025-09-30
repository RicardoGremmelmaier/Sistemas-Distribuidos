import sys
import Pyro5.api
from peer import Peer
from utils import start_ns, parse_args

def main():
    args = parse_args()
    peer_name = args.name

    ns = start_ns()

    daemon = Pyro5.server.Daemon()
    uri = daemon.register(Peer(peer_name))
    ns.register(peer_name, uri)

    print(f"[MAIN] Peer {peer_name} registrado no NameServer.")

    daemon.requestLoop()

if __name__ == "__main__":
    main()