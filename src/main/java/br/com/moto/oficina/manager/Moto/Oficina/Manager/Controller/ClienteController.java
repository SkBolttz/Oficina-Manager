package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteAtualizarDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteCadastroDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Cliente.ClienteDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.ClienteService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/cliente")
@Tag(name = "Cliente", description = "Operações de cadastro, atualização, ativação/desativação e buscas de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /*
     * ===========================
     * CADASTRO / ATUALIZAÇÃO
     * ===========================
     */

    @PostMapping("/cadastrar/{cnpj}")
    @Operation(summary = "Cadastrar cliente", description = "Realiza o cadastro de um novo cliente para o estabelecimento informado pelo CNPJ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro realizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Recursos não encontrados", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito (por exemplo: cliente já existe)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<ClienteDTO> cadastrarCliente(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para cadastro do cliente", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteCadastroDTO.class)))
            @RequestBody @Valid ClienteCadastroDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.cadastrarCliente(cnpj, dto));
    }

    @PutMapping("/atualizar/{cnpj}/{cnpjCpf}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente identificado por CNPJ/CPF/CNPJ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Atualização aceita", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na atualização", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<ClienteDTO> atualizarCliente(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj,
            @Parameter(description = "CPF/CNPJ do cliente", required = true) @PathVariable String cnpjCpf,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para atualização do cliente", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteAtualizarDTO.class)))
            @RequestBody @Valid ClienteAtualizarDTO dto) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(clienteService.editarCliente(cnpj, cnpjCpf, dto));
    }

    @PutMapping("/ativar/{cnpj}/{cnpjCpf}")
    @Operation(summary = "Ativar cliente", description = "Ativa um cliente inativo identificado por CNPJ/CPF/CNPJ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Ativação aceita", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito ao ativar cliente", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<ClienteDTO> ativarCliente(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj,
            @Parameter(description = "CPF/CNPJ do cliente", required = true) @PathVariable String cnpjCpf) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(clienteService.ativarCliente(cnpj, cnpjCpf));
    }

    @PutMapping("/desativar/{cnpj}/{cnpjCpf}")
    @Operation(summary = "Desativar cliente", description = "Desativa um cliente ativo identificado por CNPJ/CPF/CNPJ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Desativação aceita", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito ao desativar cliente", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<ClienteDTO> desativarCliente(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj,
            @Parameter(description = "CPF/CNPJ do cliente", required = true) @PathVariable String cnpjCpf) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(clienteService.desativarCliente(cnpj, cnpjCpf));
    }

    /*
     * ===========================
     * BUSCAS (PAGINADAS)
     * ===========================
     */

    @GetMapping("/buscar/{cnpj}/cpf-cnpj/{cpfCnpj}")
    @Operation(summary = "Buscar cliente por CPF/CNPJ", description = "Retorna o cliente correspondente ao CPF/CNPJ informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na busca", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<ClienteDTO> buscarPorCpfCnpj(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj,
            @Parameter(description = "CPF/CNPJ do cliente", required = true) @PathVariable String cpfCnpj) {

        return ResponseEntity.ok(
                clienteService.buscarClientePorCpfCnpj(cnpj, cpfCnpj));
    }

    @GetMapping("/buscar/{cnpj}/nome/{nome}")
    @Operation(summary = "Buscar clientes por nome", description = "Busca clientes pelo nome com paginação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na busca", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<Page<ClienteDTO>> buscarPorNome(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj,
            @Parameter(description = "Nome (ou parte) do cliente", required = true) @PathVariable String nome,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(
                clienteService.buscarClientePorNome(cnpj, nome, pageable));
    }

    @GetMapping("/buscar/{cnpj}")
    @Operation(summary = "Buscar todos os clientes", description = "Retorna todos os clientes (paginado) do estabelecimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes retornados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na busca", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<Page<ClienteDTO>> buscarTodosClientes(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(
                clienteService.buscarTodosClientes(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/ativos")
    @Operation(summary = "Buscar clientes ativos", description = "Retorna todos os clientes ativos (paginado) do estabelecimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes ativos retornados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente ativo encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na busca", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<Page<ClienteDTO>> buscarTodosClientesAtivos(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(
                clienteService.buscarTodosAtivos(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/inativos")
    @Operation(summary = "Buscar clientes inativos", description = "Retorna todos os clientes inativos (paginado) do estabelecimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes inativos retornados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente inativo encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na busca", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<Page<ClienteDTO>> buscarTodosClientesInativos(
            @Parameter(description = "CNPJ do estabelecimento", required = true) @PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(
                clienteService.buscarTodosInativos(cnpj, pageable));
    }
}