-- Department table with a primary key constraint

CREATE TABLE Department (

    department_number INT PRIMARY KEY,

    department_data VARCHAR(50)

);

-- Customer table with a primary key constraint

CREATE TABLE Customer (

    customer_name VARCHAR(50) PRIMARY KEY,

    customer_address VARCHAR(50), 

    category INT CHECK (category BETWEEN 1 and 10)

);

-- Assembly table with a primary key constraint

CREATE TABLE Assembly (

    assembly_id INT PRIMARY KEY,

    date_ordered DATE,

    assembly_details VARCHAR(50),

    customer_name VARCHAR(50),

    CONSTRAINT FK_Customer FOREIGN KEY (customer_name) REFERENCES Customer(customer_name)

);

-- Process table with a primary key constraint

CREATE TABLE Process (

    process_id INT PRIMARY KEY,

    process_data VARCHAR(50),

    department_number INT,

    CONSTRAINT FK_Department FOREIGN KEY (department_number) REFERENCES Department(department_number)

);

-- Job table with a primary key constraint

CREATE TABLE Job (

    job_number INT PRIMARY KEY,

    commenced_date DATE,

    complete_date DATE,

    job_detail VARCHAR(50)

);

-- Assembly_account table with a primary key constraint

CREATE TABLE Assembly_account (

    account_number INT PRIMARY KEY,

    date_created DATE,

    details_1 FLOAT

);

-- Department_account table with a primary key constraint

CREATE TABLE Department_account (

    account_number INT PRIMARY KEY,

    date_created DATE,

    details_2 FLOAT

);

-- Process_account table with a primary key constraint

CREATE TABLE Process_account (

    account_number INT PRIMARY KEY,

    date_created DATE,

    details_3 FLOAT

);

-- Paint_process table with a primary key constraint

CREATE TABLE Paint_process (

    process_id INT PRIMARY KEY,

    paint_type VARCHAR(50),

    painting_method VARCHAR(50),

    CONSTRAINT FK_PaintProcess_ID FOREIGN KEY (process_id) REFERENCES Process(process_id)

);

-- Fit_process table with a primary key constraint

CREATE TABLE Fit_process (

    process_id INT PRIMARY KEY,

    fit_type VARCHAR(50),

    CONSTRAINT FK_FitProcess_ID FOREIGN KEY (process_id) REFERENCES Process(process_id)

);

-- Cut_process table with a primary key constraint

CREATE TABLE Cut_process (

    process_id INT PRIMARY KEY,

    cutting_type VARCHAR(50),

    machine_type VARCHAR(50),

    CONSTRAINT FK_CutProcess_ID FOREIGN KEY (process_id) REFERENCES Process(process_id)

);

-- Paint_job table with a primary key constraint

CREATE TABLE Paint_job (

    job_number INT PRIMARY KEY,

    color VARCHAR(50),

    volume INT, 

    labor_time INT,

    CONSTRAINT FK_PaintJob_Number FOREIGN KEY (job_number) REFERENCES Job(job_number)

);

-- Fit_job table with a primary key constraint

CREATE TABLE Fit_job (

    job_number INT PRIMARY KEY, 

    labor_time INT,

    CONSTRAINT FK_FitJob_Number FOREIGN KEY (job_number) REFERENCES Job(job_number)

);

-- Cut_job table with a primary key constraint

CREATE TABLE Cut_job (

    job_number INT PRIMARY KEY,

    type_of_machine VARCHAR(50),

    time_used INT,

    material_used VARCHAR(50),

    labor_time INT,

    CONSTRAINT FK_CutJob_Number FOREIGN KEY (job_number) REFERENCES Job(job_number)

);

-- A_has table with a primary key constraint

CREATE TABLE A_has (

    account_number INT PRIMARY KEY,

    assembly_id INT,

    CONSTRAINT FK_A_has_Assembly FOREIGN KEY (assembly_id) REFERENCES Assembly(assembly_id),

    CONSTRAINT FK_A_has_AssemblyAccount FOREIGN KEY (account_number) REFERENCES Assembly_account(account_number)

);

-- D_has table with a primary key constraint

CREATE TABLE D_has (

    account_number INT PRIMARY KEY,

    department_number INT,

    CONSTRAINT FK_D_has_Department FOREIGN KEY (department_number) REFERENCES Department(department_number),

    CONSTRAINT FK_D_has_DepartmentAccount FOREIGN KEY (account_number) REFERENCES Department_account(account_number)

);

-- P_has table with a primary key constraint

CREATE TABLE P_has (

    account_number INT PRIMARY KEY,

    process_id INT,

    CONSTRAINT FK_P_has_Process FOREIGN KEY (process_id) REFERENCES Process(process_id),

    CONSTRAINT FK_P_has_ProcessAccount FOREIGN KEY (account_number) REFERENCES Process_account(account_number)

);

-- Start_manufacturing table with a primary key constraint

CREATE TABLE Start_manufacturing (

    assembly_id INT,

    job_number INT,

    process_id INT,

    PRIMARY KEY (assembly_id, process_id),

    CONSTRAINT FK_StartManufacturing_Assembly FOREIGN KEY (assembly_id) REFERENCES Assembly(assembly_id),

    CONSTRAINT FK_StartManufacturing_Job FOREIGN KEY (job_number) REFERENCES Job(job_number),

    CONSTRAINT FK_StartManufacturing_Process FOREIGN KEY (process_id) REFERENCES Process(process_id)

);

-- Transaction table with a primary key constraint

CREATE TABLE Transactions(

    transaction_id INT PRIMARY KEY,

    transaction_cost FLOAT,

    job_number INT,

    assembly_account_number INT,

    process_account_number INT,

    department_account_number INT,

    --

    CONSTRAINT FK_Transaction_Job FOREIGN KEY (job_number) REFERENCES Job(job_number),

    --

    CONSTRAINT FK_Transaction_AssemblyAccount FOREIGN KEY (assembly_account_number) REFERENCES Assembly_account(account_number),

    CONSTRAINT FK_Transaction_ProcessAccount FOREIGN KEY (process_account_number) REFERENCES Process_account(account_number),

    CONSTRAINT FK_Transaction_DepartmentAccount FOREIGN KEY (department_account_number) REFERENCES Department_account(account_number)

    --

);



CREATE INDEX customer_category ON Customer(category)