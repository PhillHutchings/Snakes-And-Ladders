/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snakesandladders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 *
 * @author hutch
 */
public class Start extends JPanel implements commons{
    
    Game game;
    Board board;
    JToggleButton[] players;
    ButtonGroup bg;
    ArrayList<Player> pickPlayers;   
    Buttons btns;
    JTextField[] nameSpace;
    JComboBox[] colorPick;
    JComboBox levelPick;
    String[] colors;
    String[] levels;
    
    PickColor pickColor;
    PickLevel pickLevel;
    JButton begin;
    Begin playGame;
    
    
    public Start(Board board, Game game){
        
        this.board = board;
        this.game = game;
        init();
    }
    
    private void init(){
        
        setLayout(null);
        setPreferredSize(new Dimension(START_WIDTH, START_HEIGHT));
        
        pickPlayers = new ArrayList<>();
        btns = new Buttons();
        nameSpace = new JTextField[3];
        colorPick = new JComboBox[3]; 
        levelPick = new JComboBox();
        pickLevel = new PickLevel();
        pickColor = new PickColor();
        begin = new JButton("BEGIN");
        playGame = new Begin();
        begin.addActionListener(playGame); 
        
        setUpNameSpace();
        addColorsAndLevels();
        makeButtons();
        
    }
    
    private void makeButtons(){
        
        players = new JToggleButton[3];
        bg = new ButtonGroup();
        int x = 10;
        int y = 30;
        
        for(int i = 0 ; i < players.length; i++){
            
            players[i] = new JToggleButton(String.valueOf(i + 1));
            players[i].setBounds(x, y, PLAYERS_BUTTON_WIDTH , PLAYERS_BUTTON_HEIGHT);
            add(players[i]);
            players[i].addActionListener(btns);
            players[i].setVisible(true);
            bg.add(players[i]);
            x += (PLAYERS_BUTTON_WIDTH + 10);
        }
        
        begin.setBounds(START_WIDTH - 140, START_HEIGHT - 100, 80, 30);
        add(begin);

        repaint();
    }
    
    /**
     * player colour chooser and level chooser combos
     */
    
    private void addColorsAndLevels(){ 
        
        colors = new String[]{"RED", "BLUE", "PINK", "BLACK", "ORANGE", "YELLOW"};
        levels = new String[]{"EASY", "MEDIUM", "HARD"};
         
        for(int i = 0 ; i < 3; i++){
            
            colorPick[i] = new JComboBox();
            add(colorPick[i]);
            
            for(int c = 0 ; c < colors.length; c++){

              colorPick[i].addItem(String.valueOf(colors[c]));
              
            }
         }
        
        for(int l = 0 ; l < levels.length; l++){

          levelPick.addItem(String.valueOf(levels[l]));
        }
        
        levelPick.setBounds((PLAYERS_BUTTON_WIDTH * 3) + 40, 30, 80, 25);
        levelPick.addItemListener(pickLevel); 
        board.setLevel(1);      //default level selection
        add(levelPick);
    }
    
    /**
     * name input fields
     */
    
    private void setUpNameSpace(){
        
        for(int i = 0 ; i < 3; i++){
            
            nameSpace[i] = new JTextField();                      
            add(nameSpace[i]);

        }     
    }
    
    /**
     * draw players with name input and colour picker
     * @param g 
     */
    
    private void drawPlayers(Graphics g){
 
        int y = 0;
        int x = 10;
        
        for(int i = 0 ; i < pickPlayers.size(); i++){
            
            g.setColor(pickPlayers.get(i).getColor());         
            g.fillRect(x + 3, 110 + y, 15, 30);
            g.setColor(Color.black);                
            g.drawRect(x + 3, 110 + y, 15, 30);

            g.setColor(pickPlayers.get(i).getColor()); 
            g.fillOval(x, 100 + y, 22, 22);
            g.setColor(Color.black);
            g.drawOval(x, 100 + y, 22, 22);
            
            nameSpace[i].setBounds(x + 30, 110 + y, 120, 25);           
            nameSpace[i].setVisible(true);

            colorPick[i].setBounds(180, 110 + y, 80, 25);
            colorPick[i].addItemListener(pickColor);
            colorPick[i].setVisible(true);
            
            y += 80;
        }
    }
    
    /**
     * paint
     * @param g 
     */
    
    @Override
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        
        g.setColor(Color.CYAN); 
        g.fillRect(0, 0, START_WIDTH, START_HEIGHT); 
        g.setColor(Color.BLACK); 
        g.drawString("Choose Number Of Players", 10, 15);
        
        drawPlayers(g2d);
        
    }
    
    /**
     * action listener for the number of player buttons
     */
    
    class Buttons implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
          
        //clears variables to chosen ammount of players
        
            pickPlayers.clear();
            
            for(int n = 0; n < nameSpace.length; n++){
                
                nameSpace[n].setVisible(false); 
            }
            
            for(int c = 0; c < colorPick.length; c++){
                
                colorPick[c].setVisible(false); 
            }
            
            AbstractButton btn = (AbstractButton)e.getSource();
            
            int number = Integer.parseInt(btn.getText());
            
            for(int i = 0 ; i < number; i++){
                
                pickPlayers.add(new Player(board, 0, i,Color.WHITE, ""));
            }
            
            repaint();
         
        } 
    }
    
    /**
     * item listener for the colour chooser in combo box
     */
    
    class PickColor implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            
            for(int i = 0 ; i < colorPick.length; i++){
                
                if(e.getItemSelectable() == colorPick[i]){
                    
                    String color = String.valueOf(colorPick[i].getSelectedItem());
                    
                    switch(color) {
                        
                        case "RED":
                            pickPlayers.get(i).setColor(Color.RED); 
                            break;
                            
                        case "BLUE":
                            pickPlayers.get(i).setColor(Color.BLUE); 
                            break;
                        
                        case "PINK":
                            pickPlayers.get(i).setColor(Color.PINK); 
                            break;
                        
                        case "BLACK":
                            pickPlayers.get(i).setColor(Color.BLACK); 
                            break;
                        
                        case "ORANGE":
                            pickPlayers.get(i).setColor(Color.ORANGE); 
                            break;
                        
                        case "YELLOW":
                            pickPlayers.get(i).setColor(Color.YELLOW); 
                            break;
                       
                    }
                    
                    repaint();
                }
            }
        } 
    }
    
    /**
     * item listener for level combo box
     */
    
    class PickLevel implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {

            if(e.getItemSelectable() == levelPick){
                
                String level = String.valueOf(levelPick.getSelectedItem());

                switch(level) {

                    case "EASY":
                        board.setLevel(1);                        
                        break;

                    case "MEDIUM":
                        board.setLevel(2); 
                        break;

                    case "HARD":
                        board.setLevel(3); 
                        break;  
                               
                }        
            }             
        }
    }
    
    /**
     * action listener for the begin button to start the game
     */
    
    class Begin implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if(!pickPlayers.isEmpty()){
            
        //adds chosen players to board class players   
        
                for(int i = 0 ; i < pickPlayers.size(); i++){

                    pickPlayers.get(i).setName(nameSpace[i].getText()); 
                    board.getPlayers().add(pickPlayers.get(i));
                }
            
                game.init();
                
            }else{
                
                JOptionPane.showMessageDialog(null, "Please Choose Number Of Players"); 
            }
        }
    
    }
}
