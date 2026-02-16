package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.AtualizarEstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.CadastroEstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Estoque.EstoqueDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.EstoqueService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controller de Estoque com documentação OpenAPI (Swagger).
 */
@RestController
@RequestMapping("/estoque")
@Tag(name = "Estoque", description = "Operações de gerenciamento de itens de estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping("/cadastrar")
    @Operation(summary = "Cadastrar item de estoque", description = "Cadastra um novo item no estoque para o estabelecimento informado pelo CNPJ.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item cadastrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstoqueDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito (por exemplo: item já existe)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<EstoqueDTO> cadastrarItemEstoque(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para cadastro do item de estoque", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CadastroEstoqueDTO.class)))
            @RequestBody @Valid CadastroEstoqueDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueService.cadastrarItemEstoque(dto));
    }

    @PutMapping("/atualizar")
    @Operation(summary = "Atualizar item de estoque", description = "Atualiza os dados de um item de estoque identificado pelo id para o estabelecimento informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstoqueDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na atualização", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<EstoqueDTO> atualizarItemEstoque(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para atualização do item de estoque", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = AtualizarEstoqueDTO.class)))
            @RequestBody @Valid AtualizarEstoqueDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.atualizarItemEstoque(dto));
    }

    @PatchMapping("/ativar/{idItem}")
    @Operation(summary = "Ativar item de estoque", description = "Ativa um item de estoque inativo identificado pelo id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Ativação aceita", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstoqueDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito ao ativar item", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<EstoqueDTO> ativarItemEstoque(
            @Parameter(description = "ID do item de estoque", required = true) @PathVariable Long idItem) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(estoqueService.ativarItemEstoque(idItem));
    }

    @PatchMapping("/desativar/{idItem}")
    @Operation(summary = "Desativar item de estoque", description = "Desativa um item de estoque ativo identificado pelo id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Desativação aceita", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstoqueDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito ao desativar item", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<EstoqueDTO> desativarItemEstoque(
            @Parameter(description = "ID do item de estoque", required = true) @PathVariable Long idItem) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(estoqueService.desativarItemEstoque(idItem));
    }

    @GetMapping("/buscar/codigo/{codigoItem}")
    @Operation(summary = "Buscar item por código", description = "Retorna o item de estoque correspondente ao código informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstoqueDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na busca", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<EstoqueDTO> buscarItemEstoque(
            @Parameter(description = "Código do item", required = true) @PathVariable String codigoItem) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarItemEstoque(codigoItem));
    }

    @GetMapping("/buscar/nome/{nome}")
    @Operation(summary = "Buscar itens por nome", description = "Busca itens de estoque pelo nome com paginação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstoqueDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nenhum item encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na busca", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<Page<EstoqueDTO>> buscarItemEstoquePorNome(
            @Parameter(description = "Nome (ou parte) do item", required = true) @PathVariable String nome,
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarItemEstoquePorNome(nome, pageable));
    }

    @GetMapping("/buscar/ativos")
    @Operation(summary = "Buscar itens ativos", description = "Retorna itens de estoque ativos (paginado) do estabelecimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens ativos retornados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstoqueDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nenhum item ativo encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na busca", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<Page<EstoqueDTO>> buscarItensEstoqueAtivos(
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarItensEstoqueAtivos(pageable));
    }

    @GetMapping("/buscar/inativos")
    @Operation(summary = "Buscar itens inativos", description = "Retorna itens de estoque inativos (paginado) do estabelecimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens inativos retornados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstoqueDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nenhum item inativo encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na busca", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<Page<EstoqueDTO>> buscarItensEstoqueInativos(
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarItensEstoqueInativos(pageable));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar todos os itens de estoque", description = "Retorna todos os itens de estoque (paginado) do estabelecimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Itens retornados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstoqueDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nenhum item encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito na busca", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<Page<EstoqueDTO>> buscarTodosItensEstoque(
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarTodosItens(pageable));
    }
}