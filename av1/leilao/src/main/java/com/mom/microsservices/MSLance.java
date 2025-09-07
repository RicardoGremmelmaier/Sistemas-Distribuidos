package com.mom.microsservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import com.mom.util.Lance;

public class MSLance {
    private Signature assinador;
    private List<Integer> leiloesAtivos;
    private List<Integer> leiloesEncerrados;

    public void validarLance(Lance lance){
        if(!leiloesAtivos.contains(lance.getLeilaoId())) {
            throw new RuntimeException("Lance inválido: leilão não está ativo.");
        }

        if (leiloesEncerrados.contains(lance.getLeilaoId())) {
            throw new RuntimeException("Lance inválido: leilão já encerrado.");
        }

        if(!validarAssinatura(lance)) {
            throw new RuntimeException("Lance inválido: assinatura não confere.");
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
}
