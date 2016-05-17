/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import com.deakin.datavist2.Activity;
import com.deakin.datavist2.AppButton;
import com.deakin.datavist2.AppButtonClickInterface;
import com.deakin.datavist2.AppletMan;
import com.deakin.datavist2.InstanceData;
import com.deakin.datavist2.PieChart;
import com.deakin.datavist2.Slider;
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

public class PieChartsScreen
extends Activity
implements MouseMotionListener,
MouseListener {
    int windowHeight;
    int windowWidth;
    Image offScreen;
    Graphics bufferGraphics;
    AppButton checkAnswersBtn;
    Slider slid1;
    Slider slid2;
    PieChart pChartSample;
    PieChart pChartPopulation;
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
        InstanceData data = this.appMan.getInstanceData();
        if (data.getCollectedCustomerMoodsArray().size() < 30) {
            this.no_data_collected = true;
        } else {
            this.slid1 = new Slider("From: ", this.windowWidth - 260, (int)((double)this.windowHeight * 0.5), this.windowWidth, (int)((double)this.windowHeight * 0.7), 0, 100, 1.0, 50, 20);
            this.slid2 = new Slider("To: ", this.windowWidth - 260, (int)((double)this.windowHeight * 0.7), this.windowWidth, (int)((double)this.windowHeight * 0.9), 0, 100, 1.0, 50, 80);
            ArrayList<String> arm = this.appMan.instData.customer_collected_moods;
            double happyNum = 0.0;
            double unhappyNum = 0.0;
            int i = 0;
            while (i < arm.size()) {
                String m = arm.get(i);
                if (m == "MoodHappy") {
                    happyNum += 1.0;
                }
                if (m == "MoodUnhappy") {
                    unhappyNum += 1.0;
                }
                ++i;
            }
            this.pChartSample = new PieChart((this.windowWidth - 260) / 2, this.windowHeight / 2, (int)((double)this.windowHeight * 0.1), happyNum / (unhappyNum + happyNum), unhappyNum / (unhappyNum + happyNum));
            this.pChartPopulation = new PieChart((this.windowWidth - 260) / 2, this.windowHeight / 2, (int)((double)this.windowHeight * 0.4), happyNum / (unhappyNum + happyNum), unhappyNum / (unhappyNum + happyNum));
            int w = this.windowWidth - 260 - (this.windowWidth - 260) / 8;
            int x = (this.windowWidth - 260) / 8;
            this.checkAnswersBtn = new AppButton("Check Answers", (int)((double)this.windowWidth - 195.0), (int)((double)this.windowHeight * 0.91), (int)((double)this.windowWidth - 65.0), (int)((double)this.windowHeight * 0.99), new Color(208, 202, 162), new AppButtonClickInterface(){

                @Override
                public void onClick() {
                    PieChartsScreen.this.slid1.verifyAnswer();
                    PieChartsScreen.this.slid2.verifyAnswer();
                }
            });
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
        g.fillRect(0, 0, this.windowWidth - 260, this.windowHeight);
        g.setColor(new Color(55, 96, 146, 255));
        g.fillRect(this.windowWidth - 260, 0, 260, this.windowHeight);
        if (this.no_data_collected) {
            g.setColor(new Color(37, 64, 97, 255));
            g.fillRect(0, 0, this.windowWidth, this.windowHeight);
            g.setColor(new Color(0, 0, 0, 60));
            g.fillRect(0, 0, this.windowWidth, this.windowHeight);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Dialog", 1, 48));
            String s = "Not enough data...Go back and collect more";
            g.drawString(s, this.windowWidth / 2 - g.getFontMetrics().stringWidth(s) / 2, this.windowHeight / 2 - g.getFontMetrics().getHeight() / 2);
        } else {
            this.slid1.paint(g);
            this.slid2.paint(g);
            this.pChartPopulation.paint(g, false);
            this.pChartSample.paint(g, true);
            this.pChartPopulation.paintLines(g);
            this.checkAnswersBtn.paint(g, Color.WHITE);
            this.paintQuestion(g);
        }
    }

    private void paintQuestion(Graphics g) {
        Font newFont = new Font("Dialog", 1, 18);
        Font lastFont = g.getFont();
        g.setFont(newFont);
        String quest = "Estimate the proportion of all unhappy customers who visit the store";
        StringBuilder sb = new StringBuilder(quest);
        int i = 0;
        while ((i = sb.indexOf(" ", i + 20)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        String parsStr = sb.toString();
        int lineNum = this.getLineNumber(parsStr);
        int pWidth = this.maxLineWidth(g, parsStr);
        this.drawString(g, parsStr, this.slid1.x1 + 10, this.slid1.y1 - g.getFontMetrics().getHeight() * (lineNum + 1));
        g.setFont(lastFont);
    }

    private void drawString(Graphics g, String text, int x, int y) {
        String[] arrstring = text.split("\n");
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String line = arrstring[n2];
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
            ++n2;
        }
    }

    private int getLineNumber(String text) {
        int lines = 1;
        String[] arrstring = text.split("\n");
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String line = arrstring[n2];
            ++lines;
            ++n2;
        }
        return lines;
    }

    private int maxLineWidth(Graphics g, String text) {
        int mWidth = 0;
        String[] arrstring = text.split("\n");
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String line = arrstring[n2];
            int temp = g.getFontMetrics().stringWidth(line);
            if (temp > mWidth) {
                mWidth = temp;
            }
            ++n2;
        }
        return mWidth;
    }

    @Override
    public void step() {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!this.no_data_collected) {
            double slid1Val = this.slid1.onMouseDragged(e) / 100.0;
            double slid2Val = this.slid2.onMouseDragged(e) / 100.0;
            this.slid1.current = (int)(100.0 - slid2Val * 100.0);
            this.slid2.current = (int)(100.0 - slid1Val * 100.0);
            this.slid1.abox.currentAnswer = (int)(100.0 - slid2Val * 100.0);
            this.slid2.abox.currentAnswer = (int)(100.0 - slid1Val * 100.0);
            this.pChartPopulation.setVal1(slid1Val);
            this.pChartPopulation.setVal2(slid2Val);
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

