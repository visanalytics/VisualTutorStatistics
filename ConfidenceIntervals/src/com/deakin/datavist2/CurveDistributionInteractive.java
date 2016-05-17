/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  umontreal.iro.lecuyer.probdist.NormalDist
 */
package com.deakin.datavist2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.PrintStream;
import umontreal.iro.lecuyer.probdist.NormalDist;

public class CurveDistributionInteractive {
    int x1;
    int y1;
    int x2;
    int y2;
    double pointX1;
    double pointX2;
    double pointX1Val;
    double pointX2Val;
    double currentProb;
    double min;
    double max;
    int axis_border = 20;
    int highestCurveY = 9999;
    NormalDist gDist;
    int[] xPoints;
    int[] yPoints;

    public CurveDistributionInteractive(int x1, int y1, int x2, int y2, double initialConfidence, double initialSampleSize, double mu) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        double sigma = 1.96 * Math.sqrt(mu * (1.0 - mu) / initialSampleSize);
        this.min = mu - sigma * 4.0;
        this.max = mu + sigma * 4.0;
        this.gDist = new NormalDist(mu, sigma);
        this.setConfidence(initialConfidence);
        this.initCurvePoints();
    }

    private void initCurvePoints() {
        double iterationNum = 100.0;
        int[] xps = new int[(int)(iterationNum + 4.0)];
        xps[0] = this.x2;
        xps[1] = this.x2;
        xps[2] = this.x1;
        xps[3] = this.x1;
        int[] yps = new int[(int)(iterationNum + 4.0)];
        yps[0] = this.y2 - this.axis_border;
        yps[1] = this.y1;
        yps[2] = this.y1;
        yps[3] = this.y2 - this.axis_border;
        double h = (double)(this.y2 - this.y1 - this.axis_border * 2) / this.gDist.density(this.gDist.getMu());
        double w = (double)(this.x2 - this.x1) / (this.max - this.min);
        int arrIndex = 4;
        double i = 0.0;
        while (i < iterationNum) {
            double xVal = this.min + (this.max - this.min) / iterationNum * i;
            double yVal = this.gDist.density(xVal);
            double x = (double)this.x1 + (xVal - this.min) * w;
            double y = (double)(this.y2 - this.axis_border) - h * yVal;
            if ((double)this.highestCurveY > y) {
                this.highestCurveY = (int)y;
            }
            System.out.println(x);
            xps[arrIndex] = (int)x;
            yps[arrIndex] = (int)y;
            ++arrIndex;
            i += 1.0;
        }
        this.xPoints = xps;
        this.yPoints = yps;
    }

    private void drawGraphLines(Graphics g, int rowNum) {
        Graphics2D g2 = (Graphics2D)g;
        double h = (double)(this.y2 - this.y1 - this.axis_border * 2) / this.gDist.density(this.gDist.getMu());
        double w = (double)(this.x2 - this.x1) / (this.max - this.min);
        g.setColor(Color.BLACK);
        g2.drawLine(this.x1, (int)((double)this.y2 - h * (double)rowNum), this.x1, this.y2);
        g2.drawLine(this.x1, this.y2 - this.axis_border, (int)((double)this.x1 + w * (this.max - this.min)), this.y2 - this.axis_border);
        int disp = 3;
        g.setColor(Color.BLACK);
        g2.drawLine((int)((double)this.x1 + (this.pointX1Val - this.min) * w), this.y2 - this.axis_border - disp, (int)((double)this.x1 + (this.pointX1Val - this.min) * w), this.y2 - this.axis_border + disp);
        String zVal1 = String.format("%.2f", (this.pointX1Val - this.gDist.getMu()) / this.gDist.getSigma());
        g2.drawString(zVal1, (int)((double)this.x1 + (this.pointX1Val - this.min) * w) - g.getFontMetrics().stringWidth(zVal1) / 2, this.y2 - this.axis_border + disp + g.getFontMetrics().getHeight());
        g2.drawLine((int)((double)this.x1 + (this.pointX2Val - this.min) * w), this.y2 - this.axis_border - disp, (int)((double)this.x1 + (this.pointX2Val - this.min) * w), this.y2 - this.axis_border + disp);
        String zVal2 = String.format("%.2f", (this.pointX2Val - this.gDist.getMu()) / this.gDist.getSigma());
        g2.drawString(zVal2, (int)((double)this.x1 + (this.pointX2Val - this.min) * w) - g.getFontMetrics().stringWidth(zVal1) / 2, this.y2 - this.axis_border + disp + g.getFontMetrics().getHeight());
        Font lFont = g.getFont();
        g.setFont(new Font("Dialog", 1, 20));
        String probS = String.valueOf(String.format("%.1f", NormalDist.cdf((double)this.gDist.getMu(), (double)this.gDist.getSigma(), (double)this.currentProb) * 100.0)) + "%";
        g.drawString(probS, this.x1 + (this.x2 - this.x1) / 2 - g.getFontMetrics().stringWidth(probS) / 2, (int)((double)(this.y2 - this.axis_border) - h * this.gDist.density(this.gDist.getMu()) / 2.0));
        g.setFont(lFont);
    }

    public void paint(Graphics g, int rowNum) {
        double h = (double)(this.y2 - this.y1 - this.axis_border * 2) / this.gDist.density(this.gDist.getMu());
        double w = (double)(this.x2 - this.x1) / (this.max - this.min);
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2.0f));
        g.setColor(Color.WHITE);
        g.fillRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
        g.setColor(new Color(51, 162, 201, 255));
        g.fillRect((int)((double)this.x1 + (this.pointX1Val - this.min) * w), this.y1, (int)((double)this.x1 + (this.pointX2Val - this.min) * w) - (int)((double)this.x1 + (this.pointX1Val - this.min) * w), this.y2 - this.y1 - this.axis_border);
        g2.setColor(Color.BLACK);
        g2.drawLine((int)((double)this.x1 + (this.pointX1Val - this.min) * w), this.y1, (int)((double)this.x1 + (this.pointX1Val - this.min) * w), this.y2 - this.axis_border);
        g2.drawLine((int)((double)this.x1 + (this.pointX2Val - this.min) * w), this.y1, (int)((double)this.x1 + (this.pointX2Val - this.min) * w), this.y2 - this.axis_border);
        g.setColor(Color.WHITE);
        g.fillPolygon(this.xPoints, this.yPoints, this.xPoints.length);
        int i = 0;
        while (i < this.xPoints.length - 1) {
            g2.setColor(Color.BLACK);
            g2.drawLine(this.xPoints[i], this.yPoints[i], this.xPoints[i + 1], this.yPoints[i + 1]);
            ++i;
        }
        this.drawGraphLines(g, rowNum);
    }

    public void step() {
    }

    public void setConfidence(double value) {
        this.setConfidence(value, this.gDist.getMu(), this.gDist.getSigma());
    }

    public void setConfidence(double confidence, double mu, double sigma) {
        double val2 = confidence + (1.0 - confidence) / 2.0;
        double prob2 = NormalDist.inverseF((double)mu, (double)sigma, (double)val2);
        this.currentProb = NormalDist.inverseF((double)mu, (double)sigma, (double)confidence);
        this.pointX1Val = mu - (prob2 - mu);
        this.pointX2Val = mu + (prob2 - mu);
        this.pointX1 = (this.max - this.min) / sigma + this.pointX1Val * sigma;
        this.pointX2 = (this.max - this.min) / sigma - this.pointX2Val * sigma;
    }

    public void setSampleSize(double size) {
        NormalDist tempDist;
        double mu = this.gDist.getMu();
        double sigma = 1.96 * Math.sqrt(mu * (1.0 - mu) / size);
        this.gDist = tempDist = new NormalDist(mu, sigma);
        this.initCurvePoints();
    }
}

