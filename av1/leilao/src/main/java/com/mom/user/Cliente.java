package com.mom.user;

import com.mom.util.Lance;

import com.rabbitmq.client.*;
import com.mom.util.Lance;
import java.io.*;
import java.security.*;

public class Cliente {
    private final int clienteId;
    private final PublicKey chavePublica;
    private final PrivateKey chavePrivada;
    private Signature assinador;

    private static int id_counter = 1;

    public Cliente() {
        this.clienteId = id_counter++;

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(2048, random);
            KeyPair pair = keyGen.generateKeyPair();
            this.chavePublica = pair.getPublic();
            this.chavePrivada = pair.getPrivate();

            if(this.chavePrivada != null && this.chavePublica != null) {
                salvarChavePublica();
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar o cliente", e);
        }
    }

    public int getClienteId() {
        return clienteId;
    }

    public void salvarChavePublica(){
        String path = System.getProperty("user.dir") 
                        + File.separator + "public-keys"
                        + File.separator + "cliente_" + clienteId + ".txt";
        byte[] key = this.chavePublica.getEncoded();
        try(FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(key);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar chave pública", e);
        }
    }

    public void publicarLance(Lance lance){

    }

    public void assinarLance(Lance lance){
        try {
            this.assinador = Signature.getInstance("SHA256withRSA");
            this.assinador.initSign(chavePrivada);
            this.assinador.update(lance.toString().getBytes("UTF-8"));
            lance.setAssinatura(this.assinador.sign());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao assinar lance", e);
        }
    }

    public PublicKey getPublicKey() {
        return chavePublica;
    }

}
