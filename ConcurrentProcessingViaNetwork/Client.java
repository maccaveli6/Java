
//package clientServerJFrames;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.awt.BorderLayout;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import javax.swing.*;

import javax.swing.SwingUtilities;

public class Client extends JFrame {
	private JTextField enterField; // inputs message from user
	private JTextArea displayArea; // display information to user
	private ObjectOutputStream output; // output stream to client
	private ObjectInputStream input;// input stream from client
	private String message = "";// server socket
	private String chatServer;
	private Socket client;// connection to client

	public Client(String host) {
		super("Client");

		chatServer = host;

		// Create a new JPanel to format file entry label and text box
		JPanel test = new JPanel();
		test.setLayout(new BoxLayout(test, BoxLayout.X_AXIS));

		// Create a label for file entry
		JLabel label = new JLabel("Enter File Name: ");
		label.setFont(new Font("Serif", Font.PLAIN, 20));
		test.add(label);

		// Create new text box for file entry
		enterField = new JTextField();
		enterField.setEditable(false);
		enterField.setFont(new Font("Serif", Font.PLAIN, 20));
		enterField.addActionListener(new ActionListener() {
			// Send message to client
			public void actionPerformed(ActionEvent event) {
				getInput(event.getActionCommand());
				enterField.setText("");
			}
		});
		test.add(enterField);

		add(test, BorderLayout.NORTH);

		// Create output window for client-server interaction
		displayArea = new JTextArea(); // create displayArea
		add(new JScrollPane(displayArea), BorderLayout.CENTER);

		// Create a button to terminate connection with server and close window
		JButton terminate = new JButton("CLICK TO TERMINATE CONNECTION");
		terminate.setFont(new Font("Serif", Font.PLAIN, 20));
		terminate.setBackground(Color.red);
		terminate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendData("TERMINATE");
				dispose();
			}
		});
		add(terminate, BorderLayout.SOUTH);

		setSize(800, 350);
		setVisible(true);
	}

	public void runClient() {
		try {
			connectToServer();
			getStreams();
			processConnection();
		} catch (EOFException eofException) {
			displayMessage("\nClient Terminated Connection");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	private void connectToServer() throws IOException {
		displayMessage("Attempting Connection\n");

		client = new Socket(InetAddress.getByName(chatServer), 12345);

		displayMessage("Connected to " + client.getInetAddress().getHostName());
	}

	private void getStreams() throws IOException {
		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();

		input = new ObjectInputStream(client.getInputStream());

		displayMessage("Got I/O Streams\n");
	}

	private void processConnection() throws IOException {
		setTextFieldEditable(true);

		do {
			try {
				message = (String) input.readObject();
				displayMessage("\n" + message);
			} catch (ClassNotFoundException classNotFoundException) {
				displayMessage("\nUnknow object type received");
			}
		} while (!message.equals("SERVER>>> TERMINATE"));
	}

	private void closeConnection() {
		displayMessage("\nTerminating Connection\n");
		setTextFieldEditable(false);

		try {
			output.close();
			input.close();
			client.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private void sendData(String message) {
		try {
			output.writeObject("CLIENT>>> " + message);
			output.flush();
			displayMessage("\nCLIENT>>> " + message);
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

	private void getInput(String filename) {
		List<String> fileInput = new ArrayList<>();
		try {
			Scanner file_in = new Scanner(new File(filename));
			while (file_in.hasNextLine()) {
				fileInput.add(file_in.nextLine());
			}
			file_in.close();
		} catch (Exception e) {
			displayArea.append("\nError Reading File");
		}
		try {
			output.writeObject("MATRIX INPUT>>> " + fileInput);
			output.flush();
			displayMessage("\nMATRIX INPUT>>> " + fileInput);
		} catch (IOException ioException) {
			displayArea.append("\nError Writing Object");
		}
	}
}
