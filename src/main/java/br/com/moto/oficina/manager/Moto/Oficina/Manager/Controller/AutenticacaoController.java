package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Usuario.LoginDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Usuario.NovaSenhaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Entity.Usuario;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Security.TokenJWT;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Security.TokenService;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AutenticacaoController(AutenticacaoService autenticacaoService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.autenticacaoService = autenticacaoService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.cnpj(), loginDTO.senha());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new TokenJWT(token));
    }

    @PostMapping("/cadastro/{cnpj}")
    public ResponseEntity<String> cadastro(@PathVariable String cnpj){
        return ResponseEntity.ok(autenticacaoService.cadastrarUsuario(cnpj));
    }

    @PutMapping("/atualizar-senha/{cnpj}")
    public ResponseEntity<String> atualizarSenha(@PathVariable String cnpj, @RequestBody NovaSenhaDTO novaSenha){
        return ResponseEntity.ok(autenticacaoService.atualizarSenhaPrimeiroAcesso(cnpj, novaSenha));
    }
}
