package com.mycompany.projetoblade.utils;

import com.mycompany.projetoblade.model.Cliente;

public class Sessao {
    private static Cliente clienteLogado;

    public static void login(Cliente cliente) {
        clienteLogado = cliente;
    }

    public static void logout() {
        clienteLogado = null;
    }

    public static Cliente getClienteLogado() {
        return clienteLogado;
    }

    public static boolean isLogado() {
        return clienteLogado != null;
    }
}