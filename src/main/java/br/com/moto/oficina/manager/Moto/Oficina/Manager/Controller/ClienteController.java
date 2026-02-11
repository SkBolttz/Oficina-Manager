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

@RestController
@RequestMapping("/cliente")
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
    public ResponseEntity<ClienteDTO> cadastrarCliente(
            @PathVariable String cnpj,
            @RequestBody @Valid ClienteCadastroDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.cadastrarCliente(cnpj, dto));
    }

    @PutMapping("/atualizar/{cnpj}/{cnpjCpf}")
    public ResponseEntity<ClienteDTO> atualizarCliente(
            @PathVariable String cnpj,
            @PathVariable String cnpjCpf,
            @RequestBody @Valid ClienteAtualizarDTO dto) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(clienteService.editarCliente(cnpj, cnpjCpf, dto));
    }

    @PutMapping("/ativar/{cnpj}/{cnpjCpf}")
    public ResponseEntity<ClienteDTO> ativarCliente(
            @PathVariable String cnpj,
            @PathVariable String cnpjCpf) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(clienteService.ativarCliente(cnpj, cnpjCpf));
    }

    @PutMapping("/desativar/{cnpj}/{cnpjCpf}")
    public ResponseEntity<ClienteDTO> desativarCliente(
            @PathVariable String cnpj,
            @PathVariable String cnpjCpf) {

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
    public ResponseEntity<ClienteDTO> buscarPorCpfCnpj(
            @PathVariable String cnpj,
            @PathVariable String cpfCnpj) {

        return ResponseEntity.ok(
                clienteService.buscarClientePorCpfCnpj(cnpj, cpfCnpj));
    }

    @GetMapping("/buscar/{cnpj}/nome/{nome}")
    public ResponseEntity<Page<ClienteDTO>> buscarPorNome(
            @PathVariable String cnpj,
            @PathVariable String nome,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(
                clienteService.buscarClientePorNome(cnpj, nome, pageable));
    }

    @GetMapping("/buscar/{cnpj}")
    public ResponseEntity<Page<ClienteDTO>> buscarTodosClientes(
            @PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(
                clienteService.buscarTodosClientes(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/ativos")
    public ResponseEntity<Page<ClienteDTO>> buscarTodosClientesAtivos(
            @PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(
                clienteService.buscarTodosAtivos(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/inativos")
    public ResponseEntity<Page<ClienteDTO>> buscarTodosClientesInativos(
            @PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(
                clienteService.buscarTodosInativos(cnpj, pageable));
    }
}
