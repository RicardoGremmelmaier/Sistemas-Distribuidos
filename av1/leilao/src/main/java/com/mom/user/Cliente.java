package com.mom.user;

import com.rabbitmq.client.*;

public class Cliente {
    private final String cliente_id;
    private final String chave_publica;
    private final String chave_privada;

    public Cliente(String cliente_id, String chave_publica, String chave_privada) {
        this.cliente_id = cliente_id;
        this.chave_publica = chave_publica;
        this.chave_privada = chave_privada;
    }

    public String getClienteId() {
        return cliente_id;
    }

    public String getChavePublica() {
        return chave_publica;
    }

    void publicarLance(){

    }


}
