INSERT INTO employees (id, version, create_date, update_date,salutation, first_name, name, address, house_number, city_code, city, tel, mail) VALUES
    (gen_random_uuid(),1,now(),now(),0, 'John', 'Doe', 'Main Street', '123', '12345', 'Example City', '555-1234', 'john.doe@example.com');
INSERT INTO employees (id, version, create_date, update_date,salutation, first_name, name, address, house_number, city_code, city, tel, mail) VALUES
    (gen_random_uuid(),1,now(),now(),1, 'Jane', 'Smith', 'Second Street', '456', '67890', 'Sample Town', '555-5678', 'jane.smith@example.com');

INSERT INTO employees (id, version, create_date, update_date,salutation, first_name, name, address, house_number, city_code, city, tel, mail) VALUES
    (gen_random_uuid(),1,now(),now(),1, 'Alice', 'Johnson', 'Third Street', '789', '54321', 'Demo City', '555-9876', 'alice.johnson@example.com');

INSERT INTO employees (id, version, create_date, update_date,salutation, first_name, name, address, house_number, city_code, city, tel, mail) VALUES
    (gen_random_uuid(),1,now(),now(),0, 'Robert', 'Brown', 'Fourth Avenue', '101', '11223', 'Testville', '555-1122', 'robert.brown@example.com');

INSERT INTO employees (id, version, create_date, update_date,salutation, first_name, name, address, house_number, city_code, city, tel, mail) VALUES
    (gen_random_uuid(),1,now(),now(),0, 'Michael', 'Davis', 'Fifth Boulevard', '202', '33445', 'Trial City', '555-2233', 'michael.davis@example.com');

INSERT INTO employees (id, version, create_date, update_date,salutation, first_name, name, address, house_number, city_code, city, tel, mail) VALUES
    (gen_random_uuid(),1,now(),now(),1, 'Emily', 'Miller', 'Sixth Street', '303', '55667', 'Sampleville', '555-3344', 'emily.miller@example.com');

INSERT INTO employees (id, version, create_date, update_date,salutation, first_name, name, address, house_number, city_code, city, tel, mail) VALUES
    (gen_random_uuid(),1,now(),now(),0, 'David', 'Wilson', 'Seventh Road', '404', '77889', 'Testtown', '555-4455', 'david.wilson@example.com');

INSERT INTO employees (id, version, create_date, update_date,salutation, first_name, name, address, house_number, city_code, city, tel, mail) VALUES
    (gen_random_uuid(),1,now(),now(),1, 'Sophia', 'Moore', 'Eighth Lane', '505', '99001', 'Trialville', '555-5566', 'sophia.moore@example.com');

INSERT INTO employees (id, version, create_date, update_date,salutation, first_name, name, address, house_number, city_code, city, tel, mail) VALUES
    (gen_random_uuid(),1,now(),now(),0, 'James', 'Taylor', 'Ninth Avenue', '606', '11223', 'Exampletown', '555-6677', 'james.taylor@example.com');

INSERT INTO employees (id, version, create_date, update_date,salutation, first_name, name, address, house_number, city_code, city, tel, mail) VALUES
    (gen_random_uuid(),1,now(),now(),1, 'Olivia', 'Anderson', 'Tenth Street', '707', '33445', 'Samplopolis', '555-7788', 'olivia.anderson@example.com');

INSERT INTO departments (id, version, create_date, update_date,department_name) VALUES (gen_random_uuid(),1,now(),now(),'Human Resources');
INSERT INTO departments (id, version, create_date, update_date,department_name) VALUES (gen_random_uuid(),1,now(),now(),'Finance');
INSERT INTO departments (id, version, create_date, update_date,department_name) VALUES (gen_random_uuid(),1,now(),now(),'Marketing');
INSERT INTO departments (id, version, create_date, update_date,department_name) VALUES (gen_random_uuid(),1,now(),now(),'Sales');
INSERT INTO departments (id, version, create_date, update_date,department_name) VALUES (gen_random_uuid(),1,now(),now(),'Research and Development');
INSERT INTO departments (id, version, create_date, update_date,department_name) VALUES (gen_random_uuid(),1,now(),now(),'IT');
INSERT INTO departments (id, version, create_date, update_date,department_name) VALUES (gen_random_uuid(),1,now(),now(),'Customer Service');
INSERT INTO departments (id, version, create_date, update_date,department_name) VALUES (gen_random_uuid(),1,now(),now(),'Production');
INSERT INTO departments (id, version, create_date, update_date,department_name) VALUES (gen_random_uuid(),1,now(),now(),'Quality Assurance');
INSERT INTO departments (id, version, create_date, update_date,department_name) VALUES (gen_random_uuid(),1,now(),now(),'Legal');


INSERT INTO capability_types (id, version, create_date, update_date,capability_type) VALUES (gen_random_uuid(),1,now(),now(),'Project Management');
INSERT INTO capability_types (id, version, create_date, update_date,capability_type) VALUES (gen_random_uuid(),1,now(),now(),'Data Analysis');
INSERT INTO capability_types (id, version, create_date, update_date,capability_type) VALUES (gen_random_uuid(),1,now(),now(),'Software Development');
INSERT INTO capability_types (id, version, create_date, update_date,capability_type) VALUES (gen_random_uuid(),1,now(),now(),'Quality Control');
INSERT INTO capability_types (id, version, create_date, update_date,capability_type) VALUES (gen_random_uuid(),1,now(),now(),'Technical Support');
INSERT INTO capability_types (id, version, create_date, update_date,capability_type) VALUES (gen_random_uuid(),1,now(),now(),'Business Strategy');
INSERT INTO capability_types (id, version, create_date, update_date,capability_type) VALUES (gen_random_uuid(),1,now(),now(),'Marketing');
INSERT INTO capability_types (id, version, create_date, update_date,capability_type) VALUES (gen_random_uuid(),1,now(),now(),'Human Resources');
INSERT INTO capability_types (id, version, create_date, update_date,capability_type) VALUES (gen_random_uuid(),1,now(),now(),'Customer Service');
INSERT INTO capability_types (id, version, create_date, update_date,capability_type) VALUES (gen_random_uuid(),1,now(),now(),'Product Management');


