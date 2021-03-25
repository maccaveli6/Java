
//package clientServerJFrames;
import java.util.List;
import java.util.ArrayList;

import java.io.*;
import java.net.ServerSocket;
import java.awt.BorderLayout;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server extends JFrame {

	private JTextField enterField; // inputs message from user
	private JTextArea displayArea; // display information to user
	private ObjectOutputStream output; // output stream to client
	private ObjectInputStream input;// input stream from client
	private ServerSocket server;// server socket
	private Socket connection;// connection to client
	private int counter = 1; // counter of number of connections

	private int[][] matrix1A;
	private int[][] matrix2A;
	private int[][] matrix1B;
	private int[][] matrix2B;
	private int[][] matrix1C;
	private int[][] matrix2C;
	private int[][] matrix1D;
	private int[][] matrix2D;
	private int[][] matrix3;
	private int rows;
	private int cols;
	private int start_row;
	private int start_col;
	private String[] fileInput;
	private ThreadOperation MatrixA;
	private ThreadOperation MatrixB;
	private ThreadOperation MatrixC;
	private ThreadOperation MatrixD;
	private ExecutorService executorService;

	// set up GUI
	public Server() {
		super("Server");

		// Create text field to send data back to manually send data back to client if
		// needed
		enterField = new JTextField();
		enterField.setEditable(false);
		enterField.addActionListener(new ActionListener() {
			// Send message to client
			public void actionPerformed(ActionEvent event) {
				sendData(event.getActionCommand());
				enterField.setText("");
			}
		});
		add(enterField, BorderLayout.NORTH);

		displayArea = new JTextArea(); // create displayArea
		add(new JScrollPane(displayArea), BorderLayout.CENTER);

		setSize(800, 350);
		setVisible(true);
	}

	public void runServer() {
		try {
			server = new ServerSocket(12345, 100);
			while (true) {
				try {
					waitForConnection();
					getStreams();
					processConnection();
				} catch (EOFException eofException) {
					displayMessage("\nServer Terminate Connection");
				} finally {
					closeConnection();
					++counter;
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}

	}

	private void waitForConnection() throws IOException {
		displayMessage("Waiting for Connection\n");
		connection = server.accept();
		displayMessage("Connection " + counter + " received from: " + connection.getInetAddress().getHostName());
	}

	private void getStreams() throws IOException {
		// set up output stream for objects
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush(); // flush output buffer to send header information

		// set up input stream for objects
		input = new ObjectInputStream(connection.getInputStream());

		System.out.println("\nGot I/O streams\n");
	}

	private void processConnection() throws IOException {
		String message = "Connection Successful";
		sendData(message);
		setTextFieldEditable(true);

		do {
			try {
				message = (String) input.readObject();
				displayMessage("\n" + message);
			} catch (ClassNotFoundException classNotFoundException) {
				displayMessage("\nUnknow object type received");
			}
			if (message.contains("MATRIX INPUT>>> ")) {
				cleanInputString(message);
				sendData(" MATRIX RECEIVED\n");
				loadMatrix(); // time complexity: O(n) space complexity: O(n) where n = N * M
				startThreads();
				verifyThreadComplete();
				transmitMatrix();
			}
		} while (!message.equals("CLIENT>>> TERMINATE"));
	}

	private void closeConnection() {
		displayMessage("\nTerminating Connection\n");
		setTextFieldEditable(false);

		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private void sendData(String message) {
		try {
			output.writeObject("SERVER>>> " + message);
			output.flush();
			displayMessage("\nSERVER>>> " + message);
		} catch (IOException ioException) {
			displayArea.append("\nError Writing Object");
		}
	}

	private void sendMatrix(String message) {
		try {
			output.writeObject("MATRIX OUTPUT>>> " + message);
			output.flush();
			displayMessage("\nMATRIX OUTPUT>>> " + message);
		} catch (IOException ioException) {
			displayArea.append("\nError Writing Object");
		}
	}

	private void displayMessage(final String messageToDisplay) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				displayArea.append(messageToDisplay);
			}

		});
	}

	private void setTextFieldEditable(final boolean editable) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				enterField.setEditable(editable);
			}

		});
	}

	private void loadMatrix() {
		String[] line = fileInput[0].split(" ");
		rows = stringToInteger(line[0]);
		cols = stringToInteger(line[1]);
		// load both matrices. this is where matrices will be split for multi-thredding
		// requires O(n) (where n = N * M) at best
		// for even number of rows and columns
		if (rows % 2 == 0 && cols % 2 == 0) {
			evenRowsEvenCols(line, rows, cols);
		}
		// odd rows even columns
		else if ((rows % 2 == 1 && cols % 2 == 0)) {
			oddRowsEvenCols(line, rows, cols);
		}
		// even rows odd columns
		else if ((rows % 2 == 0 && cols % 2 == 1)) {
			evenRowsOddCols(line, rows, cols);
		}
		// odd rows odd columns
		else if ((rows % 2 == 1 && cols % 2 == 1)) {
			oddRowsOddCols(line, rows, cols);
		}
	}

	private void evenRowsEvenCols(String[] line, int rows, int cols) {
		start_row = rows / 2;
		start_col = cols / 2;
		matrix1A = new int[rows / 2][cols / 2];
		matrix2A = new int[rows / 2][cols / 2];
		matrix1B = new int[rows / 2][cols / 2];
		matrix2B = new int[rows / 2][cols / 2];
		matrix1C = new int[rows / 2][cols / 2];
		matrix2C = new int[rows / 2][cols / 2];
		matrix1D = new int[rows / 2][cols / 2];
		matrix2D = new int[rows / 2][cols / 2];
		for (int i = 0; i < rows / 2; i++) {
			line = fileInput[i + 1].split(" ");
			String[] line2 = fileInput[i + 1 + rows].split(" ");
			String[] line3 = fileInput[i + 1 + rows / 2].split(" ");
			String[] line4 = fileInput[i + 1 + rows + rows / 2].split(" ");
			for (int j = 0; j < line.length / 2; j++) {
				matrix1A[i][j] = stringToInteger(line[j]);
				matrix2A[i][j] = stringToInteger(line2[j]);
				matrix1B[i][j] = stringToInteger(line[j + cols / 2]);
				matrix2B[i][j] = stringToInteger(line2[j + cols / 2]);
				matrix1C[i][j] = stringToInteger(line3[j]);
				matrix2C[i][j] = stringToInteger(line4[j]);
				matrix1D[i][j] = stringToInteger(line3[j + cols / 2]);
				matrix2D[i][j] = stringToInteger(line4[j + cols / 2]);

			}
		}
	}

	private void oddRowsEvenCols(String[] line, int rows, int cols) {
		start_row = rows / 2 + 1;
		start_col = cols / 2;
		matrix1A = new int[rows / 2 + 1][cols / 2];
		matrix2A = new int[rows / 2 + 1][cols / 2];
		matrix1B = new int[rows / 2 + 1][cols / 2];
		matrix2B = new int[rows / 2 + 1][cols / 2];
		matrix1C = new int[rows / 2][cols / 2];
		matrix2C = new int[rows / 2][cols / 2];
		matrix1D = new int[rows / 2][cols / 2];
		matrix2D = new int[rows / 2][cols / 2];
		// load upper A, B matrices
		for (int i = 0; i < rows / 2 + 1; i++) {
			line = fileInput[i + 1].split(" ");
			String[] line2 = fileInput[i + 1 + rows].split(" ");
			for (int j = 0; j < line.length / 2; j++) {
				matrix1A[i][j] = stringToInteger(line[j]);
				matrix2A[i][j] = stringToInteger(line2[j]);
				matrix1B[i][j] = stringToInteger(line[j + start_col]);
				matrix2B[i][j] = stringToInteger(line2[j + start_col]);
			}
		}
		// load C, D matrices
		for (int i = 0; i < rows / 2; i++) {
			String[] line3 = fileInput[i + 1 + start_row].split(" ");
			String[] line4 = fileInput[i + 1 + rows + start_row].split(" ");
			for (int j = 0; j < line.length / 2; j++) {
				matrix1C[i][j] = stringToInteger(line3[j]);
				matrix2C[i][j] = stringToInteger(line4[j]);
				matrix1D[i][j] = stringToInteger(line3[j + start_col]);
				matrix2D[i][j] = stringToInteger(line4[j + start_col]);
			}
		}
	}

	private void evenRowsOddCols(String[] line, int rows, int cols) {
		start_row = rows / 2;
		start_col = cols / 2 + 1;
		matrix1A = new int[rows / 2][cols / 2 + 1];
		matrix2A = new int[rows / 2][cols / 2 + 1];
		matrix1B = new int[rows / 2][cols / 2];
		matrix2B = new int[rows / 2][cols / 2];
		matrix1C = new int[rows / 2][cols / 2 + 1];
		matrix2C = new int[rows / 2][cols / 2 + 1];
		matrix1D = new int[rows / 2][cols / 2];
		matrix2D = new int[rows / 2][cols / 2];
		for (int i = 0; i < rows / 2; i++) {
			line = fileInput[i + 1].split(" ");
			String[] line2 = fileInput[i + 1 + rows].split(" ");
			String[] line3 = fileInput[i + 1 + rows / 2].split(" ");
			String[] line4 = fileInput[i + 1 + rows + rows / 2].split(" ");
			matrix1A[i][0] = stringToInteger(line[0]);
			matrix2A[i][0] = stringToInteger(line2[0]);
			matrix1C[i][0] = stringToInteger(line3[0]);
			matrix2C[i][0] = stringToInteger(line4[0]);
			for (int j = 0; j < line.length / 2; j++) {
				matrix1A[i][j + 1] = stringToInteger(line[j + 1]);
				matrix2A[i][j + 1] = stringToInteger(line2[j + 1]);
				matrix1B[i][j] = stringToInteger(line[j + start_col]);
				matrix2B[i][j] = stringToInteger(line2[j + start_col]);
				matrix1C[i][j + 1] = stringToInteger(line3[j + 1]);
				matrix2C[i][j + 1] = stringToInteger(line4[j + 1]);
				matrix1D[i][j] = stringToInteger(line3[j + start_col]);
				matrix2D[i][j] = stringToInteger(line4[j + start_col]);
			}
		}
	}

	private void oddRowsOddCols(String[] line, int rows, int cols) {
		start_row = rows / 2 + 1;
		start_col = cols / 2 + 1;
		matrix1A = new int[rows / 2 + 1][cols / 2 + 1];
		matrix2A = new int[rows / 2 + 1][cols / 2 + 1];
		matrix1B = new int[rows / 2 + 1][cols / 2];
		matrix2B = new int[rows / 2 + 1][cols / 2];
		matrix1C = new int[rows / 2][cols / 2 + 1];
		matrix2C = new int[rows / 2][cols / 2 + 1];
		matrix1D = new int[rows / 2][cols / 2];
		matrix2D = new int[rows / 2][cols / 2];
		// load upper A, B matrices
		for (int i = 0; i < rows / 2 + 1; i++) {
			line = fileInput[i + 1].split(" ");
			String[] line2 = fileInput[i + 1 + rows].split(" ");
			matrix1A[i][0] = stringToInteger(line[0]);
			matrix2A[i][0] = stringToInteger(line2[0]);
			for (int j = 0; j < line.length / 2; j++) {
				matrix1A[i][j + 1] = stringToInteger(line[j + 1]);
				matrix2A[i][j + 1] = stringToInteger(line2[j + 1]);
				matrix1B[i][j] = stringToInteger(line[j + start_col]);
				matrix2B[i][j] = stringToInteger(line2[j + start_col]);
			}
		}
		// load C, D matrices
		for (int i = 0; i < rows / 2; i++) {
			String[] line3 = fileInput[i + 1 + start_row].split(" ");
			String[] line4 = fileInput[i + 1 + rows + start_row].split(" ");
			matrix1C[i][0] = stringToInteger(line3[0]);
			matrix2C[i][0] = stringToInteger(line4[0]);
			for (int j = 0; j < line.length / 2; j++) {
				matrix1C[i][j + 1] = stringToInteger(line3[j + 1]);
				matrix2C[i][j + 1] = stringToInteger(line4[j + 1]);
				matrix1D[i][j] = stringToInteger(line3[j + start_col]);
				matrix2D[i][j] = stringToInteger(line4[j + start_col]);
			}
		}
	}

	private int stringToInteger(String numeric_string) {
		try {
			int int_output = Integer.parseInt(numeric_string);
			return int_output;
		} catch (Exception e) {
			displayArea.append("\nThere was an issue converting the Sting to an Integer");
			return 0;
		}
	}

	public void setMatrix(int[][] value, int row_idx, int col_idx) {
		for (int i = 0; i < value.length; i++) {
			for (int j = 0; j < value[0].length; j++) {
				matrix3[row_idx + i][col_idx + j] = value[i][j];
			}
		}
	}

	private void transmitMatrix() {
		for (int i = 0; i < rows; i++) {
			String temp = "";
			for (int j = 0; j < cols; j++) {
				temp = temp + String.format("%3d", matrix3[i][j]);
			}
			sendMatrix(temp);
		}
	}

	private void verifyThreadComplete() {
		try {
			boolean tasksEnded = executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
			if (tasksEnded) {
				setMatrix(MatrixA.getResult(), MatrixA.getFirstRow(), MatrixA.getFirstCol());
				setMatrix(MatrixB.getResult(), MatrixB.getFirstRow(), MatrixB.getFirstCol());
				setMatrix(MatrixC.getResult(), MatrixC.getFirstRow(), MatrixC.getFirstCol());
				setMatrix(MatrixD.getResult(), MatrixD.getFirstRow(), MatrixD.getFirstCol());
			} else {
				displayArea.append("Timed out while waiting for tasks to finish");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startThreads() {
		matrix3 = new int[rows][cols];
		MatrixA = new ThreadOperation(matrix1A, matrix2A, 0, 0);
		MatrixB = new ThreadOperation(matrix1B, matrix2B, 0, start_col);
		MatrixC = new ThreadOperation(matrix1C, matrix2C, start_row, 0);
		MatrixD = new ThreadOperation(matrix1D, matrix2D, start_row, start_col);
		executorService = Executors.newCachedThreadPool();
		executorService.execute(MatrixA); // time complexity: O(n/4) space complexity : O(n/4)
		executorService.execute(MatrixB); // time complexity: O(n/4) space complexity : O(n/4)
		executorService.execute(MatrixC); // time complexity: O(n/4) space complexity : O(n/4)
		executorService.execute(MatrixD); // time complexity: O(n/4) space complexity : O(n/4)
		executorService.shutdown();
	}

	private void cleanInputString(String message) {
		message = message.replace("[", "");
		message = message.replace("]", "");
		String[] tempInput = message.split(">>>");
		fileInput = tempInput[1].split(",");
		trimInput();
	}

	private void trimInput() {
		for (int i = 0; i < fileInput.length; i++) {
			fileInput[i] = fileInput[i].trim();
		}
	}
}
