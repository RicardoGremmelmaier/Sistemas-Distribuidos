package com.mom;

import com.mom.user.Cliente;

public class Main {
    public static void main(String[] args) {
        var cliente1 = new Cliente();
        cliente1.salvarChavePublica();
        var cliente2 = new Cliente();
        cliente2.salvarChavePublica();
        var cliente3 = new Cliente();
        cliente3.salvarChavePublica();
    }
}