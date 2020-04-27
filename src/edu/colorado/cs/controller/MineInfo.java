package edu.colorado.cs.controller;

import edu.colorado.cs.model.Model;

/*
 * Keeps information about the mines and the cells.
 */
public class MineInfo {
	private int[][] gameStatus;
	public static final int CLOSED = -2;
	public static final int FLAGGED = 10;
	public static final int WRONGFLAG = -20;
	public static final int MINE = -1;
	public static final int EMPTY = 0;
	private int h;
	private int w;
	private int numOfMines;
	private boolean isWin;

	public MineInfo(Model model) {
		h = model.getHeight();
		w = model.getWidth();
		numOfMines = model.getNumOfMines();
		gameStatus = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				gameStatus[i][j] = CLOSED;
			}
		}
		isWin = false;

	}

	public void infoUpdate(int i, int j, int updateValue) {
		gameStatus[i][j] = updateValue;
	}

	public int getflag() {
		return FLAGGED;
	}

	public int getclose() {
		return CLOSED;
	}

	public int getwrongFlag() {
		return WRONGFLAG;
	}

	public int[][] getGameStatus() {
		return gameStatus;
	}

	public int getHeight() {
		return this.h;
	}

	public int getWidth() {
		return this.w;
	}

	public int getNumOfMines() {
		return this.numOfMines;
	}

	public void setNumOfMines(int mines) {
		this.numOfMines = mines;
	}

	public void setWin() {
		isWin = true;
	}

	public boolean isWin() {
		return isWin;
	}

}
