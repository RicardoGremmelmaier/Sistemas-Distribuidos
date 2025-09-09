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

import org.json.JSONObject;

public class MSLance {
    private Signature assinador;

    private List<Integer> leiloesAtivos = new java.util.ArrayList<>();
    private Map<Double, Lance> maioresLances =new java.util.HashMap<>();

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
            System.out.println("Leilão inativo");
            throw new RuntimeException("Lance inválido: leilão não está ativo.");
        }

        if(!validarAssinatura(lance)) {
            System.out.println("Assinatura inválida");
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
        int clientId = lance.getClienteId();
        int leilaoId = lance.getLeilaoId();    
        PublicKey chavePublica;
        byte[] assinatura;
        try {
            chavePublica = carregarChavePublica(clientId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar chave pública do cliente " + clientId, e);
        }

        try {
            assinatura = carregarAssinatura(clientId, leilaoId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar assinatura do cliente " + clientId + " para o leilão " + leilaoId, e);
        }

        try {
            this.assinador = Signature.getInstance("SHA256withRSA");
            assinador.initVerify(chavePublica);
            this.assinador.update(lance.toString().getBytes("UTF-8"));

            return this.assinador.verify(assinatura);
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

    public byte[] carregarAssinatura(int clienteId, int leilaoId) throws Exception {
        String path = System.getProperty("user.dir") 
                        + File.separator + "assinaturas"
                        + File.separator + "cliente_" + clienteId + "_leilao_" + leilaoId + ".txt";

        byte [] assinatura = Files.readAllBytes(Paths.get(path));
        return assinatura;
    }

    public void handleMensagem(String msg) {
        String routingKey = msg.split("]")[0].substring(1);
        String body = msg.split("]")[1].trim();

        switch(routingKey){
            case routingLanceRealizado:
                handleLanceRealizado(body);
                break;
            case routingLeilaoIniciado:
                handleLeilaoIniciado(body);
                break;
            case routingLeilaoFinalizado:
                handleLeilaoFinalizado(body);
                break;
            default:
                handleRoutingKeyInvalida(msg);

        }

    }

    public void handleLanceRealizado(String msg) {
        JSONObject json = new JSONObject(msg);
        int leilaoId = json.getInt("leilaoId");
        int clienteId = json.getInt("clienteId");
        double valor = json.getDouble("valor");

        Lance lance = new Lance(leilaoId, clienteId, valor);

        validarLance(lance);
    }

    public void handleLeilaoIniciado(String msg) {
        JSONObject json = new JSONObject(msg);
        int leilaoId = json.getInt("leilaoId");
        this.leiloesAtivos.add(leilaoId);
    }

    public void handleLeilaoFinalizado(String msg) {
        JSONObject json = new JSONObject(msg);
        int leilaoId = json.getInt("leilaoId");

        Lance lanceVencedor = null;
        for (Map.Entry<Double, Lance> entry : this.maioresLances.entrySet()) {
            if (entry.getValue().getLeilaoId() == leilaoId) {
                lanceVencedor = entry.getValue();
                break;
            }
        }

        leiloesAtivos = leiloesAtivos.stream()
                        .filter(id -> id != leilaoId)
                        .toList();

        if (lanceVencedor != null) {
            try {
                this.publisher.publish(routingLeilaoVencedor, lanceVencedor.toString());
            } catch (Exception e) {
                throw new RuntimeException("Erro ao publicar leilão vencedor", e);
            }
        } else {
            String msgNone = "Nenhum lance válido foi recebido para o leilão " + leilaoId;
            System.out.println(msgNone);
            try {
                this.publisher.publish(routingLeilaoVencedor, msgNone);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao publicar leilão vencedor", e);
            }
        }
    }

    public void handleRoutingKeyInvalida(String msg) {
        System.out.println("Roteamento inválido: '" + msg + "'");
        System.out.println("Ignorando mensagem...");
    }

    public static void main(String[] args) {
        new MSLance();
    }
}
