//This is the class TicTacToe
//The method play kicks off a rousing 2 person game of TicTacToe
// Course: CSCI-2251, Spring 2021
// Author: Philip Garcia
// philip.a.garcia@gmail.com
// 01/13/2021

import java.util.Scanner;
enum GameStatus{
	CONTINUE, 
	DRAW,
	WINNER,
}
  
enum BoardValues{
	EMPTY, 
	X,
	O,
}
public class TicTacToe {
	private BoardValues[][] board;
	private boolean valid_move; private boolean game_over;
	private GameStatus game_status;
	private int player_counter;
	private Scanner in;
	
	public TicTacToe() {//constructor 
		this.board = new BoardValues[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.board[i][j] = BoardValues.EMPTY;
			}
		}
		this.valid_move = false;
		this.game_over = false;
		this.game_status = GameStatus.CONTINUE;
		this.player_counter = 0;
		this.in = new Scanner(System.in);
	}

	public void play() {
//		Scanner in = new Scanner(System.in);
		//Prompts a player to move
		//Validates the move is within set bounds and Square is open
		//Checks to see if there is a winner
		do {//Loop until game is over
			do {//loop until player makes a valid move
				String move = this.requestMove(); //Prompt player to select a square
				this.valid_move = this.validMove(move); //Evaluate validity of move
			} while (!this.valid_move); 

			this.printBoard(); //Display the updated this.board
			this.game_status = this.gameStatus(); //Determine WIN, DRAW, CONTINUE
			//resets for next player
			this.player_counter ++;
			this.valid_move = false;
		} while (!this.game_over);		
		
		this.player_counter--; //added decrement to counter increment from line 50
		this.endGamePrint(); //print WIN or DRAW messages
		this.in.close();
	}
	  
	private void endGamePrint() {
		int player = 0;
		//Player Select
		if (this.player_counter%2 == 0) {player = 1;}
		else {player=2;}
		//print if a winner has been decided or there are no remaining vaild moves
		if (this.game_over) {
			if(this.game_status.equals(GameStatus.DRAW)) {
				System.out.println("No valid moves remain");
				System.out.printf("***The game is a DRAW***");
			}
			else {
				System.out.printf("***PLAYER %d IS THE WINNER!***", player);
			}
		}
	}
		//Otherwise the game continues
	private String requestMove() {
		String row = null; String col = null; int player = 0; String temp = null;
		//Player Select
		if (this.player_counter%2 == 0) {player = 1;}
		else {player=2;}
		
		temp = String.format("Player %d please select a row {1:1st Row, 2:2nd Row, 3:3rd Row}:", player);
		System.out.print(temp);
		row = in.nextLine();
		temp = String.format("Player %d Please select a Column {1:1st Col, 2:2nd Col, 3:3rd Col}:", player);
		System.out.print(temp);
		col = in.nextLine();
		temp = String.format("Player Number %d Choose [%s,%s]", player, row, col);
		System.out.println(temp);
		String move = row + "," + col;
		return move;
	}
	private boolean validMove(String moved) {
		int[] move =new int[2]; String[] moves; BoardValues player_symbol;
		//split moved string to row = moves[0] col=moves[1]
 		moves = moved.split(",",2);
		//verify input is of type int
		try {
			move[0] = Integer.parseInt(moves[0]);
			move[1] = Integer.parseInt(moves[1]);
			//adjust rows-columns from 1-3 to 0-2
			// More natural user experience
			move[0]--;
			move[1]--;
		}
		catch (Exception e){
			System.out.println("Rows and column inputs must be integers between 1-3");
			return false;
		}

		//Check to see if rows and columns are allowed
		if ((move[0] > 2) || (move[1] > 2) || (move[0] == -1) || (move[1] == -1)) {
			System.out.println("Rows and column numbers must be between 1-3");
			return false;
		} 
		//Check to see if space has previously been used
		if (this.board[move[0]][move[1]] == BoardValues.X || this.board[move[0]][move[1]] == BoardValues.O ){
			System.out.printf("Row:%d and Col:%d has been selected by %s. Please select again:", move[0],move[1],this.board[move[0]][move[1]]);
			return false;
		}
		//Add valid move to this.board
		//Initalize Symbol for Player
		if (this.player_counter %2 == 0) {
			player_symbol = BoardValues.X;
			}
		else {
			player_symbol = BoardValues.O;
			}
		//mark position on this.board
		this.board[move[0]][move[1]] = player_symbol;
		//Return for valid moves
		this.valid_move = true;
		return true;
	}
	
	private GameStatus gameStatus() {
		boolean open = false;
		//Add logic for win/draw
		//Check ROWS
		for (int j = 0; j<3; j++) {
				if (this.board[j][0] != this.board[j][1] || this.board[j][1] != this.board[j][2] || (this.board[j][0] != BoardValues.X  && this.board[j][0] != BoardValues.O)) {
					//String temp = String.format("ROW = %d, value = %c,%c,%c",j,this.board[j][0],this.board[j][1],this.board[j][2]);
				}
				else {
					this.game_over= true;
					return GameStatus.WINNER;
				}
			}
		//Check COLS
		for (int i = 0; i <3; i++) {
			if (this.board[0][i] != this.board[1][i] || this.board[1][i] != this.board[2][i] || (this.board[0][i] != BoardValues.X && this.board[0][i] != BoardValues.O)) {
				//String temp = String.format("COL = %d, value = %c,%c,%c",i,this.board[i][0],this.board[i][1],this.board[i][2]);
			}
			else {
				this.game_over = true;
				return GameStatus.WINNER;
				
			}
		}
		
		//Check DIAGS
		//left to right diag
		if(this.board[0][0] != this.board[1][1] || this.board[1][1] != this.board[2][2] || (this.board[0][0] != BoardValues.X && this.board[0][0] != BoardValues.O)) {
			//String temp = String.format("PRIMARY DIAG, value = %c,%c,%c",this.board[0][0],this.board[1][1],this.board[2][2]);
		}
		else {
			this.game_over = true;
			return GameStatus.WINNER;

		}
		//right to left diag
		if(this.board[0][2] != this.board[1][1] || this.board[1][1] != this.board[2][0] || (this.board[0][2] != BoardValues.X && this.board[0][2] != BoardValues.O)) {
			//String temp = String.format("ANTI DIAG, value = %c,%c,%c",this.board[0][2],this.board[1][1],this.board[2][0]);
		}
		else {
			this.game_over = true;
			return GameStatus.WINNER;
		}
		//check this.board for open squares
		for (int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if (this.board[i][j] != BoardValues.X && this.board[i][j] != BoardValues.O ) {
					//String temp = String.format("[%d,%d] is an open square", i,j);
					open = true;
					break;
				}
			}
		}
		if (!open && !this.game_over) {
			this.game_over = true;
			return GameStatus.DRAW;

			}
		return GameStatus.CONTINUE;
	}
		
	public void printBoard() {
			String output = new String();
			System.out.println(" _______________________ ");
			System.out.println("|       |       |       |");
			output = printBoardHelper(0);
			System.out.println(output);
			System.out.println("|_______|_______|_______|");
			System.out.println("|       |       |       |");
			output = printBoardHelper(1);
			System.out.println(output);
			System.out.println("|_______|_______|_______|");
			System.out.println("|       |       |       |");
			output = printBoardHelper(2);
			System.out.println(output);
			System.out.println("|_______|_______|_______|");
	}

	private String printBoardHelper(int row) {
		String enumDecode = new String();
		String col0; String col1; String col2;
		//convert "EMPTY" to " "
		if (this.board[row][0] == BoardValues.EMPTY) {
			col0 = " ";
		}
		else {col0 = this.board[row][0].toString();}
		if(this.board[row][1] == BoardValues.EMPTY) {
			col1 = " ";
		}
		else {col1 = this.board[row][1].toString();}
		if(this.board[row][2] == BoardValues.EMPTY) {
			col2 = " ";
		}
		else {col2 = this.board[row][2].toString();}
		//format row print string
		enumDecode = String.format("|   %s   |   %s   |   %s   |", col0,col1,col2);
		return enumDecode;
	}
}