package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.AtualizarFuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.CadastrarFuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.FuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.FuncionarioService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controller de Funcionário com documentação OpenAPI (Swagger).
 */
@RestController
@RequestMapping("/funcionario")
@Tag(name = "Funcionário", description = "Operações de gerenciamento de funcionários")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping("/cadastrar")
    @Operation(summary = "Cadastrar funcionário", description = "Cadastra um novo funcionário para o estabelecimento informado pelo CNPJ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Estabelecimento não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Funcionário já cadastrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<FuncionarioDTO> cadastrarFuncionario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para cadastro do funcionário",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CadastrarFuncionarioDTO.class)))
            @RequestBody CadastrarFuncionarioDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(funcionarioService.cadastrarFuncionario(dto));
    }

    @PutMapping("/atualizar")
    @Operation(summary = "Atualizar funcionário", description = "Atualiza os dados de um funcionário identificado pelo CPF.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na atualização", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<FuncionarioDTO> atualizarFuncionario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualização do funcionário",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AtualizarFuncionarioDTO.class)))
            @RequestBody AtualizarFuncionarioDTO dto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(funcionarioService.atualizarFuncionario(dto));
    }

    @PatchMapping("/ativar/{cpf}")
    @Operation(summary = "Ativar funcionário", description = "Ativa um funcionário inativo identificado pelo CPF.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Ativação aceita",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioDTO.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito ao ativar funcionário", content = @Content)
    })
    public ResponseEntity<FuncionarioDTO> ativarFuncionario(
            @Parameter(description = "CPF do funcionário", required = true)
            @PathVariable String cpf) {

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(funcionarioService.ativarFuncionario(cpf));
    }

    @PatchMapping("/desativar/{cpf}")
    @Operation(summary = "Desativar funcionário", description = "Desativa um funcionário ativo identificado pelo CPF.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Desativação aceita",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FuncionarioDTO.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito ao desativar funcionário", content = @Content)
    })
    public ResponseEntity<FuncionarioDTO> inativarFuncionario(
            @Parameter(description = "CPF do funcionário", required = true)
            @PathVariable String cpf) {

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(funcionarioService.inativarFuncionario(cpf));
    }

    @GetMapping("/buscar/cpf/{cpf}")
    @Operation(summary = "Buscar funcionário por CPF", description = "Retorna o funcionário correspondente ao CPF informado.")
    @ApiResponse(responseCode = "200", description = "Funcionário encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FuncionarioDTO.class)))
    public ResponseEntity<FuncionarioDTO> buscarFuncionario(
            @Parameter(description = "CPF do funcionário", required = true)
            @PathVariable String cpf) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(funcionarioService.buscarFuncionarioPorCPF(cpf));
    }

    @GetMapping("/buscar/nome/{nome}")
    @Operation(summary = "Buscar funcionários por nome", description = "Busca funcionários pelo nome com paginação.")
    @ApiResponse(responseCode = "200", description = "Funcionários encontrados",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FuncionarioDTO.class)))
    public ResponseEntity<Page<FuncionarioDTO>> buscarFuncionarioPorNome(
            @Parameter(description = "Nome (ou parte) do funcionário", required = true)
            @PathVariable String nome,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(funcionarioService.buscarFuncionarioPorNome(nome, pageable));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar todos os funcionários", description = "Retorna todos os funcionários (paginado) do estabelecimento.")
    @ApiResponse(responseCode = "200", description = "Funcionários retornados",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FuncionarioDTO.class)))
    public ResponseEntity<Page<FuncionarioDTO>> buscarTodosFuncionarios(
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(funcionarioService.buscarTodosFuncionarios(pageable));
    }

    @GetMapping("/buscar/ativo")
    @Operation(summary = "Buscar funcionários ativos", description = "Retorna funcionários ativos (paginado) do estabelecimento.")
    @ApiResponse(responseCode = "200", description = "Funcionários ativos retornados",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FuncionarioDTO.class)))
    public ResponseEntity<Page<FuncionarioDTO>> buscarFuncionariosAtivos(
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(funcionarioService.buscarTodosFuncionariosAtivos(pageable));
    }

    @GetMapping("/buscar/inativo")
    @Operation(summary = "Buscar funcionários inativos", description = "Retorna funcionários inativos (paginado) do estabelecimento.")
    @ApiResponse(responseCode = "200", description = "Funcionários inativos retornados",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FuncionarioDTO.class)))
    public ResponseEntity<Page<FuncionarioDTO>> buscarFuncionariosInativos(
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(funcionarioService.buscarTodosFuncionariosInativos(pageable));
    }
}
