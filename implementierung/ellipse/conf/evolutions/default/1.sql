# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table achievement (
  id                            integer not null,
  name                          varchar(255) not null,
  constraint pk_achievement primary key (id)
);

create table administrator (
  id                            integer not null,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  email_address                 varchar(255) not null,
  first_name                    varchar(255) not null,
  last_name                     varchar(255) not null,
  constraint pk_administrator primary key (id)
);

create table adviser (
  id                            integer not null,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  email_address                 varchar(255) not null,
  first_name                    varchar(255) not null,
  last_name                     varchar(255) not null,
  constraint pk_adviser primary key (id)
);

create table allocation (
  id                            integer not null,
  semester_id                   integer not null,
  name                          varchar(255) not null,
  constraint pk_allocation primary key (id),
  foreign key (semester_id) references semester (id) on delete restrict on update restrict
);

create table allocation_allocation_parameter (
  allocation_id                 integer not null,
  allocation_parameter_id       integer not null,
  constraint pk_allocation_allocation_parameter primary key (allocation_id,allocation_parameter_id),
  foreign key (allocation_id) references allocation (id) on delete restrict on update restrict,
  foreign key (allocation_parameter_id) references allocation_parameter (id) on delete restrict on update restrict
);

create table allocation_parameter (
  id                            integer not null,
  name                          varchar(255) not null,
  value                         integer,
  constraint pk_allocation_parameter primary key (id)
);

create table learning_group (
  id                            integer not null,
  semester_id                   integer not null,
  name                          varchar(255) not null,
  password                      varchar(255) not null,
  is_private                    integer(1),
  constraint pk_learning_group primary key (id),
  foreign key (semester_id) references semester (id) on delete restrict on update restrict
);

create table learning_group_student (
  learning_group_id             integer not null,
  student_id                    integer not null,
  constraint pk_learning_group_student primary key (learning_group_id,student_id),
  foreign key (learning_group_id) references learning_group (id) on delete restrict on update restrict,
  foreign key (student_id) references student (id) on delete restrict on update restrict
);

create table project (
  id                            integer not null,
  semester_id                   integer not null,
  name                          varchar(255) not null,
  min_team_size                 integer,
  max_team_size                 integer,
  number_of_teams               integer,
  project_info                  varchar(255) not null,
  project_url                   varchar(255) not null,
  institute                     varchar(255) not null,
  constraint pk_project primary key (id),
  foreign key (semester_id) references semester (id) on delete restrict on update restrict
);

create table project_adviser (
  project_id                    integer not null,
  adviser_id                    integer not null,
  constraint pk_project_adviser primary key (project_id,adviser_id),
  foreign key (project_id) references project (id) on delete restrict on update restrict,
  foreign key (adviser_id) references adviser (id) on delete restrict on update restrict
);

create table rating (
  id                            integer not null,
  learning_group_id             integer not null,
  rating                        integer,
  project_id                    integer,
  constraint pk_rating primary key (id),
  foreign key (learning_group_id) references learning_group (id) on delete restrict on update restrict,
  foreign key (project_id) references project (id) on delete restrict on update restrict
);

create table spo (
  id                            integer not null,
  name                          varchar(255) not null,
  constraint pk_spo primary key (id)
);

create table spo_achievement_necessary (
  spo_id                        integer not null,
  achievement_id                integer not null,
  constraint pk_spo_achievement_necessary primary key (spo_id,achievement_id),
  foreign key (spo_id) references spo (id) on delete restrict on update restrict,
  foreign key (achievement_id) references achievement (id) on delete restrict on update restrict
);

create table spo_achievement_additional (
  spo_id                        integer not null,
  achievement_id                integer not null,
  constraint pk_spo_achievement_additional primary key (spo_id,achievement_id),
  foreign key (spo_id) references spo (id) on delete restrict on update restrict,
  foreign key (achievement_id) references achievement (id) on delete restrict on update restrict
);

create table semester (
  id                            integer not null,
  wintersemester                integer(1),
  year                          integer,
  name                          varchar(255) not null,
  info_text                     varchar(255) not null,
  final_allocation_id           integer,
  registration_start            time,
  registration_end              time,
  constraint uq_semester_final_allocation_id unique (final_allocation_id),
  constraint pk_semester primary key (id),
  foreign key (final_allocation_id) references allocation (id) on delete restrict on update restrict
);

create table semester_spo (
  semester_id                   integer not null,
  spo_id                        integer not null,
  constraint pk_semester_spo primary key (semester_id,spo_id),
  foreign key (semester_id) references semester (id) on delete restrict on update restrict,
  foreign key (spo_id) references spo (id) on delete restrict on update restrict
);

create table semester_student (
  semester_id                   integer not null,
  student_id                    integer not null,
  constraint pk_semester_student primary key (semester_id,student_id),
  foreign key (semester_id) references semester (id) on delete restrict on update restrict,
  foreign key (student_id) references student (id) on delete restrict on update restrict
);

create table student (
  id                            integer not null,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  email_address                 varchar(255) not null,
  first_name                    varchar(255) not null,
  last_name                     varchar(255) not null,
  matriculation_number          integer,
  spo_id                        integer,
  registered_pse                integer(1),
  registered_tse                integer(1),
  grade_pse                     varchar(3),
  grade_tse                     varchar(3),
  semester                      integer,
  email_verified                integer(1),
  constraint ck_student_grade_pse check (grade_pse in ('130','300','170','500','100','330','400','0','230','200','270')),
  constraint ck_student_grade_tse check (grade_tse in ('130','300','170','500','100','330','400','0','230','200','270')),
  constraint pk_student primary key (id),
  foreign key (spo_id) references spo (id) on delete restrict on update restrict
);

create table student_achievement_completed (
  student_id                    integer not null,
  achievement_id                integer not null,
  constraint pk_student_achievement_completed primary key (student_id,achievement_id),
  foreign key (student_id) references student (id) on delete restrict on update restrict,
  foreign key (achievement_id) references achievement (id) on delete restrict on update restrict
);

create table student_achievement_oral (
  student_id                    integer not null,
  achievement_id                integer not null,
  constraint pk_student_achievement_oral primary key (student_id,achievement_id),
  foreign key (student_id) references student (id) on delete restrict on update restrict,
  foreign key (achievement_id) references achievement (id) on delete restrict on update restrict
);

create table team (
  id                            integer not null,
  allocation_id                 integer not null,
  team_number                   integer,
  project_id                    integer,
  constraint pk_team primary key (id),
  foreign key (allocation_id) references allocation (id) on delete restrict on update restrict,
  foreign key (project_id) references project (id) on delete restrict on update restrict
);

create table team_student (
  team_id                       integer not null,
  student_id                    integer not null,
  constraint pk_team_student primary key (team_id,student_id),
  foreign key (team_id) references team (id) on delete restrict on update restrict,
  foreign key (student_id) references student (id) on delete restrict on update restrict
);


# --- !Downs

drop table if exists achievement;

drop table if exists administrator;

drop table if exists adviser;

drop table if exists allocation;

drop table if exists allocation_allocation_parameter;

drop table if exists allocation_parameter;

drop table if exists learning_group;

drop table if exists learning_group_student;

drop table if exists project;

drop table if exists project_adviser;

drop table if exists rating;

drop table if exists spo;

drop table if exists spo_achievement_necessary;

drop table if exists spo_achievement_additional;

drop table if exists semester;

drop table if exists semester_spo;

drop table if exists semester_student;

drop table if exists student;

drop table if exists student_achievement_completed;

drop table if exists student_achievement_oral;

drop table if exists team;

drop table if exists team_student;

