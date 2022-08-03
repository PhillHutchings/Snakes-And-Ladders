/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.snakesandladders;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author hutch
 */
public class Sounds {
    
    private final Board board;
    private File ladderSound; 
    private AudioInputStream aisLadder; 
    private Clip clipLadder; 
    private File slideDownSnakeSound;  
    private AudioInputStream aisSnake;  
    private Clip clipSnake; 
    private File diceSound;
    private AudioInputStream aisDice;
    private Clip clipDice;  
    private File playerSound;
    private AudioInputStream aisPlayer;
    private Clip clipPlayer; 
    private File winSound;
    private AudioInputStream aisWin;
    private Clip clipWin; 
    
    public Sounds(Board board){
        
        this.board = board;
        loadSounds();
    }
    
    private void loadSounds(){
        
        ladderSound = new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/sounds/ladderClimb.wav");
        slideDownSnakeSound = new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/sounds/slideDownSnake.wav");
        diceSound = new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/sounds/diceTick.wav");
        playerSound = new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/sounds/playerMoveSound.wav");
        winSound = new File(System.getProperty("user.dir") + "/src/main/java/com/mycompany/snakesandladders/sounds/gameWon.wav"); 
    }
    
                    
    /**
     * makes noise of sliding down snake
     */
    
    public void slideDownSnakeSound(){
        
        try{                                 
            
            aisSnake = AudioSystem.getAudioInputStream(slideDownSnakeSound.toURI().toURL());
            
            clipSnake = AudioSystem.getClip();
            int length = clipSnake.getFrameLength();
            
            clipSnake.open(aisSnake);
            clipSnake.start();
            
            if(clipSnake.getFramePosition() == length){
                
                clipSnake.setFramePosition(0); 
            }
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
                
    /**
     * makes noise of climbing ladder
     */
    
    public void ladderSound(){
        
        try{                                 
            
            aisLadder = AudioSystem.getAudioInputStream(ladderSound.toURI().toURL());
            
            clipLadder = AudioSystem.getClip();
            
            clipLadder.open(aisLadder);
            clipLadder.loop(10); 
            

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    /**
     * stops the ladder sound
     */
    
    public void stopLadderSound(){
        
        int length = clipLadder.getFrameLength();
        clipLadder.stop();
        clipLadder.setFramePosition(0); 
    }
    
                
    /**
     * makes noise of dice rolling
     */
    
    public void diceSound(){
        
        try{                                 
            
            aisDice = AudioSystem.getAudioInputStream(diceSound.toURI().toURL());
            
            clipDice = AudioSystem.getClip();
            int length = clipDice.getFrameLength();
            
            clipDice.open(aisDice);
            clipDice.start();
            
            if(clipDice.getFramePosition() == length){
                
                clipDice.setFramePosition(0); 
            }
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    /**
     * makes noise of player moving
     */
    
    public void playerMoveSound(){
        
        try{                                 
            
            aisPlayer = AudioSystem.getAudioInputStream(playerSound.toURI().toURL());
            
            clipPlayer = AudioSystem.getClip();
            int length = clipPlayer.getFrameLength();
            
            clipPlayer.open(aisPlayer);
            
            FloatControl fc =  (FloatControl) clipPlayer.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(10f * (float) Math.log10(0.05f));
            
            clipPlayer.start();
            
            if(clipPlayer.getFramePosition() == length){
                
                clipPlayer.setFramePosition(0); 
            }
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
        
    /**
     * makes noise of player winning
     */
    
    public void winSound(){
        
        try{                                 
            
            aisWin = AudioSystem.getAudioInputStream(winSound.toURI().toURL());
            
            clipWin = AudioSystem.getClip();
            int length = clipWin.getFrameLength();
            
            clipWin.open(aisWin);
            
            FloatControl fc =  (FloatControl) clipWin.getControl(FloatControl.Type.MASTER_GAIN);
            
            fc.setValue(10f * (float) Math.log10(0.05f));
            
            clipWin.start();
            
            if(clipPlayer.getFramePosition() == length){
                
                clipPlayer.setFramePosition(0); 
            }
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
}
