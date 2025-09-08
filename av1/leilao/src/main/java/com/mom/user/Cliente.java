package com.mom.user;

import com.mom.util.Lance;
import com.mom.rabbit.*;

import java.io.*;
import java.security.*;

public class Cliente {
    private final int clienteId;

    private final PublicKey chavePublica;
    private final PrivateKey chavePrivada;
    private Signature assinador;

    private Publisher publisher;
    private Subscriber subscriber;

    private final String routingLeilaoIniciado = "leilao.iniciado";
    private final String routingLanceRealizado = "lance.realizado";

    private static int idCounter = 1;

    public Cliente() {
        this.clienteId = idCounter++;

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
            throw new RuntimeException("Erro ao inicializar a chave pública e privada", e);
        }

        try {
            this.publisher = new Publisher();
        } 
        catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar publisher", e);
        }

        try{
            this.subscriber = new Subscriber(
                java.util.Arrays.asList(routingLeilaoIniciado),
                this::handleMensagem
            );
        } 
        catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar subscriber", e);
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

    public void handleMensagem(String msg) {
        System.out.println(msg);
    }

}
