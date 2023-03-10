DROP TABLE IF EXISTS room;

CREATE TABLE room (
    id bigint generated by default as identity,
    guests int,
    roomCode varchar(255),
    price double,
    currency varchar(3),
    hotel Hotel
    primary key (id)
);

INSERT INTO ROOM (
    id,
    guests,
    roomCode,
    price,
    currency,
    hotel
) VALUES
    (default, '2021-03-22', '2021-03-28', 3, "double-double", 1000.0, 'USD', 'HLT'),
    (default, '2021-03-24', '2021-03-28', 3, "double-double", 700.0, 'USD', 'HLT'),
    (default, '2021-03-24', '2021-03-28', 1, "queen", 520.0, 'USD', 'HLT'),
    (default, '2021-03-24', '2021-03-29', 4, "double-double", 700.0, 'USD', 'HLT'),
	(default, '2021-03-21', '2021-03-25', 4, "double-double", 900.0, 'USD', 'MAR'),
	(default, '2021-03-22', '2021-03-26', 1, "king", 760.0, 'USD', 'MAR'),
    (default, '2021-03-22', '2021-03-27', 1, "queen", 650.0, 'USD', 'MAR'),
    (default, '2021-03-22', '2021-03-28', 2, "king", 1000.0, 'USD', 'MAR');