/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  umontreal.iro.lecuyer.probdist.NormalDist
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.AppButton;
import com.deakin.datavis.AppButtonClickInterface;
import com.deakin.datavis.AppletMan;
import com.deakin.datavis.CustomComboBox;
import com.deakin.datavis.Graph;
import com.deakin.datavis.InstanceData;
import com.deakin.datavis.NormalGraph;
import com.deakin.datavis.Question;
import com.deakin.datavis.QuestionTextBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import umontreal.iro.lecuyer.probdist.NormalDist;

public class Screen5
extends Activity
implements KeyListener,
MouseListener {
    int windowHeight;
    int windowWidth;
    Image offScreen;
    Graphics bufferGraphics;
    ArrayList graphs = new ArrayList();
    NormalGraph samples_hist;
    AppButton checkAnswersBtn;
    QuestionTextBox q1;
    Question q1q;
    QuestionTextBox q2;
    Question q2q;
    AtomicReference<QuestionTextBox> currentQBox;
    QuestionTextBox[] qBoxes;
    Question q3q;
    CustomComboBox q3box;

    @Override
    public void init(AppletMan m) {
        super.init(m);
        this.windowWidth = this.getSize().width;
        this.windowHeight = this.getSize().height;
        this.setBackground(Color.black);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    public void start() {
        int rSeed = 123456789;
        Random random = new Random();
        ArrayList timesIA = new ArrayList();
        double size = 10.0;
        InstanceData data = this.appMan.getInstanceData();
        ArrayList usertimes = data.getCollectedCustomerArrivalTimesArray();
        ArrayList<ArrayList<Integer>> samples_times = data.getSamplesList();
        ArrayList<Integer> samples_means = data.getSamplesMeans();
        int mean = 0;
        int min = 9999;
        int max = 0;
        Iterator<Integer> iterator = samples_means.iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            mean += i;
            if (i < min) {
                min = i;
            }
            if (i <= max) continue;
            max = i;
        }
        this.samples_hist = new NormalGraph(0, 0, (int)((double)this.windowWidth * 0.45), this.windowHeight, samples_means, mean /= samples_means.size(), Color.YELLOW, Color.WHITE, true, 23, 36, 1);
        String question1S = "What is the lowest sample mean of inter-arrival time we would expect to get?";
        StringBuilder sb = new StringBuilder(question1S);
        int i = 0;
        while ((i = sb.indexOf(" ", i + 50)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        question1S = sb.toString();
        String question2S = "What is the highest sample mean we would expect to get?";
        sb = new StringBuilder(question2S);
        i = 0;
        while ((i = sb.indexOf(" ", i + 50)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        question2S = sb.toString();
        String question3S = "68% of all inter arrival times are approximately between 27 and 30.5 seconds.";
        sb = new StringBuilder(question3S);
        i = 0;
        while ((i = sb.indexOf(" ", i + 50)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        question3S = sb.toString();
        double q1Ans = this.samples_hist.getNormalDist().getMean() - 3.0 * this.samples_hist.getNormalDist().getStandardDeviation();
        this.q1q = new Question(question1S, new String[0], String.valueOf(q1Ans));
        this.q1 = new QuestionTextBox((int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.2), (int)((double)this.windowWidth * 0.075), (int)((double)this.windowHeight * 0.075), 9999.0, q1Ans);
        double q2Ans = this.samples_hist.getNormalDist().getMean() + 3.0 * this.samples_hist.getNormalDist().getStandardDeviation();
        this.q2q = new Question(question2S, new String[0], String.valueOf(q2Ans));
        this.q2 = new QuestionTextBox((int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.4), (int)((double)this.windowWidth * 0.075), (int)((double)this.windowHeight * 0.075), 9999.0, q2Ans);
        this.qBoxes = new QuestionTextBox[]{this.q1, this.q2};
        this.currentQBox = new AtomicReference<QuestionTextBox>(this.q1);
        this.currentQBox.get().selected = true;
        this.q3q = new Question(question3S, new String[0], String.valueOf(true));
        this.q3box = new CustomComboBox((int)((double)this.windowWidth * 0.8), (int)((double)this.windowHeight * 0.65), (int)((double)this.windowWidth * 0.95), (int)((double)this.windowHeight * 0.725), new String[]{"True", "False"}, "True");
        this.checkAnswersBtn = new AppButton("Check Answers", (int)((double)this.windowWidth * 0.68), (int)((double)this.windowHeight * 0.85), (int)((double)this.windowWidth * 0.82), (int)((double)this.windowHeight * 0.95), new Color(208, 202, 162), new AppButtonClickInterface(){

            @Override
            public void onClick() {
                QuestionTextBox[] arrquestionTextBox = Screen5.this.qBoxes;
                int n = arrquestionTextBox.length;
                int n2 = 0;
                while (n2 < n) {
                    QuestionTextBox qtb = arrquestionTextBox[n2];
                    qtb.verifyAnswer();
                    ++n2;
                }
            }
        });
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
        this.samples_hist.paint(g, 30, "Sampling distribution");
        this.paintQuestion(g);
        this.checkAnswersBtn.paint(g, Color.BLACK);
    }

    private void paintQuestion(Graphics g) {
        String q1S = this.q1q.getQuestion();
        g.setColor(Color.WHITE);
        this.drawString(g, q1S, (int)((double)this.windowWidth * 0.5) + 2, this.q1.y - this.q1.height / 2 - g.getFontMetrics().getHeight() * this.getLineNumber(q1S) / 2);
        this.q1.paint(g);
        g.setColor(Color.WHITE);
        g.drawString("seconds", this.q1.x + this.q1.width / 2 + 2, this.q1.y);
        String q2S = this.q2q.getQuestion();
        g.setColor(Color.WHITE);
        this.drawString(g, q2S, (int)((double)this.windowWidth * 0.5) + 2, this.q2.y - this.q2.height / 2 - g.getFontMetrics().getHeight() * this.getLineNumber(q2S) / 2);
        this.q2.paint(g);
        g.setColor(Color.WHITE);
        g.drawString("seconds", this.q2.x + this.q2.width / 2 + 2, this.q2.y);
        String q3S = this.q3q.getQuestion();
        g.setColor(Color.WHITE);
        this.drawString(g, q3S, (int)((double)this.windowWidth * 0.5) + 2, this.q3box.y1 - g.getFontMetrics().getHeight() * this.getLineNumber(q3S) / 2);
        this.q3box.paint(g);
        g.setColor(Color.WHITE);
        g.drawString("seconds", this.q2.x + this.q2.width / 2 + 2, this.q2.y);
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
    public void keyPressed(KeyEvent e) {
        System.out.println(String.valueOf(e.getKeyCode()) + "  +  " + 8);
        if (e.getKeyCode() == 8) {
            this.currentQBox.get().backspace();
        }
        switch (e.getKeyChar()) {
            case '.': {
                this.currentQBox.get().append('.');
                break;
            }
            case '0': {
                this.currentQBox.get().append('0');
                break;
            }
            case '1': {
                this.currentQBox.get().append('1');
                break;
            }
            case '2': {
                this.currentQBox.get().append('2');
                break;
            }
            case '3': {
                this.currentQBox.get().append('3');
                break;
            }
            case '4': {
                this.currentQBox.get().append('4');
                break;
            }
            case '5': {
                this.currentQBox.get().append('5');
                break;
            }
            case '6': {
                this.currentQBox.get().append('6');
                break;
            }
            case '7': {
                this.currentQBox.get().append('7');
                break;
            }
            case '8': {
                this.currentQBox.get().append('8');
                break;
            }
            case '9': {
                this.currentQBox.get().append('9');
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        QuestionTextBox[] arrquestionTextBox = this.qBoxes;
        int n = arrquestionTextBox.length;
        int n2 = 0;
        while (n2 < n) {
            QuestionTextBox q = arrquestionTextBox[n2];
            if (q.contains(x, y)) {
                this.currentQBox.get().selected = false;
                q.selected = true;
                System.out.println("Sleected");
                this.currentQBox.set(q);
            } else {
                q.selected = false;
                System.out.println("not select");
            }
            ++n2;
        }
        this.q3box.onClick(x, y);
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int y;
        int x = e.getX();
        if (this.checkAnswersBtn.contains(x, y = e.getY())) {
            this.checkAnswersBtn.click();
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    private class GraphExt
    extends Graph {
        public GraphExt(int drawX1, int drawY1, int drawX2, int drawY2, ArrayList<Integer> timesIA, int mean, Color cFront, Color cBack, boolean show_curve, int min, int max, int distance) {
            super(drawX1, drawY1, drawX2, drawY2, timesIA, mean, cFront, cBack, show_curve, min, max, distance);
        }

        @Override
        public void paint(Graphics g, int rowNum) {
            super.paint(g, rowNum);
            g.setColor(this.cBack);
            g.fillRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
            int columnNum = (this.max - this.min) / this.distance;
            int w = (this.x2 - this.x1 - this.axisborder_size) / columnNum;
            int h = (this.y2 - this.axisborder_size - this.y1) / rowNum;
            g.setColor(Color.BLACK);
            g.drawLine(this.x1 + this.axisborder_size, this.y2 - this.axisborder_size - h * rowNum, this.x1 + this.axisborder_size, this.y2 - this.axisborder_size);
            g.drawLine(this.x1 + this.axisborder_size, this.y2 - this.axisborder_size, this.x1 + this.axisborder_size + w * columnNum, this.y2 - this.axisborder_size);
            int i = 0;
            while (i <= columnNum) {
                int xb = this.x1 + this.axisborder_size + w * i;
                g.drawLine(xb, this.y2 - this.axisborder_size / 2, xb, this.y2 - this.axisborder_size);
                ++i;
            }
            i = rowNum;
            while (i >= 0) {
                int yl = this.y2 - this.axisborder_size - h * i;
                g.drawLine(this.x1 + this.axisborder_size / 2, yl, this.x1 + this.axisborder_size, yl);
                --i;
            }
        }
    }

}

