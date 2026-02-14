package br.com.moto.oficina.manager.Moto.Oficina.Manager.Service;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Usuario.NovaSenhaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Usuario;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.Role.Role;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.CPFCNPJDuplicadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.CPFCNPJInvalidoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Usuario.ErroSenhaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Usuario.UsuarioNaoEncontradoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Repository.UsuarioRepository;
import ch.qos.logback.core.net.SyslogOutputStream;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AutenticacaoService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public String cadastrarUsuario(String cnpj) {
        Boolean existe = usuarioRepository.existsByCnpj(cnpj).orElseThrow( () -> new CPFCNPJInvalidoException("Erro ao verificar CNPJ"));
        if(existe){
            throw new CPFCNPJInvalidoException("CNPJ já cadastrado");
        }

        Usuario user = new Usuario();
        user.setCnpj(cnpj);
        String senhaGerada = gerarSenhaAleatoria();
        user.setSenha(passwordEncoder.encode(senhaGerada));
        user.setRole(Role.OFICINA);
        usuarioRepository.save(user);

        return "Oficina cadastrada com sucesso, por favor, verifique o seu e-mail para obter a senha de acesso.";
    }

    public String atualizarSenhaPrimeiroAcesso(String cnpj, NovaSenhaDTO novaSenha) {

        Usuario usuario = usuarioRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (!usuario.getPrimeiroLogin()) {
            throw new ErroSenhaException("A senha já foi atualizada anteriormente");
        }

        if (novaSenha.novaSenha() == null || novaSenha.novaSenha().isBlank()) {
            throw new ErroSenhaException("Senha inválida");
        }

        if(novaSenha.novaSenha().equals(usuario.getSenha())){
            throw new ErroSenhaException("A nova senha deve ser diferente da senha atual");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha.novaSenha()));

        usuarioRepository.save(usuario);

        return "Senha atualizada com sucesso";
    }


    private String gerarSenhaAleatoria() {
        int comprimento = 8;
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder senha = new StringBuilder();

        for (int i = 0; i < comprimento; i++) {
            int indice = (int) (Math.random() * caracteres.length());
            senha.append(caracteres.charAt(indice));
        }

        return senha.toString();
    }
}
