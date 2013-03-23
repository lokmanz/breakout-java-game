/*
 * File: Breakout.java 
 * -------------------
 * Name:Ahmad Lokmanz (lokmanz913@gmail.com)
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 700;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 90;

/** Number of turns */
	private static final int NTURNS = 3;
	
	private static final int DELAY = 18;

	private static final int NUM_OF_CIRCLE = 5;
	
/* Method: run() */
/** Runs the Breakout program. */
//	
	public void run() {
		setSize(APPLICATION_WIDTH,APPLICATION_HEIGHT);
		
		backGrooundAnimate();
		GLabel intro = new GLabel("__BREAKOUT__");
		intro.setFont("Times New Romen-50-BOLD");
		intro.setLocation((WIDTH - intro.getWidth())/2, (HEIGHT - intro.getAscent())/2);
		add(intro);
		
		GLabel intro2 = new GLabel(">> Click to START <<");
		intro2.setFont("Senserif-13-BOLD");
		intro2.setLocation((WIDTH - intro2.getWidth())/2, (HEIGHT - intro.getAscent())/2+intro.getAscent());
		add(intro2);
		
		waitForClick();
		removeAll();
		
		setup();
		
		
		waitForClick();
		remove(startMsg);//.setLabel("");
		//x1 = getX();
		//y1 = getY();
		
		startGame();
		addMouseListeners();
		playGame();
		
		/* You fill this in, along with any subsidiary methods */
	}
	
	
	private void backGrooundAnimate(){
		int counter = 0;
		
		while(counter < NUM_OF_CIRCLE){
			int diam = rgen.nextInt(90)*2;
			createCircle(diam);
			counter++;
		}
		
	}
	
	private void createCircle(int diam){
		int x = rgen.nextInt(0,getWidth());
		int y = rgen.nextInt(0,getHeight());
		int r = 1, rad = diam / 2;
		while(r < rad){
			int xFactor, yFactor;
			xFactor = yFactor = 1;
			double newx = (x - (r / 2.0)) - xFactor;
			double newy = (y - (r / 2.0)) - yFactor;
			GOval circle = new GOval(newx, newy, r, r);
			circle.scale(xFactor);
			circle.setColor(rgen.nextColor());
			circle.setFilled(true);
			add(circle);
			r++;
			xFactor++;
			yFactor++;
			pause(DELAY);
		}
		
	}

	
	/**
	 * Create the bricks at the specified location on canvas
	 */
	private void setup(){ 
		int x = 0;
		int y = BRICK_Y_OFFSET;
		
		//create the bricks
		for (int j = 0; j< NBRICK_ROWS; j++){
			for ( int i = 0; i < NBRICKS_PER_ROW+9;i++){
				brick = new GRect (x,y,BRICK_WIDTH,BRICK_HEIGHT);
				assignColor(j);
				add (brick);			
				x+=BRICK_WIDTH + BRICK_SEP;
			}
			x=0;
			y+=BRICK_HEIGHT + BRICK_SEP;
		}
		
		startMsg = new GLabel(">> Click to START <<");
		startMsg.setFont("Senserif-33-BOLD");
		startMsg.setLocation((WIDTH - startMsg.getWidth())/2, (HEIGHT - startMsg.getAscent())/2);
		add(startMsg);
		
		
		paddle = new GRect((WIDTH - PADDLE_WIDTH)/2,getHeight() - PADDLE_Y_OFFSET,PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setColor(Color.BLACK);
		paddle.setFilled(true);
		add(paddle);
	
		
	}
	
	/**
	 * Assign the color of brick s in each row
	 * @param row The number of row for the brick
	 * @param brick The briick obj that will be colored
	 */
	private void assignColor(int row){		
			switch(row){
				case 0 : 
				case 1 : brick.setColor(Color.RED);
						 break;
				case 2 : 
				case 3 : brick.setColor(Color.ORANGE);
						 break;
				case 4 :
				case 5 : brick.setColor(Color.YELLOW);
					     break;
				case 6 :
				case 7 : brick.setColor(Color.GREEN);
						 break;
				case 8 : 
				case 9 : brick.setColor(Color.CYAN);
				    	 break;
			}
			brick.setFilled(true);
		}
	
	/**
	 * Start up the breakout game by creating the ball at the center of screen and initializing the ball velocity
	 */
	private void startGame(){
		ball= new GOval((WIDTH - (2 * BALL_RADIUS)) / 2, (getHeight() - (2 * BALL_RADIUS)) / 2,2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setColor(Color.MAGENTA);
		ball.setFilled(true);
		add(ball);
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy=2.9;
		
	}
	
	/**
	 * The game loop up
	 */
	private void playGame(){
		//int nTurn = 0;
		while(true){
			moveBall(vx);
			checkForCollisition();
			//if collition occurr
			pause(DELAY);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see acm.program.Program#mouseMoved(java.awt.event.MouseEvent)
	 * 
	 * Handle the movement of the paddle, paddle move according to the mouse move event only 
	 */
	public void mouseMoved(MouseEvent e){
		x2 = e.getX();
		y2 = e.getY();
	
		//if mouse move to the right of screen
			if(x2 + (PADDLE_WIDTH / 2) < WIDTH){//getWidth()){
				paddle.setLocation((double)x2-30, getHeight() - PADDLE_Y_OFFSET);
			}else if(x2 - (PADDLE_WIDTH / 2) > 0){
				paddle.setLocation((double)x2-30, getHeight() - PADDLE_Y_OFFSET);
			}
		//if mouse move to the left of screen
			if(x2 + (PADDLE_WIDTH / 2) >WIDTH || x2 + (PADDLE_WIDTH / 2) == WIDTH){
				paddle.setLocation((double)WIDTH-PADDLE_WIDTH, getHeight() - PADDLE_Y_OFFSET);
			}
			else if(x2 - (PADDLE_WIDTH / 2) < 0 || x2 - (PADDLE_WIDTH / 2) == 0){
				paddle.setLocation(0.0, getHeight() - PADDLE_Y_OFFSET);
			}
	}
	
	/**
	 * Move the ball with respect of the value of the velocity
	 */
	private void moveBall(double vx){
		ball.move(vx,vy);
		add(ball);
	}
	
	/**
	 * Handle the collision of the ball during the gameplay
	 * Possible cases :
	 * 		1 - The ball collide with the right or left of the game application border 
	 * 		2 - The ball goes below the paddle (END OF A TURN)
	 * 		3 - The ball collide with the top of the game application border
	 * 		4 - The ball collide with an object (paddle or brick)
	 */
	private void checkForCollisition(){
		double x,y;
		GObject collider;
		AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
		x = ball.getX();
		y = ball.getY();
		
		//The ball collide with the right or left of the game application border
		if((x + (BALL_RADIUS * 2) >= WIDTH) || (x <= 0.0)){
			vx = -1*vx;
			bounceClip.play();
		}
		
		//The ball goes below the paddle (END OF A TURN)
		else if((y + (BALL_RADIUS * 2) >= HEIGHT - (PADDLE_Y_OFFSET+BRICK_HEIGHT))){
			remove(ball);
			if(nTurn<NTURNS-1){
				nTurn++;
				showGameStatus(nTurn);
				waitForClick();
	/*			GRect r=new GRect(99,99,88,88);
				add(r);
				waitForClick();*/
				beginNewTurn();
			}else{
				endGame();
			}
		}
		
		//The ball collide with the top of the game application border
		else if (y <= 0.0){	
			vy = -1*vy;
			bounceClip.play();
		}
		
		//The ball collide with an object (paddle or brick)
		collider = getCollidingObject(x,y);
		if(collider!=null){
			if(collider == paddle){
				bounceClip.play();
				vy = -1*vy;
			}else {
				remove(collider);
				totalBrick-=1;
				vy = -1*vy;
				bounceClip.play();
				if(totalBrick==0){
					endGame();
				}
			}
		}
	}
	
	/**
	 * Return the GObject that collide with the ball
	 * @param x The x-coordinate of the top left corner of the ball
	 * @param y The y-coordinate of the top left corner of the ball 
	 */
	private GObject getCollidingObject(double x, double y){
		GObject cObject=null,r=null;
		
		int i=0;
		while(i<4){
			switch(i){
				case 0 : cObject = getElementAt(x,y);
						 if(cObject!=null){
							 return cObject;
							}
						 break;
				case 1 : cObject = getElementAt(x+2*BALL_RADIUS,y);
						 if(cObject!=null){
						 return cObject;
							 }
						 break;
				case 2 : cObject = getElementAt(x+2*BALL_RADIUS,y+2*BALL_RADIUS);
						 if(cObject!=null){
						 return cObject;
						 }
						 break;
				case 3 : cObject = getElementAt(x,y+2*BALL_RADIUS);
						 if(cObject!=null){
							 return cObject;
						 }
						 break;
			}
			i++;
		}
		return r;
	}
	
	/**
	 * Show the game status after a turn  had ended
	 * @param n The n-th of the turn ended
	 */
	private void showGameStatus(int n){
		contMsg = new GLabel("-Click to BEGIN next turn-");
		contMsg.setFont("Senserif-20-BOLD");
		contMsg.setLocation((WIDTH-contMsg.getWidth())/2, (HEIGHT-contMsg.getAscent())/2);
		add(contMsg);
		
//		contMsg2 = new GLabel("C");
		switch(n){
			case 1 : contMsg2 = new GLabel("- Turn Left :: 2 -");
					 break;
			case 2 : contMsg2 = new GLabel("- Turn Left :: 1 -");
					 break;
		}
		contMsg2.setFont("Senserif-20-BOLD");
		contMsg2.setLocation((WIDTH-contMsg2.getWidth())/2, ((HEIGHT-contMsg.getAscent())/2)+20);
		add(contMsg2);
	}

	/**
	 * Start up another turn
	 */
	private void beginNewTurn(){
		remove(contMsg);
		remove(contMsg2);
		startGame();
		playGame();
	}
	
	/**
	 * End up the game and show a msg according the value of total brick
	 */
	private void endGame(){
		GLabel endMsg, endMsg2;
		if(totalBrick==0){
			removeAll();
			 backGrooundAnimate();
			 endMsg = new GLabel("- #YOU OWSOME# -");
			 endMsg.setFont("Senserif-40-BOLD");
			 endMsg.setLocation((WIDTH-endMsg.getWidth())/2, (getHeight()-endMsg.getAscent())/2);
			 add(endMsg);
		}else{
			endMsg = new GLabel("- #YOU SUCK# -");
	 		 endMsg.setFont("Senserif-40-BOLD");
	 		 endMsg.setLocation((WIDTH-endMsg.getWidth())/2, (getHeight()-endMsg.getAscent())/2);
	 		 add(endMsg);
		}

		 endMsg2 = new GLabel("click to exit game...");
		 endMsg2.setFont("Senserif-15-BOLD");
		 endMsg2.setLocation((WIDTH-endMsg2.getWidth()), HEIGHT-endMsg.getAscent());
		 add(endMsg2);
		 waitForClick();
		 System.exit(1);

	}
		/**Private instance variables*/
		private GLabel startMsg, contMsg,contMsg2;
		private GRect brick;
		private GRect paddle;
		private GOval ball;
		private int x1,x2,y1,y2;
		private int totalBrick =NBRICK_ROWS * NBRICKS_PER_ROW;
		private double vx,vy;//represent the change in position that occurs on each time step
		private RandomGenerator rgen = RandomGenerator.getInstance();
		public static int nTurn=0;
	}

