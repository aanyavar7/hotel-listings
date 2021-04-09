package server.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.common.model.ExchangeRate;

/**
 * Currency exchange service.
 */
@Service
public class ExchangeService {
    @Autowired
    ExchangeRepository repository;

    public ExchangeRate getRate(String fromCurrency, String toCurrency) {
        return repository.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency);
    }
}