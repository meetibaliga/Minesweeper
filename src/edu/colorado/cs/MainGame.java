package edu.colorado.cs;


import edu.colorado.cs.controller.Controller;
import edu.colorado.cs.model.Model;
import edu.colorado.cs.view.View;

import java.sql.Connection;
import java.sql.DriverManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Starting point for the app. Creates the singleton Controller needed by the app.
 */

public class MainGame {
        private static BlockingQueue<Memo> queue = new LinkedBlockingQueue<Memo>();
        static final String DB_URL = "jdbc:mysql://localhost:3306/Game";	
    	static final String USER = "root";
    	static final String PASS = "Meeti123..";

        public static void main(String[] args) {
                try {
        	        Connection mycon = DriverManager.getConnection(DB_URL, USER, PASS);
                        //Singleton Pattern
        	        Controller myGame = Controller.getInstance(new View(queue, mycon), new Model(), queue);
                        myGame.mainLoop();
                } catch (Exception e) {
                        e.printStackTrace();
                }
                queue.clear();
        }
}
