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
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.AtualizarVeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.CadastrarVeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Veiculo.VeiculoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.VeiculoService;

@RestController
@RequestMapping("/veiculo")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping("/cadastrar/{cnpj}/{cnpjCpf}")
    public ResponseEntity<VeiculoDTO> cadastrarVeiculo(@PathVariable String cnpj, @PathVariable String cnpjCpf,
            @RequestBody CadastrarVeiculoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(veiculoService.cadastrarVeiculo(cnpj, cnpjCpf, dto));
    }

    @PutMapping("/atualizar/{cnpj}/{placa}")
    public ResponseEntity<VeiculoDTO> atualizarVeiculo(@PathVariable String cnpj, @PathVariable String placa,
            @RequestBody AtualizarVeiculoDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(veiculoService.atualizarVeiculo(cnpj, placa, dto));
    }

    @PatchMapping("/ativar/{cnpj}/{placa}")
    public ResponseEntity<VeiculoDTO> ativarVeiculo(@PathVariable String cnpj, @PathVariable String placa) {
        return ResponseEntity.status(HttpStatus.OK).body(veiculoService.ativarVeiculo(cnpj, placa));
    }

    @PatchMapping("/desativar/{cnpj}/{placa}")
    public ResponseEntity<VeiculoDTO> desativarVeiculo(@PathVariable String cnpj, @PathVariable String placa) {
        return ResponseEntity.status(HttpStatus.OK).body(veiculoService.desativarVeiculo(cnpj, placa));
    }

    @GetMapping("/buscar/{cnpj}/{placa}")
    public ResponseEntity<VeiculoDTO> buscarVeiculo(@PathVariable String cnpj, @PathVariable String placa) {
        return ResponseEntity.status(HttpStatus.OK).body(veiculoService.buscarVeiculoPorPlaca(cnpj, placa));
    }

    @GetMapping("/buscar/{cnpj}/nome/{nome}")
    public ResponseEntity<Page<VeiculoDTO>> buscarVeiculoPorNome(@PathVariable String cnpj, @PathVariable String nome,
            @PageableDefault(page = 0, size = 20, sort = "placa") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(veiculoService.buscarVeiculosPorNomeCliente(cnpj, nome, pageable));
    }

    @GetMapping("/buscar/{cnpj}")
    public ResponseEntity<Page<VeiculoDTO>> buscarTodosVeiculos(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "placa") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(veiculoService.buscarTodos(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/ativo")
    public ResponseEntity<Page<VeiculoDTO>> buscarTodosVeiculosAtivos(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "placa") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(veiculoService.buscarVeiculosAtivos(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/inativo")
    public ResponseEntity<Page<VeiculoDTO>> buscarTodosVeiculosInativos(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "placa") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(veiculoService.buscarVeiculosInativos(cnpj, pageable));
    }
}
