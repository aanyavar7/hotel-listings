package server.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.common.model.ExchangeRate;

import static server.common.Constants.EndPoint.RATE;

@RestController
@CrossOrigin("*")
public class ExchangeController {

    @Autowired
    ExchangeService exchangeService;

    //Check this?
    @GetMapping("/actuator/info")
    public String info() {
        return getClass().getName();
    }


    @GetMapping(RATE)
    public ResponseEntity<ExchangeRate> getRate(@RequestParam String fromCurrency,
                                                @RequestParam String toCurrency) {
        // Get the ExchangeRate for fromCurrency and toCurrency.
        ExchangeRate exchangeRate = exchangeService.getRate(fromCurrency, toCurrency);
        // Forward to the ExchangeService.

        if (exchangeRate != null) {
            // Return the exchangeRate successfully.
            return ResponseEntity.ok(exchangeRate);
        } else
            // Indicate a failure.
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
