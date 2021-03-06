create table achievement (
  id                            integer not null,
  name                          varchar(255) not null,
  constraint pk_achievement primary key (id)
);
create sequence achievement_seq;

create table administrator (
  id                            integer not null,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  email_address                 varchar(255) not null,
  first_name                    varchar(255) not null,
  last_name                     varchar(255) not null,
  constraint pk_administrator primary key (id)
);
create sequence administrator_seq;

create table adviser (
  id                            integer not null,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  email_address                 varchar(255) not null,
  first_name                    varchar(255) not null,
  last_name                     varchar(255) not null,
  constraint pk_adviser primary key (id)
);
create sequence adviser_seq;

create table allocation (
  id                            integer not null,
  name                          varchar(255) not null,
  semester_id                   integer,
  constraint pk_allocation primary key (id)
);
create sequence allocation_seq;

create table allocation_allocation_parameter (
  allocation_id                 integer not null,
  allocation_parameter_id       integer not null,
  constraint pk_allocation_allocation_parameter primary key (allocation_id,allocation_parameter_id)
);

create table allocation_parameter (
  id                            integer not null,
  name                          varchar(255) not null,
  value                         integer,
  constraint pk_allocation_parameter primary key (id)
);
create sequence allocation_parameter_seq;

create table general_data (
  id                            integer not null,
  current_semester_id           integer,
  constraint uq_general_data_current_semester_id unique (current_semester_id),
  constraint pk_general_data primary key (id)
);
create sequence general_data_seq;

create table learning_group (
  id                            integer not null,
  name                          varchar(255) not null,
  password                      varchar(255) not null,
  semester_id                   integer,
  is_private                    boolean,
  constraint pk_learning_group primary key (id)
);
create sequence learning_group_seq;

create table learning_group_student (
  learning_group_id             integer not null,
  student_id                    integer not null,
  constraint pk_learning_group_student primary key (learning_group_id,student_id)
);

create table project (
  id                            integer not null,
  name                          varchar(255) not null,
  min_team_size                 integer,
  max_team_size                 integer,
  number_of_teams               integer,
  project_info                  varchar(255) not null,
  project_url                   varchar(255) not null,
  institute                     varchar(255) not null,
  semester_id                   integer,
  constraint pk_project primary key (id)
);
create sequence project_seq;

create table project_adviser (
  project_id                    integer not null,
  adviser_id                    integer not null,
  constraint pk_project_adviser primary key (project_id,adviser_id)
);

create table rating (
  id                            integer not null,
  rating_value                  integer,
  project_id                    integer,
  learning_group_id             integer,
  constraint pk_rating primary key (id)
);
create sequence rating_seq;

create table smtpoptions (
  id                            integer not null,
  host                          varchar(255) not null,
  mail_from                     varchar(255) not null,
  port                          integer,
  ssl                           boolean,
  tls                           boolean,
  debug                         boolean,
  timeout                       integer,
  connection_timeout            integer,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  constraint pk_smtpoptions primary key (id)
);
create sequence smtpoptions_seq;

create table spo (
  id                            integer not null,
  name                          varchar(255) not null,
  constraint pk_spo primary key (id)
);
create sequence spo_seq;

create table spo_achievement_necessary (
  spo_id                        integer not null,
  achievement_id                integer not null,
  constraint pk_spo_achievement_necessary primary key (spo_id,achievement_id)
);

create table spo_achievement_additional (
  spo_id                        integer not null,
  achievement_id                integer not null,
  constraint pk_spo_achievement_additional primary key (spo_id,achievement_id)
);

create table semester (
  id                            integer not null,
  wintersemester                boolean,
  max_group_size                integer,
  name                          varchar(255) not null,
  info_text                     varchar(255) not null,
  final_allocation_id           integer,
  registration_start            timestamp,
  registration_end              timestamp,
  constraint uq_semester_final_allocation_id unique (final_allocation_id),
  constraint pk_semester primary key (id)
);
create sequence semester_seq;

create table semester_spo (
  semester_id                   integer not null,
  spo_id                        integer not null,
  constraint pk_semester_spo primary key (semester_id,spo_id)
);

create table semester_student (
  semester_id                   integer not null,
  student_id                    integer not null,
  constraint pk_semester_student primary key (semester_id,student_id)
);

create table student (
  id                            integer not null,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  email_address                 varchar(255) not null,
  first_name                    varchar(255) not null,
  last_name                     varchar(255) not null,
  matriculationnumber           integer,
  spo_id                        integer,
  registered_pse                boolean,
  registered_tse                boolean,
  grade_pse                     varchar(3),
  grade_tse                     varchar(3),
  semester                      integer,
  email_verified                boolean,
  constraint ck_student_grade_pse check (grade_pse in ('130','300','170','500','100','330','400','0','230','200','270')),
  constraint ck_student_grade_tse check (grade_tse in ('130','300','170','500','100','330','400','0','230','200','270')),
  constraint pk_student primary key (id)
);
create sequence student_seq;

create table student_achievement_completed (
  student_id                    integer not null,
  achievement_id                integer not null,
  constraint pk_student_achievement_completed primary key (student_id,achievement_id)
);

create table student_achievement_oral (
  student_id                    integer not null,
  achievement_id                integer not null,
  constraint pk_student_achievement_oral primary key (student_id,achievement_id)
);

create table team (
  id                            integer not null,
  team_number                   integer,
  project_id                    integer,
  allocation_id                 integer,
  constraint pk_team primary key (id)
);
create sequence team_seq;

create table team_student (
  team_id                       integer not null,
  student_id                    integer not null,
  constraint pk_team_student primary key (team_id,student_id)
);

