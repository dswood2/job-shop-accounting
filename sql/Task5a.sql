-- Create the stored procedure for Query 1

GO

CREATE PROCEDURE InsertCustomer(

    @customer_name VARCHAR(50),

    @customer_address VARCHAR(50),

    @category INT

)

AS

BEGIN

    -- Insert a new customer

    INSERT INTO Customer (customer_name, customer_address, category)

    VALUES (@customer_name, @customer_address, @category);

END;



-- Create the stored procedure for Query 2

GO

CREATE PROCEDURE InsertDepartment(

    @department_number INT,

    @department_data VARCHAR(50)

)

AS

BEGIN

    -- Insert a new department

    INSERT INTO Department (department_number, department_data)

    VALUES (@department_number, @department_data);

END;



-- Create the stored procedure for Query 3

GO

CREATE PROCEDURE InsertNewProcess(

    @process_id INT,

    @process_data VARCHAR(50),

    @department_number INT

)

AS

BEGIN

    -- Insert a new process

    INSERT INTO Process (process_id, process_data, department_number)

    VALUES (@process_id, @process_data, @department_number);

END;



GO

CREATE PROCEDURE InsertNewPaintProcess(

    @process_id INT,

    @paint_type VARCHAR(50),

    @painting_method VARCHAR(50)

)

AS

BEGIN

    -- Insert into Paint_process

    INSERT INTO Paint_process (process_id, paint_type, painting_method)

    VALUES (@process_id, @paint_type, @painting_method);

END;



GO

CREATE PROCEDURE InsertNewFitProcess(

    @process_id INT,

    @fit_type VARCHAR(50)

)

AS

BEGIN

    -- Insert into Fit_process

    INSERT INTO Fit_process (process_id, fit_type)

    VALUES (@process_id, @fit_type);

END;



GO

CREATE PROCEDURE InsertNewCutProcess(

    @process_id INT,

    @cutting_type VARCHAR(50),

    @machine_type VARCHAR(50)

)

AS

BEGIN

    -- Insert into Cut_process

    INSERT INTO Cut_process (process_id, cutting_type, machine_type)

    VALUES (@process_id, @cutting_type, @machine_type);

END;



-- Create the stored procedure for Query 4

GO

CREATE PROCEDURE InsertNewAssembly(

    @assembly_id INT,

    @date_ordered DATE,

    @assembly_details VARCHAR(50),

    @customer_name VARCHAR(50)

)

AS

BEGIN

    -- Insert a new assembly

    INSERT INTO Assembly (assembly_id, date_ordered, assembly_details, customer_name)

    VALUES (@assembly_id, @date_ordered, @assembly_details, @customer_name);

END;



GO

CREATE PROCEDURE ConnectAssemblyToProcesses(

    @assembly_id INT,

    @process_id INT

)

AS

BEGIN

  IF @process_id IS NOT NULL AND EXISTS (SELECT 1 FROM Process WHERE process_id = @process_id)

    BEGIN

        INSERT INTO Start_manufacturing (assembly_id, process_id)

        VALUES (@assembly_id, @process_id);

    END

    ELSE

        BEGIN

            -- Process Table is empty

            THROW 50000, 'Process Table is empty. Please provide a valid process_id or add processes to the table.', 1;

        END;

    END;



-- Create the stored procedure for Query 5

GO

-- Create the stored procedure for entering a new account with error messages and flexible account type

CREATE PROCEDURE InsertNewAccountWithType(

    @account_number INT,

    @date_created DATE,

    @details FLOAT,

    @account_type VARCHAR(20), -- 'Assembly', 'Department', or 'Process'

    @process_id INT = NULL,

    @assembly_id INT = NULL,

    @department_number INT = NULL

)

AS

