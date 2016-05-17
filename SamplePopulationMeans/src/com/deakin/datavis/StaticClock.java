/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.DistributionGraphVis;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;

public class StaticClock {
    int timeSec = 0;
    Activity main;
    DistributionGraphVis dgv;
    int winWidth;
    int winHeight;
    public static final int RADIUS = 130;
    int clockDrawOffset = 5;
    URL url;
    Image backImage;
    int imageWidth;
    int imageHeight;

    public StaticClock(Activity main, DistributionGraphVis dgv) {
        this.main = main;
        this.dgv = dgv;
        this.winWidth = main.windowWidth;
        this.winHeight = main.windowHeight;
        try {
            this.url = main.getDocumentBase();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.backImage = main.getImage(this.url, "res/images/stop_watch_face.png");
        this.imageWidth = this.backImage.getWidth(main);
        this.imageHeight = this.backImage.getHeight(main);
        System.out.println(this.backImage + "\t" + this.url);
    }

    public void addTimeDifferenceListing(int value) {
        this.dgv.addData(value);
    }

    public void update() {
    }

    public void paint(Graphics g) {
        this.drawPane(g);
        Graphics2D g2 = (Graphics2D)g;
        g.setColor(Color.WHITE);
        g.drawImage(this.backImage, this.winWidth - 260 + this.clockDrawOffset, this.clockDrawOffset, 260 - this.clockDrawOffset * 2, 260 - this.clockDrawOffset * 2, this.main);
        this.drawStopwatchData(g);
        int x1 = this.winWidth - 130;
        int y1 = 130;
        int x2 = (int)((double)x1 - 104.0 * Math.sin(-0.10471975511965977 * (double)this.timeSec));
        int y2 = (int)((double)y1 - 104.0 * Math.cos(-0.10471975511965977 * (double)this.timeSec));
        g2.setColor(Color.RED);
        g2.fillOval(x1 - 10, y1 - 10, 20, 20);
        g2.setStroke(new BasicStroke(3.0f));
        g2.drawLine(x1, y1, x2, y2);
    }

    private void drawPane(Graphics g) {
        Color c = new Color(37, 63, 96, 255);
        g.setColor(c);
        g.fillRect(this.main.windowWidth - 260, 0, 260, this.main.windowHeight);
    }

    private void drawStopwatchData(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(3.0f));
        int i = 0;
        while (i < this.dgv.times.size()) {
            int dt = (Integer)this.dgv.times.get(i);
            int x1 = this.winWidth - 130;
            int y1 = 130;
            int x2 = (int)((double)x1 - 65.0 * Math.sin(-0.10471975511965977 * (double)dt));
            int y2 = (int)((double)y1 - 65.0 * Math.cos(-0.10471975511965977 * (double)dt));
            Color tgrey = new Color(60, 60, 60, 30);
            g2.setColor(tgrey);
            g2.drawLine(x1, y1, x2, y2);
            ++i;
        }
    }
}

