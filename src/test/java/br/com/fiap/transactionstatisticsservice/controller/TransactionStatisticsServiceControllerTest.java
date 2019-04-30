package br.com.fiap.transactionstatisticsservice.controller;

import br.com.fiap.transactionstatisticsservice.dto.StatisticsDTO;
import br.com.fiap.transactionstatisticsservice.dto.TransactionDTO;
import br.com.fiap.transactionstatisticsservice.exception.MoreThan60SecException;
import br.com.fiap.transactionstatisticsservice.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionStatisticsServiceController.class)
public class TransactionStatisticsServiceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionRepository repository;

    private Long systemTimestamp;

    @Before
    public void setUp() throws Exception {
        systemTimestamp = System.currentTimeMillis();
    }

    @Test
    public void transactionCreated() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(systemTimestamp - 1, 300.00);

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        String jsonInString = mapper.writeValueAsString(transactionDTO);

        when(this.repository.add(transactionDTO)).thenCallRealMethod(); // thenReturn(true);

        mvc.perform(post("/transaction-statistics-service/transactions/")
                .contentType("application/json").content(jsonInString))
                .andExpect(status().isCreated());
    }

    @Test
    public void transactionNoContent() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO(systemTimestamp - 60001, 300.00);

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        String jsonInString = mapper.writeValueAsString(transactionDTO);

        when(this.repository.add(transactionDTO)).thenCallRealMethod(); //thenThrow(MoreThan60SecException.class);

        mvc.perform(post("/transaction-statistics-service/transactions/")
                .contentType("application/json").content(jsonInString))
                .andExpect(status().isNoContent());
    }

    @Test
    public void statisticsFoundedIn60Sec() throws Exception {
        StatisticsDTO statisticsDTO = new StatisticsDTO(
                900.00,
                300.00,
                400.00,
                200.00,
                3l
        );

        TransactionDTO t1 = new TransactionDTO(systemTimestamp - 1, 300.00);
        TransactionDTO t2 = new TransactionDTO(systemTimestamp - 2, 200.00);
        TransactionDTO t3 = new TransactionDTO(systemTimestamp - 3, 400.00);

        this.repository.add(t1);
        this.repository.add(t2);
        this.repository.add(t3);

        Map<Long, TransactionDTO> transactionsIn60s = new HashMap<>();
        if (systemTimestamp - t1.getTimestamp() <= 60000) {
            transactionsIn60s.put(t1.getTimestamp(), t1);
        }
        if (systemTimestamp - t2.getTimestamp() <= 60000) {
            transactionsIn60s.put(t2.getTimestamp(), t2);
        }
        if (systemTimestamp - t3.getTimestamp() <= 60000) {
            transactionsIn60s.put(t3.getTimestamp(), t3);
        }

        when(this.repository.getTransactionsIn60s()).thenReturn(transactionsIn60s);

        mvc.perform(get("/transaction-statistics-service/statistics/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(response -> {
                    String json = response.getResponse().getContentAsString();
                    StatisticsDTO statisticsDTOFounded = new ObjectMapper().readValue(json, StatisticsDTO.class);
                    assertThat(statisticsDTO).isEqualToComparingFieldByField(statisticsDTOFounded);
                });
    }

    @Test
    public void statisticsNotFoundedIn60Sec() throws Exception {
        StatisticsDTO statisticsDTO = new StatisticsDTO(
                0.00,
                0.00,
                0.00,
                0.00,
                0l
        );

        TransactionDTO t1 = new TransactionDTO(systemTimestamp - 60001, 300.00);
        TransactionDTO t2 = new TransactionDTO(systemTimestamp - 60002, 200.00);
        TransactionDTO t3 = new TransactionDTO(systemTimestamp - 60003, 400.00);

        this.repository.add(t1);
        this.repository.add(t2);
        this.repository.add(t3);

        Map<Long, TransactionDTO> transactionsIn60s = new HashMap<>();
        if (systemTimestamp - t1.getTimestamp() <= 60000) {
            transactionsIn60s.put(t1.getTimestamp(), t1);
        }
        if (systemTimestamp - t2.getTimestamp() <= 60000) {
            transactionsIn60s.put(t2.getTimestamp(), t2);
        }
        if (systemTimestamp - t3.getTimestamp() <= 60000) {
            transactionsIn60s.put(t3.getTimestamp(), t3);
        }

        when(this.repository.getTransactionsIn60s()).thenReturn(transactionsIn60s);

        mvc.perform(get("/transaction-statistics-service/statistics/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(response -> {
                    String json = response.getResponse().getContentAsString();
                    StatisticsDTO statisticsDTOFounded = new ObjectMapper().readValue(json, StatisticsDTO.class);
                    assertThat(statisticsDTO).isEqualToComparingFieldByField(statisticsDTOFounded);
                });
    }
}
