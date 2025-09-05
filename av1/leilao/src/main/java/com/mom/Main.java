package com.mom;

import com.mom.microsservices.*;
import com.mom.user.*;
import com.mom.util.*;

import java.io.*;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        var leilao = new Leilao("Leilão de Teste");
        System.out.println("Leilão criado: " + leilao.getLeilaoId() + " - " + leilao.getDescricao());

        var leilao2 = new Leilao("Leilão de Teste 2");
        System.out.println("Leilão criado: " + leilao2.getLeilaoId() + " - " + leilao2.getDescricao());
    }
}