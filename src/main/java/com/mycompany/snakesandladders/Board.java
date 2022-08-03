/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snakesandladders;

import static com.mycompany.snakesandladders.commons.BOARD_HEIGHT;
import static com.mycompany.snakesandladders.commons.BOARD_WIDTH;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author hutch
 */
public class Board extends JPanel implements commons, Serializable {

    private static final long serialVersionUID = 1;
    private GameBuild gb;
    private Dice dice;
    private transient Sounds sounds;
    private Square[] squares;
    private ArrayList<Snake> builtSnakes;
    private ArrayList<Ladder> builtLadders;
    private transient ArrayList<Player> players;
    private transient int playersTurn;
    private JButton newBoard;
    private transient NewBoard nb;
    private JButton saveBoard;
    private JComboBox savedBoards;
    private transient SaveBoard sb;
    private transient loadSavedBoard lsb;

    private int level;
    private int snakes;
    private int ladders;
    private File playingBoard;

    public Board() {

        init();
    }

    private void init() {

        setLayout(null);
        nb = new NewBoard();
        sb = new SaveBoard();
        lsb = new loadSavedBoard();

        newBoard = new JButton("New Board");
        newBoard.setBounds(1000, 10, 150, 40);
        newBoard.addActionListener(nb);
        add(newBoard);
         
        saveBoard = new JButton("Save Board");
        saveBoard.setBounds(1000, 60, 150, 40);
        saveBoard.addActionListener(sb);
        add(saveBoard);
        
        savedBoards = new JComboBox();
        savedBoards.setBounds(1000, 130, 150, 40);
        savedBoards.addActionListener(lsb); 
        add(savedBoards);
        
        builtSnakes = new ArrayList<>();
        builtLadders = new ArrayList<>();
        players = new ArrayList<>();

        dice = new Dice(this);
        playingBoard = new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/images/currentBoard/playingBoard.png");
        
        createBoardfile();
        loadSavedBoardsToComboBox();

        sounds = new Sounds(this);
        gb = new GameBuild(this);
        gb.buildGame();

        setSize(BOARD_WIDTH, BOARD_HEIGHT);

    }
    
    /**
     * creates the playing board file
     */
    
