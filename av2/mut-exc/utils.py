import argparse
import time
import Pyro5.api

def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("--name", type=str, required=True, help="Nome do peer (ex: PeerA)")
    return parser.parse_args()

def start_ns():
    try:
        return Pyro5.api.locate_ns()
    except:
        print("[UTILS] Nenhum NameServer encontrado. Rode 'python nameserver.py' em outro terminal.")
        raise SystemExit

def now():
    """Retorna timestamp atual (ms)."""
    return int(time.time() * 1000)