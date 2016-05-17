/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.Door;
import com.deakin.datavis.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.util.Random;

public class Person {
    double xm;
    double ym;
    double waitXm;
    double waitYm;
    int radius;
    static final double speedMps = 0.2;
    boolean done = false;
    boolean enteringStore;
    double startXm;
    double startYm;
    double bezierXm;
    double bezierYm;
    double endXm;
    double endYm;
    double bt;
    Color typeColor;
    World world;
    Door door;
    Image image;
    double angle;
    boolean willEnterStore = false;

    public Person(World world, Door door, double origXm, double origYm, double waitXm, double waitYm, int radius, boolean movingLeft, boolean enterStore, Image image) {
        this.world = world;
        this.door = door;
        this.xm = origXm;
        this.ym = origYm;
        this.waitXm = waitXm;
        this.waitYm = waitYm;
        this.image = image;
        this.radius = radius;
        this.typeColor = Color.WHITE;
        this.enteringStore = false;
        this.willEnterStore = enterStore;
        this.startXm = this.xm;
        this.startYm = this.ym;
        this.bezierXm = movingLeft ? door.getLeftXm() : door.getRightXm();
        this.bezierYm = door.getCenterYm() - (double)(world.getRandom().nextInt(6) / 10);
        this.endXm = world.getEndX() / 2.0;
        this.endYm = world.getEndY();
    }

    public void update() {
        double lastXm = this.xm;
        double lastYm = this.ym;
        double vx = 0.0;
        double vy = 0.0;
        double x3 = this.waitXm - this.xm;
        double y3 = this.waitYm - this.ym;
        double c = Math.sqrt(x3 * x3 + y3 * y3);
        vx = 0.2 * this.world.getTimeChange() * x3 / c;
        vy = 0.2 * this.world.getTimeChange() * y3 / c;
        this.xm += vx;
        this.ym += vy;
        if (this.xm <= this.waitXm + 0.1 && this.xm >= this.waitXm - 0.1 && this.ym <= this.waitYm + 0.1 && this.ym >= this.waitYm - 0.1) {
            if (this.willEnterStore) {
                this.startXm = this.xm;
                this.startYm = this.ym;
                this.enteringStore = true;
            } else {
                this.done = true;
            }
        }
        if (this.enteringStore) {
            if (this.bt < 1.1) {
                double xmt = (1.0 - this.bt) * (1.0 - this.bt) * this.startXm + 2.0 * (1.0 - this.bt) * this.bt * this.bezierXm + this.bt * this.bt * this.endXm;
                double ymt = (1.0 - this.bt) * (1.0 - this.bt) * this.startYm + 2.0 * (1.0 - this.bt) * this.bt * this.bezierYm + this.bt * this.bt * this.endYm;
                this.xm = xmt;
                this.ym = ymt;
                this.bt += 0.1 * this.world.getTimeChange();
            } else {
                this.done = true;
            }
        }
        this.angle = this.findRotation(lastXm, lastYm, this.xm, this.ym);
    }

    private double findRotation(double lastX, double lastY, double newX, double newY) {
        double a = 0.0;
        double deltaX = newX - lastX;
        double deltaY = newY - lastY;
        a = Math.atan2(deltaY, deltaX) * 180.0 / 3.141592653589793;
        System.out.println("Last X: " + lastX + " New X: " + newX + " Angle: " + a);
        return a;
    }

    public void paint(Activity activ, Graphics g) {
        AffineTransform trans = new AffineTransform();
        AffineTransform rot = new AffineTransform();
        AffineTransform scal = new AffineTransform();
        Graphics2D g2 = (Graphics2D)g;
        scal.setToScale(1.5, 1.5);
        rot.setToRotation(Math.toRadians(this.angle), this.world.metresToPixels(this.xm), this.world.metresToPixels(this.ym));
        trans.setToTranslation((double)this.world.metresToPixels(this.xm) - (double)this.image.getWidth(activ) * scal.getScaleX() / 2.0, (double)this.world.metresToPixels(this.ym) - (double)this.image.getHeight(activ) * scal.getScaleY() / 2.0);
        trans.concatenate(scal);
        rot.concatenate(trans);
        g2.drawImage(this.image, rot, activ);
    }
}

