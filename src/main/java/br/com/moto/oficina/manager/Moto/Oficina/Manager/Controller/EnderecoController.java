package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Endereco.EnderecoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.EnderecoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/endereco")
@Tag(name = "Endereço", description = "Operações de consulta de endereço por CEP")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService){
        this.enderecoService = enderecoService;
    }

    @GetMapping("/buscar/{cep}")
    @Operation(summary = "Buscar endereço por CEP", description = "Retorna os dados do endereço correspondente ao CEP informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito ao buscar endereço", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<EnderecoDTO> buscarCep(
            @Parameter(description = "CEP a ser consultado", required = true) @PathVariable String cep) {

        EnderecoDTO endereco = enderecoService.buscarCep(cep);
        System.out.println(endereco);
        return ResponseEntity.status(HttpStatus.OK).body(endereco);
    }
}