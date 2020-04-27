package edu.colorado.cs.model;

/*
 * MVC Pattern: Model
 */
public class Model {

	private Field field;
	private Counter counter;
	private int height;
	private int width;
	private int numberOfMines;

	public boolean gameOngoing;
	public boolean lost;

	public Model() {
		
		this.height = 12;
		this.width = 12;
		this.numberOfMines = 24;

		this.field = new Field(height, width, numberOfMines);
		this.counter = new Counter(this.numberOfMines);
		gameOngoing = true;
		lost = false;
	}

	public void openThisSquare(int h, int w) {
		gameOngoing = this.field.open(h, w);
	}
	
	public boolean getGameStatus() {
		return gameOngoing;
	}

	public boolean isWin() {
		if (gameOngoing) {
			if (this.field.getNumOfTotalCells() - this.field.getNumOfOpenedCells() == this.field
					.getNumOfMines()) {
				return true;
			}
		}
		return false;
	}
	
	public void toggleSquare(int h, int w) {
		this.field.toggleFlag(h, w);
		if (this.field.squares[h][w].isFlagged()) {
			this.counter.decreaseMine(); 
		} else {
			this.counter.increaseMine();
		}

	}

	public void setLose() {
		if (!gameOngoing) {
			lost = true;
		}
	}

	public Field getMineField() {
		return field;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getNumOfMines() {
		return this.numberOfMines;
	}

	public int getNumOfMinesFromCounter() {
		return this.counter.getCurrentNumOfMine();
	}

}
