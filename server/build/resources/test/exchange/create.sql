DROP TABLE IF EXISTS exchange;

CREATE TABLE exchange (
    id INT DEFAULT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    exchange_rate DOUBLE,
    from_currency VARCHAR(3) NOT NULL,
    to_currency VARCHAR(3) NOT NULL
);

INSERT INTO EXCHANGE_RATE (
    id,
    from_currency,
    to_currency,
    exchange_rate
) VALUES
	(default, 'USD', 'CAN', 1.32),
	(default, 'USD', 'JPY', 108.98),
	(default, 'CAN', 'USD', 0.68),
	(default, 'CAN', 'JPY', 87.36),
	(default, 'JPY', 'USD', 0.0092),
	(default, 'JPY', 'CAN', 0.011);