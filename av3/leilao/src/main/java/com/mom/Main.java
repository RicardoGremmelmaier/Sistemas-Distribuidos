package com.mom;

import com.mom.util.*;


public class Main {
    public static void main(String[] args) {
        var leilao = new Leilao("Leilão de Teste", 2);
        System.out.println(leilao.toString());
        System.out.println("Leilão criado: " + leilao.getLeilaoId() + " - " + leilao.getDescricao());

        var leilao2 = new Leilao("Leilão de Teste 2", 2);
        System.out.println("Leilão criado: " + leilao2.getLeilaoId() + " - " + leilao2.getDescricao());
    }

}