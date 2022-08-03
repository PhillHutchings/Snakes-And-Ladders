/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snakesandladders;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author hutch
 */
public class GameBuild implements commons, Serializable{
    
    private static final long serialVersionUID = 1;
    private Board board;
    private boolean[] snakeHeads;   //for assigning snake head squares
    private boolean[] snakeTails;   //for assigning snake tail squares
    private boolean[] ladderTops;   //for assigning ladder top squares
    private boolean[] ladderBottoms;   //for assigning ladder bottom squares
    private int[][] snakePositions;     //log positions first as adding to board.builtSnakes first will cause null pointer because squares not initialised
    private int[][] ladderPositions;    //ditto

   
    public GameBuild(Board board) {
        
        this.board = board;
        board.setSnakes(getSnakes());
        board.setLadders(getLadders());   
        board.setSquares(new Square[NUMBER_OF_SQUARES]);
          
        snakeHeads = new boolean[NUMBER_OF_SQUARES];
        snakeTails = new boolean[NUMBER_OF_SQUARES];
        ladderTops = new boolean[NUMBER_OF_SQUARES];
        ladderBottoms = new boolean[NUMBER_OF_SQUARES];
        
        snakePositions = new int[board.getSnakes()][2];
        ladderPositions = new int[board.getLadders()][2];
        
    }
    
    /**
     * builds the snakes, ladders and game squares
     */
    
    public void buildGame(){
        
        assignSnakesAndLadders();  
        buildBoard();
        
    }

    public int getLevel() {
        
        return board.getLevel();
    }
   
    /**
     * get number of snakes to add
     * @return 
     */
    private int getSnakes() {
        
        return getLevel() == 3 ? 15 : board.getLevel() == 2 ? 10 : 5;
     
    }

    /**
     * get number of ladders to add
     * @return 
     */
    
    private int getLadders() {
        
        return getLevel() == 1 ? 12 : board.getLevel() == 2 ? 6 : 3;
         
    }
    
    /**
     * randomly generates snake and ladder squares
     */
    
    private void assignSnakesAndLadders(){
   
        Random rand = new Random();
        ArrayList<Integer> taken = new ArrayList<>();
        int snakes = getSnakes() -1;
        int ladders = getLadders() -1;
        int s1;
        int s2;
        
        int sn = 1;
        int rows[][] = new int[10][10];
        
        for(int i = 0 ; i < rows.length ;i++){
            
            for(int j = 0 ; j < rows[i].length; j++ ){
                
                rows[i][j] = sn;
                
                sn++;
            }
        }
       
    
    //SNAKES
    
        do{
            
            boolean s1Row = false;          //for checking snake squares not on same row ie not horizontal as causes problems
            boolean s2Row = false;
            
            do{
   
        //generating two random numbers on board

                s1 = rand.ints(2,NUMBER_OF_SQUARES - 1).findFirst().getAsInt();
                s2 = rand.ints(2,NUMBER_OF_SQUARES - 1).findFirst().getAsInt();
                
rowCheck:       for(int i = 0 ; i < rows.length ;i++){
            
                    s1Row = false;
                    s2Row = false;
                    
                    for(int j = 0 ; j < rows[i].length; j++ ){
                        
                        if(rows[i][j] == s1){
                            
                            s1Row = true;
                            
                        }
                        
                        if(rows[i][j] == s2){
                            
                            s2Row = true;
                          
                        }
                         
                        if(s1Row && s2Row){

                            break rowCheck;
                        }    
                    }
                }

            }while(taken.contains(s1) || taken.contains(s2) || s1 == s2 || s1Row && s2Row);
            
            taken.add(s1);
            taken.add(s2);
            
         //finding out which is larger to work out which one is for the head

            int head = s1 > s2 ? s1 : s2;
            int tail = s2 > s1 ? s1 : s2; 
        
        //assinging true to align with square numbers
        
            snakeHeads[head] = true;
            snakeTails[tail] = true;
            
        //log positions 
        
            snakePositions[snakes][0] = head; 
            snakePositions[snakes][1] = tail; 
            
            snakes--;
                  
        }while(snakes > -1);
        
   //LADDERS     

        do{
            
            boolean s1Row = false;      //for checking ladder squares not on same row ie not horizontal as causes problems
            boolean s2Row = false;
            
            do{
                
        //generating two random numbers on board  // starts with 3 to stop bottom of ladder going on second square (unlandable square)

                s1 =  rand.ints(3,NUMBER_OF_SQUARES - 1).findFirst().getAsInt();
                s2 = rand.ints(3,NUMBER_OF_SQUARES - 1).findFirst().getAsInt();
                                
rowCheck:       for(int i = 0 ; i < rows.length ;i++){
            
                    s1Row = false;
                    s2Row = false;
                    
                    for(int j = 0 ; j < rows[i].length; j++ ){
                        
                        if(rows[i][j] == s1){
                            
                            s1Row = true;
                        }
                        
                         if(rows[i][j] == s2){
                            
                            s2Row = true;
                        }
                         
                         if(s1Row && s2Row){
                             
                             break rowCheck;
                         }
                        
                    }
                }

            }while(taken.contains(s1) || taken.contains(s2) || s1 == s2 || s1Row && s2Row);
            
            taken.add(s1);
            taken.add(s2);
            
         //finding out which is larger to work out which one is for the top of ladder

            int top = s1 > s2 ? s1 : s2;
            int bottom = s2 > s1 ? s1 : s2;  

            ladderTops[top] = true;
            ladderBottoms[bottom] = true;
            
            ladderPositions[ladders][0] = top;
            ladderPositions[ladders][1] = bottom;

            ladders--;
                  
        }while(ladders > -1);  
       
        
    }
    
