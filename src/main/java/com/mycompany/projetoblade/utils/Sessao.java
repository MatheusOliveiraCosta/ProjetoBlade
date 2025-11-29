package com.mycompany.projetoblade.utils;

import com.mycompany.projetoblade.model.Cliente;
import com.mycompany.projetoblade.model.Usuario;

/**
 * Sessão da aplicação — agora guarda um `Usuario` genérico (Cliente ou Funcionario).
 */
public class Sessao {
    private static Usuario usuarioLogado;
    // Se o login for feito com um Cliente (entidade que contém Usuario), guardamos o Cliente também
    private static Cliente clienteLogado;

    /** Faz login com um Usuario (ex.: Funcionario). */
    public static void login(Usuario usuario) {
        usuarioLogado = usuario;
        clienteLogado = null;
    }

    /** Faz login com um Cliente (entidade que contém Usuario). */
    public static void login(Cliente cliente) {
        if (cliente != null) {
            clienteLogado = cliente;
            usuarioLogado = cliente.getUsuario();
        } else {
            clienteLogado = null;
            usuarioLogado = null;
        }
    }

    public static void logout() {
        usuarioLogado = null;
    }

    /** Retorna o usuário logado (pode ser Cliente, Funcionario, etc) ou null. */
    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    /** Conveniência: retorna o Cliente logado (quando o login foi feito via Cliente), caso exista. */
    public static Cliente getClienteLogado() {
        return clienteLogado;
    }

    public static boolean isLogado() {
        return usuarioLogado != null;
    }
}