BEGIN

    -- Insert into the corresponding account table based on the account type

    IF @account_type = 'Process' 

    BEGIN

        -- Insert into Process_account

        INSERT INTO Process_account (account_number, date_created, details_3)

        VALUES (@account_number, @date_created, @details);

    END

    ELSE IF @account_type = 'Assembly' 

    BEGIN

        -- Insert into Assembly_account

        INSERT INTO Assembly_account (account_number, date_created, details_1)

        VALUES (@account_number, @date_created, @details);

    END

    ELSE IF @account_type = 'Department' 

    BEGIN

        -- Insert into Department_account

        INSERT INTO Department_account (account_number, date_created, details_2)

        VALUES (@account_number, @date_created, @details);

    END

    ELSE

    BEGIN

        -- Invalid account type

        THROW 50000, 'Invalid account type. Please provide a valid account type (Assembly, Department, or Process).', 1;

    END;



    -- Associate the account with a specific process, assembly, or department

    IF @account_type = 'Process' AND @process_id IS NOT NULL AND EXISTS (SELECT 1 FROM Process WHERE process_id = @process_id) 

    BEGIN

        -- Insert into P_has

        INSERT INTO P_has (account_number, process_id)

        VALUES (@account_number, @process_id);

    END

    ELSE IF @account_type = 'Assembly' AND @assembly_id IS NOT NULL AND EXISTS (SELECT 1 FROM Assembly WHERE assembly_id = @assembly_id) 

    BEGIN

        -- Insert into A_has

        INSERT INTO A_has (account_number, assembly_id)

        VALUES (@account_number, @assembly_id);

    END

    ELSE IF @account_type = 'Department' AND @department_number IS NOT NULL AND EXISTS (SELECT 1 FROM Department WHERE department_number = @department_number) 

    BEGIN

        -- Insert into D_has

        INSERT INTO D_has (account_number, department_number)

        VALUES (@account_number, @department_number);

    END

    ELSE

    BEGIN

        -- Invalid association or account type

        THROW 50000, 'Invalid association or account type. Please provide valid associations and account type.', 1;

    END;

END;



-- Create the stored procedure for Query 6

GO

CREATE PROCEDURE InsertNewJob(

    @job_number INT,

    @commenced_date DATE,

    @assembly_id INT,

    @process_id INT

)

AS

BEGIN

    -- Check if the assembly_id exists

    IF NOT EXISTS (SELECT 1 FROM Assembly WHERE assembly_id = @assembly_id)

    BEGIN

        THROW 50000, 'Invalid assembly_id. Please provide a valid assembly_id.', 1;

    END;



    -- Check if the process_id exists

    IF NOT EXISTS (SELECT 1 FROM Process WHERE process_id = @process_id)

    BEGIN

        THROW 50000, 'Invalid process_id. Please provide a valid process_id.', 1;

    END;



    -- Check if the assembly_id and process_id are associated in Start_manufacturing

    IF NOT EXISTS (

        SELECT 1

        FROM Start_manufacturing

        WHERE assembly_id = @assembly_id

        AND process_id = @process_id

    )

    BEGIN

        -- Assembly and process are not associated

        THROW 50000, 'The specified assembly_id and process_id are not associated in Start_manufacturing. Please associate them before creating a job.', 1;

    END;



    -- Insert into Job table

    INSERT INTO Job (job_number, commenced_date)

    VALUES (@job_number, @commenced_date);

    

    -- Update the job_number in Start_manufacturing

    UPDATE Start_manufacturing

    SET job_number = @job_number

    WHERE assembly_id = @assembly_id

    AND process_id = @process_id;



    

END;



GO

-- Create the stored procedure for Query 7 (Update Job Table)

CREATE PROCEDURE UpdateJobCompletionDetails(

    @job_number INT,

    @complete_date DATE,

    @job_detail VARCHAR(50)

)

AS

BEGIN

    -- Check if the job_number exists in the Job table

    IF NOT EXISTS (SELECT 1 FROM Job WHERE job_number = @job_number)

    BEGIN

        THROW 50000, 'Invalid job_number. Please provide a valid job_number.', 1;

    END;



    -- Update the completion details in the Job table

    UPDATE Job

    SET complete_date = @complete_date,

        job_detail = @job_detail

    WHERE job_number = @job_number;

END;



GO

-- Create the stored procedure for updating Paint_job details

CREATE PROCEDURE InsertOrUpdatePaintJobDetails(

    @job_number INT,

    @color VARCHAR(50),

    @volume INT,

    @labor_time INT

)

AS

BEGIN

    -- Insert new row

    INSERT INTO Paint_job (job_number, color, volume, labor_time)

    VALUES (@job_number, @color, @volume, @labor_time);

