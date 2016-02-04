create table player (id serial primary key, name varchar(55), color varchar(6), created timestamp default now());
create table game (id serial primary key, name varchar(55), player_one_id integer references player (id), player_two_id integer references player (id), created timestamp default now(), last_move timestamp default now());
create table move (id serial primary key, nature varchar(255), player_id integer references player (id), game_id integer references game (id), previous_move_id integer references move (id), created timestamp default now());
create table point (id serial primary key, move_id integer references move (id), game_id integer references game (id));
