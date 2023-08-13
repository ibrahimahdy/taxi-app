/**
 * CREATE Script for init of DB
 */

-- create 2 manufacturer
insert into manufacturer (id, date_created, name) values (1, now(), 'PORSCHE');
insert into manufacturer (id, date_created, name) values (2, now(), 'BMW');

-- create 2 Cars with manufacturer

insert into car (id, date_created, deleted, license_plate, seat_count, rating, manufacturer_id, convertible)
values
 (1, now(), false, 'car01', 4, 7.0, 1, false);


insert into car (id, date_created, deleted, license_plate, seat_count, rating, manufacturer_id, convertible)
values
 (2, now(), false, 'car02', 3, 6.0, 2, true);

-- create 2 electric cars
insert into car (id, date_created, deleted, license_plate, seat_count, engine_type)
values
 (3, now(), false, 'car03', 4, 'ELECTRIC');

insert into car (id, date_created, deleted, license_plate, seat_count, engine_type)
values
 (4, now(), false, 'car04', 5, 'ELECTRIC');

-- create 2 gas cars
insert into car (id, date_created, deleted, license_plate, seat_count, engine_type)
values
 (5, now(), false, 'car05', 4, 'GAS');

insert into car (id, date_created, deleted, license_plate, seat_count, engine_type)
values
 (6, now(), false, 'car06', 2, 'GAS');


-- Create 3 OFFLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username, car_id) values (1, now(), false, 'OFFLINE',
'driver01pw', 'driver01', 1);

insert into driver (id, date_created, deleted, online_status, password, username, car_id) values (2, now(), false, 'OFFLINE',
'driver02pw', 'driver02', null);

insert into driver (id, date_created, deleted, online_status, password, username, car_id) values (3, now(), false, 'OFFLINE',
'driver03pw', 'driver03', null);


-- Create 3 ONLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username, car_id) values (4, now(), false, 'ONLINE',
'driver04pw', 'driver04', 2);

insert into driver (id, date_created, deleted, online_status, password, username, car_id) values (5, now(), false, 'ONLINE',
'driver05pw', 'driver05', null);

insert into driver (id, date_created, deleted, online_status, password, username, car_id) values (6, now(), false, 'ONLINE',
'driver06pw', 'driver06', null);

-- Create 1 OFFLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username, car_id)
values
 (7,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'OFFLINE',
'driver07pw', 'driver07', 3);

-- Create 1 ONLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username, car_id)
values
 (8,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'ONLINE',
'driver08pw', 'driver08', null);


