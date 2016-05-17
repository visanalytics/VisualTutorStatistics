/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import com.deakin.datavist2.Main;
import com.deakin.datavist2.World;
import java.awt.Color;
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
        Color insC = new Color(48, 85, 130);
        g.setColor(insC);
        g.fillRect(0, 0, this.world.metresToPixels(this.getCenterXm()), this.world.metresToPixels(this.world.metresHeight));
        Color outC = new Color(69, 120, 183);
        g.setColor(outC);
        g.fillRect(this.world.metresToPixels(this.getCenterXm()), this.world.metresToPixels(0.0), this.world.metresToPixels(this.world.getEndX()), this.world.metresToPixels(this.world.getEndY()));
    }

    public void paintForeground(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(this.world.metresToPixels(this.xm), this.world.metresToPixels(0.0), this.world.metresToPixels(this.xm2 - this.xm), this.world.metresToPixels(this.ym));
        g.fillRect(this.world.metresToPixels(this.xm), this.world.metresToPixels(this.ym2), this.world.metresToPixels(this.xm2 - this.xm), this.world.metresToPixels(this.world.metresHeight - this.ym2));
    }

    public double getY1() {
        return this.ym + (this.ym2 - this.ym) * 0.2;
    }

    public double getY2() {
        return this.ym + (this.ym2 - this.ym) * 0.8;
    }

    public double getLeftXm() {
        return this.xm;
    }

    public double getRightXm() {
        return this.xm2;
    }

    public double getLeftYm() {
        return this.ym;
    }

    public double getRightYm() {
        return this.ym2;
    }

    public double getCenterXm() {
        return this.xm + (this.xm2 - this.xm) / 2.0;
    }

    public double getCenterYm() {
        return this.ym + (this.ym2 - this.ym) / 2.0;
    }
}

