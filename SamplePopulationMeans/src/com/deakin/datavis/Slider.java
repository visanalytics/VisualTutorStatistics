/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.StaticClock;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.PrintStream;

public class Slider {
    int x1;
    int y1;
    int x2;
    int y2;
    int min;
    int max;
    int current;
    double distance;
    int offset = 2;
    int circle_radius = 15;
    String title;
    AnswerBox abox;

    public Slider(String title, int x1, int y1, int x2, int y2, int min, int max, double distance, int start, int answer) {
        this.title = title;
        int bLength = (x2 - x1) / 8 * 6 / (max - min) * (max - min);
        int n = x1 + (x2 - x1) / 8;
        int x = n + (x2 - n - bLength) / 2;
        this.abox = new AnswerBox(x1 + (x2 - x1) / 8 * 7, y1, x + bLength + (x2 - x - bLength) / 2, y2, start, answer);
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
        Color back1 = new Color(37, 64, 97, 255);
        g.setColor(back1);
        g.fillRect(this.x1, this.y1, (this.x2 - this.x1) / 8, this.y2 - this.y1);
        Color back2 = new Color(55, 96, 146, 255);
        g.setColor(back2);
        g.fillRect(this.x1 + (this.x2 - this.x1) / 8, this.y1, this.x2 - this.x1 - (this.x2 - this.x1) / 8, this.y2 - this.y1);
        g.setColor(Color.WHITE);
        g.drawString(this.title, this.x1 + (this.x2 - this.x1) / 8 / 2 - g.getFontMetrics().stringWidth(this.title) / 2, this.y1 + (this.y2 - this.y1) / 2);
        Color boxC = new Color(92, 146, 211, 255);
        g.setColor(boxC);
        double bLength = (int)((double)(this.x2 - this.x1) * 0.75) / (this.max - this.min) * (this.max - this.min);
        int n = this.x1 + (this.x2 - this.x1) / 8;
        int n2 = this.x1 + (this.x2 - this.x1) / 8 * 2;
        int x = (int)((double)n + ((double)(this.x2 - n2) - bLength) / 2.0);
        g.fillRect(x, this.y1 + (this.y2 - this.y1) / 2 - this.offset, (int)bLength, this.offset * 2);
        Color slidC = new Color(72, 111, 154, 255);
        g.setColor(slidC);
        int dist = (this.x2 - this.x1) / 8 * 6 / (this.max - this.min) * this.current;
        g.fillOval(x + dist - this.circle_radius, this.y1 + (this.y2 - this.y1) / 2 - this.circle_radius, this.circle_radius * 2, this.circle_radius * 2);
        this.abox.paint(g);
    }

    public void step() {
    }

    public void onMouseDragged(MouseEvent e, StaticClock clock) {
        int x = e.getX();
        int y = e.getY();
        double bLength = (this.x2 - this.x1) / 8 * 6;
        double n = this.x1 + (this.x2 - this.x1) / 8;
        double n2 = this.x1 + (this.x2 - this.x1) / 8 * 2;
        double m = n + ((double)this.x2 - n2 - bLength);
        double pdist = bLength / ((double)(this.max - this.min) / this.distance);
        if (y > this.y1 && y < this.y2) {
            this.current = (double)x <= m ? 0 : ((double)x > m + bLength ? this.max - this.min : (int)((this.roundUp(x, pdist) - m) / pdist));
            this.abox.setCurrentAnswer(this.min + this.current);
            clock.timeSec = this.current;
        }
    }

    public double onMouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        double bLength = (this.x2 - this.x1) / 8 * 6;
        double n = this.x1 + (this.x2 - this.x1) / 8;
        double n2 = this.x1 + (this.x2 - this.x1) / 8 * 2;
        double m = n + ((double)this.x2 - n2 - bLength);
        double pdist = bLength / ((double)(this.max - this.min) / this.distance);
        if (y > this.y1 && y < this.y2) {
            if ((double)x <= m) {
                this.current = 0;
            } else if ((double)x > m + bLength) {
                this.current = this.max - this.min;
            } else {
                System.out.println((this.roundUp(x * 10, pdist * 10.0) - m * 10.0) / 100.0 / pdist);
                this.current = (int)((this.roundUp(x * 10, pdist * 10.0) - m * 10.0) / 100.0 / pdist);
            }
            this.abox.setCurrentAnswer(this.min + this.current);
        }
        return this.current;
    }

    double roundUp(double x, double f) {
        return (int)(f * Math.ceil(x / f));
    }

    int round(int num, int closestto) {
        int temp = num % closestto;
        if (temp < closestto / 2) {
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
            String s = String.format("%.0f", this.currentAnswer);
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

