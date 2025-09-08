package com.mom.microsservices;

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
        System.out.println("[SUB] Recebido: '" + msg + "'");

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
        System.out.println("[SUB] Recebido lance validado: '" + msg + "'");

        int leilaoId = Integer.parseInt(msg.split(",")[0].split(":")[1].trim());
        int client_id = Integer.parseInt(msg.split(",")[1].split(":")[1].trim());
        double valor = Double.parseDouble(msg.split(",")[2].split(":")[1].replace("}", "").trim());

        Lance lance = new Lance(leilaoId, client_id, valor);

        try{
            publisher.publish("leilao." + leilaoId, lance.toString());
        }
        catch (Exception e) {
            throw new RuntimeException("Erro ao publicar notificação de lance validado", e);
        }
    }

    public void handleLeilaoVencedor(String msg) {
        System.out.println("[SUB] Recebido leilão vencedor: '" + msg + "'");
    }

    public void handleRoutingKeyInvalida(String msg) {
        System.out.println("[SUB] Roteamento inválido: '" + msg + "'");
        System.out.println("Ignorando mensagem...");
    }

    public static void main(String[] args) {
        new MSNotificacao();
    }
}
