/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.AppButton;
import com.deakin.datavis.AppButtonClickInterface;
import com.deakin.datavis.AppletMan;
import com.deakin.datavis.ControlPanelBar;
import com.deakin.datavis.DistributionGraphVis;
import com.deakin.datavis.InstanceData;
import com.deakin.datavis.Slider;
import com.deakin.datavis.StaticClock;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class ViewMainQuestions
extends Activity
implements MouseMotionListener,
MouseListener {
    int windowHeight;
    int windowWidth;
    Image offScreen;
    Graphics bufferGraphics;
    Slider slid1;
    Slider slid2;
    Slider slid3;
    StaticClock clock;
    DistributionGraphVis dgv;
    AppButton checkAnswersBtn;
    boolean no_data_collected = false;

    @Override
    public void init(AppletMan m) {
        super.init(m);
        this.windowWidth = this.getSize().width;
        this.windowHeight = this.getSize().height;
        this.setBackground(Color.black);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    public void start() {
        int mean = 0;
        int min = 0;
        int max = 0;
        InstanceData data = this.appMan.getInstanceData();
        if (data.getCollectedCustomerArrivalTimesArray().size() < 30) {
            this.no_data_collected = true;
        } else {
            this.appMan.pb1.title = "Based on your sample of arrival times, what is the:";
            int i = 0;
            while (i < data.getCollectedCustomerArrivalTimesArray().size()) {
                int dataPoint = (Integer)data.getCollectedCustomerArrivalTimesArray().get(i);
                mean += dataPoint;
                if (min == 0) {
                    min = dataPoint;
                } else if (dataPoint < min) {
                    min = dataPoint;
                }
                if (max == 0) {
                    max = dataPoint;
                } else if (dataPoint > max) {
                    max = dataPoint;
                }
                ++i;
            }
            mean = Math.round(mean / data.getCollectedCustomerArrivalTimesArray().size());
            this.slid1 = new Slider("Shortest: ", 0, 0, this.windowWidth - 260, this.windowHeight / 4, 0, 59, 1.0, 30, min);
            this.slid2 = new Slider("Longest: ", 0, this.windowHeight / 4, this.windowWidth - 260, this.windowHeight / 4 * 2, 0, 59, 1.0, 30, max);
            this.slid3 = new Slider("Average: ", 0, this.windowHeight / 4 * 2, this.windowWidth - 260, this.windowHeight / 4 * 3, 0, 59, 1.0, 30, mean);
            int w = this.windowWidth - 260 - (this.windowWidth - 260) / 8;
            int x = (this.windowWidth - 260) / 8;
            this.checkAnswersBtn = new AppButton("Check Answers", x + w / 2 - this.windowWidth / 10, (int)((double)this.windowHeight * 0.875 - (double)(this.windowHeight / 16)), x + w / 2 + this.windowWidth / 10, (int)((double)this.windowHeight * 0.875 + (double)(this.windowHeight / 16)), new Color(208, 202, 162), new AppButtonClickInterface(){

                @Override
                public void onClick() {
                    ViewMainQuestions.this.slid1.verifyAnswer();
                    ViewMainQuestions.this.slid2.verifyAnswer();
                    ViewMainQuestions.this.slid3.verifyAnswer();
                }
            });
            this.dgv = new DistributionGraphVis(this.windowWidth - 260, 260, this.windowWidth, this.windowHeight, 0, 60, 5);
            this.clock = new StaticClock(this, this.dgv);
            ArrayList t = this.appMan.instData.customer_collectedtimes;
            int i2 = 0;
            while (i2 < t.size()) {
                int time = (Integer)t.get(i2);
                this.clock.addTimeDifferenceListing(time);
                ++i2;
            }
        }
    }

    public void stop() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void update(Graphics g) {
        if (this.offScreen == null) {
            this.offScreen = this.createImage(this.getSize().width, this.getSize().height);
            this.bufferGraphics = this.offScreen.getGraphics();
        }
        this.bufferGraphics.setColor(this.getBackground());
        this.bufferGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
        this.bufferGraphics.setColor(this.getForeground());
        this.paint(this.bufferGraphics);
        g.drawImage(this.offScreen, 0, 0, this);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(37, 64, 97, 255));
        g.fillRect(0, this.windowHeight / 4 * 3, (this.windowWidth - 260) / 8, this.windowHeight / 4);
        g.setColor(new Color(55, 96, 146, 255));
        g.fillRect((this.windowWidth - 260) / 8, this.windowHeight / 4 * 3, this.windowWidth - 260 - (this.windowWidth - 260) / 8, this.windowHeight / 4);
        if (this.no_data_collected) {
            g.setColor(new Color(37, 64, 97, 255));
            g.fillRect(0, 0, this.windowWidth, this.windowHeight);
            g.setColor(new Color(0, 0, 0, 60));
            g.fillRect(0, 0, this.windowWidth, this.windowHeight);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Dialog", 1, 48));
            String s = "No Data Collected...Go Back";
            g.drawString(s, this.windowWidth / 2 - g.getFontMetrics().stringWidth(s) / 2, this.windowHeight / 2 - g.getFontMetrics().getHeight() / 2);
        } else {
            this.slid1.paint(g);
            this.slid2.paint(g);
            this.slid3.paint(g);
            this.clock.paint(g);
            this.dgv.paint(g);
            this.checkAnswersBtn.paint(g, Color.BLACK);
        }
    }

    private void paintCheckAnswers(Graphics g) {
        g.setColor(new Color(37, 64, 97, 255));
        g.fillRect(0, this.windowHeight / 4 * 3, (this.windowWidth - 260) / 8, this.windowHeight / 4);
        g.setColor(new Color(55, 96, 146, 255));
        g.fillRect((this.windowWidth - 260) / 8, this.windowHeight / 4 * 3, this.windowWidth - 260 - (this.windowWidth - 260) / 8, this.windowHeight / 4);
        int w = this.windowWidth - 260 - (this.windowWidth - 260) / 8;
        int x = (this.windowWidth - 260) / 8;
        String s = "Check Answers";
        g.setColor(Color.WHITE);
        g.drawString(s, x + w / 2 - g.getFontMetrics().stringWidth(s) / 2, this.windowHeight / 4 * 3 + this.windowHeight / 4 / 2 - g.getFontMetrics().getHeight());
    }

    @Override
    public void step() {
        if (!this.no_data_collected) {
            this.dgv.update();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!this.no_data_collected) {
            this.slid1.onMouseDragged(e, this.clock);
            this.slid2.onMouseDragged(e, this.clock);
            this.slid3.onMouseDragged(e, this.clock);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int y;
        int x = e.getX();
        if (this.checkAnswersBtn.contains(x, y = e.getY())) {
            this.checkAnswersBtn.click();
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

}