END;



GO

-- Create the stored procedure for updating Fit_job details

CREATE PROCEDURE InsertOrUpdateFitJobDetails(

    @job_number INT,

    @labor_time INT

)

AS

BEGIN

    -- Insert new row

    INSERT INTO Fit_job (job_number, labor_time)

    VALUES (@job_number, @labor_time);

END;



GO

-- Create the stored procedure for updating Cut_job details

CREATE PROCEDURE InsertOrUpdateCutJobDetails(

    @job_number INT,

    @type_of_machine VARCHAR(50),

    @time_used INT,

    @material_used VARCHAR(50),

    @labor_time INT

)

AS

BEGIN

    -- Insert new row

    INSERT INTO Cut_job (job_number, type_of_machine, time_used, material_used, labor_time)

    VALUES (@job_number, @type_of_machine, @time_used, @material_used, @labor_time);

END;



-- Create the stored procedure for Query 8 (Update Job Table)

GO

CREATE PROCEDURE UpdateAccountDetailsWithTransaction (

    @transaction_id INT,

    @transaction_cost FLOAT,

    @job_number INT

)

AS

BEGIN

    DECLARE @department_account_number INT,

            @assembly_account_number INT,

            @process_account_number INT,

            @department_number INT;



    -- Find the corresponding accounts based on the job_number

    SELECT 

        @assembly_account_number = A.account_number,

        @process_account_number = P.account_number

    FROM Start_manufacturing S

    LEFT JOIN A_has A ON S.assembly_id = A.assembly_id

    LEFT JOIN P_has P ON S.process_id = P.process_id

    WHERE S.job_number = @job_number;



    IF @assembly_account_number IS NOT NULL AND @process_account_number IS NOT NULL

    BEGIN

        -- Update Assembly_account details

        UPDATE Assembly_account

        SET details_1 = details_1 + @transaction_cost

        WHERE account_number = @assembly_account_number;



        -- Update Process_account details

        UPDATE Process_account

        SET details_3 = details_3 + @transaction_cost

        WHERE account_number = @process_account_number;



        -- Find department_number based on the process_id

        SELECT @department_number = department_number

        FROM Process

        WHERE process_id = (SELECT process_id FROM Start_manufacturing WHERE job_number = @job_number);



        IF @department_number IS NOT NULL

        BEGIN

            -- Set department_account_number

            SET @department_account_number = (SELECT account_number FROM D_has WHERE department_number = @department_number);



            IF @department_account_number IS NOT NULL

            BEGIN

                -- Update Department_account details

                UPDATE Department_account

                SET details_2 = details_2 + @transaction_cost

                WHERE account_number = @department_account_number;



                -- Insert into Transactions table

                INSERT INTO Transactions (transaction_id, transaction_cost, job_number, assembly_account_number, process_account_number, department_account_number)

                VALUES (@transaction_id, @transaction_cost, @job_number, @assembly_account_number, @process_account_number, @department_account_number);

            END

            ELSE

            BEGIN

                THROW 50000, 'No matching department_account_number found for the specified job_number.', 1;

            END;

        END

        ELSE

        BEGIN

            THROW 50000, 'No matching department_number found for the specified job_number.', 1;

        END;

    END

    ELSE

    BEGIN

        THROW 50000, 'No matching assembly_id and process_id found for the specified job_number.', 1;

    END;

END;



-- Create the stored procedure for Query 9

GO

CREATE PROCEDURE RetrieveTotalCostForAssembly(

    @assembly_id INT

)

AS

BEGIN

    DECLARE @account_number INT;



    -- Find the account_number for the specified assembly_id in A_has

    SELECT @account_number = account_number

    FROM A_has

    WHERE assembly_id = @assembly_id;



    -- Check if account_number was found

    IF @account_number IS NOT NULL

    BEGIN

        -- Use the account_number to retrieve details_1 from Assembly_account and directly output the result

        SELECT details_1

        FROM Assembly_account

        WHERE account_number = @account_number;

    END

    ELSE

    BEGIN

        -- No matching assembly_id found in A_has

        THROW 50000, 'No matching assembly_id found in A_has.', 1;

    END;