    private void createBoardfile(){
        
        if(!playingBoard.exists()){
            
            try {
                
                playingBoard.createNewFile();
                
            } catch (IOException ex) {
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }      
    }
    
    /**
     * populates saved boards combo box with saved boards
     */
    
    private void loadSavedBoardsToComboBox(){
        
        savedBoards.removeAllItems();
        
        try {
            Stream<Path> getSavedFiles = Files.list(Paths.get(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/savedBoards"));
            
            getSavedFiles.map(f -> f.getFileName().toString()).map(fn -> fn.replace(".ser", "")).forEach( s -> savedBoards.addItem(s));
            
        } catch (IOException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * gets saved board
     *
     * @return
     */
    
    public BufferedImage playingBoard() {

        BufferedImage image = null;

        try {

            image = ImageIO.read(playingBoard);

        } catch (IOException ex) {

            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }

        return image;
    }


    public int getPlayersTurn() {

        return playersTurn;
    }

    public void nextPlayersTurn() {

        if (playersTurn == players.size() - 1) {

            playersTurn = 0;

        } else {

            playersTurn++;
        }
    }

    /**
     * draws board squares
     *
     * @param g
     */
    private void drawSqaures(Graphics2D g) {

        for (int i = 0; i < NUMBER_OF_SQUARES; i++) {

            squares[i].draw(g);
        }
    }

    /**
     * draws board snakes
     *
     * @param g
     */
    private void drawSnakes(Graphics2D g) {

        builtSnakes.forEach(s -> s.draw(g));
    }

    /**
     * draws board snakes
     *
     * @param g
     */
    private void drawLadders(Graphics2D g) {

        builtLadders.forEach(l -> l.draw(g));
    }

    /**
     * draws players
     *
     * @param g
     */
    private void drawPlayers(Graphics2D g) {

        players.forEach(p -> p.draw(g));
    }

    /**
     * draws board for saving to file
     * @param g 
     */
    
    public void drawBoard(Graphics2D g) {

        Graphics2D g2d = (Graphics2D) g;

        g.setColor(new Color(0, 170, 255));
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        
        drawSqaures(g2d);
        drawSnakes(g2d);
        drawLadders(g2d);
        
        
        g.setFont(new Font("Rockwell", 0, 18));
        g.drawString("Saved Boards", 1020, 125);
        

    }

    /**
     * draws game
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(playingBoard(), null, null);

        drawPlayers(g2d);

        dice.draw(g2d);
    }

    /**
     * action listener to generate new board
     */
    class NewBoard implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            builtSnakes.clear();
            builtLadders.clear();

            gb = new GameBuild(getThis());
            gb.buildGame();
            
            saveBoard.setEnabled(true);

        }
    }
    
    /**
     * action listener to save current board
     */
    
    class SaveBoard implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            String name = null;
            
            String savedFile = System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/savedBoards/";
            
            name = JOptionPane.showInputDialog(null, "Save Board As", "Save Board", JOptionPane.OK_CANCEL_OPTION);
            
            if(name != null){
           
                try(ObjectOutputStream os = new ObjectOutputStream(new  FileOutputStream(savedFile + name + ".ser"))){

                    os.writeObject(getThis()); 

                } catch (FileNotFoundException ex) {
                    
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                    
                } catch (IOException ex) {
                    
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            loadSavedBoardsToComboBox();
            saveBoard.setEnabled(false);
        }
    }
    
    /**
     * loads the saved board
     */
    
    class loadSavedBoard implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            Board saved = null; 
            
         
                String savedBoard = String.valueOf(savedBoards.getSelectedItem() + ".ser");
                String getSavedFile = System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/savedBoards/" + savedBoard;
                
                try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getSavedFile))){
              
                    saved = (Board) ois.readObject();
                       
                    setSquares(saved.getSquares());
                    setBuiltSnakes(saved.getBuiltSnakes());
                    setBuiltLadders(saved.getBuiltLadders());
        
            //re adds images to snake and ladders
            
                    for(int s = 0; s < getBuiltSnakes().size(); s++){
                        
                       getBuiltSnakes().get(s).setSnake();
                    }
                    
                    for(int l = 0 ; l < getBuiltLadders().size(); l++){
                        
                        getBuiltLadders().get(l).setLadder();
                    }
                    

                    if(getPlayingBoard().exists()){

                        BufferedImage image = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT,BufferedImage.TYPE_INT_RGB);

                        try {
                            
                            Graphics2D graphic = image.createGraphics();  

                            drawBoard(graphic);    

                            ImageIO.write(image, "png", getPlayingBoard());
                            
                            repaint();
                            
                            saveBoard.setEnabled(false);  //stop from saving already saved board

                        } catch (IOException ex) {

                            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                } catch (IOException | ClassNotFoundException ex) {
                    
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                    
                }      
            }
         
    }
    
    /**
     * resets game
     */
    
    public void resetGame() {

        for (int i = 0; i < players.size(); i++) {

            players.get(i).resetSquare();

        }

        newBoard.setEnabled(true);
        savedBoards.setEnabled(true);

        repaint();
    }
    

    private Board getThis() {

        return this;
    }   

    public GameBuild getGb() {
        return gb;
    }

    public void setGb(GameBuild gb) {
        this.gb = gb;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public Sounds getSounds() {
        return sounds;
    }

    public void setSounds(Sounds sounds) {
        this.sounds = sounds;
    }

    public Square[] getSquares() {
        return squares;
    }

    public void setSquares(Square[] squares) {
        this.squares = squares;
    }

    public ArrayList<Snake> getBuiltSnakes() {
        return builtSnakes;
    }

    public void setBuiltSnakes(ArrayList<Snake> builtSnakes) {
        this.builtSnakes = builtSnakes;
    }

    public ArrayList<Ladder> getBuiltLadders() {
        return builtLadders;
    }

    public void setBuiltLadders(ArrayList<Ladder> builtLadders) {
        this.builtLadders = builtLadders;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public JButton getNewBoard() {
        return newBoard;
    }

    public void setNewBoard(JButton newBoard) {
        this.newBoard = newBoard;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSnakes() {
        return snakes;
    }

    public void setSnakes(int snakes) {
        this.snakes = snakes;
    }

    public int getLadders() {
        return ladders;
    }

    public void setLadders(int ladders) {
        this.ladders = ladders;
    }

    public File getPlayingBoard() {
        return playingBoard;
    }

    public void setPlayingBoard(File playingBoard) {
        this.playingBoard = playingBoard;
    }

    public JComboBox getSavedBoards() {
        return savedBoards;
    }
 
}
