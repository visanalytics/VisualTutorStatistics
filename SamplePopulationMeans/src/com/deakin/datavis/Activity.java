/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.AppletMan;
import com.deakin.datavis.InstanceData;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

public class Activity
extends Canvas {
    protected AppletMan appMan;
    public int windowWidth;
    public int windowHeight;

    public void init(AppletMan m) {
        this.setSize(800, 400);
        this.windowWidth = 800;
        this.windowHeight = 400;
        this.appMan = m;
    }

    public URL getDocumentBase() {
        return this.appMan.getDocumentBase();
    }

    public Image getImage(URL url, String path) {
        return this.appMan.getImage(url, path);
    }

    public void appendInterArrivalTimes(int value) {
        this.appMan.instData.customer_collectedtimes.add(value);
    }

    public void start() {
    }

    public void step() {
    }

    public void destroy() {
    }

    @Override
    public void paint(Graphics g) {
    }
}

