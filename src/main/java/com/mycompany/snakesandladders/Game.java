/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snakesandladders;

import javax.swing.JFrame;

/**
 *
 * @author hutch
 */
public class Game extends JFrame implements commons{
    
    private Board board;
    private Start start;
    
    public Game(){
        
        initStart();
    }
    
    private void initStart(){
        
        board = new Board();
        start = new Start(board, this);
        
        add(start);
        setSize(START_WIDTH, START_HEIGHT);
        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
     void init(){
        
        board.getNewBoard().doClick();     //beacuse board instantiated so needs click to get current players and level 
        remove(start);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        add(board);
        setVisible(true);
        
    }
    
    
    public static void main(String[] args){
        
        java.awt.EventQueue.invokeLater(new Runnable(){
        
            public void run(){
                
                new Game();
            }
        });
    }
}
