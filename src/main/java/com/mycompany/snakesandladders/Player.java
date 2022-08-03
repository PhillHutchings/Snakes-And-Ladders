/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snakesandladders;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;

/**
 *
 * @author hutch
 */
public class Player implements commons, Serializable{
    
    private static final long serialVersionUID = 1;
    private Board board;
    private int squareNumber;
    private int playerNumber;
    private int x;
    private int y;
    private Color color;
    private String name;


    public Player(Board board, int squareNumber, int playerNumber, Color color, String name) {
        
        this.board = board;
        this.squareNumber = squareNumber;        
        this.playerNumber = playerNumber;
        this.color = color;
        this.name = name;   
        this.squareNumber = squareNumber;
        
        setX(board.getSquares()[squareNumber].getX() + (SQUARE_WIDTH / 2) - (PLAYER_WIDTH / 2));
        setY(board.getSquares()[squareNumber].getY() + (SQUARE_HEIGHT / 2) - (PLAYER_HEIGHT / 2));
  
        
    }
    

    public int getX() {
        
        return x;
    }

    private void setX(int x) {
        
        this.x = x;
    }

    public int getY() {
        
        return y;
    }

    private void setY(int y) {
       
        this.y = y;
    }

    public Color getColor() {
        
        return color;
    }

    public void setColor(Color color) {
        
        this.color = color;
    }

    public String getName() {
        
        return name;
    }

    public void setName(String name) {
        
        this.name = name;
    }

    private Rectangle getBounds(){
        
        return new Rectangle(getX(), getY(),PLAYER_WIDTH, PLAYER_HEIGHT);
    }
    
    /**
     * finds precise square number currently on
     * @return 
     */
    
    private int findSquareNumber(){ 
          
        for(int i = 0 ; i < board.getSquares().length; i++){
            
            if(board.getSquares()[i].getBounds().contains(getBounds())){
                
                setSquareNumber(i);
                break;
            }  
        }     
        
        return squareNumber;
    } 
 
    /**
     * gets square number
     * @return 
     */
    
    public int getSquareNumber() {
        
        return squareNumber;
    }

    public void setSquareNumber(int squareNumber) {
        
        this.squareNumber = squareNumber;
    }
    
    
    
    /**
     * reset back to start
     */
    
    public void resetSquare(){
        
        setSquareNumber(0);
        setX(board.getSquares()[squareNumber].getX() + (SQUARE_WIDTH / 2) - (PLAYER_WIDTH / 2));
        setY(board.getSquares()[squareNumber].getY() + (SQUARE_HEIGHT / 2) - (PLAYER_HEIGHT / 2));
    }
    
    /**
     * moves player across board
     */

    public void move(){
        
        ExecutorService ex = Executors.newSingleThreadExecutor();
        
        Runnable task = () -> {
             
            Instant start;
            long time;
            int current = getSquareNumber();
            int toMove = board.getDice().getMove();  

            if(current + toMove < 100){
                
                for(int move = current ; move <= (current + toMove); move++){

                    do{         //move across to next square

                        if(board.getSquares()[move].getX() + (SQUARE_WIDTH / 2) - (PLAYER_WIDTH / 2) > getX()){

                            setX(getX() +1);

                        }else{

                            setX(getX() -1);
                        }

                        board.repaint();
                        start = Instant.now();

                        do{

                            Instant finish = Instant.now();
                            time = Duration.between(start, finish).toMillis();

                        }while(time < 7);

                    }while(getX() != board.getSquares()[move].getX() + (SQUARE_WIDTH / 2) - (PLAYER_WIDTH / 2));

                    do{        //move up to next square

                        if(getY() > board.getSquares()[move].getY() + (SQUARE_HEIGHT / 2) - (PLAYER_HEIGHT / 2)){

                            setY(getY() -1);
                        }


                        board.repaint();
                        start = Instant.now();

                        do{

                            Instant finish = Instant.now();
                            time = Duration.between(start, finish).toMillis();

                        }while(time < 7);                   

                    }while(getY() > board.getSquares()[move].getY() + (SQUARE_HEIGHT / 2) - (PLAYER_HEIGHT / 2));
                       
                    if(move != current){
                        
                        board.getSounds().playerMoveSound();
                    }
                }
            }
            
            findSquareNumber();
        
            board.nextPlayersTurn(); 
            checkSnakeOrLadder();
            
            ex.shutdown();
  
        };
        
        ex.submit(task);
    }
    
