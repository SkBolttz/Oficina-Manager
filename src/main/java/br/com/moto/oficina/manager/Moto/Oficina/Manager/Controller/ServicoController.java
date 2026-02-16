package br.com.moto.oficina.manager.Moto.Oficina.Manager.Controller;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.AtualizarServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.CadastrarServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.DTO.Servico.ServicoDTO;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servico")
@Tag(name = "Serviços", description = "Endpoints responsáveis pelo gerenciamento de serviços da oficina")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @Operation(summary = "Cadastrar novo serviço",
            description = "Cadastra um novo serviço para a oficina informada pelo CNPJ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Serviço cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Oficina não encontrada")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<ServicoDTO> cadastrarServico(
            @RequestBody @Valid CadastrarServicoDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(servicoService.cadastrarServico(dto));
    }

    @Operation(summary = "Atualizar serviço",
            description = "Atualiza os dados de um serviço específico da oficina")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Serviço atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    @PutMapping("/atualizar")
    public ResponseEntity<ServicoDTO> atualizarServico(
            @RequestBody @Valid AtualizarServicoDTO dto) {

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(servicoService.atualizarServico(dto));
    }

    @Operation(summary = "Ativar serviço",
            description = "Ativa um serviço previamente desativado")
    @ApiResponse(responseCode = "202", description = "Serviço ativado com sucesso")
    @PatchMapping("/ativar/{idServico}")
    public ResponseEntity<ServicoDTO> ativarServico(
            @PathVariable Long idServico) {

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(servicoService.ativarServico(idServico));
    }

    @Operation(summary = "Desativar serviço",
            description = "Desativa um serviço da oficina")
    @ApiResponse(responseCode = "202", description = "Serviço desativado com sucesso")
    @PatchMapping("/desativar/{idServico}")
    public ResponseEntity<ServicoDTO> desativarServico(
            @PathVariable Long idServico) {

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(servicoService.desativarServico(idServico));
    }

    @Operation(summary = "Buscar serviço por descrição",
            description = "Busca serviços contendo o texto informado na descrição (paginado)")
    @ApiResponse(responseCode = "200", description = "Serviços listados com sucesso")
    @GetMapping("/buscar/{descricao}")
    public ResponseEntity<Page<ServicoDTO>> buscarServicoPorDescricao(
            @PathVariable String descricao,
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(servicoService.buscarPorDescricao(descricao, pageable));
    }

    @Operation(summary = "Buscar todos os serviços",
            description = "Lista todos os serviços da oficina (paginado)")
    @ApiResponse(responseCode = "200", description = "Serviços listados com sucesso")
    @GetMapping("/buscar")
    public ResponseEntity<Page<ServicoDTO>> buscarTodosServicos(
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(servicoService.buscarTodos(pageable));
    }

    @Operation(summary = "Buscar serviços ativos",
            description = "Lista apenas serviços ativos da oficina")
    @ApiResponse(responseCode = "200", description = "Serviços ativos listados com sucesso")
    @GetMapping("/buscar/ativos")
    public ResponseEntity<Page<ServicoDTO>> buscarAtivos(
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(servicoService.buscarAtivos(pageable));
    }

    @Operation(summary = "Buscar serviços inativos",
            description = "Lista apenas serviços inativos da oficina")
    @ApiResponse(responseCode = "200", description = "Serviços inativos listados com sucesso")
    @GetMapping("/buscar/inativos")
    public ResponseEntity<Page<ServicoDTO>> buscarInativos(
            @PageableDefault(page = 0, size = 20, sort = "descricao") Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(servicoService.buscarInativos(pageable));
    }
}
