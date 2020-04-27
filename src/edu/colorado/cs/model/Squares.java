package edu.colorado.cs.model;

/*
 * Class that maintains state of each cell on the grid.
 */
public class Squares {
	private int adjacentMines;
	private boolean hasFlagSet;
	private boolean isOpened;

	public Squares(int adjacentMines) {
		this.adjacentMines = adjacentMines;
		hasFlagSet = false;
		isOpened = false;
	}

	public int getAdjacentMines() {
		return this.adjacentMines;
	}

	public boolean toggleFlag() {
		hasFlagSet = !hasFlagSet;
		return hasFlagSet;
	}

	public boolean isFlagged() {
		return hasFlagSet;
	}

	public void setOpen() {
		isOpened = true;
	}

	public boolean isOpened() {
		return this.isOpened;
	}
}