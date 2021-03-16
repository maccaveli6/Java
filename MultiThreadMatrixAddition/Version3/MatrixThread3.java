//Programming Assignment #3: Matrix addition using multithredding
//break 2 NxM matrices into 8 equal (or almost equal) matrices
//Then add them using multithredding 
//================================================
//School of Business and Information Technology
//Central New Mexico Community College
//Course: CSCI-2251, Spring 2021
//Author: Philip Garcia
//02/20/2021

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit; 

public class MatrixThread3 {
//	Matrix layout
//	 _______
//	|		|
//	| A , B |
//	| C , D |
//  |_______|
//	private static int[][] matrix1; private static int[][] matrix2;
	private static int[][] matrix1A; private static int [][] matrix2A; private static int[][] matrix1B; private static int [][] matrix2B;
	private static int[][] matrix1C; private static int [][] matrix2C; private static int[][] matrix1D; private static int [][] matrix2D;
	private static int[][] matrix3; private static int rows; private static int cols; private static int start_row; private static int start_col;
	private static List<String> input = new ArrayList<>();
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Please Enter a Filename:");
		String filename = in.nextLine();
		getInput(filename);

		loadMatrix(); //time complexity: O(n) space complexity: O(n) where n = N * M
		matrix3 = new int [rows][cols];
//		long startTime = System.currentTimeMillis();
		ThreadOperation MatrixA = new ThreadOperation(matrix1A, matrix2A, 0, 0); 
		ThreadOperation MatrixB = new ThreadOperation(matrix1B, matrix2B, 0, start_col); 
		ThreadOperation MatrixC = new ThreadOperation(matrix1C, matrix2C, start_row, 0); 
		ThreadOperation MatrixD = new ThreadOperation(matrix1D, matrix2D, start_row, start_col); 
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(MatrixA); // time complexity: O(n/4) space complexity : O(n/4) 
		executorService.execute(MatrixB); // time complexity: O(n/4) space complexity : O(n/4)
		executorService.execute(MatrixC); // time complexity: O(n/4) space complexity : O(n/4)
		executorService.execute(MatrixD); // time complexity: O(n/4) space complexity : O(n/4)
		executorService.shutdown();
		verifyThreadComplete(executorService, MatrixA, MatrixB, MatrixC, MatrixD); //time complexity: O(n) space complexity: O(n) where n = N * M

//		long endTime   = System.currentTimeMillis();

//		long totalTime = endTime - startTime;
//		System.out.printf("\nTotal Runtime Multi Thread %d\n", totalTime);
		
//		loadSingleMatrix(); //time complexity: O(n) space complexity: O(n) where n = N * M
//		startTime = System.currentTimeMillis();
//		addMatrices(matrix1, matrix2); //time complexity: O(2n) space complexity: O(2n) which generalize to O(n)
//		endTime   = System.currentTimeMillis();

//		totalTime = endTime - startTime;
//		System.out.printf("\nTotal Runtime Single Thread %d\n", totalTime);
		
