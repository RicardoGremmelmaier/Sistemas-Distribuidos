package com.mom.user;

import com.mom.util.Lance;
import com.mom.rabbit.Publisher;
import com.mom.rabbit.Subscriber;

import java.io.*;
import java.security.*;
import java.util.Scanner;
import java.util.UUID;

import org.json.JSONObject;

public class Cliente {
    private final int clienteId;

    private final PublicKey chavePublica;
    private final PrivateKey chavePrivada;
    private Signature assinador;

    private Publisher publisher;
    private final String routingLanceRealizado = "lance.realizado";
    
    private Subscriber subscriber;
    private final String routingLeilaoIniciado = "leilao.iniciado";

    private static final Scanner scanner = new Scanner(System.in);

    public Cliente() {
        this.clienteId = Math.abs(UUID.randomUUID().hashCode());
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

    public void salvarAssinatura(Lance lance){
        String path = System.getProperty("user.dir") 
                        + File.separator + "assinaturas"
                        + File.separator + "cliente_" + clienteId + "_leilao_" + lance.getLeilaoId() + ".txt";
        byte[] assinatura = lance.getAssinatura();
        try(FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(assinatura);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar assinatura", e);
        }
    }

    public void publicarLance(Lance lance){
        assinarLance(lance);
        salvarAssinatura(lance);

        try {
            this.publisher.publish(routingLanceRealizado, lance.toString());
        } 
        catch (Exception e) {
            throw new RuntimeException("Erro ao publicar lance", e);
        }

        try{
            this.subscriber.queueBind("leilao." + lance.getLeilaoId() + ".lance");
            this.subscriber.queueBind("leilao." + lance.getLeilaoId() + ".vencedor");
        } 
        catch (Exception e) {
            throw new RuntimeException("Erro ao vincular fila do leilão", e);
        }

        System.out.println("Lance de " + lance.getValor() + " publicado para o leilão " + lance.getLeilaoId());
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
        String routingKey = msg.split("]")[0].substring(1);
        String body = msg.split("]")[1].trim();

        if (routingKey.equals(routingLeilaoIniciado)) {
            handleLeilaoIniciado(body);
        } else if (routingKey.contains(".vencedor")) {
            handleLeilaoFinalizado(body);
        } else if (routingKey.contains(".lance")) {
            handleLance(body);
        } else {
            handleRoutingKeyInvalida(routingKey);
        }

    }

    public void handleLeilaoIniciado(String msg) {
        JSONObject json = new JSONObject(msg);

        int leilaoId = json.getInt("leilaoId");
        String descricao = json.getString("descricao");
        int durationMinutes = json.getInt("durationMinutes");
        System.out.println("Leilão iniciado com ID " + leilaoId + " com descrição: " + descricao + " e duração de " + durationMinutes + " minuto(s).");
        System.out.println("Deseja participar? (s/n)");

        String resposta = scanner.nextLine().trim().toLowerCase();

        if(resposta.equals("s")){
            System.out.println("Digite o valor do lance:");
            double valor = Double.parseDouble(scanner.nextLine().trim());

            Lance lance = new Lance(leilaoId, this.clienteId, valor);
            publicarLance(lance);
        } else {
            System.out.println("Você optou por não participar do leilão " + leilaoId);
        }

    }

    public void handleLeilaoFinalizado(String msg) {
        System.out.println(msg);
    }

    public void handleLance(String msg) {
        JSONObject json = new JSONObject(msg);
        int leilaoId = json.getInt("leilaoId");
        int clienteId = json.getInt("clienteId");
        double valor = json.getDouble("valor");

        System.out.println("Lance recebido no leilão " + leilaoId + " do cliente " + clienteId + ": " + valor);

        if (clienteId == this.clienteId) {
            System.out.println("Este é o seu lance. Ignorando...");
            return; 
        }
        System.out.println("Deseja fazer um lance? (s/n)");

        String resposta = scanner.nextLine().trim().toLowerCase();

        if(resposta.equals("s")){
            System.out.println("Digite o valor do lance:");
            double valorLance = Double.parseDouble(scanner.nextLine().trim());

            Lance lance = new Lance(leilaoId, this.clienteId, valorLance);
            publicarLance(lance);
        } else {
            System.out.println("Você optou por não gerar um lance para o leilão " + leilaoId);
        }
    }

    public void handleRoutingKeyInvalida(String msg) {
        System.out.println("Roteamento inválido: '" + msg + "'");
        System.out.println("Ignorando mensagem...");
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();
    }

}
