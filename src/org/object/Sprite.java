/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.graphics.Renderer;

/**
 *
 * @author gerar
 */
public class Sprite {
    
    public float posX = 0;
    public float posY = 0;
    public int width;
    public int height;
    public boolean solid = false;
    
    public BufferedImage image = null;
    
    public Sprite(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void update(float deltaTime) {
        //
    }
    
    public void render(Graphics g) {    
        if(image == null) {
            return;
        }
        int realX = (int)posX;
        int realY = (int)posY;
           
        g.drawImage(image, realX, realY, width, height, null);
    }

    public boolean isSolid() {
        return solid;
    }   
}
