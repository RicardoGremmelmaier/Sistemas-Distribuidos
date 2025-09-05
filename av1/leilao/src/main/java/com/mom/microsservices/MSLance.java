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

import com.mom.util.Lance;

public class MSLance {
    private Signature assinador;


    public boolean validarLance(Lance lance){
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
            throw new RuntimeException("Erro ao verificar lance", e);
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
