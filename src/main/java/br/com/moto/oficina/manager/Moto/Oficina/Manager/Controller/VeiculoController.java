package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.AtualizarVeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.CadastrarVeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.VeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.VeiculoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculo")
@Tag(name = "Veículos", description = "Endpoints responsáveis pelo gerenciamento de veículos da oficina")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @Operation(summary = "Cadastrar veículo",
            description = "Cadastra um novo veículo vinculado a um cliente da oficina")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veículo cadastrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Oficina ou cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/cadastrar/{cnpj}/{cnpjCpf}")
    public ResponseEntity<VeiculoDTO> cadastrarVeiculo(
            @Parameter(description = "CNPJ da oficina", example = "12345678000199")
            @PathVariable String cnpj,

            @Parameter(description = "CPF ou CNPJ do cliente", example = "12345678901")
            @PathVariable String cnpjCpf,

            @RequestBody CadastrarVeiculoDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(veiculoService.cadastrarVeiculo(cnpj, cnpjCpf, dto));
    }

    @Operation(summary = "Atualizar veículo",
            description = "Atualiza os dados de um veículo da oficina")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @PutMapping("/atualizar/{cnpj}/{placa}")
    public ResponseEntity<VeiculoDTO> atualizarVeiculo(
            @Parameter(description = "CNPJ da oficina", example = "12345678000199")
            @PathVariable String cnpj,

            @Parameter(description = "Placa do veículo", example = "ABC1D23")
            @PathVariable String placa,

            @RequestBody AtualizarVeiculoDTO dto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(veiculoService.atualizarVeiculo(cnpj, placa, dto));
    }

    @Operation(summary = "Ativar veículo",
            description = "Ativa um veículo previamente desativado")
    @ApiResponse(responseCode = "200", description = "Veículo ativado com sucesso")
    @PatchMapping("/ativar/{cnpj}/{placa}")
    public ResponseEntity<VeiculoDTO> ativarVeiculo(
            @PathVariable String cnpj,
            @PathVariable String placa) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(veiculoService.ativarVeiculo(cnpj, placa));
    }

    @Operation(summary = "Desativar veículo",
            description = "Desativa um veículo da oficina")
    @ApiResponse(responseCode = "200", description = "Veículo desativado com sucesso")
    @PatchMapping("/desativar/{cnpj}/{placa}")
    public ResponseEntity<VeiculoDTO> desativarVeiculo(
            @PathVariable String cnpj,
            @PathVariable String placa) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(veiculoService.desativarVeiculo(cnpj, placa));
    }

    @Operation(summary = "Buscar veículo por placa",
            description = "Retorna os dados de um veículo específico pela placa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    @GetMapping("/buscar/{cnpj}/{placa}")
    public ResponseEntity<VeiculoDTO> buscarVeiculo(
            @PathVariable String cnpj,
            @PathVariable String placa) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(veiculoService.buscarVeiculoPorPlaca(cnpj, placa));
    }

    @Operation(summary = "Buscar veículos por nome do cliente",
            description = "Lista veículos vinculados a clientes cujo nome contenha o texto informado")
    @ApiResponse(responseCode = "200", description = "Veículos listados com sucesso")
    @GetMapping("/buscar/{cnpj}/nome/{nome}")
    public ResponseEntity<Page<VeiculoDTO>> buscarVeiculoPorNome(
            @PathVariable String cnpj,
            @PathVariable String nome,
            @PageableDefault(page = 0, size = 20, sort = "placa") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(veiculoService.buscarVeiculosPorNomeCliente(cnpj, nome, pageable));
    }

    @Operation(summary = "Buscar todos os veículos",
            description = "Lista todos os veículos cadastrados na oficina (paginado)")
    @ApiResponse(responseCode = "200", description = "Veículos listados com sucesso")
    @GetMapping("/buscar/{cnpj}")
    public ResponseEntity<Page<VeiculoDTO>> buscarTodosVeiculos(
            @PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "placa") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(veiculoService.buscarTodos(cnpj, pageable));
    }

    @Operation(summary = "Buscar veículos ativos",
            description = "Lista apenas veículos ativos da oficina")
    @ApiResponse(responseCode = "200", description = "Veículos ativos listados com sucesso")
    @GetMapping("/buscar/{cnpj}/ativo")
    public ResponseEntity<Page<VeiculoDTO>> buscarTodosVeiculosAtivos(
            @PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "placa") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(veiculoService.buscarVeiculosAtivos(cnpj, pageable));
    }

    @Operation(summary = "Buscar veículos inativos",
            description = "Lista apenas veículos inativos da oficina")
    @ApiResponse(responseCode = "200", description = "Veículos inativos listados com sucesso")
    @GetMapping("/buscar/{cnpj}/inativo")
    public ResponseEntity<Page<VeiculoDTO>> buscarTodosVeiculosInativos(
            @PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "placa") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(veiculoService.buscarVeiculosInativos(cnpj, pageable));
    }
}
