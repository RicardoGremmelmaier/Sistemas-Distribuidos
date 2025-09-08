package com.mom.microsservices;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Map;

import com.mom.rabbit.Publisher;
import com.mom.rabbit.Subscriber;
import com.mom.util.Lance;

public class MSLance {
    private Signature assinador;

    private List<Integer> leiloesAtivos;
    private Map<Double, Lance> maioresLances;

    private Publisher publisher;
    private final String routingLanceValidado = "lance.validado";
    private final String routingLeilaoVencedor = "leilao.vencedor";
    
    private Subscriber subscriber;
    private final String routingLanceRealizado = "lance.realizado";
    private final String routingLeilaoIniciado = "leilao.iniciado"; 
    private final String routingLeilaoFinalizado = "leilao.finalizado"; 
    
    public MSLance() {
        try {
            this.publisher = new Publisher();
        } 
        catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar publisher", e);
        }

        try{
            this.subscriber = new Subscriber(
                java.util.Arrays.asList(routingLanceRealizado,routingLeilaoIniciado,routingLeilaoFinalizado),
                this::handleMensagem
            );
        }
        catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar subscriber", e);
        }
    }


    public void validarLance(Lance lance){
        if(!leiloesAtivos.contains(lance.getLeilaoId())) {
            throw new RuntimeException("Lance inválido: leilão não está ativo.");
        }

        if(!validarAssinatura(lance)) {
            throw new RuntimeException("Lance inválido: assinatura não confere.");
        }

        Lance maiorLance = null;
        for (Map.Entry<Double, Lance> entry : maioresLances.entrySet()) {
            if (entry.getValue().getLeilaoId() == lance.getLeilaoId()) {
                maiorLance = entry.getValue();
                break;
            }
        }
        if (maiorLance == null || lance.getValor() > maiorLance.getValor()) {
            maioresLances.put(lance.getValor(), lance);

            try {
                this.publisher.publish(routingLanceValidado, lance.toString());
            } catch (Exception e) {
                throw new RuntimeException("Erro ao publicar lance validado", e);
            }
               
        }

    }

    public boolean validarAssinatura(Lance lance){
        int client_id = lance.getClienteId();
        PublicKey chavePublica;
        try {
            chavePublica = carregarChavePublica(client_id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar chave pública do cliente " + client_id, e);
        }

        try {
            this.assinador = Signature.getInstance("SHA256withRSA");
            assinador.initVerify(chavePublica);
            this.assinador.update(lance.toString().getBytes("UTF-8"));

            return this.assinador.verify(lance.getAssinatura());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar assinatura", e);
        }
    }

    public PublicKey carregarChavePublica(int clienteId) throws Exception {
        String path = System.getProperty("user.dir") 
                        + File.separator + "public-keys"
                        + File.separator + "cliente_" + clienteId + ".txt";

        byte[] encKey = Files.readAllBytes(Paths.get(path));
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(pubKeySpec);
    }

    public void handleMensagem(String msg) {
        System.out.println("[SUB] Recebido: '" + msg + "'");

        String routingKey = msg.split("]")[0].substring(1);
        String body = msg.split("]")[1].trim();

        switch(routingKey){
            case routingLanceRealizado:
                System.out.println("Lance realizado");
                handleLanceRealizado(body);
                break;
            case routingLeilaoIniciado:
                System.out.println("Leilão iniciado");
                handleLeilaoIniciado(body);
                break;
            case routingLeilaoFinalizado:
                System.out.println("Leilão finalizado");
                handleLeilaoFinalizado(body);
                break;
            default:
                handleRoutingKeyInvalida(msg);

        }

    }

    public void handleLanceRealizado(String msg) {

        // int leilaoId = Integer.parseInt(msg.split(",")[0].split(":")[1].trim());
        // int clienteId = Integer.parseInt(msg.split(",")[1].split(":")[1].trim());
        // double valor = Double.parseDouble(msg.split(",")[2].split(":")[1].trim());

        // Lance lance = new Lance(leilaoId, clienteId, valor);
        // validarLance(lance);
    }

    public void handleLeilaoIniciado(String msg) {
        int leilaoId = Integer.parseInt(msg.split(":")[1].split(",")[0].trim());
        this.leiloesAtivos.add(leilaoId);
    }

    public void handleLeilaoFinalizado(String msg) {
        int leilaoId = Integer.parseInt(msg.split(":")[1].split(",")[0].trim());

        Lance lanceVencedor = null;
        for (Map.Entry<Double, Lance> entry : maioresLances.entrySet()) {
            if (entry.getValue().getLeilaoId() == leilaoId) {
                lanceVencedor = entry.getValue();
                break;
            }
        }

        leiloesAtivos.remove(leilaoId);

        if (lanceVencedor != null) {
            try {
                this.publisher.publish(routingLeilaoVencedor, lanceVencedor.toString());
            } catch (Exception e) {
                throw new RuntimeException("Erro ao publicar leilão vencedor", e);
            }
        } else {
            System.out.println("Nenhum lance válido foi recebido para o leilão " + leilaoId);
        }   

    }

    public void handleRoutingKeyInvalida(String msg) {
        System.out.println("[SUB] Roteamento inválido: '" + msg + "'");
        System.out.println("Ignorando mensagem...");
    }

    public static void main(String[] args) {
        new MSLance();
    }
}
