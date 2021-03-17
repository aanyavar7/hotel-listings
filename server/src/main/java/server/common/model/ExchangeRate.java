package server.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;

/**
 * This class keeps track of currency to convert from and the currency
 * to convert to. The @Builder annotation automatically creates a
 * static builder factory method for this class which can be used as
 * follows:
 * <p>
 * CurrencyConversion conversion =
 * CurrencyConversion.builder().to("CAN").from("US");
 */
@Value
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@With
@Entity // For JPA
@Table(name = "EXCHANGE_RATE") // For saving schema
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
    Double exchangeRate;
}