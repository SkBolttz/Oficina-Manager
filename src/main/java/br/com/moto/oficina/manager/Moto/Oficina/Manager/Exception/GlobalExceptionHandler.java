package br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception;

import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.API.ErroReceitaFederalException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Endereco.CepNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.CodigoItemDuplicadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.EstoqueInsuficienteException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.EstoqueNuloException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Estoque.ItemEstoqueNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Funcionario.FuncionarioNaoLocalizadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Funcionario.FuncionarioStatusException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.OS.*;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.CNPJInvalidoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.DuplicidadeCnpjException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Oficina.OficinaNaoLocalizadaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Servico.ServicoDuplicadoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Usuario.ErroSenhaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Usuario.UsuarioNaoEncontradoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Veiculo.PlacaDuplicadaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Veiculo.PlacaVaziaException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Veiculo.VeiculoNaoLocalizadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleExceptionGeral(Exception ex) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ApiError(
                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                "Erro interno do servidor",
                                                "Ocorreu um erro inesperado. Contate o suporte."));
        }


        @ExceptionHandler(ErroReceitaFederalException.class)
        public ResponseEntity<ApiError> handleErroReceitaFederal(ErroReceitaFederalException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_GATEWAY)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ApiError(
                                                HttpStatus.BAD_GATEWAY.value(),
                                                "Erro ao consultar a Receita Federal",
                                                "Ocorreu um erro ao tentar obter os dados da Receita Federal. Tente novamente mais tarde."));
        }

        @ExceptionHandler(ClienteNaoLocalizadoException.class)
        public ResponseEntity<ApiError> handleErroClienteNaoLocalizado(ClienteNaoLocalizadoException ex) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ApiError(
                                                HttpStatus.NOT_FOUND.value(),
                                                "Cliente não localizado",
                                                "O cliente com o CPF informado não foi encontrado. Verifique o CPF e tente novamente."));
        }

        @ExceptionHandler(CPFCNPJDuplicadoException.class)
        public ResponseEntity<ApiError> handleErroCPFCNPJDuplicado(CPFCNPJDuplicadoException ex) {

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ApiError(
                                                HttpStatus.CONFLICT.value(),
                                                "CPF/CNPJ duplicado",
                                                "O CPF ou CNPJ informado já está cadastrado no sistema. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(CPFCNPJInvalidoException.class)
        public ResponseEntity<ApiError> handleErroCPFCNPJInvalido(CPFCNPJInvalidoException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ApiError(
                                                HttpStatus.BAD_REQUEST.value(),
                                                "CPF/CNPJ inválido",
                                                "O CPF ou CNPJ informado é inválido. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(CPFCNPJNuloException.class)
        public ResponseEntity<ApiError> handleErroCPFCNPJNulo(CPFCNPJNuloException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ApiError(
                                                HttpStatus.BAD_REQUEST.value(),
                                                "CPF/CNPJ nulo",
                                                "O CPF ou CNPJ não pode ser nulo. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(EmailDuplicadoException.class)
        public ResponseEntity<ApiError> handleErroEmailDuplicado(EmailDuplicadoException ex) {

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ApiError(
                                                HttpStatus.CONFLICT.value(),
                                                "Email duplicado",
                                                "O email informado já está cadastrado no sistema. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(RecursoNaoEncontradoException.class)
        public ResponseEntity<ApiError> handleErroRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Recurso não encontrado",
                                "Recurso solicitado não encontrado. Verifique e tente novamente."));
        }

        @ExceptionHandler(RegraNegocioException.class)
        public ResponseEntity<ApiError> handleErroRegraNegocio(RegraNegocioException ex) {

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Erro de regra de negócio",
                                "Ocorreu um erro de regra de negócio. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(CepNaoLocalizadoException.class)
        public ResponseEntity<ApiError> handleErroCepNaoLocalizado(CepNaoLocalizadoException ex) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "CEP não localizado",
                                "O CEP informado não foi encontrado. Verifique o CEP e tente novamente."));
        }

        @ExceptionHandler(CodigoItemDuplicadoException.class)
        public ResponseEntity<ApiError> handleErroCodigoItemDuplicado(CodigoItemDuplicadoException ex) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.CONFLICT.value(),
                                "Código de item duplicado",
                                "O código do item informado já está cadastrado no sistema. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(EstoqueInsuficienteException.class)
        public ResponseEntity<ApiError> handleErroEstoqueInsuficiente(EstoqueInsuficienteException ex) {

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Estoque insuficiente",
                                "A quantidade solicitada excede o estoque disponível. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(EstoqueNuloException.class)
        public ResponseEntity<ApiError> handleErroEstoqueNulo(EstoqueNuloException ex) {

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Estoque nulo",
                                "O estoque não pode ser nulo. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(ItemEstoqueNaoLocalizadoException.class)
        public ResponseEntity<ApiError> handleErroItemEstoqueNaoLocalizado(ItemEstoqueNaoLocalizadoException ex) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Item de estoque não localizado",
                                "O item de estoque solicitado não foi encontrado. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(FuncionarioNaoLocalizadoException.class)
        public ResponseEntity<ApiError> handleErroFuncionarioNaoLocalizado(FuncionarioNaoLocalizadoException ex) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Funcionário não localizado",
                                "O funcionário solicitado não foi encontrado. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(FuncionarioStatusException.class)
        public ResponseEntity<ApiError> handleErroFuncionarioStatus(FuncionarioStatusException ex) {

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Erro de status do funcionário",
                                "O status do funcionário é inválido para esta operação. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(CNPJInvalidoException.class)
        public ResponseEntity<ApiError> handleErroCNPJInvalido(CNPJInvalidoException ex) {

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "CNPJ inválido",
                                "O CNPJ informado é inválido. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(DuplicidadeCnpjException.class)
        public ResponseEntity<ApiError> handleErroDuplicidadeCnpj(DuplicidadeCnpjException ex) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.CONFLICT.value(),
                                "CNPJ duplicado",
                                "O CNPJ informado já está cadastrado no sistema. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(OficinaNaoLocalizadaException.class)
        public ResponseEntity<ApiError> handleErroOficinaNaoLocalizada(OficinaNaoLocalizadaException ex) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Oficina não localizada",
                                "A oficina solicitada não foi encontrada. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(OSNaoLocalizadaException.class)
        public ResponseEntity<ApiError> handleErroOSNaoLocalizada(OSNaoLocalizadaException ex) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Ordem de serviço não localizada",
                                "A ordem de serviço solicitada não foi encontrada. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(ProdutoNaoLocalizadoException.class)
        public ResponseEntity<ApiError> handleErroProdutoNaoLocalizado(ProdutoNaoLocalizadoException ex) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Produto não localizado",
                                "O produto solicitado não foi encontrado. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(QuantidadeEstoqueException.class)
        public ResponseEntity<ApiError> handleErroQuantidadeEstoque(QuantidadeEstoqueException ex) {

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Quantidade de estoque inválida",
                                "A quantidade solicitada é maior que disponível. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(ServicoDuplicadoOSException.class)
        public ResponseEntity<ApiError> handleErroServicoDuplicadoOS(ServicoDuplicadoOSException ex) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.CONFLICT.value(),
                                "Serviço duplicado",
                                "O serviço informado já está cadastrado na OS. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(ServicoNaoLocalizadoException.class)
        public ResponseEntity<ApiError> handleErroServicoNaoLocalizado(ServicoNaoLocalizadoException ex) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Serviço não localizado",
                                "O serviço solicitado não foi encontrado. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(StatusOSException.class)
        public ResponseEntity<ApiError> handleErroStatusOS(StatusOSException ex) {

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Status da OS inválido",
                                "O status da ordem de serviço é inválido para esta operação. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(StatusProdutoException.class)
        public ResponseEntity<ApiError> handleErroStatusProduto(StatusProdutoException ex) {

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Status do produto inválido",
                                "O status do produto é inválido para esta operação. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(ServicoDuplicadoException.class)
        public ResponseEntity<ApiError> handleErroServicoDuplicado(ServicoDuplicadoException ex) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.CONFLICT.value(),
                                "Serviço duplicado",
                                "O serviço informado já está cadastrado no sistema. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(ErroSenhaException.class)
        public ResponseEntity<ApiError> handleErroSenha(ErroSenhaException ex) {

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Erro de senha",
                                "A senha informada é inválida. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(UsuarioNaoEncontradoException.class)
        public ResponseEntity<ApiError> handleErroUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Usuário não encontrado",
                                "O usuário solicitado não foi encontrado. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(PlacaDuplicadaException.class)
        public ResponseEntity<ApiError> handleErroPlacaDuplicada(PlacaDuplicadaException ex) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.CONFLICT.value(),
                                "Placa duplicada",
                                "A placa informada já está cadastrada no sistema. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(PlacaVaziaException.class)
        public ResponseEntity<ApiError> handleErroPlacaVazia(PlacaVaziaException ex) {

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                "Placa vazia",
                                "A placa não pode ser vazia. Verifique os dados e tente novamente."));
        }

        @ExceptionHandler(VeiculoNaoLocalizadoException.class)
        public ResponseEntity<ApiError> handleErroVeiculoNaoLocalizado(VeiculoNaoLocalizadoException ex) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ApiError(
                                HttpStatus.NOT_FOUND.value(),
                                "Veículo não localizado",
                                "O veículo solicitado não foi encontrado. Verifique os dados e tente novamente."));
        }
}
