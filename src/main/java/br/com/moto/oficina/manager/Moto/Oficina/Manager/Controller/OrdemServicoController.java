package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.OrdemServicoService;
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
@Tag(name = "Ordem de Serviço", description = "Operações de gerenciamento de Ordens de Serviço")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    public OrdemServicoController(OrdemServicoService ordemServicoService){
        this.ordemServicoService = ordemServicoService;
    }

    @PostMapping("/cadastrar/{cnpj}/cliente/{cpfCnpj}/responsavel/{cpfFuncionarioResponsavel}")
    @Operation(summary = "Criar nova Ordem de Serviço",
            description = "Cria uma nova ordem de serviço vinculada a um cliente e a um funcionário responsável.")
    @ApiResponse(responseCode = "201", description = "Ordem de Serviço criada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrdemServicoDTO.class)))
    public ResponseEntity<OrdemServicoDTO> cadastrarNovaOS(
            @Parameter(description = "CNPJ da oficina", required = true)
            @PathVariable String cnpj,
            @Parameter(description = "CPF ou CNPJ do cliente", required = true)
            @PathVariable String cpfCnpj,
            @Parameter(description = "CPF do funcionário responsável", required = true)
            @PathVariable String cpfFuncionarioResponsavel,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação da Ordem de Serviço",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CriarOrdemServicoDTO.class)))
            @RequestBody CriarOrdemServicoDTO dto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordemServicoService.criarOrdemServico(cnpj, cpfCnpj, cpfFuncionarioResponsavel, dto));
    }

    @PutMapping("/adicionar-servico/{cnpj}/OS/{osId}")
    @Operation(summary = "Adicionar serviço à OS",
            description = "Adiciona um serviço à Ordem de Serviço informada.")
    @ApiResponse(responseCode = "200", description = "Serviço adicionado com sucesso",
            content = @Content(schema = @Schema(implementation = OrdemServicoDTO.class)))
    public ResponseEntity<OrdemServicoDTO> adicionarServicoOs(
            @Parameter(description = "CNPJ da oficina", required = true)
            @PathVariable String cnpj,
            @Parameter(description = "ID da Ordem de Serviço", required = true)
            @PathVariable Long osId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do serviço a ser adicionado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AdicionarServicoOSDTO.class)))
            @RequestBody AdicionarServicoOSDTO dto){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ordemServicoService.adicionarServicoOS(cnpj, osId, dto));
    }

    @PutMapping("/adicionar-produto/{cnpj}/OS/{osId}")
    @Operation(summary = "Adicionar produto à OS",
            description = "Adiciona um produto do estoque à Ordem de Serviço.")
    @ApiResponse(responseCode = "200", description = "Produto adicionado com sucesso",
            content = @Content(schema = @Schema(implementation = OrdemServicoDTO.class)))
    public ResponseEntity<OrdemServicoDTO> adicionarProdutoOS(
            @Parameter(description = "CNPJ da oficina", required = true)
            @PathVariable String cnpj,
            @Parameter(description = "ID da Ordem de Serviço", required = true)
            @PathVariable Long osId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do produto a ser adicionado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AdicionarProdutoOSDTO.class)))
            @RequestBody AdicionarProdutoOSDTO dto){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ordemServicoService.adicionarProdutoOS(cnpj, osId, dto));
    }

    @PutMapping("/finalizar/{cnpj}/{osId}")
    @Operation(summary = "Finalizar Ordem de Serviço",
            description = "Finaliza a Ordem de Serviço alterando seu status.")
    @ApiResponse(responseCode = "200", description = "Ordem de Serviço finalizada",
            content = @Content(schema = @Schema(implementation = OrdemServicoDTO.class)))
    public ResponseEntity<OrdemServicoDTO> finalizarOS(
            @Parameter(description = "CNPJ da oficina", required = true)
            @PathVariable String cnpj,
            @Parameter(description = "ID da Ordem de Serviço", required = true)
            @PathVariable Long osId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para finalização da OS",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FinalizarOsDTO.class)))
            @RequestBody FinalizarOsDTO finalizarOsDTO){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ordemServicoService.finalizarOS(cnpj, osId, finalizarOsDTO));
    }

    @PutMapping("/cancelar/{cnpj}/{osId}")
    @Operation(summary = "Cancelar Ordem de Serviço",
            description = "Cancela a Ordem de Serviço informada.")
    @ApiResponse(responseCode = "200", description = "Ordem de Serviço cancelada",
            content = @Content(schema = @Schema(implementation = OrdemServicoDTO.class)))
    public ResponseEntity<OrdemServicoDTO> cancelarOS(
            @Parameter(description = "CNPJ da oficina", required = true)
            @PathVariable String cnpj,
            @Parameter(description = "ID da Ordem de Serviço", required = true)
            @PathVariable Long osId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ordemServicoService.cancelarOS(cnpj, osId));
    }

    @GetMapping("/listar/{cnpj}")
    @Operation(summary = "Listar todas as Ordens de Serviço",
            description = "Retorna todas as Ordens de Serviço da oficina (paginado).")
    @ApiResponse(responseCode = "200", description = "Ordens retornadas com sucesso",
            content = @Content(schema = @Schema(implementation = OrdemServicoDTO.class)))
    public ResponseEntity<Page<OrdemServicoDTO>> listarTodasOrdens(
            @Parameter(description = "CNPJ da oficina", required = true)
            @PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "numero") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ordemServicoService.listarOrdensDeServico(cnpj, pageable));
    }

}
