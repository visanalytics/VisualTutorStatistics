/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.Main;
import com.deakin.datavis.World;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

public class Door {
    double xm;
    double ym;
    double xm2;
    double ym2;
    World world;
    Main main;
    Image concrete;
    Image floorboards;
    URL url;

    public Door(World world, Main main, double xm, double ym, double xm2, double ym2) {
        this.world = world;
        this.main = main;
        this.xm = xm;
        this.ym = ym;
        this.xm2 = xm2;
        this.ym2 = ym2;
        try {
            this.url = main.getDocumentBase();
        }
        catch (Exception var11_7) {
            // empty catch block
        }
        this.concrete = main.getImage(this.url, "res/images/concrete.png");
        this.floorboards = main.getImage(this.url, "res/images/floorboards.png");
    }

    public void update() {
    }

    public void paintBackground(Graphics g) {
        Color outC = new Color(48, 85, 130);
        g.setColor(outC);
        g.fillRect(0, 0, this.world.metresToPixels(this.world.getEndX()), this.world.metresToPixels(this.ym) + 15);
        Color insC = new Color(69, 120, 183);
        g.setColor(insC);
        g.fillRect(0, this.world.metresToPixels(this.ym) + 15, this.world.metresToPixels(this.world.getEndX()), this.world.metresToPixels(this.world.getEndY()));
    }

    public void paintForeground(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, this.world.metresToPixels(this.ym), this.world.metresToPixels(this.xm), 15);
        g.fillRect(this.world.metresToPixels(this.xm2), this.world.metresToPixels(this.ym), this.world.metresToPixels(this.world.metresWidth - this.xm2), 15);
        Font lFont = g.getFont();
        g.setFont(new Font("Dialog", 1, 24));
        g.setColor(Color.WHITE);
        g.drawString("Store", this.world.metresToPixels(this.world.metresWidth / 2.0) - g.getFontMetrics().stringWidth("Store"), (int)((double)(this.world.metresToPixels(this.ym2) + (this.world.metresToPixels(this.world.metresHeight) - this.world.metresToPixels(this.ym2)) / 2) - (double)g.getFontMetrics().getHeight() * 1.5));
        g.setFont(lFont);
    }

    public double getLeftXm() {
        return this.xm;
    }

    public double getRightXm() {
        return this.xm2;
    }

    public double getCenterXm() {
        return this.xm + (this.xm2 - this.xm) / 2.0;
    }

    public double getCenterYm() {
        return this.ym + (this.ym2 - this.ym) / 2.0;
    }
}

