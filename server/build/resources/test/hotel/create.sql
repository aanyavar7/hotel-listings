DROP TABLE IF EXISTS hotel;

CREATE TABLE hotel (
    id varchar(3) not null,
    hotel_name varchar(255),
    street varchar(255),
    city varchar(255),
    state varchar(255),
    zipcode varchar(255),
    country
    primary key (id)
);

INSERT INTO hotel (
    id,
    hotel_name,
    street,
    city,
    state,
    zipcode,
    country
) VALUES
    ("MAR", "The Ritz Carlton", "10295 Collins Avenue", "Bal Harbour", "FL", "33154", "USA"),
    ("MAR", "St.Regis", "Eighty-Eight, West Paces Ferry Rd NW", "Atlanta", "GA", "30305", "USA"),
    ("MAR", "JW Marriott", "201 8th Ave S", "Nashville", "TN", "37203", "USA"),
    ("MAR", "JW Marriott", "3300 Lenox Rd NE", "Atlanta", "GA", "30326", "USA"),
    ("HLT", "Hampton Inn by Hilton", "310 4th Ave S", "Nashville", "TN", "37201", "USA"),
    ("HLT", "Waldorf Astoria Hotels & Resorts", "3752 Las Vegas Blvd S", "Las Vegas", "NV", "89158", "USA")