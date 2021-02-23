public class ThreadOperation implements Runnable{
	private int rows; private int cols; private int start_row; private int start_col;
	private int[][] matrix1; private int[][] matrix2;
	
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
		
		try {
			for(int i = 0; i < rows; i++) {
				for(int j = 0; j < cols; j++) {
					 MatrixThread.setMatrix(matrix1[i][j] + matrix2[i][j], i + start_row, j + start_col);
//					 System.out.printf("%s wrote %3d to matrix3 at position %d, %d\n",Thread.currentThread().getName(),matrix1[i][j] + matrix2[i][j], i + start_row, j + start_col);
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
}
