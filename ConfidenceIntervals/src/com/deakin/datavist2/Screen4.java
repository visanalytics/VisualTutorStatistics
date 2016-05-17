/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import com.deakin.datavist2.Activity;
import com.deakin.datavist2.AppButton;
import com.deakin.datavist2.AppButtonClickInterface;
import com.deakin.datavist2.AppletMan;
import com.deakin.datavist2.ControlPanelBar;
import com.deakin.datavist2.CurveDistributionInteractive;
import com.deakin.datavist2.InstanceData;
import com.deakin.datavist2.Question;
import com.deakin.datavist2.QuestionTextBox;
import com.deakin.datavist2.RadioBoxList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class Screen4
extends Activity
implements KeyListener,
MouseListener,
MouseMotionListener {
    int windowHeight;
    int windowWidth;
    Image offScreen;
    Graphics bufferGraphics;
    AppButton checkAnswersBtn;
    Question q1q;
    RadioBoxList q1boxes;
    AtomicReference<QuestionTextBox> currentQBox;
    QuestionTextBox[] qBoxes;
    CurveDistributionInteractive cdi;
    CDISlider point1Slid;
    CDISlider point2Slid;

    @Override
    public void init(AppletMan m) {
        super.init(m);
        this.windowWidth = this.getSize().width;
        this.windowHeight = this.getSize().height;
        this.setBackground(Color.black);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    public void start() {
        this.appMan.pb1.title = "Confidence Interval and accuracy of the estimate";
        int rSeed = 123456789;
        Random random = new Random();
        ArrayList timesIA = new ArrayList();
        double size = 10.0;
        InstanceData data = this.appMan.getInstanceData();
        this.point1Slid = new CDISlider("Confidence:", 0, (int)((double)this.windowHeight * 0.775), (int)((double)this.windowWidth * 0.6), (int)((double)this.windowHeight * 0.925), 90.0, 99.9, 0.1, 5, 1);
        this.cdi = new CurveDistributionInteractive(0, 0, (int)((double)this.windowWidth * 0.6), (int)((double)this.windowHeight * 0.7), (this.point1Slid.min + this.point1Slid.current) / 100.0, 30.0, this.appMan.getInstanceData().population_unhappytohappy_ratio);
        String question1S = "If you increase the confidence level from 90% to 99%, the estimate would be :";
        this.q1q = new Question(question1S, new String[]{"No Change", "More Precise", "Less Precise"}, "Less Precise");
        this.q1boxes = new RadioBoxList((int)((double)this.windowWidth * 0.61), (int)((double)this.windowHeight * 0.2), (int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.5), "Vertical", this.q1q.getQuestion(), this.q1q.getAnswers(), this.q1q.getCorrectAnswer());
        this.checkAnswersBtn = new AppButton("Check Answers", (int)((double)this.windowWidth * 0.68), (int)((double)this.windowHeight * 0.85), (int)((double)this.windowWidth * 0.82), (int)((double)this.windowHeight * 0.95), new Color(208, 202, 162), new AppButtonClickInterface(){

            @Override
            public void onClick() {
                Screen4.this.q1boxes.checkAnswers();
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
        this.paintQuestion(g);
        this.checkAnswersBtn.paint(g, Color.BLACK);
        this.point1Slid.paint(g);
        this.cdi.paint(g, 30);
    }

    private void paintQuestion(Graphics g) {
        this.q1boxes.paint(g);
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
        this.point1Slid.step();
        this.cdi.step();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(String.valueOf(e.getKeyCode()) + "  +  " + 8);
        if (e.getKeyCode() == 8) {
            this.currentQBox.get().backspace();
        }
        switch (e.getKeyChar()) {
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
                break;
            }
            case '.': {
                this.currentQBox.get().append('.');
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
        this.q1boxes.onMouseClick(e);
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

    @Override
    public void mouseDragged(MouseEvent e) {
        this.cdi.setConfidence(this.point1Slid.onMouseDragged(e) / 100.0);
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    private class CDISlider {
        int x1;
        int y1;
        int x2;
        int y2;
        double min;
        double max;
        double current;
        double distance;
        int offset;
        int circle_radius;
        String title;
        AnswerBox abox;

        public CDISlider(String title, int x1, int y1, int x2, int y2, double min, double max, double distance, int start, int answer) {
            this.offset = 2;
            this.circle_radius = 15;
            this.title = title;
            int bLength = (int)((double)((x2 - x1) / 8 * 6) / (max - min) * (max - min));
            int n = x1 + (x2 - x1) / 8;
            int x = n + (x2 - n - bLength) / 2;
            this.abox = new AnswerBox(x2 - x / 2, y1, x2, y2, start, answer);
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.min = min;
            this.max = max;
            this.distance = distance;
            this.current = start;
            this.circle_radius = 15;
        }

        public void paint(Graphics g) {
            String label = "Select the confidence level";
            Font lFont = g.getFont();
            g.setFont(new Font("Dialog", 0, 18));
            g.setColor(Color.WHITE);
            g.drawString(label, this.x1 + 2, this.y1);
            g.setFont(lFont);
            Color back2 = new Color(55, 96, 146, 255);
            g.setColor(back2);
            g.fillRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
            Color boxC = new Color(92, 146, 211, 255);
            g.setColor(boxC);
            double bLength = (double)((int)((double)(this.x2 - this.x1) * 0.75)) / (this.max - this.min) * (this.max - this.min);
            int n = this.x1;
            int n2 = this.x1 + (this.x2 - this.x1) / 8;
            int x = (int)((double)n + ((double)(this.x2 - n2) - bLength) / 2.0);
            g.fillRect(x, this.y1 + (this.y2 - this.y1) / 2 - this.offset, (int)bLength, this.offset * 2);
            String str1 = String.format("%.1f", this.min);
            String str2 = String.format("%.1f", this.max);
            g.setFont(new Font("Dialog", 0, 14));
            g.setColor(Color.WHITE);
            g.drawString(str1, x - g.getFontMetrics().getHeight() / 2, this.y1 + (this.y2 - this.y1) / 2 + this.offset + g.getFontMetrics().getHeight());
            g.drawString(str2, (int)((double)x + bLength - (double)(g.getFontMetrics().getHeight() / 2)), this.y1 + (this.y2 - this.y1) / 2 + this.offset + g.getFontMetrics().getHeight());
            g.setFont(lFont);
            Color slidC = Color.WHITE;
            g.setColor(slidC);
            int dist = (int)((double)((this.x2 - this.x1) / 8 * 6) / (this.max - this.min) * this.current);
            g.fillOval(x + dist - this.circle_radius / 3, this.y1 + (this.y2 - this.y1) / 2 - this.circle_radius, (int)((double)this.circle_radius / 1.5), this.circle_radius * 2);
            this.abox.setCurrentAnswer(this.min + this.current);
        }

        public void step() {
        }

        public double onMouseDragged(MouseEvent e) {
            double x = e.getX();
            double y = e.getY();
            double bLength = (double)((int)((double)(this.x2 - this.x1) * 0.75)) / (this.max - this.min) * (this.max - this.min);
            double n = this.x1;
            double n2 = this.x1 + (this.x2 - this.x1) / 8;
            double m = (int)(n + ((double)this.x2 - n2 - bLength) / 2.0);
            double pdist = bLength / ((this.max - this.min) / this.distance);
            if (y > (double)this.y1 && y < (double)this.y2) {
                this.current = x <= m ? 0.0 : (x > m + bLength ? this.max - this.min : (this.roundUp(x * 10.0, pdist * 10.0) - m * 10.0) / 100.0 / pdist);
                this.abox.setCurrentAnswer(this.min + this.current);
            }
            return this.min + this.current;
        }

        double roundUp(double x, double f) {
            return (int)(f * Math.floor(x / f));
        }

        double round(double num, double closestto) {
            double temp = num % closestto;
            if (temp < closestto / 2.0) {
                return num - temp;
            }
            return num + closestto - temp;
        }

        public void verifyAnswer() {
            this.abox.verifyAnswer();
        }

        private class AnswerBox {
            int x1;
            int y1;
            int x2;
            int y2;
            double currentAnswer;
            int finalAnswer;
            private static final int STATE_UNVERIFIED = 0;
            private static final int STATE_VERIFIED = 1;
            int state;
            boolean correct;

            public AnswerBox(int x1, int y1, int x2, int y2, int startingAnswer, int finalAnswer) {
                this.correct = false;
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
                this.finalAnswer = finalAnswer;
                this.currentAnswer = startingAnswer;
                this.state = 0;
            }

            public void paint(Graphics g) {
                Color c;
                g.setColor(new Color(37, 63, 96));
                int radius = 15;
                g.fillRect(this.x1 + (this.x2 - this.x1) / 2 - radius * 2, this.y1 + (this.y2 - this.y1) / 2 - radius, radius * 4, radius * 2);
                int disp = 3;
                if (this.state == 1) {
                    c = this.currentAnswer == (double)this.finalAnswer ? Color.GREEN : (Math.abs((double)this.finalAnswer - this.currentAnswer) < 3.0 ? Color.ORANGE : Color.RED);
                    g.setColor(c);
                } else {
                    c = new Color(78, 128, 189, 255);
                }
                g.setColor(c);
                g.fillRect(this.x1 + (this.x2 - this.x1) / 2 - radius * 2 + disp, this.y1 + (this.y2 - this.y1) / 2 - radius + disp, radius * 4 - disp * 2, radius * 2 - disp * 2);
                if (this.currentAnswer == (double)this.finalAnswer && this.state == 1) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                String s = String.format("%.1f", this.currentAnswer);
                g.drawString(s, this.x1 + (this.x2 - this.x1) / 2 - g.getFontMetrics().stringWidth(s) / 2, this.y1 + (this.y2 - this.y1) / 2 + g.getFontMetrics().getHeight() / 2 / 2);
            }

            public void setCurrentAnswer(double value) {
                this.currentAnswer = value;
            }

            public int getCurrentAnswer() {
                return (int)this.currentAnswer;
            }

            public void verifyAnswer() {
                this.correct = this.currentAnswer == (double)this.finalAnswer;
                this.state = 1;
            }
        }

    }

}