END;



-- Create the stored procedure for Query 10

GO

CREATE PROCEDURE RetrieveTotalLaborTimeInDepartment(

    @department_number INT,

    @completion_date DATE

)

AS

BEGIN



    DECLARE @totalLaborTime INT;

    -- Check if department_number exists in the Process table

    IF NOT EXISTS (SELECT 1 FROM Process WHERE department_number = @department_number)

    BEGIN

        PRINT 'No matching department_number found in the Process table.';

        RETURN;

    END;



    -- Retrieve all process_ids associated with the given department_number

    DECLARE @processIds TABLE (process_id INT);

    INSERT INTO @processIds (process_id)

    SELECT process_id

    FROM Process

    WHERE department_number = @department_number;



    -- Check if there are matching process_ids in the Start_manufacturing table

    IF NOT EXISTS (SELECT 1 FROM Start_manufacturing WHERE process_id IN (SELECT process_id FROM @processIds))

    BEGIN

        PRINT 'No matching process_ids found in the Start_manufacturing table for the specified department_number.';

        RETURN;

    END;



    -- Calculate the total labor time for jobs completed on the given date

    SELECT @totalLaborTime = ISNULL(SUM(FJ.labor_time), 0) + ISNULL(SUM(CJ.labor_time), 0) + ISNULL(SUM(PJ.labor_time), 0)

    FROM Start_manufacturing SM

    INNER JOIN Process P ON SM.process_id = P.process_id

    LEFT JOIN Fit_job FJ ON SM.job_number = FJ.job_number

    LEFT JOIN Cut_job CJ ON SM.job_number = CJ.job_number

    LEFT JOIN Paint_job PJ ON SM.job_number = PJ.job_number

    INNER JOIN Job J ON SM.job_number = J.job_number

    WHERE P.department_number = @department_number

    AND J.complete_date = @completion_date;

    -- Output the total labor time as a result set

    SELECT @totalLaborTime AS TotalLaborTime;

END;



-- Create the stored procedure for Query 11

GO

CREATE PROCEDURE RetrieveProcessesForAssembly(

    @assembly_id INT

)

AS

BEGIN

    -- Declare variables to store results

    DECLARE @process_id INT;

    DECLARE @department_number INT;

    

    -- Create a temporary table to store intermediate results

    CREATE TABLE #TempResults (

        ProcessID INT,

        DepartmentNumber INT,

        CommencedDate DATE

    );



    -- Insert relevant data from Start_manufacturing into the temporary table

    INSERT INTO #TempResults (ProcessID, DepartmentNumber, CommencedDate)

    SELECT P.process_id, P.department_number, J.commenced_date

    FROM Start_manufacturing SM

    INNER JOIN Process P ON SM.process_id = P.process_id

    INNER JOIN Job J ON SM.job_number = J.job_number

    WHERE SM.assembly_id = @assembly_id;



    -- Select the final result in the desired order

    SELECT ProcessID, DepartmentNumber, CommencedDate

    FROM #TempResults

    ORDER BY CommencedDate;



    -- Drop the temporary table

    --DROP TABLE #TempResults;

END;



-- Create the stored procedure for Query 12

GO

CREATE PROCEDURE RetrieveCustomerNamesByCategoryRange(

    @min_category INT,

    @max_category INT

)

AS

BEGIN

    -- Select customer names within the specified category range

    SELECT customer_name

    FROM Customer

    WHERE category BETWEEN @min_category AND @max_category

    ORDER BY customer_name;

END;



-- Create the stored procedure for Query 13

GO

CREATE PROCEDURE DeleteCutJobsByJobNumberRange(

    @min_job_number INT,

    @max_job_number INT

)

AS

BEGIN

    -- Delete cut-jobs within the specified job number range

    DELETE FROM Cut_job

    WHERE job_number BETWEEN @min_job_number AND @max_job_number;

    



END;



-- Create the stored procedure for Query 14

GO

CREATE PROCEDURE UpdatePaintJobColor(

    @job_number INT,

    @new_color VARCHAR(50)

)

AS

BEGIN

    -- Update the color of the specified paint job

    UPDATE Paint_job

    SET color = @new_color

    WHERE job_number = @job_number;

END;