    /**
     * check if player lands on snake or ladder
     */
    
    private void checkSnakeOrLadder(){
        
        boolean snake = false;          
        boolean ladder = false;
       
        for(int s = 0; s < board.getBuiltSnakes().size(); s++){
          
            if(board.getBuiltSnakes().get(s).getHeadSquare() == getSquareNumber()){
                
                slideDownSnake(s);
                snake = true;
            }
        }
        
        for(int l = 0; l < board.getBuiltLadders().size(); l++){
            
            if(board.getBuiltLadders().get(l).getBottomSquare() == getSquareNumber()){
                
                climbUpLadder(l);
                ladder = true;
            }
        }
        
        //check if won
        
        if(board.getSquares()[getSquareNumber()].isFinish()){
            
            wonGame();
        }
        
        if(!snake && !ladder){      //to enable dice else leave to ladder and snake methods
            
            board.getDice().getRollBtn().setEnabled(true);
        }
    }
    
    /**
     * method for sliding down the snake
     * @param snake 
     */
    
    private void slideDownSnake(int snake){
               
        int headX = board.getSquares()[board.getBuiltSnakes().get(snake).getHeadSquare()].getX() + (SQUARE_WIDTH / 2) - (PLAYER_WIDTH / 2);
        int headY = board.getSquares()[board.getBuiltSnakes().get(snake).getHeadSquare()].getY() + (SQUARE_HEIGHT / 2) - (PLAYER_HEIGHT / 2); 
        
        int tailX = board.getSquares()[board.getBuiltSnakes().get(snake).getTailSquare()].getX() + (SQUARE_WIDTH / 2) - (PLAYER_WIDTH / 2);
        int tailY = board.getSquares()[board.getBuiltSnakes().get(snake).getTailSquare()].getY() + (SQUARE_HEIGHT / 2) - (PLAYER_HEIGHT / 2);

        SnakeLadderLine snl = new SnakeLadderLine();
        snl.setLine(headX, headY, tailX, tailY);
        
        LinkedHashMap<Double, Double> points = new LinkedHashMap<>();
        
    //get x, y points of line between head and tail of snake
    
        for(double ly = headY; ly < BOARD_HEIGHT; ly++){
            
            for(double lx = 0 ; lx < BOARD_WIDTH; lx++){
               
                if(snl.intersects(lx, ly, 2, 2)){
                  
                    points.put(lx, ly);
                }
            }
        }
      
        
        ExecutorService ex = Executors.newSingleThreadExecutor();
        
        Runnable task = () -> {
            
            board.getSounds().slideDownSnakeSound();
            
            Instant start;
            long time;

            
        //traverse the points on the line
        
            for(Double getX : points.keySet()){

                start = Instant.now();
                setX(getX.intValue());
                setY(points.get(getX).intValue());

                board.repaint();

                do{

                Instant finish = Instant.now();
                time = Duration.between(start, finish).toMillis();

                }while(time < 7);

            }

            board.getDice().getRollBtn().setEnabled(true);
            findSquareNumber();
            ex.shutdown();
        };
        
        ex.submit(task);
    }
    
        
    /**
     * method for climbing up ladder
     * @param snake 
     */
    
