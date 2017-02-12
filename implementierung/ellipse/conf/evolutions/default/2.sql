# --- !Ups
insert into administrator (id, username, password, email_address, first_name, last_name)
values (1, 'admin', '$2a$10$nVJZIOx02y8lk5L3N4MAhuE1QFjBKKQDEDBvTHFCc7Jmw4jQPbhG.', 'admin@kit.edu', 'admin', 'admin');
# --- !Downs
delete from administrator
where id=1
