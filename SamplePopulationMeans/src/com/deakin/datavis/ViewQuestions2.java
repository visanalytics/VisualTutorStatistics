/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  umontreal.iro.lecuyer.probdist.WeibullDist
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.AppletMan;
import com.deakin.datavis.ControlPanelBar;
import com.deakin.datavis.CustomComboBox;
import com.deakin.datavis.DistributionGraphVis;
import com.deakin.datavis.Graph;
import com.deakin.datavis.InstanceData;
import com.deakin.datavis.Question;
import com.deakin.datavis.StaticClock;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Iterator;
import umontreal.iro.lecuyer.probdist.WeibullDist;

public class ViewQuestions2
extends Activity
implements MouseListener {
    int windowHeight;
    int windowWidth;
    Image offScreen;
    Graphics bufferGraphics;
    Graph histogram;
    CustomComboBox combobox;
    StaticClock clock;
    DistributionGraphVis dgv;
    Question[] questions;
    int questionIndex = 0;
    boolean no_data_collected = false;

    @Override
    public void init(AppletMan m) {
        super.init(m);
        this.windowWidth = this.getSize().width;
        this.windowHeight = this.getSize().height;
        this.setBackground(Color.black);
        this.addMouseListener(this);
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    public void start() {
        InstanceData data = this.appMan.getInstanceData();
        this.dgv = new DistributionGraphVis(this.windowWidth - 260, 260, this.windowWidth, this.windowHeight, 0, 60, 5);
        this.clock = new StaticClock(this, this.dgv);
        if (data.getCollectedCustomerArrivalTimesArray().size() < 30) {
            this.no_data_collected = true;
        } else {
            double sSd;
            double sMedian;
            this.appMan.pb1.title = "";
            ArrayList t = data.customer_collectedtimes;
            double mean = 0.0;
            Iterator iterator = t.iterator();
            while (iterator.hasNext()) {
                int i = (Integer)iterator.next();
                mean += (double)i;
                this.clock.addTimeDifferenceListing(i);
            }
            int x1 = (int)((double)(this.windowWidth - 260) * 0.05);
            int x2 = (int)((double)(this.windowWidth - 260) * 0.8);
            int y1 = (int)((double)this.windowHeight * 0.05);
            int y2 = (int)((double)this.windowHeight * 0.85);
            Color colBack = new Color(55, 96, 146, 255);
            Color colBar = Color.ORANGE;
            this.histogram = new Graph(x1, y1, x2, y2, t, mean /= (double)t.size(), colBar, colBack, false, 0, 60, 5);
            double sMean = this.histogram.getWeibullDist().getMean();
            double skewVal = 3.0 * (sMean - (sMedian = this.histogram.getWeibullDist().inverseF(0.5))) / (sSd = this.histogram.getWeibullDist().getStandardDeviation());
            String q1Ans = skewVal < -0.25 ? "Skewed-" : (skewVal > 0.25 ? "Skewed+" : "Symmetrical");
            Question q1 = new Question("Approximately what shape is the distribution?", new String[]{"Skewed+", "Skewed-", "Symmetrical"}, q1Ans);
            this.questions = new Question[]{q1};
            this.combobox = new CustomComboBox((int)((double)this.windowWidth - 195.0), (int)((double)this.windowHeight * 0.9), this.windowWidth, this.windowHeight, this.questions[this.questionIndex].getAnswers(), this.questions[this.questionIndex].getCorrectAnswer());
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
        Color backCol = new Color(55, 96, 146, 255);
        g.setColor(backCol);
        g.fillRect(0, 0, this.windowWidth, this.windowHeight);
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
            this.histogram.paint(g, 12, true);
            this.clock.paint(g);
            this.drawQuestion(g);
            this.combobox.paint(g);
        }
    }

    private void drawQuestion(Graphics g) {
        Font lastFont = g.getFont();
        StringBuilder sb = new StringBuilder(this.questions[this.questionIndex].getQuestion());
        int i = 0;
        while ((i = sb.indexOf(" ", i + 13)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        String parsStr = sb.toString();
        g.setFont(new Font("Dialog", 0, 24));
        int lineNum = this.getLineNumber(parsStr);
        int pWidth = this.maxLineWidth(g, parsStr);
        g.setColor(Color.WHITE);
        this.drawString(g, parsStr, this.windowWidth - pWidth, (int)((double)this.combobox.y1 - (double)g.getFontMetrics().getHeight() * ((double)lineNum - 0.5)));
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
        if (!this.no_data_collected) {
            this.combobox.step();
        }
    }

    private void nextQuestion() {
        if (this.questionIndex + 1 < this.questions.length) {
            ++this.questionIndex;
            this.combobox.setAnswers(this.questions[this.questionIndex].getAnswers());
            this.combobox.setCorrectAnswer(this.questions[this.questionIndex].getCorrectAnswer());
            this.combobox.resetAnswer();
        }
    }

    private void previousQuestion() {
        if (this.questionIndex - 1 >= 0) {
            --this.questionIndex;
            this.combobox.setAnswers(this.questions[this.questionIndex].getAnswers());
            this.combobox.setCorrectAnswer(this.questions[this.questionIndex].getCorrectAnswer());
            this.combobox.resetAnswer();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        this.combobox.onClick(x, y);
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

