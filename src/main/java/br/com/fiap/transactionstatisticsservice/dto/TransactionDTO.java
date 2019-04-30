package br.com.fiap.transactionstatisticsservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    @ApiModelProperty(notes = "Transaction time in milliseconds at timezone UTC", required = true)
    private Long timestamp;

    @ApiModelProperty(notes = "Transaction amount", required = true)
    private Double amount;
}