alter table allocation add constraint fk_allocation_semester_id foreign key (semester_id) references semester (id) on delete restrict on update restrict;
create index ix_allocation_semester_id on allocation (semester_id);

alter table allocation_allocation_parameter add constraint fk_allocation_allocation_parameter_allocation foreign key (allocation_id) references allocation (id) on delete restrict on update restrict;
create index ix_allocation_allocation_parameter_allocation on allocation_allocation_parameter (allocation_id);

alter table allocation_allocation_parameter add constraint fk_allocation_allocation_parameter_allocation_parameter foreign key (allocation_parameter_id) references allocation_parameter (id) on delete restrict on update restrict;
create index ix_allocation_allocation_parameter_allocation_parameter on allocation_allocation_parameter (allocation_parameter_id);

alter table general_data add constraint fk_general_data_current_semester_id foreign key (current_semester_id) references semester (id) on delete restrict on update restrict;

alter table learning_group add constraint fk_learning_group_semester_id foreign key (semester_id) references semester (id) on delete restrict on update restrict;
create index ix_learning_group_semester_id on learning_group (semester_id);

alter table learning_group_student add constraint fk_learning_group_student_learning_group foreign key (learning_group_id) references learning_group (id) on delete restrict on update restrict;
create index ix_learning_group_student_learning_group on learning_group_student (learning_group_id);

alter table learning_group_student add constraint fk_learning_group_student_student foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_learning_group_student_student on learning_group_student (student_id);

alter table project add constraint fk_project_semester_id foreign key (semester_id) references semester (id) on delete restrict on update restrict;
create index ix_project_semester_id on project (semester_id);

alter table project_adviser add constraint fk_project_adviser_project foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_project_adviser_project on project_adviser (project_id);

alter table project_adviser add constraint fk_project_adviser_adviser foreign key (adviser_id) references adviser (id) on delete restrict on update restrict;
create index ix_project_adviser_adviser on project_adviser (adviser_id);

alter table rating add constraint fk_rating_project_id foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_rating_project_id on rating (project_id);

alter table rating add constraint fk_rating_learning_group_id foreign key (learning_group_id) references learning_group (id) on delete restrict on update restrict;
create index ix_rating_learning_group_id on rating (learning_group_id);

alter table spo_achievement_necessary add constraint fk_spo_achievement_necessary_spo foreign key (spo_id) references spo (id) on delete restrict on update restrict;
create index ix_spo_achievement_necessary_spo on spo_achievement_necessary (spo_id);

alter table spo_achievement_necessary add constraint fk_spo_achievement_necessary_achievement foreign key (achievement_id) references achievement (id) on delete restrict on update restrict;
create index ix_spo_achievement_necessary_achievement on spo_achievement_necessary (achievement_id);

alter table spo_achievement_additional add constraint fk_spo_achievement_additional_spo foreign key (spo_id) references spo (id) on delete restrict on update restrict;
create index ix_spo_achievement_additional_spo on spo_achievement_additional (spo_id);

alter table spo_achievement_additional add constraint fk_spo_achievement_additional_achievement foreign key (achievement_id) references achievement (id) on delete restrict on update restrict;
create index ix_spo_achievement_additional_achievement on spo_achievement_additional (achievement_id);

alter table semester add constraint fk_semester_final_allocation_id foreign key (final_allocation_id) references allocation (id) on delete restrict on update restrict;

alter table semester_spo add constraint fk_semester_spo_semester foreign key (semester_id) references semester (id) on delete restrict on update restrict;
create index ix_semester_spo_semester on semester_spo (semester_id);

alter table semester_spo add constraint fk_semester_spo_spo foreign key (spo_id) references spo (id) on delete restrict on update restrict;
create index ix_semester_spo_spo on semester_spo (spo_id);

alter table semester_student add constraint fk_semester_student_semester foreign key (semester_id) references semester (id) on delete restrict on update restrict;
create index ix_semester_student_semester on semester_student (semester_id);

alter table semester_student add constraint fk_semester_student_student foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_semester_student_student on semester_student (student_id);

alter table student add constraint fk_student_spo_id foreign key (spo_id) references spo (id) on delete restrict on update restrict;
create index ix_student_spo_id on student (spo_id);

alter table student_achievement_completed add constraint fk_student_achievement_completed_student foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_student_achievement_completed_student on student_achievement_completed (student_id);

alter table student_achievement_completed add constraint fk_student_achievement_completed_achievement foreign key (achievement_id) references achievement (id) on delete restrict on update restrict;
create index ix_student_achievement_completed_achievement on student_achievement_completed (achievement_id);

alter table student_achievement_oral add constraint fk_student_achievement_oral_student foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_student_achievement_oral_student on student_achievement_oral (student_id);

alter table student_achievement_oral add constraint fk_student_achievement_oral_achievement foreign key (achievement_id) references achievement (id) on delete restrict on update restrict;
create index ix_student_achievement_oral_achievement on student_achievement_oral (achievement_id);

alter table team add constraint fk_team_project_id foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_team_project_id on team (project_id);

alter table team add constraint fk_team_allocation_id foreign key (allocation_id) references allocation (id) on delete restrict on update restrict;
create index ix_team_allocation_id on team (allocation_id);

alter table team_student add constraint fk_team_student_team foreign key (team_id) references team (id) on delete restrict on update restrict;
create index ix_team_student_team on team_student (team_id);

alter table team_student add constraint fk_team_student_student foreign key (student_id) references student (id) on delete restrict on update restrict;
create index ix_team_student_student on team_student (student_id);

