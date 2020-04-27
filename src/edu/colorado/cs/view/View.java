package edu.colorado.cs.view;

import javax.swing.*;
import javax.swing.Timer;
import java.text.SimpleDateFormat;

import java.awt.*;
import java.awt.event.*;

import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.ArrayList;

import edu.colorado.cs.LeftClickMemo;
import edu.colorado.cs.Memo;
import edu.colorado.cs.NewGameMemo;
import edu.colorado.cs.RightClickMemo;
import edu.colorado.cs.controller.MineInfo;
import edu.colorado.cs.model.Model;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.lang.Math;


/*
 * MVC Pattern: View
 */
public class View extends JFrame {
	int numCol;
	int numRow;
	
	JPanel topPanel;
	JLabel mineCounter;
	Timer gameTimer;
	long initTime;
	
	JPanel centerPanel;
	List<JButton> CellList;
	
	JPanel bottomPanel;
	JLabel statusBar;
	BlockingQueue<Memo> queue;
	Model model;
	Connection mycon;
	static JFrame f;
	
	Icon redMine;
    	Icon mine;
    	Icon flag;
    	Icon tile;
    
   	 Color DARK_BROWN = new Color(153,102,0);
   	 Color DARK_GREEN = new Color(0,102,0);

   	 private int FRAME_LOC_X = 430;
   	 private int FRAME_LOC_Y = 50;

