package br.com.fiap.transactionstatisticsservice.controller;

import br.com.fiap.transactionstatisticsservice.dto.StatisticsDTO;
import br.com.fiap.transactionstatisticsservice.dto.TransactionDTO;
import br.com.fiap.transactionstatisticsservice.exception.MoreThan60SecException;
import br.com.fiap.transactionstatisticsservice.exception.ServerException;
import br.com.fiap.transactionstatisticsservice.repository.TransactionRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/transaction-statistics-service")
public class TransactionStatisticsServiceController {

    @Autowired
    private TransactionRepository repository;

    @ApiOperation(httpMethod = "POST", value = "POST method to send transactions")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created",
                    response = Object.class),
            @ApiResponse(
                    code = 204,
                    message = "When the difference between timestamp transaction and system timestamp is more than 60 seconds",
                    response = Object.class)
    })
    @PostMapping("/transactions")
    public ResponseEntity post(
            @ApiParam(format = "Json", name = "TransactionDTO")
            @RequestBody TransactionDTO transaction) throws MoreThan60SecException, ServerException {

        try {
            repository.add(transaction);
        } catch (MoreThan60SecException e) {
            throw e;
        } catch (Exception e) {
            throw new ServerException();
        }
//        if (repository.add(transaction)) {
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "GET", value = "GET method returns statistcs based on the transactions that occurred in the last 60 seconds")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Return StatisticsDTO with success message",
                    response = StatisticsDTO.class)
    })
    @GetMapping(value = "/statistics", produces = "application/json")
    public ResponseEntity<StatisticsDTO> get() {
        Map<Long, TransactionDTO> transactions = repository.getTransactionsIn60s();
        StatisticsDTO statistics = new StatisticsDTO();

        if (transactions.size() == 0) {
            statistics.setSum(0.00);
            statistics.setAvg(0.00);
            statistics.setMax(0.00);
            statistics.setMin(0.00);
            statistics.setCount(0l);
        } else {
            statistics.setSum(
                    transactions.values().stream()
                            .mapToDouble(value -> value.getAmount()).sum()
            );
            statistics.setAvg(
                    transactions.values().stream()
                            .mapToDouble(value -> value.getAmount()).average().getAsDouble()
            );
            statistics.setMax(
                    transactions.values().stream()
                            .mapToDouble(value -> value.getAmount()).max().getAsDouble()
            );
            statistics.setMin(
                    transactions.values().stream()
                            .mapToDouble(value -> value.getAmount()).min().getAsDouble()
            );
            statistics.setCount(
                    transactions.values().stream()
                            .mapToDouble(value -> value.getAmount()).count()
            );
        }

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
