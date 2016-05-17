/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.DistributionGraphVis;
import com.deakin.datavis.World;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;

public class Clock {
    public static final String TYPE_STOPWATCH = "ClockTypeStopwatch";
    public static final String TYPE_CLOCK = "ClockTypeClock";
    public static final String TYPE_COUNTER = "ClockTypeCounter";
    String clock_type = "ClockTypeStopwatch";
    double lastTimeSec = 0.0;
    int incDelay = 0;
    int max_inc_delay = 25;
    int timeSec = 0;
    double lastSpawnTime = 0.0;
    Activity main;
    World world;
    DistributionGraphVis dgv;
    int winWidth;
    int winHeight;
    public static final int RADIUS = 130;
    int clockDrawOffset = 5;
    URL url;
    Image backImage;
    int imageWidth;
    int imageHeight;
    WatchButton speed1button;
    WatchButton speed3button;
    WatchButton listButton;

    public Clock(World world, Activity main, DistributionGraphVis dgv) {
        this.world = world;
        this.main = main;
        this.dgv = dgv;
        this.winWidth = main.windowWidth;
        this.winHeight = main.windowHeight;
        this.speed1button = new WatchButton(Color.LIGHT_GRAY, Color.GRAY, "SLOW", this.winWidth - 260, 260, this.winWidth - 260 + 86, 292);
        this.speed1button.setListener(new WatchButtonListener(){

            @Override
            public void onClick() {
                Clock.this.changeWorldTime(200.0);
            }
        });
        this.speed1button.setPressed(true);
        this.speed3button = new WatchButton(Color.LIGHT_GRAY, Color.GRAY, "FAST", this.winWidth - 86, 260, this.winWidth, 292);
        this.speed3button.setListener(new WatchButtonListener(){

            @Override
            public void onClick() {
                Clock.this.changeWorldTime(100.0);
            }
        });
        this.listButton = new WatchButton(Color.RED, Color.MAGENTA, "", this.winWidth - 32 - 5, 223, this.winWidth - 5, 255);
        this.listButton.setListener(new WatchButtonListener(){

            @Override
            public void onClick() {
                Clock.this.addInfo();
            }
        });
        this.url = main.getDocumentBase();
        this.backImage = main.getImage(this.url, "res/images/stop_watch_face.png");
        this.imageWidth = this.backImage.getWidth(main);
        this.imageHeight = this.backImage.getHeight(main);
        System.out.println(this.url);
    }

    public void incTimeSpeed(int value) {
        this.max_inc_delay += value;
    }

    public void changeWorldTime(double value) {
        this.world.setTimeRatio(value);
    }

    public void addTimeListing() {
        int hours = this.world.getTimeInHours();
        String h = hours < 10 ? "0" + Integer.toString(hours) : Integer.toString(hours);
        int mins = this.world.getTimeInMinutes();
        String m = mins < 10 ? "0" + Integer.toString(mins) : Integer.toString(mins);
        int secs = this.world.getTimeInSecs();
        String s = secs < 10 ? "0" + Integer.toString(secs) : Integer.toString(secs);
        String listing = String.valueOf(h) + ":" + m + ":" + s;
    }

    public void addTimeDifferenceListing() {
        int dif = (int)(this.world.getTime() - this.lastTimeSec);
        String listing = Integer.toString(dif);
        this.lastTimeSec = this.world.getTime();
        this.dgv.addData(dif);
        this.main.appendInterArrivalTimes(dif);
    }

    public void addTimeDifListing(int time) {
        this.dgv.addData(time);
    }

    public void addInfo() {
        if (this.clock_type.equals("ClockTypeStopwatch")) {
            this.addTimeDifferenceListing();
        }
        if (this.clock_type.equals("ClockTypeClock")) {
            this.addTimeListing();
        }
    }

    public void setTime(double time) {
        this.lastTimeSec = time;
        this.timeSec = (int)time;
    }

    public void update() {
        if (this.clock_type.equals("ClockTypeClock")) {
            this.timeSec = (int)this.world.getTime();
        }
        if (this.clock_type.equals("ClockTypeStopwatch")) {
            this.timeSec = (int)(this.world.getTime() - this.lastTimeSec);
        }
    }

    public void paint(Graphics g) {
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
        this.speed1button.paint(g);
        this.speed3button.paint(g);
        this.listButton.paint(g);
    }

    private void drawStopwatchData(Graphics g) {
        int i = 0;
        while (i < this.dgv.times.size()) {
            int dt = (Integer)this.dgv.times.get(i);
            int x1 = this.winWidth - 130;
            int y1 = 130;
            int x2 = (int)((double)x1 - 65.0 * Math.sin(-0.10471975511965977 * (double)dt));
            int y2 = (int)((double)y1 - 65.0 * Math.cos(-0.10471975511965977 * (double)dt));
            Color tgrey = new Color(60, 60, 60, 30);
            g.setColor(tgrey);
            g.drawLine(x1, y1, x2, y2);
            ++i;
        }
    }

    public void setLastSpawnTime(double value) {
        this.lastSpawnTime = value;
    }

    public double getLastSpawnTime() {
        return this.lastSpawnTime;
    }

    public void onClick(MouseEvent e) {
        int ey;
        int ex = e.getX();
        if (this.speed1button.contains(ex, ey = e.getY())) {
            this.speed1button.click();
            this.speed1button.setPressed(true);
            this.speed3button.setPressed(false);
        }
        if (this.speed3button.contains(ex, ey)) {
            this.speed3button.click();
            this.speed1button.setPressed(false);
            this.speed3button.setPressed(true);
        }
        if (this.listButton.contains(ex, ey)) {
            this.listButton.click();
        }
    }

    protected class WatchButton {
        int x1;
        int x2;
        int y1;
        int y2;
        boolean pressed;
        Color col_press;
        Color col_nopress;
        String speedString;
        WatchButtonListener listener;

        public WatchButton(Color col_nopress, Color col_press, String speedString, int x1, int y1, int x2, int y2) {
            this.col_press = col_press;
            this.col_nopress = col_nopress;
            this.speedString = speedString;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public boolean contains(int x, int y) {
            if (x > this.x1 && x < this.x2 && y > this.y1 && y < this.y2) {
                return true;
            }
            return false;
        }

        public void setPressed(boolean value) {
            this.pressed = value;
        }

        public void paint(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
            if (this.pressed) {
                g.setColor(this.col_press);
            } else {
                g.setColor(this.col_nopress);
            }
            g.fillRect(this.x1 + 2, this.y1 + 2, this.x2 - this.x1 - 4, this.y2 - this.y1 - 4);
            g.setColor(Color.BLACK);
            g.drawString(this.speedString, this.x1 + (this.x2 - this.x1) / 3, (int)((double)this.y1 + (double)(this.y2 - this.y1) * 0.6));
        }

        public void setListener(WatchButtonListener listener) {
            this.listener = listener;
        }

        public void click() {
            this.listener.onClick();
        }
    }

    protected static interface WatchButtonListener {
        public void onClick();
    }

}

