package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.OrdemServico.Status;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.OrdemServicoService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controller de Ordem de Serviço com documentação OpenAPI (Swagger).
 */
@RestController
@RequestMapping("/ordem-servico")
@Tag(name = "Ordem de Serviço", description = "Operações relacionadas ao gerenciamento de Ordens de Serviço")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    public OrdemServicoController(OrdemServicoService ordemServicoService){
        this.ordemServicoService = ordemServicoService;
    }

    @PostMapping("/cadastrar")
    @Operation(summary = "Criar nova Ordem de Serviço")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ordem de Serviço criada com sucesso",
                    content = @Content(schema = @Schema(implementation = OrdemServicoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<OrdemServicoDTO> cadastrarNovaOS(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para criação da Ordem de Serviço",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CriarOrdemServicoDTO.class)))
            @RequestBody CriarOrdemServicoDTO dto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordemServicoService.criarOrdemServico(dto));
    }

    @PutMapping("/adicionar-servico")
    @Operation(summary = "Adicionar serviço à Ordem de Serviço")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviço adicionado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrdemServicoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ordem de Serviço não encontrada")
    })
    public ResponseEntity<OrdemServicoDTO> adicionarServicoOs(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do serviço a ser adicionado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AdicionarServicoOSDTO.class)))
            @RequestBody AdicionarServicoOSDTO dto){

        return ResponseEntity.ok(ordemServicoService.adicionarServicoOS(dto));
    }

    @PutMapping("/adicionar-produto")
    @Operation(summary = "Adicionar produto à Ordem de Serviço")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto adicionado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrdemServicoDTO.class)))
    })
    public ResponseEntity<OrdemServicoDTO> adicionarProdutoOS(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do produto a ser adicionado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AdicionarProdutoOSDTO.class)))
            @RequestBody AdicionarProdutoOSDTO dto){

        return ResponseEntity.ok(ordemServicoService.adicionarProdutoOS(dto));
    }

    @PutMapping("/remover/OS/{osId}/servico/{itemServicoId}")
    @Operation(summary = "Remover serviço da Ordem de Serviço")
    public ResponseEntity<OrdemServicoDTO> removerServicoOS(
            @Parameter(description = "ID da Ordem de Serviço", required = true)
            @PathVariable Long osId,
            @Parameter(description = "ID do item de serviço", required = true)
            @PathVariable Long itemServicoId){

        return ResponseEntity.ok(ordemServicoService.removerServicoOS(osId, itemServicoId));
    }

    @PutMapping("/remover/OS/{osId}/produto/{itemEstoqueId}")
    @Operation(summary = "Remover produto da Ordem de Serviço")
    public ResponseEntity<OrdemServicoDTO> removerProdutoOS(
            @Parameter(description = "ID da Ordem de Serviço", required = true)
            @PathVariable Long osId,
            @Parameter(description = "ID do item de estoque", required = true)
            @PathVariable Long itemEstoqueId){

        return ResponseEntity.ok(ordemServicoService.removerProdutoOS(osId, itemEstoqueId));
    }

    @PutMapping("/iniciar/{osId}")
    @Operation(summary = "Iniciar Ordem de Serviço")
    public ResponseEntity<OrdemServicoDTO> iniciarOS(
            @Parameter(description = "ID da Ordem de Serviço", required = true)
            @PathVariable Long osId) {

        return ResponseEntity.ok(ordemServicoService.iniciarOS(osId));
    }

    @PutMapping("/aguardando-pecas/{osId}")
    @Operation(summary = "Colocar Ordem de Serviço em status 'Aguardando Peças'")
    public ResponseEntity<OrdemServicoDTO> aguardarPecasOS(
            @Parameter(description = "ID da Ordem de Serviço", required = true)
            @PathVariable Long osId) {

        return ResponseEntity.ok(ordemServicoService.aguardarPecaOS(osId));
    }

    @GetMapping("/listar/OS/{osId}")
    @Operation(summary = "Buscar Ordem de Serviço por ID")
    public ResponseEntity<OrdemServicoDTO> buscarOSPorId(
            @Parameter(description = "ID da Ordem de Serviço", required = true)
            @PathVariable Long osId) {

        return ResponseEntity.ok(ordemServicoService.listarOrdensDeServicoPorId(osId));
    }

    @PutMapping("/finalizar")
    @Operation(summary = "Finalizar Ordem de Serviço")
    public ResponseEntity<OrdemServicoDTO> finalizarOS(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para finalização da Ordem de Serviço",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FinalizarOsDTO.class)))
            @RequestBody FinalizarOsDTO finalizarOsDTO){

        return ResponseEntity.ok(ordemServicoService.finalizarOS(finalizarOsDTO));
    }

    @PutMapping("/cancelar/{osId}")
    @Operation(summary = "Cancelar Ordem de Serviço")
    public ResponseEntity<OrdemServicoDTO> cancelarOS(
            @Parameter(description = "ID da Ordem de Serviço", required = true)
            @PathVariable Long osId) {

        return ResponseEntity.ok(ordemServicoService.cancelarOS(osId));
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todas as Ordens de Serviço (paginado)")
    public ResponseEntity<Page<OrdemServicoDTO>> listarTodasOrdens(
            @Parameter(description = "Paginação padrão do Spring (page, size, sort)")
            @PageableDefault(page = 0, size = 20, sort = "numero")
            Pageable pageable) {

        return ResponseEntity.ok(ordemServicoService.listarOrdensDeServico(pageable));
    }

    @GetMapping("/listar/status/{status}")
    @Operation(summary = "Listar Ordens de Serviço por status (paginado)")
    public ResponseEntity<Page<OrdemServicoDTO>> listarOrdensPorStatus(
            @Parameter(description = "Status da Ordem de Serviço", required = true)
            @PathVariable Status status,
            @PageableDefault(page = 0, size = 20, sort = "numero")
            Pageable pageable) {

        return ResponseEntity.ok(
                ordemServicoService.listarOrdensDeServicoPorStatus(status, pageable)
        );
    }

    @GetMapping("/listar/funcionario/{cpfFuncionario}")
    @Operation(summary = "Listar Ordens de Serviço de um funcionário (paginado)")
    public ResponseEntity<Page<OrdemServicoDTO>> listarOsFuncionario(
            @Parameter(description = "CPF do funcionário", required = true)
            @PathVariable String cpfFuncionario,
            @Parameter(description = "Status opcional para filtro")
            @RequestParam(required = false) Status status,
            @PageableDefault(page = 0, size = 20, sort = "numero")
            Pageable pageable) {

        return ResponseEntity.ok(
                ordemServicoService.listarOsDoFuncionario(cpfFuncionario, status, pageable)
        );
    }
}

