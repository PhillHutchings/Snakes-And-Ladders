/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snakesandladders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 *
 * @author hutch
 */
public class Square implements commons, Serializable{
    
    private static final long serialVersionUID = 1;
    private int squareNumber;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean snakeHead;
    private boolean snakeTail;
    private boolean ladderBottom;
    private boolean ladderTop;
    private boolean start;
    private boolean finish;

    public Square(int squareNumber, int x, int y, boolean snakeHead, boolean snakeTail, boolean ladderBottom, boolean ladderTop) {
        
        this.squareNumber = squareNumber;
        this.x = x;
        this.y = y;
        width = SQUARE_WIDTH;
        height = SQUARE_HEIGHT;
        this.snakeHead = snakeHead;
        this.snakeTail = snakeTail;
        this.ladderBottom = ladderBottom;
        this.ladderTop = ladderTop;
        
        this.start = squareNumber == 1;
        this.finish = squareNumber == 100;
        
    }

    public void draw(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(Color.WHITE);
        
        g2d.fillRect(x, y, width, height);
        
        g2d.setColor(Color.BLACK); 
        g2d.drawRect(x, y, width, height);
        
        g2d.drawString(String.valueOf(squareNumber), x + 5, y + 12);
        
    }

    public int getSquareNumber() {
        return squareNumber;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isSnakeHead() {
        return snakeHead;
    }

    public boolean isSnakeTail() {
        return snakeTail;
    }

    public boolean isLadderBottom() {
        return ladderBottom;
    }

    public boolean isLadderTop() {
        return ladderTop;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isFinish() {
        return finish;
    }

    public Rectangle getBounds(){
        
        return new Rectangle(getX(), getY(), width, height);
    }
    
   
}
