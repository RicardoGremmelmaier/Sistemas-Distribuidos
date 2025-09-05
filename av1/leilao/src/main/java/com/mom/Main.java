package com.mom;

import com.mom.microsservices.*;
import com.mom.user.Cliente;
import com.mom.util.Lance;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        var cliente1 = new Cliente();
        cliente1.salvarChavePublica();
        System.out.println("Cliente 1 ID: " + cliente1.getClienteId());
        System.out.println("Cliente 1 key: " + cliente1.getPublicKey().getEncoded());

        Lance lanceC1 = new Lance(1, cliente1.getClienteId(), 100);
        cliente1.assinarLance(lanceC1);
        MSLance msLance = new MSLance();
        System.out.println(msLance.validarLance(lanceC1));

        var cliente2 = new Cliente();
        cliente2.salvarChavePublica();
    }
}