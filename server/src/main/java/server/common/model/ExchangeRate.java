package server.common.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class keeps track of currency to convert from
 * and the currency to convert to.
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@With
@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * The name of the currency to convert from.
     */
    String fromCurrency;

    /**
     * The name of the currency to convert to.
     */
    String toCurrency;

    /**
     * The exchange rate for the "from" currency to
     * the "to" currency.
     */
    double exchangeRate;
}