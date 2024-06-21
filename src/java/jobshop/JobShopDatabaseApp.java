package jobshop;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JobShopDatabaseApp {
	public static void main(String[] args) {
        String url = "jdbc:sqlserver://<server-name>.database.windows.net;database=<database-name>";
        String user = "<username>";
        String password = "<password>";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            int choice = -1;
            while (choice != 17) {
                showMenu();
                choice = getUserChoice();
                switch (choice) {
                case 1:
                    insertCustomer(connection);
                    break;
                case 2:
                    insertDepartment(connection);
                    break;
                case 3:
                    insertNewProcessAndDetails(connection);
                    break;
                case 4:
                    insertNewAssemblyWithProcesses(connection);
                    break;
                case 5:
                    insertNewAccountWithType(connection);
                    break;
                case 6:
                    insertNewJob(connection);
                    break;
                case 7:
                    updateJobCompletionDetails(connection);
                    break;
                case 8:
                    updateAccountDetailsWithTransaction(connection);
                    break;
                case 9:
                    retrieveTotalCostForAssembly(connection);
                    break;
                case 10:
                    retrieveTotalLaborTimeInDepartment(connection);
                    break;
                case 11:
                    retrieveProcessesForAssembly(connection);
                    break;
                case 12:
                    retrieveCustomersByCategoryRange(connection);
                    break;
                case 13:
                    deleteCutJobsByJobNumberRange(connection);
                    break;
                case 14:
                    updatePaintJobColor(connection);
                    break;
                case 15:
                    importDataFromFile(connection);
                    break;
                case 16:
                	retrieveCustomersByCategoryRangeAndExport(connection);
                    break;
                case 17:
                    System.out.println("Exiting the program. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
            }      
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	private static void showMenu() {
		System.out.println("WELCOME TO THE JOB-SHOP ACCOUNTING DATABASE SYSTEM");
        System.out.println("(1) Insert a new customer");
        System.out.println("(2) Insert a new department");
        System.out.println("(3) Insert a new process");
        System.out.println("(4) Insert a new assembly");
        System.out.println("(5) Insert a new account");
        System.out.println("(6) Insert a new job");
        System.out.println("(7) Update Job Completion Details");
        System.out.println("(8) Update Account Details with Transaction");
        System.out.println("(9) Retrieve Total Cost for Assembly");
        System.out.println("(10) Retrieve Total Labor Time in Department");
        System.out.println("(11) Retrieve Processes and Department for an Assembly ID");
        System.out.println("(12) Retrieve Customers for given Range");
        System.out.println("(13) Delete all Cut Jobs for given Range");
        System.out.println("(14) Change the color of a Paint Job");
        System.out.println("(15) Import: enter new customers from a data file.");
        System.out.println("(16) Export: Retrieve the customers whose category is in a given range");
        System.out.println("(17) Quit");
    }
	private static int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }
	private static void insertCustomer(Connection connection) {
	        try (PreparedStatement statement = connection.prepareStatement("{call InsertCustomer(?, ?, ?)}")) {
	            Scanner scanner = new Scanner(System.in);

	            System.out.print("Enter customer name: ");
	            String customerName = scanner.nextLine();

	            System.out.print("Enter customer address: ");
	            String customerAddress = scanner.nextLine();

	            System.out.print("Enter category: ");
	            int category = scanner.nextInt();

	            statement.setString(1, customerName);
	            statement.setString(2, customerAddress);
	            statement.setInt(3, category);

	            statement.execute();
	            System.out.println("Customer inserted successfully.");

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	private static void insertDepartment(Connection connection) {
	        try (PreparedStatement statement = connection.prepareStatement("{call InsertDepartment(?, ?)}")) {
	            Scanner scanner = new Scanner(System.in);

	            System.out.print("Enter department number: ");
	            int departmentNumber = scanner.nextInt();

	            scanner.nextLine(); // Consume newline

	            System.out.print("Enter department data: ");
	            String departmentData = scanner.nextLine();

	            statement.setInt(1, departmentNumber);
	            statement.setString(2, departmentData);

	            statement.execute();
	            System.out.println("Department inserted successfully.");

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	private static void insertNewProcessAndDetails(Connection connection) {
		    Scanner scanner = new Scanner(System.in);

		    // Insert into the Process table
		    System.out.print("Enter process_id: ");
		    int processId = scanner.nextInt();
		    scanner.nextLine(); // Consume newline

		    System.out.print("Enter process_data: ");
		    String processData = scanner.nextLine();

		    System.out.print("Enter department_number: ");
		    int departmentNumber = scanner.nextInt();

		    // Call the stored procedure to insert into the Process table
		    try (CallableStatement statement = connection.prepareCall("{call InsertNewProcess(?, ?, ?)}")) {
		        statement.setInt(1, processId);
		        statement.setString(2, processData);
		        statement.setInt(3, departmentNumber);
		        statement.execute();
		        System.out.println("Process inserted successfully.");
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return;
		    }

		    // Ask for the type of process (Paint, Fit, or Cut)
		    System.out.println("Select the type of process:");
		    System.out.println("(1) Paint");
		    System.out.println("(2) Fit");
		    System.out.println("(3) Cut");
		    System.out.print("Enter your choice: ");
		    int processTypeChoice = scanner.nextInt();
		    scanner.nextLine(); // Consume newline

		    // Insert into the specific process-related table based on the user's choice
		    switch (processTypeChoice) {
		        case 1:
		            insertNewPaintProcess(connection, processId, getPaintDetails(scanner));
		            break;
		        case 2:
		            insertNewFitProcess(connection, processId, getFitDetails(scanner));
		            break;
		        case 3:
		            insertNewCutProcess(connection, processId, getCutDetails(scanner));
		            break;
		        default:
		            System.out.println("Invalid process type. No further details inserted.");
		    }
		}
	private static String getPaintDetails(Scanner scanner) {
		    System.out.print("Enter paint type: ");
		    String paint_type = scanner.nextLine();
		    
		    System.out.print("Enter paint method: ");
		    String paint_method = scanner.nextLine();
		    
		    return paint_type + "," + paint_method;
		}
	private static String getFitDetails(Scanner scanner) {
		    System.out.print("Enter fit_type: ");
		    return scanner.nextLine();
		}
	private static String getCutDetails(Scanner scanner) {
		    System.out.print("Enter cutting_type: ");
		    String cuttingType = scanner.nextLine();

		    System.out.print("Enter machine_type: ");
		    String machineType = scanner.nextLine();

		    return cuttingType + "," + machineType;
		}
	private static void insertNewPaintProcess(Connection connection, int processId, String paintDetails) {
		    // Call the stored procedure to insert into the Paint_process table
		    try (CallableStatement statement = connection.prepareCall("{call InsertNewPaintProcess(?, ?, ?)}")) {
		    	String[] paintDetailsArray = paintDetails.split(",");
		    	statement.setInt(1, processId);
		        statement.setString(2, paintDetailsArray[0]);
		        statement.setString(3, paintDetailsArray[1]);
		        statement.execute();
		        System.out.println("Paint process inserted successfully.");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	private static void insertNewFitProcess(Connection connection, int processId, String fitDetails) {
		    // Insert into the Fit_process table
		    try (CallableStatement statement = connection.prepareCall("{call InsertNewFitProcess(?, ?)}")) {
		        statement.setInt(1, processId);
		        statement.setString(2, fitDetails);
		        statement.execute();
		        System.out.println("Fit process inserted successfully.");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	private static void insertNewCutProcess(Connection connection, int processId, String cutDetails) {
			try (CallableStatement statement = connection.prepareCall("{call InsertNewCutProcess(?, ?, ?)}")) {
		        String[] cutDetailsArray = cutDetails.split(",");
		        statement.setInt(1, processId);
		        statement.setString(2, cutDetailsArray[0]); // cutting_type
		        statement.setString(3, cutDetailsArray[1]); // machine_type
		        statement.execute();
		        System.out.println("Cut process inserted successfully.");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	private static void insertNewAssemblyWithProcesses(Connection connection) {
	    Scanner scanner = new Scanner(System.in);

	    // Gather information for the new assembly
	    System.out.print("Enter assembly_id: ");
	    int assemblyId = scanner.nextInt();
	    scanner.nextLine(); // Consume newline

	    System.out.print("Enter date_ordered (YYYY-MM-DD): ");
	    String dateOrderedStr = scanner.nextLine();
	    LocalDate dateOrdered = LocalDate.parse(dateOrderedStr);

	    System.out.print("Enter assembly_details: ");
	    String assemblyDetails = scanner.nextLine();

	    System.out.print("Enter customer_name: ");
	    String customerName = scanner.nextLine();

	    // Call the stored procedure to insert into the Assembly table
	    try (CallableStatement assemblyStatement = connection.prepareCall("{call InsertNewAssembly(?, ?, ?, ?)}")) {
	        assemblyStatement.setInt(1, assemblyId);
	        assemblyStatement.setDate(2, Date.valueOf(dateOrdered));
	        assemblyStatement.setString(3, assemblyDetails);
	        assemblyStatement.setString(4, customerName);
	        assemblyStatement.execute();
	        System.out.println("Assembly inserted successfully.");

	        // Connect the assembly to one or more process_ids
	        boolean connectToAnotherProcess = true;

	        while (connectToAnotherProcess) {
	            System.out.print("Which process_id do you want to connect the assembly to? ");
	            int processId = scanner.nextInt();
	            scanner.nextLine(); // Consume newline

	            // Call the stored procedure to insert into the Start_manufacturing table
	            try (CallableStatement startManufacturingStatement = connection.prepareCall("{call ConnectAssemblyToProcesses(?, ?)}")) {
	                startManufacturingStatement.setInt(1, assemblyId);
	                startManufacturingStatement.setInt(2, processId);
	                startManufacturingStatement.execute();
	                System.out.println("Assembly connected to process_id " + processId + " successfully.");
	            } catch (SQLException e) {
	                e.printStackTrace();
	                // Handle the exception based on your application's requirements
	            }

	            System.out.print("Do you want to connect the assembly to another process? (Y/N): ");
	            String userInput = scanner.nextLine();
	            connectToAnotherProcess = userInput.equals("Y");
	          
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle the exception based on your application's requirements
	    }
	}
	private static void insertNewAccountWithType(Connection connection) {
            Scanner scanner = new Scanner(System.in);

            // Get user input
            System.out.println("Enter Account Number: ");
            int accountNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            System.out.println("Enter Date Created (YYYY-MM-DD): ");
            String dateCreatedStr = scanner.nextLine();
	        LocalDate dateCreated = LocalDate.parse(dateCreatedStr);
	        
            System.out.println("Enter Details: ");
            float details = scanner.nextFloat();

            scanner.nextLine(); // Consume the newline character

            System.out.println("Enter Account Type (Assembly, Department, or Process): ");
            String accountType = scanner.nextLine();

            // Initialize optional parameters
            Integer processId = null;
            Integer assemblyId = null;
            Integer departmentNumber = null;

            // Check account type and get additional information
            if (accountType.equalsIgnoreCase("Process")) {
                System.out.println("Enter Process ID: ");
                processId = scanner.nextInt();
            } else if (accountType.equalsIgnoreCase("Assembly")) {
                System.out.println("Enter Assembly ID: ");
                assemblyId = scanner.nextInt();
            } else if (accountType.equalsIgnoreCase("Department")) {
                System.out.println("Enter Department Number: ");
                departmentNumber = scanner.nextInt();
            } else {
                System.out.println("Invalid account type. Please provide a valid account type.");
                return;
            }

            // Prepare the SQL statement
            try(CallableStatement statement = connection.prepareCall("{call InsertNewAccountWithType( ?, ?, ?, ?, ?, ?, ?)}")) {
                // Set parameters
                statement.setInt(1, accountNumber);
                statement.setDate(2, Date.valueOf(dateCreated));
                statement.setFloat(3, details);
                statement.setString(4, accountType);
                statement.setObject(5, processId);
                statement.setObject(6, assemblyId);
                statement.setObject(7, departmentNumber);

                // Execute the stored procedure
                statement.execute();

                System.out.println("Account details inserted successfully.");
            }
            catch (SQLException e) {
            	e.printStackTrace();
            }
    	}	
	private static void insertNewJob(Connection connection) {
	    try (PreparedStatement statement = connection.prepareStatement("{call InsertNewJob(?, ?, ?, ?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input for job details
	        System.out.print("Enter job-no: ");
	        int jobNo = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        System.out.print("Enter assembly-id: ");
	        int assemblyId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        System.out.print("Enter process-id: ");
	        int processId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        System.out.print("Enter date the job commenced (YYYY-MM-DD): ");
	        String dateCommencedStr = scanner.nextLine();
	        LocalDate dateCommenced = LocalDate.parse(dateCommencedStr);

	        // Set parameters and execute the stored procedure
	        statement.setInt(1, jobNo);
	        statement.setDate(2, Date.valueOf(dateCommenced));
	        statement.setInt(3, assemblyId);
	        statement.setInt(4, processId);
	        
	        statement.execute();
	        System.out.println("Job inserted successfully.");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private static void updateJobCompletionDetails(Connection connection) {
	    try (PreparedStatement statement = connection.prepareStatement("{call UpdateJobCompletionDetails(?, ?, ?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input for job completion details
	        System.out.print("Enter job-no for completed job: ");
	        int jobNo = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        System.out.print("Enter date the job completed (YYYY-MM-DD): ");
	        String dateCompletedStr = scanner.nextLine();
	        LocalDate dateCompleted = LocalDate.parse(dateCompletedStr);

	        System.out.print("Enter job detail (Paint, Fit, or Cut): ");
	        String jobDetail = scanner.nextLine();
	        while (!(jobDetail.equalsIgnoreCase("Paint")||jobDetail.equalsIgnoreCase("Fit")||jobDetail.equalsIgnoreCase("Cut"))) {
	        	System.out.print("Invalid job detail selection, please try again: ");
	        	jobDetail = scanner.nextLine();
	        }

	        // Set parameters and execute the stored procedure
	        statement.setInt(1, jobNo);
	        statement.setDate(2, Date.valueOf(dateCompleted));
	        statement.setString(3, jobDetail.toLowerCase());

	        statement.execute();
	        System.out.println("Job completion details updated successfully.");

	        // Call the specific stored procedure based on job detail
	        switch (jobDetail.toLowerCase()) {
	            case "paint":
	                updatePaintJobDetails(connection, jobNo);
	                break;
	            case "fit":
	                updateFitJobDetails(connection, jobNo);
	                break;
	            case "cut":
	                updateCutJobDetails(connection, jobNo);
	                break;
	            default:
	                System.out.println("Invalid job detail. No specific job details updated.");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private static void updatePaintJobDetails(Connection connection, int jobNo) {
	    try (CallableStatement statement = connection.prepareCall("{call InsertOrUpdatePaintJobDetails(?, ?, ?, ?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input for paint job details
	        System.out.print("Enter paint color: ");
	        String paintColor = scanner.nextLine();

	        System.out.print("Enter paint volume: ");
	        int paintVolume = scanner.nextInt();

	        System.out.print("Enter paint labor time: ");
	        int paintLaborTime = scanner.nextInt();
	        
	        // Set parameters and execute the stored procedure
	        statement.setInt(1, jobNo);
	        statement.setString(2, paintColor);
	        statement.setInt(3, paintVolume);
	        statement.setInt(4, paintLaborTime);

	        statement.execute();
	        System.out.println("Paint job details updated successfully.");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private static void updateFitJobDetails(Connection connection, int jobNo) {
	    try (CallableStatement statement = connection.prepareCall("{call InsertOrUpdateFitJobDetails(?, ?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input for fit job details
	        System.out.print("Enter fit labor time: ");
	        int fitLaborTime = scanner.nextInt();

	        // Set parameters and execute the stored procedure
	        statement.setInt(1, jobNo);
	        statement.setInt(2, fitLaborTime);

	        statement.execute();
	        System.out.println("Fit job details updated successfully.");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private static void updateCutJobDetails(Connection connection, int jobNo) {
	    try (CallableStatement statement = connection.prepareCall("{call InsertOrUpdateCutJobDetails(?, ?, ?, ?, ?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input for cut job details
	        System.out.print("Enter machine type: ");
	        String machineType = scanner.nextLine();
	        
	        System.out.print("Enter time used: ");
	        int cuttingTime = scanner.nextInt();
	        scanner.nextLine(); // Consume newline
	        
	        System.out.print("Enter material used: ");
	        String cuttingMaterial = scanner.nextLine();
	        
	        System.out.print("Enter labor time: ");
	        int cuttingLaborTime = scanner.nextInt();

	        // Set parameters and execute the stored procedure
	        statement.setInt(1, jobNo);
	        statement.setString(2, machineType);
	        statement.setInt(3, cuttingTime);
	        statement.setString(4, cuttingMaterial);
	        statement.setInt(5, cuttingLaborTime);

	        statement.execute();
	        System.out.println("Cut job details updated successfully.");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private static void updateAccountDetailsWithTransaction(Connection connection) {
	    try (CallableStatement statement = connection.prepareCall("{call UpdateAccountDetailsWithTransaction(?, ?, ?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input
	        System.out.print("Enter transaction ID: ");
	        int transactionID = scanner.nextInt();

	        System.out.print("Enter supplied cost: ");
	        float supCost = scanner.nextFloat();
	        
	        System.out.print("Enter Job Number: ");
	        int jobNumber = scanner.nextInt();

	        // Set parameters and execute the stored procedure
	        statement.setInt(1, transactionID);
	        statement.setFloat(2, supCost);
	        statement.setInt(3, jobNumber);

	        statement.execute();
	        System.out.println("Account details updated successfully with transaction.");

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private static void retrieveTotalCostForAssembly(Connection connection) {
	    try (CallableStatement statement = connection.prepareCall("{call RetrieveTotalCostForAssembly(?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input
	        System.out.print("Enter assembly ID: ");
	        int assemblyId = scanner.nextInt();

	        // Set parameters and execute the stored procedure
	        statement.setInt(1, assemblyId);
	        statement.execute();
	        
	        float totalCost = 0; // Initialize totalCost variable
	        ResultSet resultSet = statement.getResultSet();
	        if (resultSet != null && resultSet.next()) {
	            totalCost = resultSet.getFloat(1);
	        }

	        System.out.println("Total Cost for Assembly ID " + assemblyId + ": " + totalCost);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private static void retrieveTotalLaborTimeInDepartment(Connection connection) {
	    try (CallableStatement statement = connection.prepareCall("{call RetrieveTotalLaborTimeInDepartment(?, ?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input
	        System.out.print("Enter department number: ");
	        int departmentNumber = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        System.out.print("Enter date (YYYY-MM-DD): ");
	        String dateString = scanner.nextLine();
	        LocalDate date = LocalDate.parse(dateString); // Convert String to Date

	        // Set parameters
	        statement.setInt(1, departmentNumber);
	        statement.setDate(2, Date.valueOf(date));

	        // Execute the stored procedure
	        ResultSet resultSet = statement.executeQuery();

	        // Process the result set
	        if (resultSet.next()) {
	            int totalLaborTime = resultSet.getInt(1);
	            System.out.println("Total Labor Time in Department " + departmentNumber + " on " + date + ": " + totalLaborTime);
	        } else {
	            System.out.println("No result found for the specified department and date.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private static void retrieveProcessesForAssembly(Connection connection) {
	    try (CallableStatement statement = connection.prepareCall("{call RetrieveProcessesForAssembly(?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input
	        System.out.print("Enter assembly_id: ");
	        int assemblyId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        // Set parameters and execute the stored procedure
	        statement.setInt(1, assemblyId);
	        ResultSet resultSet = statement.executeQuery();
	       
	        while (resultSet.next()) {
	            int processId = resultSet.getInt("ProcessID");
	            int departmentNumber = resultSet.getInt("DepartmentNumber");
	            Date commencedDate = resultSet.getDate("CommencedDate");
	            System.out.println("ProcessID: " + processId + ", DepartmentNumber: " + departmentNumber + ", CommencedDate: " + commencedDate);
	                }
	         
	       
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



	private static void retrieveCustomersByCategoryRange(Connection connection) {
	    try (CallableStatement statement = connection.prepareCall("{call RetrieveCustomerNamesByCategoryRange(?, ?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input for category range
	        System.out.print("Enter minimum category: ");
	        int minCategory = scanner.nextInt();

	        System.out.print("Enter maximum category: ");
	        int maxCategory = scanner.nextInt();

	        // Set parameters for the stored procedure
	        statement.setInt(1, minCategory);
	        statement.setInt(2, maxCategory);

	        // Execute the stored procedure
	        ResultSet resultSet = statement.executeQuery();
	        while(resultSet.next()) {
	        	String customerName = resultSet.getString("customer_name");
	        	System.out.println(customerName);
	        }

	        System.out.println("Customers retrieved successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private static void deleteCutJobsByJobNumberRange(Connection connection) {
	    try (CallableStatement statement = connection.prepareCall("{call DeleteCutJobsByJobNumberRange(?, ?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input for job number range
	        System.out.print("Enter minimum job number: ");
	        int minJobNumber = scanner.nextInt();

	        System.out.print("Enter maximum job number: ");
	        int maxJobNumber = scanner.nextInt();

	        // Set parameters for the stored procedure
	        statement.setInt(1, minJobNumber);
	        statement.setInt(2, maxJobNumber);

	        // Execute the stored procedure
	        statement.execute();

	        System.out.println("Cut jobs deleted successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private static void updatePaintJobColor(Connection connection) {
	    try (CallableStatement statement = connection.prepareCall("{call UpdatePaintJobColor(?, ?)}")) {
	        Scanner scanner = new Scanner(System.in);

	        // Get user input for paint job details
	        System.out.print("Enter job number for the paint job: ");
	        int jobNumber = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        System.out.print("Enter new paint color: ");
	        String newColor = scanner.nextLine();

	        // Set parameters for the stored procedure
	        statement.setInt(1, jobNumber);
	        statement.setString(2, newColor);

	        // Execute the stored procedure
	        statement.execute();

	        System.out.println("Paint job color updated successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private static void importDataFromFile(Connection connection) {
        
            Scanner scanner = new Scanner(System.in);

            // Get user input for the CSV file path
            System.out.print("Enter the path of the CSV file: ");
            String filePath = scanner.nextLine();
            File file = new File(filePath);

            // Read data from the CSV file and insert new customers into the database
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Split CSV line into values
                    String[] values = line.split(",");
                    
                    // Assuming the CSV format is: customerName, customerAddress, category
                    String customerName = values[0].trim();
                    String customerAddress = values[1].trim();
                    int category = Integer.parseInt(values[2].trim());

                    // Call the stored procedure to insert a new customer
                    insertCustomerFromFile(connection, customerName, customerAddress, category);
                }

                System.out.println("Data imported successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
	private static void insertCustomerFromFile(Connection connection, String customerName, String customerAddress, int category) {
        try (CallableStatement statement = connection.prepareCall("{call InsertCustomer(?, ?, ?)}")) {
            statement.setString(1, customerName);
            statement.setString(2, customerAddress);
            statement.setInt(3, category);

            // Execute the stored procedure
            statement.execute();

            System.out.println("Customer '" + customerName + "' inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	private static void retrieveCustomersByCategoryRangeAndExport(Connection connection) {
        try (CallableStatement statement = connection.prepareCall("{call RetrieveCustomerNamesByCategoryRange(?, ?)}")) {
            Scanner scanner = new Scanner(System.in);

            // Get user input for category range
            System.out.print("Enter minimum category: ");
            int minCategory = scanner.nextInt();

            System.out.print("Enter maximum category: ");
            int maxCategory = scanner.nextInt();

            // Set parameters for the stored procedure
            statement.setInt(1, minCategory);
            statement.setInt(2, maxCategory);

            // Execute the stored procedure
            ResultSet resultSet = statement.executeQuery();

            // Get output file name from the user
            System.out.print("Enter the output file name: ");
            String outputFileName = scanner.next();

            // Write the results to the output file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                while (resultSet.next()) {
                    String customerName = resultSet.getString("customer_name");
                    
                    // Write customer details to the file
                    writer.write("Name: " + customerName);
                    writer.newLine();
                }

                System.out.println("Customers exported successfully to " + outputFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
