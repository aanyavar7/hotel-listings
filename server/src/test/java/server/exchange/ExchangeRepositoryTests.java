package server.exchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import server.common.model.ExchangeRate;

import static org.assertj.core.api.Assertions.assertThat;

// Ensure that app does not create and populate repositories.
@DataJpaTest(properties = {"spring.datasource.schema=", "spring.datasource.data="})
public class ExchangeRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExchangeRepository repository;

    @BeforeEach
    public void beforeEach() {
        entityManager.clear();
    }

    @Test
    public void testFindExchangeRate() {
        ExchangeRate[] exchangeRates = new ExchangeRate[]{
                new ExchangeRate(null, "USD", "CAN", 1.32),
                new ExchangeRate(null, "USD", "JPY", 108.98),
                new ExchangeRate(null, "CAN", "USD", 0.68),
                new ExchangeRate(null, "CAN", "JPY", 87.36),
                new ExchangeRate(null, "JPY", "USD", 0.0092),
                new ExchangeRate(null, "JPY", "CAN", 0.011)
        };

        for (ExchangeRate exchangeRate : exchangeRates) {
            entityManager.persist(exchangeRate);
        }

        entityManager.flush();

        // Ensure that good data succeeds.
        for (ExchangeRate expected : exchangeRates) {
            ExchangeRate response =
                    repository.findByFromCurrencyAndToCurrency(
                            expected.getFromCurrency(),
                            expected.getToCurrency()
                    );
            assertThat(response).isSameAs(expected);
        }

        // Ensure that bad data fails.
        assertThat(repository.findByFromCurrencyAndToCurrency("XXX", "YYY")).isNull();
    }
}