package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Util.ObterUsuarioLogado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Api.ReceitaWS;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.AtualizarOficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.CadastrarOficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaBuscaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.OficinaService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controller de Oficina com documentação OpenAPI (Swagger).
 */
@RestController
@RequestMapping("/oficina")
@Tag(name = "Oficina", description = "Operações de gerenciamento da oficina")
public class OficinaController {

    private final OficinaService oficinaService;
    private final ReceitaWS receitaWS;

    public OficinaController(OficinaService oficinaService, ReceitaWS receitaWS) {
        this.oficinaService = oficinaService;
        this.receitaWS = receitaWS;
    }

    @PostMapping("/cadastrar")
    @Operation(summary = "Cadastrar oficina",
            description = "Cadastra uma nova oficina no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Oficina cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "409", description = "Oficina já cadastrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<String> cadastrarOficina(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para cadastro da oficina",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CadastrarOficinaDTO.class)))
            @RequestBody @Valid CadastrarOficinaDTO dto) {

        oficinaService.cadastrarOficina(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Oficina com o CNPJ " + ObterUsuarioLogado.obterCnpjUsuarioLogado() + " cadastrada com sucesso!");
    }

    @PutMapping("/atualizar")
    @Operation(summary = "Atualizar oficina",
            description = "Atualiza os dados de uma oficina identificada pelo CNPJ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Oficina atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Oficina não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na atualização", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<String> atualizarOficina(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualização da oficina",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AtualizarOficinaDTO.class)))
            @RequestBody AtualizarOficinaDTO dto) {

        oficinaService.atualizarDadosOficina(dto);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Oficina atualizada com sucesso!");
    }

    @GetMapping("/localizar")
    @Operation(summary = "Localizar oficina",
            description = "Retorna os dados da oficina cadastrada no sistema pelo CNPJ informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Oficina encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OficinaBuscaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Oficina não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<OficinaBuscaDTO> localizarOficina() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(oficinaService.localizarOficina());
    }

    @GetMapping("/localizar/receita")
    @Operation(summary = "Consultar CNPJ na Receita Federal",
            description = "Consulta os dados da oficina diretamente na API da Receita Federal pelo CNPJ informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OficinaDTO.class))),
            @ApiResponse(responseCode = "404", description = "CNPJ não encontrado na Receita", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao consultar serviço externo", content = @Content)
    })
    public ResponseEntity<OficinaDTO> localizarOficinaReceita() {

        OficinaDTO oficina = receitaWS.buscarCnpj();

        return ResponseEntity.status(HttpStatus.OK).body(oficina);
    }
}
