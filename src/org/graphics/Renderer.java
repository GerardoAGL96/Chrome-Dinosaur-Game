/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.graphics;

import org.game.Game;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.VolatileImage;

/**
 *
 * @author gerar
 */
public class Renderer {
    
    private static Frame frame;
    private static Canvas canvas;
    
    private static int canvasWidth = 0;
    private static int canvasHeight = 0;
    
    private static final int DEFAULT_GAME_WIDTH = 500;
    private static final int DEFAULT_GAME_HEIGHT = 350;
    
    private static long lastFPSCheck = 0;
    private static int currentFPS = 0;
    private static int totalFrames = 0;
    
    public static void init() {
        frame = new Frame();
        canvas = new Canvas();
        
        setIdealSize();
        canvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        
        frame.add(canvas);
        makeFullscreen();
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                Game.quit();
            }
        });
        
        frame.setVisible(true);
        startRendering();
    }
    
    private static void startRendering() {
        
        Thread thread = new Thread() {
            public void run() {
                
                GraphicsConfiguration gc = canvas.getGraphicsConfiguration();
                VolatileImage vImage = gc.createCompatibleVolatileImage(DEFAULT_GAME_WIDTH, DEFAULT_GAME_HEIGHT);
                
                while(true) {     
                    //FPS Counter
                    totalFrames++;
                    if(System.nanoTime() > (lastFPSCheck + 1000000000)) {
                        lastFPSCheck = System.nanoTime();
                        currentFPS = totalFrames;
                        totalFrames = 0;
                    }
                    
                    if(vImage.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
                        vImage = gc.createCompatibleVolatileImage(DEFAULT_GAME_WIDTH, DEFAULT_GAME_HEIGHT);
                    }
                    
                    Graphics g = vImage.getGraphics();
                    
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, canvasWidth, canvasHeight);
                    
                    
                    //RENDER STUFF
                    
                    //Draw FPS Counter
                    g.setColor(Color.YELLOW);
                    g.drawString(String.valueOf(currentFPS), (DEFAULT_GAME_WIDTH - 25), 15);
                    
                    g.dispose();
                    
                    g = canvas.getGraphics();
                    g.drawImage(vImage, 0, 0, canvasWidth, canvasHeight, null);
                    
                    g.dispose();
                }
            }
        };
        
        thread.setName("Rendering Thread");
        thread.start();
    }
    
    private static void makeFullscreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        
        if(gd.isFullScreenSupported()) {
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);
        }
    }
    
    private static void setIdealSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen = toolkit.getScreenSize();
        
        boolean done = false;
        
        while(!done) {
            canvasWidth += DEFAULT_GAME_WIDTH;
            canvasHeight += DEFAULT_GAME_HEIGHT;
            
            if((canvasWidth+DEFAULT_GAME_WIDTH) > screen.width || (canvasHeight+DEFAULT_GAME_HEIGHT) > screen.height) {
                done = true;
            }
        }
    }
}