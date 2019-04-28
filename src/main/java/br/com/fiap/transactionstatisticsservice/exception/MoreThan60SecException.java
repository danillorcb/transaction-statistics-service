package br.com.fiap.transactionstatisticsservice.exception;

import static br.com.fiap.transactionstatisticsservice.exception.Exceptions.toMap;

public class MoreThan60SecException extends Exception {

    public MoreThan60SecException(String... searchParamsMap) {
        super("The last transaction occurred more than 60 seconds ago " + toMap(String.class, String.class, searchParamsMap));
    }
}
