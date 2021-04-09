package server.exchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import server.common.model.ExchangeRate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.config.name=",
        "spring.jpa.properties.hibernate.show_sql=false",
})
public class ExchangeRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExchangeRepository repository;

    List<ExchangeRate> rates;

    @BeforeEach
    public void beforeEach() {
        rates = List.of(
                new ExchangeRate(null, "ABC", "DEF", 1.0),
                new ExchangeRate(null, "DEF", "ABC", 2.0),
                new ExchangeRate(null, "GHI", "JKL", 3.0));

        for (ExchangeRate exchangeRate : rates) {
            entityManager.persist(exchangeRate);
        }

        entityManager.flush();
    }

    @Test
    public void testFindExchangeRate() {
        // Ensure that good data succeeds.
        for (ExchangeRate expected : rates) {
            ExchangeRate response =
                    repository.findByFromCurrencyAndToCurrency(
                            expected.getFromCurrency(),
                            expected.getToCurrency()
                    );
            assertThat(response).isSameAs(expected);
        }
    }

    @Test
    public void testFindNonExistingExchangeRate() {
        // Ensure that bad data fails.
        assertThat(repository.findByFromCurrencyAndToCurrency("XXX", "YYY")).isNull();
    }
}