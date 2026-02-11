package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.EnderecoService;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {
    
    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService){
        this.enderecoService = enderecoService;
    }

    @GetMapping("/buscar/{cep}")
    public ResponseEntity<EnderecoDTO> buscarCep(@PathVariable String cep){

        EnderecoDTO endereco = enderecoService.buscarCep(cep);
        System.out.println(endereco);
        return ResponseEntity.status(HttpStatus.OK).body(endereco);
    }
}
