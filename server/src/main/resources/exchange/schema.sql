create table exchange_rate (
    id bigint generated by default as identity,
    exchange_rate double,
    from_currency varchar(255),
    to_currency varchar(255),
    primary key (id)
);