	/*
	 * Initial view of game.
	 */
	public View(BlockingQueue<Memo> queue, Connection mycon) {
		this.queue = queue;
		int numberOfMines = 24;
		this.numCol = 12;
		this.numRow = 12;
		this.mycon = mycon;
		
		this.setTitle("Minesweeper"); 	
		this.setLocation(FRAME_LOC_X, FRAME_LOC_Y);
		
		
		
		
		/***************TopPanel*****************/
		topPanel = new JPanel();		
		topPanel.setBackground(DARK_GREEN);	
		
		mineCounter = new JLabel("" +numberOfMines+"  ");
		mineCounter.setForeground(Color.WHITE);
		mineCounter.setFont(new Font("Serif", Font.BOLD, 28));
		
		JLabel flag = new JLabel("");
        	flag.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/resources/flag.png")), 26,26));
		
		JButton newGameButton = new JButton();
		newGameButton.setText("  New Game  ");
		newGameButton.addActionListener(new NgameListener());
		
		JLabel timer = new JLabel(" 00:00");
		timer.setForeground(Color.WHITE);
		timer.setFont(new Font("Serif", Font.BOLD, 28));
		
		JLabel clock = new JLabel("");
        	clock.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/resources/clock.png")), 28,28));
		initTime = -1;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gameTimer = new Timer(1000, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (initTime < 0)
							initTime = System.currentTimeMillis();
						long now = System.currentTimeMillis();
						long timeElapsed = now - initTime;
						timer.setText(new SimpleDateFormat(" mm:ss").format(timeElapsed));
					} 
				}); 
				gameTimer.start(); 
			} 
		}); 
		
		topPanel.add(flag);
		topPanel.add(mineCounter);
		topPanel.add(newGameButton);
		topPanel.add(timer);
		topPanel.add(clock);
		
		
		

		/**********************CenterPanel********************/
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(numRow, numCol,0,0));
		
		centerPanel.setBackground(DARK_BROWN);
		
		CellList = new ArrayList<>();

		for (int i = 0; i < numRow * numCol; i++) {
			JButton cell = new JButton();

			cell.setName(i + ", ");
			cell.setContentAreaFilled(true);
			cell.setBackground(DARK_BROWN);
			cell.setForeground(Color.BLACK);
			cell.setOpaque(true);
			cell.setPreferredSize(new Dimension(39, 39));
			cell.addMouseListener(new FieldListener());
			cell.addActionListener(new FieldActionListener());
			//cell.setBorder(null);
			centerPanel.add(cell);
			CellList.add(cell);
		}

		/***********************BottomPanel*****************/
		bottomPanel = new JPanel();
		statusBar = new JLabel("");
		bottomPanel.add(statusBar);	
		
		
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(topPanel, BorderLayout.NORTH);		
		this.add(bottomPanel, BorderLayout.SOUTH);
		
			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	/*
	 * Resize Icon Helper
	 */
	private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
	    Image img = icon.getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
	    return new ImageIcon(resizedImage);
	}
	
	/*
	 * Display Stats from DB
	 */	
	public void displayStats(long yourTime, String message) {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			f = new JFrame("frame"); 
			JDialog d = new JDialog(f, Dialog.ModalityType.DOCUMENT_MODAL); 			
			statement = mycon.createStatement();
			
			//Get Total Games
			resultSet = statement.executeQuery("SELECT COUNT(*) FROM Score");
			resultSet.next();
			JLabel totalGames = new JLabel("  Total Games: "+ resultSet.getInt("count(*)"));
			
			//Get Games Won
			resultSet =  statement.executeQuery("SELECT COUNT(*) FROM Score WHERE isWon = 1");
			resultSet.next();
			JLabel gamesWon = new JLabel("  Games Won: "+ resultSet.getInt("count(*)"));
			
			//JLabel winPercent = new JLabel("New Percentage: "+ (100*gamesWon)/totalGames);
			
			//Get Best Time
			resultSet = statement.executeQuery("SELECT MIN(Time) AS 'MinimumValue' FROM Score WHERE isWon = 1");
			resultSet.next();
			JLabel bestTime = new JLabel("  Best Time: "+ (float)resultSet.getInt("MinimumValue")/1000+ " sec");
			JLabel youTime = new JLabel("  Your Time: "+ (float) yourTime/1000+" sec");
			JLabel displaymessage = new JLabel(message, SwingConstants.CENTER);
			
			JPanel statistics = new JPanel();
			statistics.add(displaymessage);
			statistics.add(totalGames);
			statistics.add(gamesWon);
			statistics.add(bestTime);
			statistics.add(youTime);
			statistics.setLayout(new GridLayout(5,1));
			//statistics.setBackground(DARK_BROWN);
			
			d.add(statistics);
			d.setSize(200, 200); 
			d.setLocation(FRAME_LOC_X+100, FRAME_LOC_Y+100);
			d.setVisible(true); 
		}
        catch(SQLException sqlex) {
           sqlex.printStackTrace();
        }
	}
	
	/*
	 * Add stats to DB
	 */	
	public void addStatstoDB(long elapsed, int iswon) {
		PreparedStatement statement = null;
		
		Date today = new Date();
		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);
		String todayAsString = df.format(today);
		
		String template = "INSERT INTO Score (Date, Time, isWon) values (?,?,?)";
		try {
			statement = mycon.prepareStatement(template);
			statement.setString(1, todayAsString);
			statement.setLong(2, elapsed);
			statement.setInt(3, iswon);
			statement.executeUpdate();
		}
        catch(SQLException sqlex) {
            sqlex.printStackTrace();
        }
	}
	
	/*
	 * Update View every time a move is made.
	 */
	public void changeView(MineInfo gameInfo) {
		
		boolean isMineBlown = false;
		int cellNum = 0;

		for (JButton jb : CellList) {
			int row = (int) cellNum / numCol;
			int column = cellNum - (row * numCol);

			int adjacentMines = gameInfo.getGameStatus()[row][column];
			if (adjacentMines == MineInfo.MINE) 
			{
				jb.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/resources/mine.png")), 30,30));
				isMineBlown = true;
			} else if (adjacentMines == MineInfo.FLAGGED) 
			{
				jb.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/resources/flag.png")), 25,25));
				jb.setEnabled(true);
			} else if (adjacentMines > 0 && adjacentMines < 10)
			{
				jb.setText(Integer.toString(adjacentMines));
				jb.setEnabled(false);

			} else if (adjacentMines == MineInfo.EMPTY) 
			{
				jb.setText("0");
				jb.setEnabled(false);
			} else if (adjacentMines == MineInfo.WRONGFLAG) 
			{
				//jb.setText("X");
				jb.setIcon(resizeIcon(new ImageIcon(getClass().getResource("/resources/check.jpg")), 35,35));
				jb.setEnabled(true);
			} else if (adjacentMines == MineInfo.CLOSED && isMineBlown == false) 
			{
				jb.setEnabled(true);
				jb.setIcon(null);
				jb.setText("");
			}

			cellNum++;
		}
		mineCounter.setText("" + gameInfo.getNumOfMines()+"  ");
		if (gameInfo.isWin()) {
			statusBar.setText("You win!");
			long elapsed=System.currentTimeMillis()-initTime;			
			addStatstoDB(elapsed, 1);
			displayStats(elapsed, "Congratulations, you win!" );
			
			for (JButton jb : CellList) {
				jb.setEnabled(false);
			}
		}
		
		if (isMineBlown) {
			for (JButton jb : CellList) {
				jb.setEnabled(false);
			}
			statusBar.setText("Game over!");
			long elapsed=System.currentTimeMillis()-initTime;			
			addStatstoDB(elapsed, 0);
			displayStats(elapsed, "Game over!");
			gameTimer.stop();
		}
		
	} 
	
	/*
	 * Listener for new game button click
	 */
	private class NgameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						initTime = -1;
						statusBar.setText("");
						gameTimer.start();
					}
				}); 
				queue.put(new NewGameMemo());
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}

	}

	/*
	 * Listener for Left click on cell
	 */
	private class FieldActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String info = e.getSource().toString();
			info = info.replaceFirst("javax.swing.JButton\\[", "").split(",")[0];
			int cellNum = Integer.parseInt(info);

			int row = (int) cellNum / numCol;
			int column = cellNum - (row * numCol);

			try {
				queue.put(new LeftClickMemo(row, column));
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	/*
	 * Listener for Right click on cell
	 */
	private class FieldListener extends MouseAdapter {
		public void mousePressed(MouseEvent event) {

			String info = event.getSource().toString();
			info = info.replaceFirst("javax.swing.JButton\\[", "").split(",")[0];
			int cellNum = Integer.parseInt(info);

			int row = (int) cellNum / numCol;
			int column = cellNum - (row * numCol);

			if (SwingUtilities.isRightMouseButton(event)) {
				try {
					queue.put(new RightClickMemo(row, column));
				} catch (InterruptedException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}