    private void climbUpLadder(int ladder){
       
        int topX = board.getSquares()[board.getBuiltLadders().get(ladder).getTopSquare()].getX() + (SQUARE_WIDTH / 2) - (PLAYER_WIDTH / 2);
        int topY = board.getSquares()[board.getBuiltLadders().get(ladder).getTopSquare()].getY() + (SQUARE_HEIGHT / 2) - (PLAYER_HEIGHT / 2); 
        
        int bottomX = board.getSquares()[board.getBuiltLadders().get(ladder).getBottomSquare()].getX() + (SQUARE_WIDTH / 2) - (PLAYER_WIDTH / 2);
        int bottomY = board.getSquares()[board.getBuiltLadders().get(ladder).getBottomSquare()].getY() + (SQUARE_HEIGHT / 2) - (PLAYER_HEIGHT / 2);
        
        SnakeLadderLine snl = new SnakeLadderLine();
        snl.setLine(bottomX, bottomY, topX, topY);
        
        LinkedHashMap<Double, Double> points = new LinkedHashMap<>();
        
    //get x y points of the line between top and bottom of ladder
    
        for(double ly = bottomY; ly > 0; ly--){
            
            for(double lx = 0 ; lx < BOARD_WIDTH; lx++){
               
                if(snl.intersects(lx, ly, 2, 2)){
                  
                    points.put(lx, ly);
                }
            }
        }
      
        
        ExecutorService ex = Executors.newSingleThreadExecutor();
        
        Runnable task = () -> {
            
            board.getSounds().ladderSound();
            
            Instant start;
            long time;
            
         //traverse the points on the line
         
            for(Double getX : points.keySet()){

                start = Instant.now();
                setX(getX.intValue());
                setY(points.get(getX).intValue());

                board.repaint();

                do{

                Instant finish = Instant.now();
                time = Duration.between(start, finish).toMillis();

                }while(time < 7);

            }
            
            board.getSounds().stopLadderSound();
            board.getDice().getRollBtn().setEnabled(true);
            findSquareNumber();
            ex.shutdown();
        };
        
        ex.submit(task);
    }
       
    /**
     * win game
     */
    
    private void wonGame(){
        
        board.getSounds().winSound();
        
        int choice = JOptionPane.showConfirmDialog(null, getName() + " Wins The Game \n Restart?", "WINNER", JOptionPane.OK_CANCEL_OPTION);
        
        if(choice == JOptionPane.OK_OPTION){
            
            board.resetGame();
            
        }else{
            
            System.exit(0); 
        }
    }

    /**
     * draw player
     * @param g 
     */
    
    public void draw(Graphics2D g){
          
        g.setStroke(new BasicStroke(1));
        
        g.setColor(Color.black);
        g.drawString(name, getX() - (name.length() / 2), getY() - 5);
        g.setColor(color);         
        g.fillRect(getX() + 3, getY() + 15, 15, 30);
        g.setColor(Color.black);                
        g.drawRect(getX() + 3, getY() + 15, 15, 30);
         
        g.setColor(color); 
        g.fillOval(getX(), getY(), 20, 20);
        g.setColor(Color.black);
        g.drawOval(getX(), getY(), 20, 20);
    
    }
    
    /**
     * to get the line from head of snake to tail or top of ladder to bottom
     */
    
    class SnakeLadderLine extends Line2D{

        public double x1;
        public double y1;
        public double x2;
        public double y2;
        
        @Override
        public double getX1() {
            
            return x1;
        }

        @Override
        public double getY1() {
           
            return y1;
        }

        @Override
        public Point2D getP1() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public double getX2() {
           
            return x2;
        }

        @Override
        public double getY2() {
            
            return y2;
        }

        @Override
        public Point2D getP2() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setLine(double x1, double y1, double x2, double y2) {
            this.x1 = (float) x1;
            this.y1 = (float) y1;
            this.x2 = (float) x2;
            this.y2 = (float) y2;
        }

        @Override
        public Rectangle2D getBounds2D() {
            double x, y, w, h;
            
            if (x1 < x2) {
                x = x1;
                w = x2 - x1;
            } else {
                x = x2;
                w = x1 - x2;
            }
            if (y1 < y2) {
                y = y1;
                h = y2 - y1;
            } else {
                y = y2;
                h = y1 - y2;
            }
            
            return new Rectangle2D.Double(x, y, w, h);
        }
        
    }
}
