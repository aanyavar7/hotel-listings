package server.exchange;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.common.model.ExchangeRate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {"spring.datasource.data="})
public class ExchangeServiceTests {

    @Autowired
    ExchangeService exchangeService;

    @MockBean
    ExchangeRepository repository;

    @Test
    public void testExchangeRate() {
        ExchangeRate expected = new ExchangeRate(null, "DEF", "ABC", 2.0);
        List<ExchangeRate> rates = List.of(
                new ExchangeRate(null, "ABC", "DEF", 1.0),
                expected,
                new ExchangeRate(null, "GHI", "JKL", 3.0)
        );

        when(repository
                .findByFromCurrencyAndToCurrency(
                        expected.getFromCurrency(),
                        expected.getToCurrency()))
                .thenReturn(expected);

        ExchangeRate result = exchangeService.getRate(
                expected.getFromCurrency(), expected.getToCurrency());

        assertThat(result).isEqualTo(expected);

        verify(repository).findByFromCurrencyAndToCurrency(
                expected.getFromCurrency(), expected.getToCurrency());
    }
}