package br.com.fiap.transactionstatisticsservice.exception;

import static br.com.fiap.transactionstatisticsservice.exception.Exceptions.toMap;

public class ServerException extends Exception {

    public ServerException(String... searchParamsMap) {
        super("Internal server error " + toMap(String.class, String.class, searchParamsMap));
    }
}