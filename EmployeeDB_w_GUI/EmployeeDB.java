//Philip Garcia CS2251 Spring 2021
// Lab 6.2 Employee Db with GUI

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.time.LocalDate;
import java.time.Month;

import javax.swing.*;

public class EmployeeDB extends JFrame {
	// database URL, username and password
	private static final String URL = "jdbc:derby:employees";
	private static final String USERNAME = "deitel";
	private static final String PASSWORD = "deitel";

	// default query retrieves all data from authors table
	private static final String DEFAULT_QUERY = "SELECT * FROM employees";
	private static final long serialVersionUID = 87553159;

	//Create vars for gui
	static JComboBox<String> comboBox;
	static JTable jTable;
	TableModel tableModel;

	// Vars for SQL connections
	Connection connection;
	Statement statement;

	public EmployeeDB() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
		} catch (SQLException sqlException) {
			System.out.println(sqlException.getMessage());
			System.exit(1);
		}
		// set up JTextArea in which user types queries
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JTextArea queryArea = new JTextArea(DEFAULT_QUERY, 3, 100);
		queryArea.setWrapStyleWord(true);
		queryArea.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(queryArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// set up JButton for submitting queries
		JButton submitButton = new JButton("Submit Query");

		// create Box to manage placement of queryArea and
		// submitButton in GUI
		Box boxNorth = Box.createHorizontalBox();
		boxNorth.add(scrollPane);
		boxNorth.add(submitButton);

		// Init controls for table output
		tableModel = new TableModel();

		// Creates an instance of JTable with a TableModel
		// as the constructor parameters.
		jTable = new JTable(tableModel);
		// Load JTable w data based on default query
		queryDB(DEFAULT_QUERY);

		// ComboBox Labels
		String comboBoxLabels[] = { "show all sales people", "hourly employees with more than 30 hours",
				"show commission employees", "Increase base+commission salary 10%", "Birthday Bonus",
				"Commission performance bonus" };

		// create comboBox Dropdown
		comboBox = new JComboBox<String>(comboBoxLabels);

		JLabel comboBoxLabel = new JLabel("Common Operations:");
		comboBoxLabel.setFont(new Font("Serif", Font.PLAIN, 12));
		comboBoxLabel.setForeground(Color.blue);
		JButton comboBoxButton = new JButton("Apply Shortcut");
		comboBoxButton.setFont(new Font("Serif", Font.PLAIN, 12));
		Box boxSouth = Box.createHorizontalBox();
	
		boxSouth.add(comboBoxLabel);
		boxSouth.add(comboBox);
		boxSouth.add(comboBoxButton);

		// place GUI components on JFrame's content pane
		JFrame window = new JFrame("Displaying Query Results");
		window.add(boxNorth, BorderLayout.NORTH);
		window.add(new JScrollPane(jTable), BorderLayout.CENTER);
		window.add(boxSouth, BorderLayout.SOUTH);

		// create event listener for submitButton
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String sql = queryArea.getText();
				boolean isquery = isQuery(sql);

				// is the command a query or update
				if (isquery) {
					queryDB(sql);
				} else {
					updateDB(sql);
				}
			}
		});
		comboBoxButton.addActionListener(new ActionListener() {
			// pass filter text to listener
			public void actionPerformed(ActionEvent e) {
				String sql;
				if (comboBox.getSelectedItem().equals("show all sales people")) {
					sql = "SELECT * FROM employees WHERE departmentname = 'SALES'";
					queryDB(sql);
				} else if (comboBox.getSelectedItem().equals("hourly employees with more than 30 hours")) {
					sql = "SELECT * FROM employees INNER JOIN hourlyemployees ON employees.socialsecuritynumber=hourlyemployees.socialsecuritynumber WHERE hourlyemployees.hours >= 30";
					queryDB(sql);
				} else if (comboBox.getSelectedItem().equals("show commission employees")) {
					sql = "SELECT * FROM employees INNER JOIN commissionEmployees ON employees.socialsecuritynumber = commissionEmployees.socialsecuritynumber ORDER BY commissionEmployees.commissionrate DESC";
					queryDB(sql);
				} else if (comboBox.getSelectedItem().equals("Increase base+commission salary 10%")) {
					sql = "UPDATE basepluscommissionEmployees SET basesalary = basesalary * 1.1";
					updateDB(sql);
					sql = "SELECT * FROM basepluscommissionEmployees";
					queryDB(sql);
				} else if (comboBox.getSelectedItem().equals("Birthday Bonus")) {
					LocalDate currentdate = LocalDate.now();
					int currentMonth = currentdate.getMonthValue();
					sql = "UPDATE hourlyEmployees SET hourlyemployees.bonus = hourlyemployees.bonus + 100 WHERE EXISTS (SELECT socialsecuritynumber FROM employees WHERE hourlyEmployees.socialsecuritynumber =  employees.socialsecuritynumber AND MONTH(employees.birthday)="
							+ currentMonth + ")";
					updateDB(sql);
					sql = "UPDATE basepluscommissionEmployees SET basepluscommissionEmployees.bonus = basepluscommissionEmployees.bonus + 100 WHERE EXISTS (SELECT socialsecuritynumber FROM employees WHERE basepluscommissionEmployees.socialsecuritynumber =  employees.socialsecuritynumber AND MONTH(employees.birthday)="
							+ currentMonth + ")";
					updateDB(sql);
					sql = "UPDATE salariedEmployees SET salariedEmployees.bonus = salariedEmployees.bonus + 100 WHERE EXISTS (SELECT socialsecuritynumber FROM employees WHERE salariedEmployees.socialsecuritynumber =  employees.socialsecuritynumber AND MONTH(employees.birthday)="
							+ currentMonth + ")";
					updateDB(sql);
					sql = "UPDATE commissionEmployees SET commissionEmployees.bonus = commissionEmployees.bonus + 100 WHERE EXISTS (SELECT socialsecuritynumber FROM employees WHERE commissionEmployees.socialsecuritynumber =  employees.socialsecuritynumber AND MONTH(employees.birthday)="
							+ currentMonth + ")";
					updateDB(sql);
					String[] columns = { "Birthday Bonus was applied" };
					String[][] data = new String[1][1];
					data[0][0] = "";
					tableModel.updateTable(columns, data);

				} else if (comboBox.getSelectedItem().equals("Commission performance bonus")) {
					sql = "UPDATE commissionEmployees SET bonus = bonus + 100 WHERE grosssales>=10000";
					updateDB(sql);
					sql = "SELECT * FROM commissionEmployees";
					queryDB(sql);
				}
			}
		});

		// dispose of window when user quits application (this overrides
		// the default of HIDE_ON_CLOSE)
		window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		window.setSize(650, 500);
		window.setVisible(true);

		// ensure database is closed when user quits application
		window.addWindowListener(new WindowAdapter() {
		});
	}

	private boolean isQuery(String input) {
		if (input.toLowerCase().matches("(.*)select(.*)")) {
			return true;
		} else {
			return false;
		}
	}

	private void queryDB(String sql) {
		ResultSet result;
		String[] columnHeaders;
		String[][] data;
		try {
			result = statement.executeQuery(sql);
			// process query results
			ResultSetMetaData metaData = result.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			columnHeaders = new String[numberOfColumns];
			result.last();
			data = new String[result.getRow()][numberOfColumns];

			// // display rowset header
			for (int i = 1; i <= numberOfColumns; i++) {
				columnHeaders[i - 1] = metaData.getColumnName(i);
			}

			// // display each row
			result.beforeFirst();
			int idx = 0;
			while (result.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					data[idx][i - 1] = result.getObject(i).toString();
				}
				idx++;
			}
			tableModel.updateTable(columnHeaders, data);
		} catch (SQLException sqlException) {
			System.out.println(sqlException.getMessage());
			System.exit(1);
		}
	}

	private void updateDB(String sql) {
		try {
			int rslt = statement.executeUpdate(sql); // set query
			String[] columns = { rslt + " Rows were updated" };
			String[][] data = new String[1][1];
			data[0][0] = "";
			tableModel.updateTable(columns, data);
		} catch (SQLException sqlException) {
			System.out.println(sqlException.getMessage());
			System.exit(1);
		}
	}
}
