package edu.colorado.cs.model;

/*
 * Class that maintains state of the entire field.
 * Contains application logic responsible to place the mines and calculate adjacent values.
 */
public class Field {
	
	Squares[][] squares;
	private int height;
	private int width;
	private int numberOfMines;
	private int numberOfCells;
	private boolean[] mineBoolean;
	private int numberOfOpenedCells;

	public Field(int height, int width, int mines) {
		this.height = height;
		this.width = width;
		numberOfCells = height * width;
		mineBoolean = new boolean[numberOfCells];
		this.numberOfMines = mines;
		squares = new Squares[height][width];
		this.calcAdjacentMines();
		this.numberOfOpenedCells = 0;
	}
	
	/*
	 * Update numbers to be displayed around a mine.
	 */
	public int calculateAdjacentMineNumbers(int x, int y) {
		int mineCounter = 0;
		for (int i = y - 1; i < y + 2; i++) {
			for (int j = x - 1; j < x + 2; j++) {
				
				if (x == -1 || y == -1 || x == squares[0].length || y == squares.length) {
					continue;
				}
				
				if (hasMine(i, j)) {
					mineCounter++;
				}
			}
		}
		return mineCounter;
	}
	
	/*
	 * Checks to see if a square(h,w) has a mine
	 */
	private boolean hasMine(int h, int w) {
		BoundaryChecker object = new BoundaryCheckerProxy();
		if (!object.boundaryProxy(h, w))
			return false;
		
		int i = h * width + w;
		if (mineBoolean[i])
			return true;
		else
			return false;
	}
	
	/*
	 * Place mines randomly on the field
	 */
	public void generateMineField()
	{
	
		for (int i = 0; i < numberOfCells; i++) {
			if (i < numberOfMines)
				mineBoolean[i] = true;
			else
				mineBoolean[i] = false;
		}
		for (int i = 0; i < numberOfCells; i++) {
			int index = new java.util.Random().nextInt(numberOfCells - i);
			boolean temp = mineBoolean[i];
			mineBoolean[i] = mineBoolean[index];
			mineBoolean[index] = temp;
		}
	}
	
	/*
	 * Calculate mines around each cell
	 */
	public void calcAdjacentMines() {
		generateMineField();
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				int adjacentMines = -1;
				int i = h * width + w;
				if (!mineBoolean[i]) {
					adjacentMines = calculateAdjacentMineNumbers(w, h);
				}
				squares[h][w] = new Squares(adjacentMines);
			}
		}
	}
	
	public Squares[][] getCells() {
		return squares;
	}

	public void toggleFlag(int h, int w) 
	{
		Squares cell = squares[h][w];
		if (cell.isOpened()) {
			return;
		}
		cell.toggleFlag();
	}

	public boolean open(int h, int w) {
		Squares cell = squares[h][w];
		if (cell.isFlagged() || cell.isOpened()) {
			return true;
		}
		// closed cell
		int adjacentMines = cell.getAdjacentMines();
		if (adjacentMines == -1) {
			cell.setOpen();
			return false;
		}
		if (adjacentMines > 0) {
			cell.setOpen();
			numberOfOpenedCells++;
			return true;
		} else { // empty cell
			numberOfOpenedCells += openZeroArea(h, w);
			return true;
		}
	}

	private int openZeroArea(int h, int w) {
		BoundaryChecker object = new BoundaryCheckerProxy();
		if (!object.boundaryProxy(h, w))
			return 0;

		Squares cell = squares[h][w];

		if (cell.isFlagged() || cell.isOpened())
			return 0;
		cell.setOpen();
		int opened_counter = 1;

		int adjacentMines = cell.getAdjacentMines();
		if (adjacentMines > 0) {
			return opened_counter;
		}
		opened_counter += openZeroArea(h - 1, w - 1); 
		opened_counter += openZeroArea(h - 1, w + 0);
		opened_counter += openZeroArea(h - 1, w + 1); 
		opened_counter += openZeroArea(h + 0, w - 1); 
		opened_counter += openZeroArea(h + 0, w + 1); 
		opened_counter += openZeroArea(h + 1, w - 1); 
		opened_counter += openZeroArea(h + 1, w + 0);
		opened_counter += openZeroArea(h + 1, w + 1);
		return opened_counter;

	}

	public int getNumOfOpenedCells() {
		return numberOfOpenedCells;
	}

	public int getNumOfTotalCells() {
		return numberOfCells;
	}

	public int getNumOfMines() {
		return numberOfMines;
	}


}
