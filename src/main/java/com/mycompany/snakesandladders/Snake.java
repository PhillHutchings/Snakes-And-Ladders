/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snakesandladders;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author hutch
 */
public class Snake implements commons, Serializable{
    
    private static final long serialVersionUID = 1;
    private final Board board;
    private transient Image snake;
    private final int headSquare;
    private final int tailSquare;
    private final int snakeLength;
    private final int x;
    private final int y;
    private int centreX;
    private int centreY;
    private double angleX;
    private double angleY;
    private Random rand;
    private String serializeImage;
    private int snakeNum;

    public Snake(Board board, int headSquare, int tailSquare) {

        this.board = board;
        this.headSquare = --headSquare;     //need to decrease because it retreives next square because of array index
        this.tailSquare = --tailSquare;     //need to decrease because it retreives next square because of array index
        snakeLength = getSnakeLength();
        snake = getSnakeImage();
        x = getX();
        y = getY();
             
    }
    
    /**
     * gets length of snake
     * @return 
     */
    
    private int getSnakeLength(){ 

        int headX = board.getSquares()[headSquare].getX(); 
        int tailX = board.getSquares()[tailSquare].getX(); 
        
        angleX = tailX - headX; 
        
        int headY = board.getSquares()[headSquare].getY();  
        int tailY = board.getSquares()[tailSquare].getY();  
        
        angleY = tailY - headY;   
        
        double pointX = Math.abs(angleX);
        double pointY = Math.abs(angleY);
        
        return (int) Math.hypot(pointX, pointY);
        
    }
    
    /**
     * x pos of head square
     * @return 
     */
    
    private int getX(){
        
        return board.getSquares()[headSquare].getX() + (SQUARE_WIDTH / 2) - (SNAKE_WIDTH / 2);
    }
    
    /**
     * y pos of head square
     * @return 
     */
    
    private int getY(){
        
        return board.getSquares()[headSquare].getY()  + (SQUARE_HEIGHT / 2);
    }
    
    /**
     * centre x of rotation
     * @return 
     */
    
    private int centreX(){
        
        return (SNAKE_WIDTH / 2) + getX();
    }
      
    /**
     * centre y of rotation
     * @return 
     */
    
    private int centreY(){
        
        return getY() + (snakeLength / 2);
    }

    public int getHeadSquare() {
        
        return headSquare;
    }

    public int getTailSquare() {
        
        return tailSquare;
    }  
    
    /**
     * gets snake image
     * @return 
     */
    
    private Image getSnakeImage(){
        
        ImageIcon ii = null;
        
        rand = new Random();        
        snakeNum = rand.ints(1,4).findFirst().getAsInt();
        
        try {
            
            BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/images/board/snake" + snakeNum +".png"));
            ii = new ImageIcon(bi);
               
        } catch (IOException ex) {
            
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ii.getImage().getScaledInstance(SNAKE_WIDTH, snakeLength, Image.SCALE_SMOOTH);
    }

    /**
     * for loading saved board
     */
    
    public void setSnake() {
        
        this.snake = getSnakeImage();
    }

    
    /**
     * draw snake
     * @param g 
     */
    
    public void draw(Graphics2D g){
        
        AffineTransform af = new AffineTransform();
        AffineTransform old = g.getTransform();
        
        af.rotate(Math.atan2(angleY, angleX) - Math.toRadians(90), getX(), getY());
        g.setTransform(af);
        g.drawImage(snake, getX(), getY(), null);
        
        g.setTransform(old);
        
    }
    
    @Override
    public String toString(){
        
        return String.valueOf(snakeLength);
    }


}
