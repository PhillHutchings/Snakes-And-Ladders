/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snakesandladders;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.KeyStroke;

/**
 *
 * @author hutch
 */
public class Dice implements commons, Serializable{
    
    private static final long serialVersionUID = 1;
    private Board board;
    private transient Image[] diceNumber1;
    private transient Image[] diceNumber2;
    private int diceNum1;
    private int diceNum2;
    private Random rand;
    private JButton rollBtn;
    private transient Roll roll;
    private int move;

    
    public Dice(Board board){
        
        this.board = board;
        diceNumber1 = new Image[6];
        diceNumber2 = new Image[6];
        rand = new Random();
        roll = new Roll();
        
        rollBtn = new JButton("Roll");
        rollBtn.setBounds(DICE_1_X, DICE_1_Y + (DICE_HEIGHT + 10), (DICE_WIDTH * 2) + 5, 50);
        rollBtn.addActionListener(roll);
   
        enableKeys(board);
        
        board.add(rollBtn);
        loadDiceNumbers();
    }
    
    /**
     * load dice face images
     */
    
    private void loadDiceNumbers(){
        
        ImageIcon ii = null;
        int diceNum = 1;
        
        try {
            
            for(int i = 0 ; i < diceNumber1.length; i++){

                BufferedImage bi = ImageIO.read(new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/images/dice/dice-" + diceNum + ".png"));
                ii = new ImageIcon(bi);

                diceNumber1[i] = ii.getImage().getScaledInstance(DICE_WIDTH, DICE_HEIGHT, Image.SCALE_SMOOTH);
                diceNumber2[i] = ii.getImage().getScaledInstance(DICE_WIDTH, DICE_HEIGHT, Image.SCALE_SMOOTH);
                
                diceNum++; 
            }
            
        } catch (IOException ex) {
            
            Logger.getLogger(Dice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * action listener to roll the dice
     */
    
    class Roll implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            ExecutorService ex = Executors.newSingleThreadExecutor();
        
            Runnable task = () -> {

                board.getSavedBoards().setEnabled(false);  //stops board change during game
                board.getNewBoard().setEnabled(false);   //stops new board being created after first roll of dice
                rollBtn.setEnabled(false); 
                
                int roll1;
                int roll2;
                int dice1 = 1;
                int dice2 = 1;
                int numOfDice = 0; //needed to get correct amount from dice faces

                Instant start;
                long time;

                if(board.getPlayers().get(board.getPlayersTurn()).getSquareNumber() <= 93){     //only use one dice if 6 or less squares from end
                    
                    roll1 = rand.ints(8, 20).findFirst().getAsInt();
                    roll2 = rand.ints(8, 20).findFirst().getAsInt();
                    
                    numOfDice = 2;
                    
                    do{

                    //to count up to random number 

                        if(dice1 != roll1){

                            diceNum1++;
                            dice1++;

                            if(diceNum1 == diceNumber1.length){   //change dice num back to zero to begin at dice spot 1

                                diceNum1 = 0;
                            }    
                        }    

                        if(dice2 != roll2){

                            diceNum2++; 
                            dice2++;

                            if(diceNum2 == diceNumber2.length){  //change dice num back to zero to begin at dice spot 1

                                diceNum2 = 0;
                            }         
                        } 

                        board.repaint();
                        start = Instant.now();

                        do{

                           Instant finish = Instant.now();
                           time = Duration.between(start, finish).toMillis();

                        }while(time < 100);
                        
                        board.getSounds().diceSound();

                    }while(dice1 != roll1 || dice2 != roll2);
                     
                    
                }else{
                    
                    roll1 = rand.ints(8, 20).findFirst().getAsInt();
                    numOfDice = 1; 
                    diceNum2 = 0;
                    
                    do{

                    //to count up to random number 

                        if(dice1 != roll1){

                            diceNum1++;
                            dice1++;

                            if(diceNum1 == diceNumber1.length){   //change dice num back to zero to begin at dice spot 1

                                diceNum1 = 0;
                            }    
                        }    

                        board.repaint();
                        start = Instant.now();

                        do{

                           Instant finish = Instant.now();
                           time = Duration.between(start, finish).toMillis();

                        }while(time < 100);
                        
                        board.getSounds().diceSound();

                    }while(dice1 != roll1);
                                                           
                }

                setMove((diceNum1 + diceNum2) + numOfDice); 
                                         
                board.getPlayers().get(board.getPlayersTurn()).move();
                           
                ex.shutdown();

            };
            
            ex.submit(task);
        }
    }

    
    /**
     * space bar key binding
     * @param board 
     */
    
    private void enableKeys(Board board){

        AbstractAction rolls = new AbstractAction(){               //roll
   
            @Override
            public void actionPerformed(ActionEvent e) {
                
                rollBtn.requestFocus();
                rollBtn.doClick();
                
            }
        };
        
        board.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "rolls");
        board.getActionMap().put("rolls", rolls);


     }
    

    public void setMove(int move) {
        
        this.move = move;
    }

    
    public int getMove() {
        
        return move;
    }

    public JButton getRollBtn() {
        
        return rollBtn;
    }

    
    public void draw(Graphics2D g){
        
        g.drawImage(diceNumber1[diceNum1], DICE_1_X , DICE_1_Y,  null);
        g.drawImage(diceNumber2[diceNum2], DICE_2_X , DICE_2_Y , null);
        
    }
}
