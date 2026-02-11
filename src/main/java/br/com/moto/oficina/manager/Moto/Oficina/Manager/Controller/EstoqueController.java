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

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping("/cadastrar/{cnpj}")
    public ResponseEntity<EstoqueDTO> cadastrarItemEstoque(@PathVariable String cnpj,
            @RequestBody CadastroEstoqueDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueService.cadastrarItemEstoque(cnpj, dto));
    }

    @PutMapping("/atualizar/{cnpj}/{idItem}")
    public ResponseEntity<EstoqueDTO> atualizarItemEstoque(@PathVariable String cnpj, @PathVariable Long idItem,
            @RequestBody AtualizarEstoqueDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.atualizarItemEstoque(cnpj, idItem, dto));
    }

    @PatchMapping("/ativar/{cnpj}/{idItem}")
    public ResponseEntity<EstoqueDTO> ativarItemEstoque(@PathVariable String cnpj, @PathVariable Long idItem) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(estoqueService.ativarItemEstoque(cnpj, idItem));
    }

    @PatchMapping("/desativar/{cnpj}/{idItem}")
    public ResponseEntity<EstoqueDTO> desativarItemEstoque(@PathVariable String cnpj, @PathVariable Long idItem) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(estoqueService.desativarItemEstoque(cnpj, idItem));
    }

    @GetMapping("/buscar/{cnpj}/codigo/{codigoItem}")
    public ResponseEntity<EstoqueDTO> buscarItemEstoque(@PathVariable String cnpj, @PathVariable String codigoItem) {

        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarItemEstoque(cnpj, codigoItem));
    }

    @GetMapping("/buscar/{cnpj}/nome/{nome}")
    public ResponseEntity<Page<EstoqueDTO>> buscarItemEstoquePorNome(@PathVariable String cnpj,
            @PathVariable String nome,
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarItemEstoquePorNome(cnpj, nome, pageable));
    }

    @GetMapping("/buscar/{cnpj}/ativos")
    public ResponseEntity<Page<EstoqueDTO>> buscarItensEstoqueAtivos(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarItensEstoqueAtivos(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/inativos")
    public ResponseEntity<Page<EstoqueDTO>> buscarItensEstoqueInativos(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarItensEstoqueInativos(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}")
    public ResponseEntity<Page<EstoqueDTO>> buscarTodosItensEstoque(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarTodosItens(cnpj, pageable));
    }
}
