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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/autenticacao")
@Tag(name = "Autenticação", description = "Operações de login, cadastro inicial e atualização de senha")
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
    @Operation(summary = "Autenticar usuário", description = "Realiza o login e retorna um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenJWT.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciais de login", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginDTO.class)))
            @RequestBody @Valid LoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.cnpj(), loginDTO.senha());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new TokenJWT(token));
    }

    @PostMapping("/cadastro/{cnpj}")
    @Operation(summary = "Cadastrar usuário inicial", description = "Realiza o cadastro inicial do usuário pelo CNPJ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro realizado com sucesso", content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<String> cadastro(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj){
        return ResponseEntity.status(201).body(autenticacaoService.cadastrarUsuario(cnpj));
    }

    @PutMapping("/atualizar-senha/{cnpj}")
    @Operation(summary = "Atualizar senha no primeiro acesso", description = "Atualiza a senha do usuário no primeiro acesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso", content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<String> atualizarSenha(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nova senha", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = NovaSenhaDTO.class)))
            @RequestBody NovaSenhaDTO novaSenha){
        return ResponseEntity.ok(autenticacaoService.atualizarSenhaPrimeiroAcesso(cnpj, novaSenha));
    }
}