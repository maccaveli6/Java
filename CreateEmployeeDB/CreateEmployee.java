import java.sql.*;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateEmployee {
	private static final String URL = "jdbc:derby:employees";
	private static final String USERNAME = "deitel";
	private static final String PASSWORD = "deitel";

	private static Connection connection;
	private static PreparedStatement insertNewPerson;
	private static PreparedStatement addHourlyEmployee;
	private static PreparedStatement addSalariedEmployee;
	private static PreparedStatement addCommissionEmployee;
	private static PreparedStatement addBasePlusCommissionEmployee;

	private static final int MAX_SALARY = 500000;
	private static final int MAX_HOURLY = 7500;
	private static final int MAX_SALES = 10000000;
	private static final int MAX_BONUS = 100000;
	private static final int MAX_HOURS = 7000;
	private static Scanner in = new Scanner(System.in);

	public static void main(String args[]) {
		boolean EXIT = false;
		// Establish connection to DB and create prepared statements
		connectionManager();
		do {
			System.out.println("Would you like to enter a new employee? (y/n)");
			String newEntry = in.nextLine();
			if (newEntry.toLowerCase().equals("y") || newEntry.toLowerCase().equals("yes")) {

				// Prompt user to enter new employee data adds employee to Db generates random
				// data for employeeType
				// pay data and adds employee to employee type Db
				addEmployee();

				System.out.println("EMPLOYEE ADDED!");
			} else {
				EXIT = true;
			}
		} while (!EXIT);

		// print DB tables
		String table = "hourlyEmployees";
		printTable(table);
		printTable("commissionEmployees");
		printTable("basePlusCommissionEmployees");
		printTable("salariedEmployees");
		printTable("employees");

	}

	private static void addEmployee() {
		List<String> input = getEmployeeInput();
		if (!input.get(0).trim().equals("")) {
			String SocialSecurityNumber = input.get(0);
			String firstName = input.get(1);
			String lastName = input.get(2);
			String DOB = input.get(3);
			// randomly generate employee pay type and department
			String employeeType = generatePayType();
			String departmentname = generateDepartment();

			// Validate SSN was correctly formatted
			SocialSecurityNumber = validateSSN(SocialSecurityNumber);
			// add employee to database
			addEmployeeToDB(SocialSecurityNumber, firstName, lastName, DOB, employeeType, departmentname);
			System.out.println(SocialSecurityNumber + " " + DOB);
			// add new employee to "employee type" database // hourly salary commission
			// based ...
			addEmployeeType(SocialSecurityNumber, employeeType);
		}

	}

	private static void connectionManager() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

			// create insert that adds a new entry into the database
			insertNewPerson = connection.prepareStatement("INSERT INTO Employees "
					+ "(SOCIALSECURITYNUMBER, FIRSTNAME, LASTNAME, BIRTHDAY, EMPLOYEETYPE, DEPARTMENTNAME) "
					+ "VALUES (?, ?, ?, ?, ?, ?)");

			addHourlyEmployee = connection.prepareStatement("INSERT INTO hourlyEmployees "
					+ "(SOCIALSECURITYNUMBER, HOURS, WAGE, BONUS) " + "VALUES (?, ?, ?, ?)");

			addCommissionEmployee = connection.prepareStatement("INSERT INTO commissionEmployees "
					+ "(SOCIALSECURITYNUMBER, GROSSSALES, COMMISSIONRATE, BONUS) " + "VALUES (?, ?, ?, ?)");

			addBasePlusCommissionEmployee = connection.prepareStatement("INSERT INTO basePlusCommissionEmployees "
					+ "(SOCIALSECURITYNUMBER, GROSSSALES, COMMISSIONRATE, BASESALARY, BONUS) "
					+ "VALUES (?, ?, ?, ?, ?)");

			addSalariedEmployee = connection.prepareStatement("INSERT INTO salariedEmployees "
					+ "(SOCIALSECURITYNUMBER, WeeklySalary, Bonus) " + "VALUES (?, ?, ?)");
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.exit(1);
		}
	}

	private static int addEmployeeToDB(String SocialSecurityNumber, String firstName, String lastName, String birthday,
			String employeeType, String departmentname) {

		// insert the new entry; returns # of rows updated
		try {
			// set parameters
			insertNewPerson.setString(1, SocialSecurityNumber);
			insertNewPerson.setString(2, firstName);
			insertNewPerson.setString(3, lastName);
			validateBday(birthday); // validates bday format and adds valid value to Db
			insertNewPerson.setString(5, employeeType);
			insertNewPerson.setString(6, departmentname);

			return insertNewPerson.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			return 0;
		}
	}

	private static int newHourlyEmployee(String SocialSecurityNumber, double hours, double wage, double bonus) {
		try {
			// set parameters
			addHourlyEmployee.setString(1, SocialSecurityNumber);
			addHourlyEmployee.setDouble(2, hours);
			addHourlyEmployee.setDouble(3, wage);
			addHourlyEmployee.setDouble(4, bonus);

			return addHourlyEmployee.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			return 0;
		}
	}

	private static int newCommissionEmployee(String SocialSecurityNumber, double sales, double commissionRate,
			double bonus) {
		try {
			// set parameters
			addCommissionEmployee.setString(1, SocialSecurityNumber);
			addCommissionEmployee.setDouble(2, sales);
			addCommissionEmployee.setDouble(3, commissionRate / 100);
			addCommissionEmployee.setDouble(4, bonus);

			return addCommissionEmployee.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			return 0;
		}
	}

	private static int newBasePlusCommissionEmployee(String SocialSecurityNumber, double sales, double commissionRate,
			double baseSalary, double bonus) {
		try {
			// set parameters
			addBasePlusCommissionEmployee.setString(1, SocialSecurityNumber);
			addBasePlusCommissionEmployee.setDouble(2, sales);
			addBasePlusCommissionEmployee.setDouble(3, commissionRate / 100);
			addBasePlusCommissionEmployee.setDouble(4, baseSalary);
			addBasePlusCommissionEmployee.setDouble(5, bonus);

			return addBasePlusCommissionEmployee.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			return 0;
		}
	}

	private static int newSalariedEmployee(String SocialSecurityNumber, double WeeklySalary, double bonus) {
		try {
			// set parameters
			addSalariedEmployee.setString(1, SocialSecurityNumber);
			addSalariedEmployee.setDouble(2, WeeklySalary);
			addSalariedEmployee.setDouble(3, bonus);

			return addSalariedEmployee.executeUpdate();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			return 0;
		}
	}

	private static void printTable(String table) {
		try (JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet()) {
			// specify JdbcRowSet properties
			rowSet.setUrl(URL);
			rowSet.setUsername(USERNAME);
			rowSet.setPassword(PASSWORD);
			rowSet.setCommand("SELECT * FROM " + table); // set query
			rowSet.execute(); // execute query
			// process query results
			ResultSetMetaData metaData = rowSet.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			System.out.printf(table + "Table of Employee Database:%n%n");

			// display rowset header
			for (int i = 1; i <= numberOfColumns; i++) {
				System.out.printf("%-8s\t", metaData.getColumnName(i));
			}
			System.out.println();

			// display each row
			while (rowSet.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					System.out.printf("%-18s\t", rowSet.getObject(i));
				}
				System.out.println();
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.exit(1);
		}
	}

	private static List<String> getEmployeeInput() {
		List<String> input = new ArrayList<>();
		String temp = "Enter the new employee's Social Security number (format XXX-XX-XXXX): ";
		System.out.print(temp);
		input.add(in.nextLine());
		temp = "Enter the new employee's first name: ";
		System.out.print(temp);
		input.add(in.nextLine());
		temp = "Enter the new employee's last name: ";
		System.out.print(temp);
		input.add(in.nextLine());
		temp = "Enter the new employee's date of birth (format YYYY-MM-DD): ";
		System.out.print(temp);
		input.add(in.nextLine());
		return input;
	}

	private static String validateSSN(String SocialSecurityNumber) {
		String regex = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$";

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(SocialSecurityNumber);
		if (!matcher.matches()) {
			System.out
					.println("Social security number must be in the format XXX-XX-XXXX \n Please enter a valid SSN: ");
			SocialSecurityNumber = in.nextLine();
			SocialSecurityNumber = validateSSN(SocialSecurityNumber);
		}
		System.out.println(SocialSecurityNumber + " : " + matcher.matches());
		return SocialSecurityNumber;
	}

	private static void validateBday(String birthday) {
		try {
			insertNewPerson.setString(4, birthday);
		} catch (SQLException sqlException) {
			System.out.println("Invalid Bday");
			System.out.println("Enter a valid date of birth.\nThe proper format is YYYY-MM-DD");
			birthday = in.nextLine();
			validateBday(birthday.trim());
		}
	}

	private static String generateDepartment() {
		Random rand = new Random();
		int rng = rand.nextInt(3);
		if (rng == 0) {
			return "R&D";
		} else if (rng == 1) {
			return "HR";
		} else {
			return "SALES";
		}
	}

	private static String generatePayType() {
		Random rand = new Random();
		int rng = rand.nextInt(4);
		if (rng == 0) {
			return "salariedEmployee";
		} else if (rng == 1) {
			return "hourlyEmployee";
		} else if (rng == 2) {
			return "commissionEmployee";
		} else {
			return "basePlusCommissionEmployee";
		}
	}

	private static void addEmployeeType(String SocialSecurityNumber, String employeeType) {
		Random rand = new Random();
		if (employeeType.equals("hourlyEmployee")) {
			newHourlyEmployee(SocialSecurityNumber, (rand.nextInt(MAX_HOURS) / 100.0),
					(rand.nextInt(MAX_HOURLY) / 100.0), (rand.nextInt(MAX_BONUS) / 100.0));
		} else if (employeeType.equals("commissionEmployee")) {
			newCommissionEmployee(SocialSecurityNumber, (rand.nextInt(MAX_SALES) / 100.0), (rand.nextInt(100)),
					(rand.nextInt(MAX_BONUS) / 100.0));
		} else if (employeeType.equals("basePlusCommissionEmployee")) {
			newBasePlusCommissionEmployee(SocialSecurityNumber, (rand.nextInt(MAX_SALES) / 100.0), (rand.nextInt(100)),
					(rand.nextInt(MAX_SALARY) / 100.0), (rand.nextInt(MAX_BONUS) / 100.0));
		} else {
			newSalariedEmployee(SocialSecurityNumber, (rand.nextInt(MAX_SALARY) / 100.0),
					(rand.nextInt(MAX_BONUS) / 100.0));
		}

	}
}