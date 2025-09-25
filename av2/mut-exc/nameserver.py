import Pyro5.api
import Pyro5.nameserver

def main():
    try:
        # tenta localizar
        Pyro5.api.locate_ns()
        print("[NameServer] Já existe um servidor em execução.")
    except:
        print("[NameServer] Nenhum servidor encontrado, criando um novo...")
        Pyro5.nameserver.start_ns_loop()

if __name__ == "__main__":
    main()
