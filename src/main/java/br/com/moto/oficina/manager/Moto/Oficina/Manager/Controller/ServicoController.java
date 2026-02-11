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
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.AtualizarServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.CadastrarServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.ServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.ServicoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/servico")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping("/cadastrar/{cnpj}")
    public ResponseEntity<ServicoDTO> cadastrarServico(@PathVariable String cnpj,
            @RequestBody @Valid CadastrarServicoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoService.cadastrarServico(cnpj, dto));
    }

    @PutMapping("/atualizar/{cnpj}/{idServico}")
    public ResponseEntity<ServicoDTO> atualizarServico(@PathVariable String cnpj, @PathVariable Long idServico,
            @RequestBody @Valid AtualizarServicoDTO dto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(servicoService.atualizarServico(cnpj, idServico, dto));
    }

    @PatchMapping("/ativar/{cnpj}/{idServico}")
    public ResponseEntity<ServicoDTO> ativarServico(@PathVariable String cnpj, @PathVariable Long idServico) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(servicoService.ativarServico(cnpj, idServico));
    }

    @PatchMapping("/desativar/{cnpj}/{idServico}")
    public ResponseEntity<ServicoDTO> desativarServico(@PathVariable String cnpj, @PathVariable Long idServico) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(servicoService.desativarServico(cnpj, idServico));
    }

    @GetMapping("/buscar/{cnpj}/descricao/{descricao}")
    public ResponseEntity<Page<ServicoDTO>> buscarServicoPorDescricao(@PathVariable String cnpj,
            @PathVariable String descricao,
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(servicoService.buscarPorDescricao(cnpj, descricao, pageable));
    }

    @GetMapping("/buscar/{cnpj}")
    public ResponseEntity<Page<ServicoDTO>> buscarTodosServicos(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(servicoService.buscarTodos(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/ativos")
    public ResponseEntity<Page<ServicoDTO>> buscarAtivos(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(servicoService.buscarAtivos(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/inativos")
    public ResponseEntity<Page<ServicoDTO>> buscarInativos(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(servicoService.buscarInativos(cnpj, pageable));
    }
}