alter table allocation drop constraint if exists fk_allocation_semester_id;
drop index if exists ix_allocation_semester_id;

alter table allocation_allocation_parameter drop constraint if exists fk_allocation_allocation_parameter_allocation;
drop index if exists ix_allocation_allocation_parameter_allocation;

alter table allocation_allocation_parameter drop constraint if exists fk_allocation_allocation_parameter_allocation_parameter;
drop index if exists ix_allocation_allocation_parameter_allocation_parameter;

alter table general_data drop constraint if exists fk_general_data_current_semester_id;

alter table learning_group drop constraint if exists fk_learning_group_semester_id;
drop index if exists ix_learning_group_semester_id;

alter table learning_group_student drop constraint if exists fk_learning_group_student_learning_group;
drop index if exists ix_learning_group_student_learning_group;

alter table learning_group_student drop constraint if exists fk_learning_group_student_student;
drop index if exists ix_learning_group_student_student;

alter table project drop constraint if exists fk_project_semester_id;
drop index if exists ix_project_semester_id;

alter table project_adviser drop constraint if exists fk_project_adviser_project;
drop index if exists ix_project_adviser_project;

alter table project_adviser drop constraint if exists fk_project_adviser_adviser;
drop index if exists ix_project_adviser_adviser;

alter table rating drop constraint if exists fk_rating_project_id;
drop index if exists ix_rating_project_id;

alter table rating drop constraint if exists fk_rating_learning_group_id;
drop index if exists ix_rating_learning_group_id;

alter table spo_achievement_necessary drop constraint if exists fk_spo_achievement_necessary_spo;
drop index if exists ix_spo_achievement_necessary_spo;

alter table spo_achievement_necessary drop constraint if exists fk_spo_achievement_necessary_achievement;
drop index if exists ix_spo_achievement_necessary_achievement;

alter table spo_achievement_additional drop constraint if exists fk_spo_achievement_additional_spo;
drop index if exists ix_spo_achievement_additional_spo;

alter table spo_achievement_additional drop constraint if exists fk_spo_achievement_additional_achievement;
drop index if exists ix_spo_achievement_additional_achievement;

alter table semester drop constraint if exists fk_semester_final_allocation_id;

alter table semester_spo drop constraint if exists fk_semester_spo_semester;
drop index if exists ix_semester_spo_semester;

alter table semester_spo drop constraint if exists fk_semester_spo_spo;
drop index if exists ix_semester_spo_spo;

alter table semester_student drop constraint if exists fk_semester_student_semester;
drop index if exists ix_semester_student_semester;

alter table semester_student drop constraint if exists fk_semester_student_student;
drop index if exists ix_semester_student_student;

alter table student drop constraint if exists fk_student_spo_id;
drop index if exists ix_student_spo_id;

alter table student_achievement_completed drop constraint if exists fk_student_achievement_completed_student;
drop index if exists ix_student_achievement_completed_student;

alter table student_achievement_completed drop constraint if exists fk_student_achievement_completed_achievement;
drop index if exists ix_student_achievement_completed_achievement;

alter table student_achievement_oral drop constraint if exists fk_student_achievement_oral_student;
drop index if exists ix_student_achievement_oral_student;

alter table student_achievement_oral drop constraint if exists fk_student_achievement_oral_achievement;
drop index if exists ix_student_achievement_oral_achievement;

alter table team drop constraint if exists fk_team_project_id;
drop index if exists ix_team_project_id;

alter table team drop constraint if exists fk_team_allocation_id;
drop index if exists ix_team_allocation_id;

alter table team_student drop constraint if exists fk_team_student_team;
drop index if exists ix_team_student_team;

alter table team_student drop constraint if exists fk_team_student_student;
drop index if exists ix_team_student_student;

drop table if exists achievement;
drop sequence if exists achievement_seq;

drop table if exists administrator;
drop sequence if exists administrator_seq;

drop table if exists adviser;
drop sequence if exists adviser_seq;

drop table if exists allocation;
drop sequence if exists allocation_seq;

drop table if exists allocation_allocation_parameter;

drop table if exists allocation_parameter;
drop sequence if exists allocation_parameter_seq;

drop table if exists general_data;
drop sequence if exists general_data_seq;

drop table if exists learning_group;
drop sequence if exists learning_group_seq;

drop table if exists learning_group_student;

drop table if exists project;
drop sequence if exists project_seq;

drop table if exists project_adviser;

drop table if exists rating;
drop sequence if exists rating_seq;

drop table if exists smtpoptions;
drop sequence if exists smtpoptions_seq;

drop table if exists spo;
drop sequence if exists spo_seq;

drop table if exists spo_achievement_necessary;

drop table if exists spo_achievement_additional;

drop table if exists semester;
drop sequence if exists semester_seq;

drop table if exists semester_spo;

drop table if exists semester_student;

drop table if exists student;
drop sequence if exists student_seq;

drop table if exists student_achievement_completed;

drop table if exists student_achievement_oral;

drop table if exists team;
drop sequence if exists team_seq;

drop table if exists team_student;

