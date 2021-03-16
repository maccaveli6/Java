public class ThreadOperation implements Runnable{
	private int rows; private int cols; private int start_row; private int start_col;
	private int[][] matrix1; private int[][] matrix2; private int[][] output;
	
	public ThreadOperation(int[][] matrixA, int[][] matrixB, int first_row, int first_col){ //constructor
		this.rows = matrixA.length;
		this.cols = matrixA[0].length;
		this.matrix1 = matrixA;
		this.matrix2 = matrixB;
		this.start_row = first_row;
		this.start_col = first_col;
	}
	@Override
	public void run(){
		output = new int[rows][cols];
		try {
			for(int i = 0; i < rows; i++) {
				for(int j = 0; j < cols; j++) {
					 output[i][j] = matrix1[i][j] + matrix2[i][j]; 
					 }
			}
		}
		catch (Exception e){
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
	public int[][] getResult(){
		return output;
	}
	public int getFirstRow() {
		return start_row;
	}
	public int getFirstCol() {
		return start_col;
	}	
}
