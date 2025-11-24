package com.mom.microsservices;

import org.json.JSONObject;

import com.mom.rabbit.Publisher;
import com.mom.rabbit.Subscriber;
import com.mom.util.Lance;
public class MSNotificacao {
    private Publisher publisher;

    private Subscriber subscriber;
    private final String routingLanceValidado = "lance.validado";
    private final String routingLeilaoVencedor = "leilao.vencedor";

    public MSNotificacao() {
        try {
            this.publisher = new Publisher();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar publisher", e);
        }

        try {
            this.subscriber = new Subscriber(
                java.util.Arrays.asList(routingLanceValidado, routingLeilaoVencedor),
                this::handleMensagem
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar subscriber", e);
        }
    }

    public void handleMensagem(String msg) {

        String routingKey = msg.split("]")[0].substring(1);
        String body = msg.split("]")[1].trim();

        switch (routingKey) {
            case routingLanceValidado:
                handleLanceValidado(body);
                break;
            case routingLeilaoVencedor:
                handleLeilaoVencedor(body);
                break;
            default:
                handleRoutingKeyInvalida(routingKey);
        }

    }

    public void handleLanceValidado(String msg) {
        JSONObject json = new JSONObject(msg);
        int leilaoId = json.getInt("leilaoId");
        int clienteId = json.getInt("clienteId");
        double valor = json.getDouble("valor");

        Lance lance = new Lance(leilaoId, clienteId, valor);
        try{
            publisher.publish("leilao." + leilaoId + ".lance", lance.toString());
        }
        catch (Exception e) {
            throw new RuntimeException("Erro ao publicar notificação de lance validado", e);
        }
    }

    public void handleLeilaoVencedor(String msg) {
        if (msg.contains("Nenhum lance válido")) {
            int leilaoId = Integer.parseInt(msg.split("leilão")[1].trim());
            System.out.println("Nenhum lance válido recebido.");
            try{
                publisher.publish("leilao." + leilaoId + ".vencedor", msg);
            }
            catch (Exception e) {
                throw new RuntimeException("Erro ao publicar notificação de leilão vencedor", e);
            }   
            return;
        }
        JSONObject json = new JSONObject(msg);
        int leilaoId = json.getInt("leilaoId");
        int clienteId = json.getInt("clienteId");
        double valor = json.getDouble("valor");


        Lance lance = new Lance(leilaoId, clienteId, valor);
        try{    
            publisher.publish("leilao." + leilaoId + ".vencedor", leilaoVencedorToString(lance));
        }
        catch (Exception e) {
            System.out.println("Erro ao publicar notificação de leilão vencedor");  
            throw new RuntimeException("Erro ao publicar notificação de leilão vencedor", e);
        } 
    }

    public void handleRoutingKeyInvalida(String msg) {
        System.out.println("[SUB] Roteamento inválido: '" + msg + "'");
        System.out.println("Ignorando mensagem...");
    }

    public String leilaoVencedorToString(Lance lance){
        return "Leilão " + lance.getLeilaoId() + " vencido pelo cliente " + lance.getClienteId() + " com o valor de " + lance.getValor();
    }

    public static void main(String[] args) {
        new MSNotificacao();
    }
}
