package server.exchange;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import server.common.model.ExchangeRate;

import static org.assertj.core.api.Assertions.assertThat;

// Ensure that app does not create and populate repositories.
@SpringBootTest(properties = {"spring.datasource.schema=", "spring.datasource.data="})
@AutoConfigureMockMvc
// Create test table along with test data.
@Sql({"classpath:/exchange/create.sql"})
public class ExchangeServiceTests {

    @Autowired
    ExchangeService exchangeService;

    @Test
    public void testExchangeRate() {
        ExchangeRate expected = ExchangeRate.builder()
                .fromCurrency("CAN")
                .toCurrency("USD")
                .exchangeRate(0.68)
                .build();

        ExchangeRate result = exchangeService.findExchangeRate(
                expected.getFromCurrency(), expected.getToCurrency());

        assertThat(result.withId(null)).isEqualTo(expected);
    }
}