		in.close();
	}
	private static void getInput (String filename) {
		try{
			Scanner file_in = new Scanner(new File(filename));
			while(file_in.hasNextLine()) {
				input.add(file_in.nextLine());	
			}
			file_in.close();
		}
		catch (Exception e) {
			System.out.println("Error Reading File");
		}
	}
	private static void loadMatrix() {
		String[] line = input.get(0).split(" ");
		rows = stringToInteger(line[0]);
		cols = stringToInteger(line[1]);
		//for even number of rows and columns
		if (rows % 2 == 0 && cols % 2 == 0) {
			evenRowsEvenCols(line, rows, cols);
		}
		// odd rows even columns
		else if ((rows % 2 == 1 && cols % 2 == 0)) {
			oddRowsEvenCols(line, rows, cols);
		}
		//even rows odd columns
		else if ((rows % 2 == 0 && cols % 2 == 1)) {
			evenRowsOddCols(line, rows, cols);
		}
		//odd rows odd columns
		else if ((rows % 2 == 1 && cols % 2 == 1)) {
			oddRowsOddCols(line, rows, cols);
		}
	//		System.out.printf("number of rows %d \nnumber of columns %d\n", rows, cols);
	}
	private static void evenRowsEvenCols(String[] line, int rows, int cols) {
		start_row = rows/2; start_col = cols/2;
		matrix1A = new int[rows/2][cols/2]; matrix2A = new int[rows/2][cols/2];
		matrix1B = new int[rows/2][cols/2]; matrix2B = new int[rows/2][cols/2];
		matrix1C = new int[rows/2][cols/2]; matrix2C = new int[rows/2][cols/2];
		matrix1D = new int[rows/2][cols/2]; matrix2D = new int[rows/2][cols/2];
		//load both matrices.  this is likely where matrices will be split for multi-thredding
		// requires O(n) (where n = N * M) at best
		for (int i = 0; i < rows/2; i++) {
			line = input.get(i+1).split(" ");
			String [] line2 = input.get(i+1+rows).split(" ");
			String [] line3 = input.get(i+1+rows/2).split(" ");
			String [] line4 = input.get(i+1+rows + rows/2).split(" ");
			for(int j =0; j < line.length/2; j++) {
				matrix1A[i][j] =  stringToInteger(line[j]);
				matrix2A[i][j] =  stringToInteger(line2[j]);
				matrix1B[i][j] =  stringToInteger(line[j + cols/2]);
				matrix2B[i][j] =  stringToInteger(line2[j + cols/2]);
				matrix1C[i][j] =  stringToInteger(line3[j]);
				matrix2C[i][j] =  stringToInteger(line4[j]);
				matrix1D[i][j] =  stringToInteger(line3[j + cols/2]);
				matrix2D[i][j] =  stringToInteger(line4[j + cols/2]);

			}
		}			
	}
	private static void oddRowsEvenCols(String[] line, int rows, int cols) {
		start_row = rows/2 + 1; start_col = cols/2;
		matrix1A = new int[rows/2 + 1][cols/2]; matrix2A = new int[rows/2 + 1][cols/2];
		matrix1B = new int[rows/2 + 1][cols/2]; matrix2B = new int[rows/2 + 1][cols/2];
		matrix1C = new int[rows/2][cols/2]; matrix2C = new int[rows/2][cols/2];
		matrix1D = new int[rows/2][cols/2]; matrix2D = new int[rows/2][cols/2];
		//load upper A, B matrices
		for (int i = 0; i < rows/2 + 1; i++) {
			line = input.get(i+1).split(" ");
			String [] line2 = input.get(i+1+rows).split(" ");
			for(int j =0; j < line.length/2; j++) {
				matrix1A[i][j] =  stringToInteger(line[j]);
				matrix2A[i][j] =  stringToInteger(line2[j]);
				matrix1B[i][j] =  stringToInteger(line[j + start_col]);
				matrix2B[i][j] =  stringToInteger(line2[j + start_col]);
			}			
		}
		//load C, D matrices
		for (int i = 0; i < rows/2; i++) {
			String [] line3 = input.get(i+1+start_row).split(" ");
			String [] line4 = input.get(i+1+rows + start_row).split(" ");
			for(int j =0; j < line.length/2; j++) {
				matrix1C[i][j] =  stringToInteger(line3[j]);
				matrix2C[i][j] =  stringToInteger(line4[j]);
				matrix1D[i][j] =  stringToInteger(line3[j + start_col]);
				matrix2D[i][j] =  stringToInteger(line4[j + start_col]);
			}
		}
	}
	private static void evenRowsOddCols(String[] line, int rows, int cols) {
		start_row = rows/2; start_col = cols/2 + 1;
		matrix1A = new int[rows/2][cols/2 + 1]; matrix2A = new int[rows/2][cols/2 + 1];
		matrix1B = new int[rows/2][cols/2]; matrix2B = new int[rows/2][cols/2];
		matrix1C = new int[rows/2][cols/2 + 1]; matrix2C = new int[rows/2][cols/2 + 1];
		matrix1D = new int[rows/2][cols/2]; matrix2D = new int[rows/2][cols/2];
		for (int i = 0; i < rows/2; i++) {
			line = input.get(i+1).split(" ");
			String [] line2 = input.get(i+1+rows).split(" ");
			String [] line3 = input.get(i+1+rows/2).split(" ");
			String [] line4 = input.get(i+1+rows + rows/2).split(" ");
			matrix1A[i][0] =  stringToInteger(line[0]);
			matrix2A[i][0] =  stringToInteger(line2[0]);
			matrix1C[i][0] =  stringToInteger(line3[0]);
			matrix2C[i][0] =  stringToInteger(line4[0]);				
			for(int j =0; j < line.length/2; j++) {
				matrix1A[i][j+1] =  stringToInteger(line[j+1]);
				matrix2A[i][j+1] =  stringToInteger(line2[j+1]);
				matrix1B[i][j] =  stringToInteger(line[j + start_col]);
				matrix2B[i][j] =  stringToInteger(line2[j + start_col]);
				matrix1C[i][j+1] =  stringToInteger(line3[j+1]);
				matrix2C[i][j+1] =  stringToInteger(line4[j+1]);
				matrix1D[i][j] =  stringToInteger(line3[j + start_col]);
				matrix2D[i][j] =  stringToInteger(line4[j + start_col]);
			}
		}
	}
	private static void oddRowsOddCols(String[] line, int rows, int cols) {
		start_row = rows/2 + 1; start_col = cols/2 + 1;
		matrix1A = new int[rows/2 + 1][cols/2 + 1]; matrix2A = new int[rows/2 + 1][cols/2 + 1];
		matrix1B = new int[rows/2 + 1][cols/2]; matrix2B = new int[rows/2 + 1][cols/2];
		matrix1C = new int[rows/2][cols/2+ 1]; matrix2C = new int[rows/2][cols/2+ 1];
		matrix1D = new int[rows/2][cols/2]; matrix2D = new int[rows/2][cols/2];
		//load upper A, B matrices
		for (int i = 0; i < rows/2 + 1; i++) {
			line = input.get(i+1).split(" ");
			String [] line2 = input.get(i+1+rows).split(" ");
			matrix1A[i][0] =  stringToInteger(line[0]);
			matrix2A[i][0] =  stringToInteger(line2[0]);
			for(int j =0; j < line.length/2; j++) {
				matrix1A[i][j+1] =  stringToInteger(line[j+1]);
				matrix2A[i][j+1] =  stringToInteger(line2[j+1]);
				matrix1B[i][j] =  stringToInteger(line[j + start_col]);
				matrix2B[i][j] =  stringToInteger(line2[j + start_col]);
			}			
		}
		//load C, D matrices
		for (int i = 0; i < rows/2; i++) {
			String [] line3 = input.get(i+1+start_row).split(" ");
			String [] line4 = input.get(i+1+rows + start_row).split(" ");
			matrix1C[i][0] =  stringToInteger(line3[0]);
			matrix2C[i][0] =  stringToInteger(line4[0]);				
			for(int j =0; j < line.length/2; j++) {
				matrix1C[i][j+1] =  stringToInteger(line3[j+1]);
				matrix2C[i][j+1] =  stringToInteger(line4[j+1]);
				matrix1D[i][j] =  stringToInteger(line3[j + start_col]);
				matrix2D[i][j] =  stringToInteger(line4[j + start_col]);
			}
		}
	}
	
	private static int stringToInteger (String numeric_string) {
		try {
			int int_output = Integer.parseInt(numeric_string);
			return int_output;
		}
		catch (Exception e) {
			System.out.println("There was an issue converting the Sting to an Integer");
			return 0;
		}
	}
	/*private static void addMatrices(int[][]matrix1, int [][] matrix2) {
			int[][] matrix4 = new int[rows][cols];
			for(int i = 0; i < rows; i++) {
				for(int j = 0; j < cols; j++) {
					matrix4[i][j] = matrix1[i][j] + matrix2[i][j]; 
				}
			}
			System.out.printf("\n\n");
			for(int i = 0; i < rows; i++) {
				System.out.printf("\n");
				for(int j = 0; j < cols; j++) {
					System.out.printf("%3d", matrix4[i][j]); 
			}
		}
	}*/
	public static void setMatrix(int[][] value, int row_idx, int col_idx) {
		for(int i = 0; i < value.length; i++) {
			for(int j = 0; j < value[0].length; j++) {
				matrix3[row_idx+i][col_idx+j] = value[i][j];
			}
		}
//		System.out.println("calling print matrix Overloaded");
//		printMatrix(matrix3);
	}
	private static void printMatrix() {
		for(int i = 0; i < rows; i++) {
			System.out.printf("\n");
			for(int j = 0; j < cols; j++) {
				 System.out.printf("%3d", matrix3[i][j]);
			}
		}
	}
	public static void printMatrix(int[][] matrix) { //Overloaded method for checking intermediate array states
		for(int i = 0; i < matrix.length; i++) {
			System.out.printf("\n");
			for(int j = 0; j < matrix[0].length; j++) {
				 System.out.printf("%3d", matrix[i][j]);
			}
		}
		System.out.printf("\n");
	}
	private static void verifyThreadComplete(ExecutorService executorService, ThreadOperation MatrixA, ThreadOperation MatrixB, ThreadOperation MatrixC, ThreadOperation MatrixD) {
		try {
			boolean tasksEnded = executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
			if (tasksEnded) {
				setMatrix(MatrixA.getResult(), MatrixA.getFirstRow(), MatrixA.getFirstCol());  
				setMatrix(MatrixB.getResult(), MatrixB.getFirstRow(), MatrixB.getFirstCol());
				setMatrix(MatrixC.getResult(), MatrixC.getFirstRow(), MatrixC.getFirstCol());
				setMatrix(MatrixD.getResult(), MatrixD.getFirstRow(), MatrixD.getFirstCol());
				System.out.println("calling print matrix no args");
				printMatrix();
			}
			else {
				System.out.println("Timed out while waiting for tasks to finish");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			}
		}
	/*private static void loadSingleMatrix() {
		matrix1 = new int[rows][cols]; matrix2 = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			String[] line = input.get(i+1).split(" ");
			String [] line2 = input.get(i+1+rows).split(" ");
			for(int j =0; j < line.length; j++) {
				matrix1[i][j] =  stringToInteger(line[j]);
				matrix2[i][j] =  stringToInteger(line2[j]);
				}
			}			
	//		printMatrix(matrix1);
	//		printMatrix(matrix2);
	}*/
}
