package br.com.fiap.transactionstatisticsservice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDTO {

    @ApiModelProperty(notes = "Sum of transactions about last 60 seconds", required = true)
    private Double sum;

    @ApiModelProperty(notes = "Average value of transactions about last 60 seconds", required = true)
    private Double avg;

    @ApiModelProperty(notes = "Max value of transactions about last 60 seconds", required = true)
    private Double max;

    @ApiModelProperty(notes = "Min value of transactions about last 60 seconds", required = true)
    private Double min;

    @ApiModelProperty(notes = "Count of transactions about last 60 seconds", required = true)
    private Long count;
}
