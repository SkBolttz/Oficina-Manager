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

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.AtualizarFuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.CadastrarFuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Funcionario.FuncionarioDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.FuncionarioService;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping("/cadastrar/{cnpj}")
    public ResponseEntity<FuncionarioDTO> cadastrarFuncionario(@PathVariable String cnpj,
            @RequestBody CadastrarFuncionarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioService.cadastrarFuncionario(cnpj, dto));
    }

    @PutMapping("/atualizar/{cnpj}/{cpf}")
    public ResponseEntity<FuncionarioDTO> atualizarFuncionario(@PathVariable String cnpj, @PathVariable String cpf,
            @RequestBody AtualizarFuncionarioDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioService.atualizarFuncionario(cnpj, cpf, dto));
    }

    @PatchMapping("/ativar/{cnpj}/{cpf}")
    public ResponseEntity<FuncionarioDTO> ativarFuncionario(@PathVariable String cnpj, @PathVariable String cpf) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(funcionarioService.ativarFuncionario(cnpj, cpf));
    }

    @PatchMapping("/desativar/{cnpj}/{cpf}")
    public ResponseEntity<FuncionarioDTO> inativarFuncionario(@PathVariable String cnpj, @PathVariable String cpf) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(funcionarioService.inativarFuncionario(cnpj, cpf));
    }

    @GetMapping("/buscar/{cnpj}/cpf/{cpf}")
    public ResponseEntity<FuncionarioDTO> buscarFuncionario(@PathVariable String cnpj, @PathVariable String cpf) {
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioService.buscarFuncionarioPorCPF(cnpj, cpf));
    }

    @GetMapping("/buscar/{cnpj}/nome/{nome}")
    public ResponseEntity<Page<FuncionarioDTO>> buscarFuncionarioPorNome(@PathVariable String cnpj,
            @PathVariable String nome,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(funcionarioService.buscarFuncionarioPorNome(cnpj, nome, pageable));
    }

    @GetMapping("/buscar/{cnpj}")
    public ResponseEntity<Page<FuncionarioDTO>> buscarTodosFuncionarios(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioService.buscarTodosFuncionarios(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/ativo")
    public ResponseEntity<Page<FuncionarioDTO>> buscarFuncionariosAtivos(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(funcionarioService.buscarTodosFuncionariosAtivos(cnpj, pageable));
    }

    @GetMapping("/buscar/{cnpj}/inativo")
    public ResponseEntity<Page<FuncionarioDTO>> buscarFuncionariosInativos(@PathVariable String cnpj,
            @PageableDefault(page = 0, size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(funcionarioService.buscarTodosFuncionariosInativos(cnpj, pageable));
    }
}
