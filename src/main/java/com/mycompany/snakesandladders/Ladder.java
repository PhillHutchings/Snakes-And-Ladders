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
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author hutch
 */
public class Ladder implements commons, Serializable{
    
    private static final long serialVersionUID = 1;
    private final Board board;
    private transient Image ladder;
    private final int topSquare;
    private final int bottomSquare;
    private final int ladderLength;
    private final int x;
    private final int y;
    private double angleX;
    private double angleY;
    
    
    public Ladder(Board board, int topSquare, int bottomSquare) {

        this.board = board;
        this.topSquare = --topSquare;       //need to decrease because it retreives next square because of array index
        this.bottomSquare = --bottomSquare;     //need to decrease because it retreives next square because of array index
        ladderLength = getLadderLength();
        ladder = chooseLadderImage();
        x = getX();
        y = getY();

    }
       
    /**
     * gets length of ladder
     * @return 
     */
    
    private int getLadderLength(){ 

        int topX = board.getSquares()[topSquare].getX(); 
        int bottomX = board.getSquares()[bottomSquare].getX(); 
        
        angleX = bottomX - topX; 
        
        int topY = board.getSquares()[topSquare].getY();  
        int bottomY = board.getSquares()[bottomSquare].getY();  
        
        angleY = bottomY - topY;   
        
        double pointX = Math.abs(angleX);
        double pointY = Math.abs(angleY);
        
        return (int) Math.hypot(pointX, pointY);
        
    }
    
    /**
     * chooses which size ladder to use
     * @return 
     */
    
    private Image chooseLadderImage(){
        
       return ladderLength < 270 ? getLadderImageS() : ladderLength < 480 ? getLadderImageM() : getLadderImageL();
    }
    
    /**
     * x pos of top square
     * @return 
     */
    
    private int getX(){
        
        return board.getSquares()[topSquare].getX() + (SQUARE_WIDTH / 2) - (SNAKE_WIDTH / 2);
    }
    
    /**
     * y pos of top square
     * @return 
     */
    
    private int getY(){
        
        return board.getSquares()[topSquare].getY()  + (SQUARE_HEIGHT / 2);
    }

    public int getTopSquare() {
        
        return topSquare;
    }

    public int getBottomSquare() {
        
        return bottomSquare;
    }
      
    /**
     * gets ladder image small
     * @return 
     */
    
    private Image getLadderImageS(){
        
        ImageIcon ii = null;
        
        try {
            
            BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/images/board/ladderS.png"));
            ii = new ImageIcon(bi);
               
        } catch (IOException ex) {
            
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ii.getImage().getScaledInstance(LADDER_WIDTH, ladderLength, Image.SCALE_SMOOTH);
    }
          
    /**
     * gets ladder image medium
     * @return 
     */
    
    private Image getLadderImageM(){
        
        ImageIcon ii = null;
        
        try {
            
            BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/images/board/ladderM.png"));
            ii = new ImageIcon(bi);
               
        } catch (IOException ex) {
            
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ii.getImage().getScaledInstance(LADDER_WIDTH, ladderLength, Image.SCALE_SMOOTH);
    }
    
          
    /**
     * gets ladder image large
     * @return 
     */
    
    private Image getLadderImageL(){
        
        ImageIcon ii = null;
        
        try {
            
            BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/images/board/ladderL.png"));
            ii = new ImageIcon(bi);
               
        } catch (IOException ex) {
            
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ii.getImage().getScaledInstance(LADDER_WIDTH, ladderLength, Image.SCALE_SMOOTH);
    }

    /**
     * for loading saved board
     */
    
    public void setLadder() {
        
        this.ladder = chooseLadderImage();
    }
    
    
    /**
     * draw ladder
     * @param g 
     */
    
    public void draw(Graphics2D g){
        
            AffineTransform af = new AffineTransform();
            AffineTransform old = g.getTransform();

            af.rotate(Math.atan2(angleY, angleX) - Math.toRadians(90), getX(), getY());
            g.setTransform(af);
            g.drawImage(ladder, getX(), getY(), null);

            g.setTransform(old);
        
    }
}
