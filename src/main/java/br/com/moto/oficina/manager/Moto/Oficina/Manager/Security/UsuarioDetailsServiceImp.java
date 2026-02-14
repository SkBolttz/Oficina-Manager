package br.com.moto.oficina.manager.Moto.Oficina.Manager.Security;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Usuario;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsServiceImp implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsServiceImp(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String cnpj) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com CNPJ: " + cnpj));
        return user;
    }
}
