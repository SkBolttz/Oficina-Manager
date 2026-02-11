package br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RecursoNaoEncontradoException;
import br.com.moto.oficina.manager.Moto.Oficina.Manager.Exception.Cliente.RegraNegocioException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(RecursoNaoEncontradoException.class)
        public ResponseEntity<ApiError> handleRecursoNaoEncontrado(
                        RecursoNaoEncontradoException ex) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ApiError(
                                                HttpStatus.NOT_FOUND.value(),
                                                "Recurso não encontrado",
                                                ex.getMessage()));
        }

        @ExceptionHandler(RegraNegocioException.class)
        public ResponseEntity<ApiError> handleRegraNegocio(
                        RegraNegocioException ex) {

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(new ApiError(
                                                HttpStatus.BAD_REQUEST.value(),
                                                "Erro de regra de negócio",
                                                ex.getMessage()));
        }

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
}
