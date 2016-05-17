/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import java.util.Random;

public class World {
    int wXBegin;
    int wYBegin;
    int wWidth;
    int wHeight;
    double metresWidth;
    double metresHeight;
    int pixelsPerMetre;
    Random random;
    double ticksPerSec;
    double timeRatio = 1000.0;
    double time = 0.0;
    double timeChange = 0.0;

    public World(int windowXStart, int windowYStart, int windowWidth, int windowHeight, int metresWidth, int metresHeight, Random r) {
        this.random = r;
        this.wXBegin = windowXStart;
        this.wYBegin = windowYStart;
        this.wHeight = windowHeight;
        this.wWidth = windowWidth;
        this.metresWidth = metresWidth;
        this.metresHeight = metresHeight;
        this.pixelsPerMetre = windowWidth / metresWidth;
    }

    public int metresToPixels(double metres) {
        return (int)((double)this.pixelsPerMetre * metres);
    }

    public double pixelsToMetres(int pixels) {
        return pixels / this.pixelsPerMetre;
    }

    public double getEndX() {
        return this.metresWidth;
    }

    public double getEndY() {
        return this.metresHeight;
    }

    public int getEndXPix() {
        return this.wWidth;
    }

    public int getEndYPix() {
        return this.wHeight;
    }

    public int getBeginXPix() {
        return this.wXBegin;
    }

    public int getBeginYPix() {
        return this.wYBegin;
    }

    public Random getRandom() {
        return this.random;
    }

    public void incTime(double value) {
        this.timeChange = value;
        this.time += value;
    }

    public double getTime() {
        return this.time;
    }

    public double getTimeChange() {
        return this.timeChange;
    }

    public double getTimeRatio() {
        return this.timeRatio;
    }

    public void setTimeRatio(double millisPerSec) {
        this.timeRatio = millisPerSec;
    }

    public int getTimeInHours() {
        int totTimeHours = (int)Math.floor(this.time / 3600.0);
        return totTimeHours;
    }

    public int getTimeInMinutes() {
        int totTimeHours = (int)Math.floor(this.time / 3600.0);
        int totTimeMinutes = (int)Math.floor((this.time - (double)(totTimeHours * 3600)) / 60.0);
        return totTimeMinutes;
    }

    public int getTimeInSecs() {
        int totTimeHours = (int)Math.floor(this.time / 3600.0);
        int totTimeMinutes = (int)Math.floor((this.time - (double)(totTimeHours * 3600)) / 60.0);
        int totTimeSecs = (int)Math.floor(this.time - (double)(totTimeHours * 3600) - (double)(totTimeMinutes * 60));
        return totTimeSecs;
    }
}

