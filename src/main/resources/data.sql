drop table if exists flight;
drop table if exists cargo_item;
drop table if exists cargo;

create table flight (
    id                          int auto_increment primary key,
    flight_number               int not null,
    departure_airport_iata_code varchar(3) not null,
    arrival_airport_iata_code   varchar(3) not null,
    departure_date              datetime default null,
    zone_offset                 varchar(6) default null
);

create table cargo_item (
    id                          int auto_increment primary key,
    weight                      float not null,
    weight_unit                 varchar(20) not null,
    pieces                      int not null
);

create table cargo (
    id                          int auto_increment primary key,
    flight_id                   int not null,
    cargo_item_id               int not null,
    cargo_type                  varchar(100) not null,
    foreign key (flight_id) references flight (id),
    foreign key (cargo_item_id) references cargo_item (id)
);