package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.OrdemServico.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Enum.OrdemServico.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.OrdemServicoService;

@RestController
@RequestMapping("/ordem-servico")
public class OrdemServicoController {
    
    private final OrdemServicoService ordemServicoService;

    public OrdemServicoController(OrdemServicoService ordemServicoService){
        this.ordemServicoService = ordemServicoService;
    }

    @PostMapping("/cadastrar/{cnpj}/cliente/{cpfCnpj}/responsavel/{cpfFuncionarioResponsavel}")
    public ResponseEntity<OrdemServicoDTO> cadastrarNovaOS(@PathVariable String cnpj, @PathVariable String cpfCnpj, @PathVariable  String cpfFuncionarioResponsavel, @RequestBody CriarOrdemServicoDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemServicoService.criarOrdemServico(cnpj, cpfCnpj,cpfFuncionarioResponsavel, dto));
    }

    @PutMapping("/adicionar-servico/{cnpj}/OS/{osId}")
    public ResponseEntity<OrdemServicoDTO> adicionarServicoOs(@PathVariable String cnpj, @PathVariable Long osId, @RequestBody AdicionarServicoOSDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.adicionarServicoOS(cnpj, osId, dto));
    }

    @PutMapping("/adicionar-produto/{cnpj}/OS/{osId}")
    public ResponseEntity<OrdemServicoDTO> adicionarProdutoOS(@PathVariable String cnpj, @PathVariable Long osId, @RequestBody AdicionarProdutoOSDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.adicionarProdutoOS(cnpj, osId, dto));
    }

    @PutMapping("/remover/{cnpj}/OS/{osId}/servico/{itemServicoId}")
    public ResponseEntity<OrdemServicoDTO> removerServicoOS(@PathVariable String cnpj, @PathVariable Long osId, @PathVariable Long itemServicoId){
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.removerServicoOS(cnpj, osId, itemServicoId));
    }

    @PutMapping("/remover/{cnpj}/OS/{osId}/produto/{itemEstoqueId}")
    public ResponseEntity<OrdemServicoDTO> removerProdutoOS(@PathVariable String cnpj, @PathVariable Long osId, @PathVariable Long itemEstoqueId){
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.removerProdutoOS(cnpj, osId, itemEstoqueId));
    }

    @PutMapping("/finalizar/{cnpj}/{osId}")
    public ResponseEntity<OrdemServicoDTO> finalizarOS(@PathVariable String cnpj, @PathVariable Long osId, @RequestBody FinalizarOsDTO finalizarOsDTO){
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.finalizarOS(cnpj, osId, finalizarOsDTO));
    }

    @PutMapping("/cancelar/{cnpj}/{osId}")
    public ResponseEntity<OrdemServicoDTO> cancelarOS(@PathVariable String cnpj, @PathVariable Long osId) {
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.cancelarOS(cnpj, osId));
    }

    @PutMapping("/iniciar/{cnpj}/{osId}")
    public ResponseEntity<OrdemServicoDTO> iniciarOS(@PathVariable String cnpj, @PathVariable Long osId) {
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.iniciarOS(cnpj, osId));
    }

    @PutMapping("/aguardando-pecas/{cnpj}/{osId}")
    public ResponseEntity<OrdemServicoDTO> aguardarPecasOS(@PathVariable String cnpj, @PathVariable Long osId) {
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.aguardarPecaOS(cnpj, osId));
    }

    @GetMapping("/listar/{cnpj}/OS/{osId}")
    public ResponseEntity<OrdemServicoDTO> buscarOSPorId(@PathVariable String cnpj, @PathVariable Long osId) {
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.listarOrdensDeServicoPorId(cnpj, osId));
    }

    @GetMapping("/listar/{cnpj}/status/{status}")
    public ResponseEntity<Page<OrdemServicoDTO>> listarOrdensPorStatus(@PathVariable String cnpj, @PathVariable Status status, @PageableDefault(page = 0, size = 20, sort = "numero") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.listarOrdensDeServicoPorStatus(cnpj, status, pageable));
    }

    @GetMapping("/listar/{cnpj}")
    public ResponseEntity<Page<OrdemServicoDTO>> listarTodasOrdens(@PathVariable String cnpj, @PageableDefault(page = 0, size = 20, sort = "numero") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(ordemServicoService.listarOrdensDeServico(cnpj, pageable));
    }

    @GetMapping("/listar/{cnpj}/funcionario/{cpfFuncionario}")
    public ResponseEntity<Page<OrdemServicoDTO>> listarOsFuncionario(
            @PathVariable String cnpj,
            @PathVariable String cpfFuncionario,
            @RequestParam(required = false) Status status,
            @PageableDefault(page = 0, size = 20, sort = "numero") Pageable pageable
    ) {
        return ResponseEntity.ok(
                ordemServicoService.listarOsDoFuncionario(cnpj, cpfFuncionario, status, pageable)
        );
    }

}
