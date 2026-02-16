package br.com.moto.oficina.manager.Moto.Oficina.Manager.Util;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Usuario.UsuarioNaoEncontradoException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class ObterUsuarioLogado {

        public static String obterCnpjUsuarioLogado() {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                throw new UsuarioNaoEncontradoException("Usuário não autenticado.");
            }

            System.out.println("Authentication: " + authentication.getName());
            return authentication.getName();
        }
}
