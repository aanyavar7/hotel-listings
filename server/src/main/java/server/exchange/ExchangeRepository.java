package server.exchange;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import server.common.model.ExchangeRate;


/**
 * Currency exchange repository.
 */
@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeRate, Long> {
    ExchangeRate findByFromCurrencyAndToCurrency(
            @Param("fromCurrency") String fromCurrency,
            @Param("toCurrency") String toCurrency);
}