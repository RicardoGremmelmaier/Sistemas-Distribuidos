package com.mom.user;

import com.rabbitmq.client.*;
import com.mom.util.Lance;
import java.io.*;
import java.security.*;

public class Cliente {
    private final int cliente_id;
    private final PublicKey chave_publica;
    private final PrivateKey chave_privada;
    private Signature assinador;

    private static int id_counter = 1;

    public Cliente() {
        this.cliente_id = id_counter++;

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(2048, random);
            KeyPair pair = keyGen.generateKeyPair();
            this.chave_publica = pair.getPublic();
            this.chave_privada = pair.getPrivate();

            // Debug para criação de cliente, apagar após teste
            System.out.println("Cliente criado com ID: " + cliente_id);
            System.out.println("Chave pública: " + chave_publica.getEncoded());
            System.out.println("Chave privada: " + chave_privada.getEncoded());
            // Debug para criação de cliente, apagar após teste
        }
        catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar o cliente", e);
        }
    }

    public int getClienteId() {
        return cliente_id;
    }

    public void salvarChavePublica(){
        try(FileOutputStream fos = new FileOutputStream("chaves.txt", true);
        OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            osw.write("" + cliente_id + "; " + chave_publica.getEncoded() + "\n");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar chave pública", e);
        }
    }

    public void publicarLance(int leilao_id, double valor){
        try {
            this.assinador = Signature.getInstance("SHA256withRSA");
            this.assinador.initSign(chave_privada);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao publicar lance", e);
        }
    }

}
