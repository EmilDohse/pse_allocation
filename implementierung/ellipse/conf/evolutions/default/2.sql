# --- !Ups
insert into administrator (id, username, password, email_address, first_name, last_name)
values (1, 'admin', '$2a$10$nVJZIOx02y8lk5L3N4MAhuE1QFjBKKQDEDBvTHFCc7Jmw4jQPbhG.', 'admin@kit.edu', 'admin', 'admin');

insert into semester (id, wintersemester, max_group_size, year, name, info_text, final_allocation_id, registration_start, registration_end)
values (1, 0, 6, 2017, 'default', 'Default Semester', 0, 1486636365000, 1486636365000);

insert into general_data (id, current_semester_id)
values (1, 1);

# --- !Downs
delete from general_data
where id=1;

delete from semester
where id=1;

delete from administrator
where id=1;
