package com.example.talentos.exception;

/**
 * Exceção customizada lançada pela camada de serviço quando uma
 * regra de negócio é violada. O GlobalExceptionHandler a converte
 * em um JSON de erro 400 Bad Request para o cliente.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
