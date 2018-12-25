CREATE TABLE t_org_company(
id BIGINT,
company_name VARCHAR(200),
parent_company_id BIGINT,
build_time TIMESTAMP,
create_time TIMESTAMP,
update_time TIMESTAMP,
PRIMARY KEY(id)
);

CREATE TABLE t_org_department (
id BIGINT,
department_name VARCHAR(200),
company_id BIGINT,
build_time TIMESTAMP,
create_time TIMESTAMP,
update_time TIMESTAMP,
PRIMARY KEY(id)
);

CREATE TABLE t_org_position_type (
id BIGINT,
type_name VARCHAR(200),
parent_type_id BIGINT,
create_time TIMESTAMP,
update_time TIMESTAMP,
PRIMARY KEY(id)
);

CREATE TABLE t_org_position (
id BIGINT,
position_type_id BIGINT,
position_name VARCHAR(200),
create_time TIMESTAMP,
update_time TIMESTAMP,
PRIMARY KEY(id)
);

CREATE TABLE t_hr_user(
id BIGINT,
company_id BIGINT,
department_id BIGINT,
NAME VARCHAR(200),
position_id BIGINT,
salt VARCHAR(200),
username VARCHAR(200),
pwd VARCHAR(200),
create_time TIMESTAMP,
update_time TIMESTAMP,
PRIMARY KEY(id)
);