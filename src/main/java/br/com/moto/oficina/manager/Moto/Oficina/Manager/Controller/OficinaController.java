package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Api.ReceitaWS;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.AtualizarOficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.CadastrarOficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaBuscaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Oficina.OficinaDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.OficinaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/oficina")
public class OficinaController {

    private final OficinaService oficinaService;
    private final ReceitaWS receitaWS;

    public OficinaController(OficinaService oficinaService, ReceitaWS receitaWS) {
        this.oficinaService = oficinaService;
        this.receitaWS = receitaWS;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarOficina(@RequestBody @Valid CadastrarOficinaDTO dto) {
        oficinaService.cadastrarOficina(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Oficina com o CNPJ " + dto.cnpj() + " cadastrada com sucesso!");
    }

    @PutMapping("/atualizar/{cnpj}")
    public ResponseEntity<String> atualizarOficina(@PathVariable String cnpj,
            @RequestBody AtualizarOficinaDTO dto) {
        oficinaService.atualizarDadosOficina(cnpj, dto);
        return ResponseEntity.status(HttpStatus.OK).body("Oficina atualizada com sucesso!");
    }

    @GetMapping("/localizar/{cnpj}")
    public ResponseEntity<OficinaBuscaDTO> localizarOficina(@PathVariable String cnpj) {
        return ResponseEntity.status(HttpStatus.OK).body(oficinaService.localizarOficina(cnpj));
    }

    @GetMapping("/localizar/receita/{cnpj}")
    public ResponseEntity<OficinaDTO> localizarOficinaReceita(@PathVariable String cnpj) {
        OficinaDTO oficina = receitaWS.buscarCnpj(cnpj);
        return ResponseEntity.status(HttpStatus.OK).body(oficina);
    }
}
