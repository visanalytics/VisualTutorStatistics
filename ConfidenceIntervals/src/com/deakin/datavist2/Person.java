/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import com.deakin.datavist2.Activity;
import com.deakin.datavist2.Door;
import com.deakin.datavist2.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.Random;

public class Person {
    public static final String MOOD_HAPPY = "MoodHappy";
    public static final String MOOD_UNHAPPY = "MoodUnhappy";
    public static final String MOOD_NEUTRAL = "MoodNeutral";
    double xm;
    double ym;
    double waitXm;
    double waitYm;
    int radius;
    static final double speedMps = 0.3;
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
    int iWidth;
    int iHeight;
    double scale = 1.5;
    Image moodImage;
    int moodIWidth;
    int moodIHeight;
    String mood;
    double angle;
    boolean willEnterStore = false;
    boolean shownMood = false;

    public Person(Activity activ, World world, Door door, double origXm, double origYm, double waitXm, double waitYm, int radius, boolean movingLeft, boolean enterStore, Image image, String mood, Image moodImage) {
        this.world = world;
        this.door = door;
        this.xm = origXm;
        this.ym = origYm;
        this.waitXm = waitXm;
        this.waitYm = waitYm;
        this.image = image;
        this.iWidth = image.getWidth(activ);
        this.iHeight = image.getHeight(activ);
        this.moodImage = moodImage;
        this.moodIWidth = moodImage.getWidth(activ);
        this.moodIHeight = moodImage.getHeight(activ);
        this.mood = mood;
        this.radius = radius;
        this.typeColor = Color.WHITE;
        this.enteringStore = false;
        this.willEnterStore = enterStore;
        this.startXm = this.xm;
        this.startYm = this.ym;
        this.bezierYm = movingLeft ? door.getLeftYm() : door.getRightYm();
        this.bezierXm = door.getCenterXm() + (double)((world.getRandom().nextInt(20) - 10) / 10);
        Random r = new Random();
        this.endXm = world.metresWidth;
        this.endYm = world.metresHeight * r.nextDouble();
    }

    public boolean contains(int x, int y, int leeway) {
        //Rectangle bounds = new Rectangle((int)((double)this.world.metresToPixels(this.xm) - (double)this.iWidth * this.scale / 2.0 - (double)leeway), (int)((double)this.world.metresToPixels(this.ym) - (double)this.iHeight * this.scale / 2.0 - (double)leeway), (int)((double)this.iWidth * this.scale + (double)(leeway * 2)), (int)((double)this.iHeight * this.scale + (double)(leeway * 2)));
        Rectangle bounds = new Rectangle((int)(((double)this.world.metresToPixels(this.xm) - ((double)this.iWidth * this.scale / 2.0) - leeway)),
        		(int)((double)this.world.metresToPixels(this.ym) - ((double)this.iWidth * this.scale / 2.0) - leeway),
        		(int)((double)this.iWidth * this.scale + (double)(leeway * 2)),
        		(int)((double)this.iHeight * this.scale + (double)(leeway * 2)));
    	if (bounds.contains(x, y)) {
            return true;
        }
        return false;
    }

    public void update() {
        double lastXm = this.xm;
        double lastYm = this.ym;
        double vx = 0.0;
        double vy = 0.0;
        double x3 = this.waitXm - this.xm;
        double y3 = this.waitYm - this.ym;
        double c = Math.sqrt(x3 * x3 + y3 * y3);
        vx = 0.3 * this.world.getTimeChange() * x3 / c;
        vy = 0.3 * this.world.getTimeChange() * y3 / c;
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
                this.bt += 0.045 * this.world.getTimeChange();
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
        return a;
    }

    public void paint(Activity activ, Graphics g) {
        AffineTransform trans = new AffineTransform();
        AffineTransform rot = new AffineTransform();
        AffineTransform scal = new AffineTransform();
        Graphics2D g2 = (Graphics2D)g;
        scal.setToScale(this.scale, this.scale);
        rot.setToRotation(Math.toRadians(this.angle), this.world.metresToPixels(this.xm), this.world.metresToPixels(this.ym));
        trans.setToTranslation((double)this.world.metresToPixels(this.xm) - (double)this.image.getWidth(activ) * scal.getScaleX() / 2.0, (double)this.world.metresToPixels(this.ym) - (double)this.image.getHeight(activ) * scal.getScaleY() / 2.0);
        trans.concatenate(scal);
        rot.concatenate(trans);
        g2.drawImage(this.image, rot, activ);
    }

    public boolean showMood() {
        if (!this.shownMood) {
            this.shownMood = true;
            return true;
        }
        return false;
    }

    public String getMood() {
        return this.mood;
    }
}

