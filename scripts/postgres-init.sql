create table randomstuff (id serial, item varchar(64));
insert into randomstuff (item) values ('hammer'), ('screw'), ('box');
grant all on randomstuff to developer;
grant all on randomstuff_id_seq to developer;