    /**
     * assigns squares with snakes and ladders
     */
    
    private void buildBoard(){

        int squareNumber = 1;
        int x = SQUARE_WIDTH;
        int y = (BOARD_HEIGHT - (SQUARE_HEIGHT * 2));
        boolean right = true;     //to go up and count left to right then right to left

        
        for(int i = 0 ; i < NUMBER_OF_SQUARES; i++){      
                      
            if(squareNumber == 1 || squareNumber == 100){

                board.getSquares()[i] = new Square(squareNumber, x, y, false, false, false, false);   //start square and finish square

            }else{

                board.getSquares()[i] = new Square(squareNumber, x, y, snakeHeads[i], snakeTails[i], ladderTops[i], ladderBottoms[i]); 
                
            }     

            if(right){
                
                x += SQUARE_WIDTH;
        
            }else{
                
                x -= SQUARE_WIDTH;
                
            }
            
            if(x == SQUARE_WIDTH * 11 || x == 0){
                
                y -= SQUARE_HEIGHT;
                right = !right;
                
                if(x == SQUARE_WIDTH * 11){         //needed to make sure square rows are equal each row tryed many ways this is what i had to do
                    
                    x -= SQUARE_WIDTH;
                    
                }
                
                if(x == 0){
                    
                    x += SQUARE_WIDTH;
                }
                ////////
            }
        
            squareNumber++;
        }
   
    //adds created snakes to to the built snakes arraylist for drawing on board
    
        for(int s = 0; s < board.getSnakes(); s++){

            board.getBuiltSnakes().add(new Snake(board, snakePositions[s][0], snakePositions[s][1]));
        }
        
    //adds created ladders to to the built ladders arraylist for drawing on board    
    
        for(int l = 0; l < board.getLadders(); l++){

            board.getBuiltLadders().add(new Ladder(board, ladderPositions[l][0], ladderPositions[l][1]));
        }
    
    /**
     * saves the background
     */
    

        if(board.getPlayingBoard().exists()){
            
            BufferedImage image = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,BufferedImage.TYPE_INT_RGB);

            try {

                Graphics2D graphic = image.createGraphics();  

                board.drawBoard(graphic);    

                ImageIO.write(image, "png", board.getPlayingBoard());


            } catch (IOException ex) {

                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
   
        board.repaint();
    }   
}
