package edu.colorado.cs.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import edu.colorado.cs.LeftClickMemo;
import edu.colorado.cs.Memo;
import edu.colorado.cs.NewGameMemo;
import edu.colorado.cs.RightClickMemo;
import edu.colorado.cs.model.Squares;
import edu.colorado.cs.model.Model;
import edu.colorado.cs.view.View;

/*
 * MVC Pattern: Controller
 */

public class Controller {
	BlockingQueue<Memo> queue;
	Model model;
	View view;
	MineInfo mineInfo;
	private List<MessageHandler> handlers = new LinkedList<MessageHandler>();

	public static final int ERROR = -1;
	public static final int END = 0;
	public static final int DONE = 1;

	// Singleton Pattern
	private static Controller controller;

	public static Controller getInstance(View view, Model model, BlockingQueue<Memo> queue) {
		if (controller == null) {
			controller = new Controller(view, model, queue);
		}
		return controller;
	}

	// Singleton Pattern
	private Controller(View view, Model model, BlockingQueue<Memo> queue) {
		this.model = model;
		this.mineInfo = new MineInfo(model);
		this.view = view;
		this.queue = queue;
		this.handlers.add(new NewGameHandler());
		this.handlers.add(new LeftClickHandler());
		this.handlers.add(new RightClickHandler());

	}

	/*
	 * Function reads messages sent by View until end of game.
	 */
	public void mainLoop() throws Exception {
		int response = DONE;
		Memo message = null;
		while (response != END) {
			try {
				message = (Memo) queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			for (MessageHandler handler : handlers) {
				response = handler.run(message);
				if (response != ERROR)
					break;
			}
		}

	}

	/*
	 * Update app data as and when the game proceeds.
	 */
	public void updateGameInfo() {
		Squares[][] currentSquares = model.getMineField().getCells();
		int h = model.getHeight();
		int w = model.getWidth();
		if (model.getGameStatus()) {
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					if (!currentSquares[i][j].isOpened()) {
						if (!(currentSquares[i][j]).isFlagged()) {
							mineInfo.infoUpdate(i, j, mineInfo.getclose());
						} else {
							mineInfo.infoUpdate(i, j, mineInfo.getflag());
						}
					} else {
						int currCell = (currentSquares[i][j]).getAdjacentMines();
						mineInfo.infoUpdate(i, j, currCell);
					}
				}
			}
		} else {
			showAllMines(mineInfo);
		}
		mineInfo.setNumOfMines(model.getNumOfMinesFromCounter());
	}

	/*
	 * Show all mines when game is over.
	 */
	private void showAllMines(MineInfo mineInfo) {
		Squares[][] currentSquares = model.getMineField().getCells();
		int h = model.getHeight();
		int w = model.getWidth();
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (currentSquares[i][j].getAdjacentMines() == -1) {
					if (currentSquares[i][j].isFlagged()) {
						mineInfo.infoUpdate(i, j, mineInfo.getwrongFlag());
					} else {
						mineInfo.infoUpdate(i, j, currentSquares[i][j].getAdjacentMines());
					}
				}

			}
		}
	}

	/*
	 * Functions implements tasks to be completed on left click message received.
	 * COMMAND PATTERN : Concrete Function
	 */
	private class LeftClickHandler implements MessageHandler {
		@Override
		public int run(Memo message) {
			if (message.getClass() != LeftClickMemo.class) {
				return ERROR;
			}
			LeftClickMemo leftClick = (LeftClickMemo) message;
			model.openThisSquare(leftClick.getHeight(), leftClick.getWidth());
			updateGameInfo();
			if (model.getGameStatus()) {
				if (!model.isWin()) {
					view.changeView(mineInfo);
				} else {
					mineInfo.setWin();
					view.changeView(mineInfo);
				}

			} else {
				view.changeView(mineInfo);

			}

			return DONE;
		}
	}

	/*
	 * Functions implements tasks to be completed on right click message received.
	 * COMMAND PATTERN: Concrete Function
	 */
	private class RightClickHandler implements MessageHandler {
		@Override
		public int run(Memo message) {
			if (message.getClass() != RightClickMemo.class) {
				return ERROR;
			}
			RightClickMemo rightClick = (RightClickMemo) message;
			model.toggleSquare(rightClick.getHeight(), rightClick.getWidth());
			updateGameInfo();
			view.changeView(mineInfo);
			return DONE;
		}
	}

	/*
	 * Functions implements tasks to be completed on new game message received.
	 * COMMAND PATTERN: Concrete Function
	 */
	private class NewGameHandler implements MessageHandler {

		@Override
		public int run(Memo message) {
			if (message.getClass() != NewGameMemo.class) {
				return ERROR;
			}
			queue.clear();
			model = new Model();
			mineInfo = new MineInfo(model);
			view.changeView(mineInfo);
			return DONE;

		}

	}

}
