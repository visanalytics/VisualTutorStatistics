/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  umontreal.iro.lecuyer.probdist.NormalDist
 */
package com.deakin.datavis;

import com.deakin.datavis.Graph;
import java.awt.BasicStroke;
import java.awt.Color;
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
    int axisborder_size;
    double pointX1;
    double pointX2;
    double pointX1Val;
    double pointX2Val;
    int min;
    int max;
    double currentProb;
    int highestCurveY = 9999;
    Graph dist;
    int[] xPoints;
    int[] yPoints;

    public CurveDistributionInteractive(int x1, int y1, int x2, int y2, int min, int max, double currentP1, double currentP2, Graph distDraw) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.axisborder_size = (x2 - x1) / 13;
        this.min = min;
        this.max = max;
        this.dist = distDraw;
        this.pointX1Val = (double)min + currentP1;
        this.pointX2Val = (double)min + currentP2;
        this.currentProb = 0.5;
        this.setPointX1(currentP1);
        this.setPointX2(currentP2);
        this.initCurvePoints();
    }

    private void initCurvePoints() {
        double iterationNum = 60.0;
        int[] xps = new int[(int)(iterationNum + 4.0)];
        xps[0] = this.x2;
        xps[1] = this.x2;
        xps[2] = this.x1 + this.axisborder_size;
        xps[3] = this.x1 + this.axisborder_size;
        int[] yps = new int[(int)(iterationNum + 4.0)];
        yps[0] = this.y2 - this.axisborder_size * 2;
        yps[1] = this.y1;
        yps[2] = this.y1;
        yps[3] = this.y2 - this.axisborder_size * 2;
        double wx = (double)(this.x2 - this.x1) / iterationNum;
        double h = (double)(this.x2 - this.x1) / ((double)this.dist.size * 0.5);
        double w = (double)(this.x2 - this.x1 - this.axisborder_size) / ((double)this.max - (double)this.min);
        int arrIndex = 4;
        double i = 0.0;
        while (i < iterationNum) {
            double xVal = (double)this.dist.min + ((double)this.dist.max - (double)this.dist.min) / iterationNum * i;
            double yVal = this.dist.getNormalDist().density(xVal) * (double)this.dist.distance * (double)this.dist.size;
            double x = (double)(this.x1 + this.axisborder_size) + (xVal - (double)this.dist.min) * w;
            double y = (double)(this.y2 - this.axisborder_size * 2) - h * yVal;
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
        String s;
        Graphics2D g2 = (Graphics2D)g;
        int columnNum = (this.max - this.min) / this.dist.distance;
        int w = (this.x2 - this.x1 - this.axisborder_size) / columnNum;
        int h = (this.y2 - this.axisborder_size * 2 - this.y1) / rowNum;
        g.setColor(Color.BLACK);
        g2.drawLine(this.x1 + this.axisborder_size, this.y2 - this.axisborder_size * 2 - h * rowNum, this.x1 + this.axisborder_size, this.y2 - this.axisborder_size * 2);
        g2.drawLine(this.x1 + this.axisborder_size, this.y2 - this.axisborder_size * 2, this.x1 + this.axisborder_size + w * columnNum, this.y2 - this.axisborder_size * 2);
        int i = 0;
        while (i <= columnNum) {
            int xb = this.x1 + this.axisborder_size + w * i;
            g2.drawLine(xb, this.y2 - (int)((double)this.axisborder_size * 1.5), xb, this.y2 - this.axisborder_size * 2);
            s = String.valueOf(this.min + i * this.dist.distance);
            g.drawString(s, xb - g.getFontMetrics().stringWidth(s) / 2, (int)((double)this.y2 - (double)this.axisborder_size * 1.5 + (double)g.getFontMetrics().getHeight()));
            ++i;
        }
        i = rowNum;
        while (i >= 0) {
            int yl = this.y2 - this.axisborder_size * 2 - h * i;
            g2.drawLine(this.x1 + (int)((double)this.axisborder_size * 0.75), yl, this.x1 + this.axisborder_size, yl);
            if (i % 5 == 0) {
                s = String.valueOf(i);
                g.drawString(s, this.x1 + (int)((double)this.axisborder_size * 0.75) - (g.getFontMetrics().stringWidth(s) + 2), yl + g.getFontMetrics().getHeight() / 4);
            }
            --i;
        }
    }

    public void paint(Graphics g, int rowNum) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2.0f));
        g.setColor(new Color(208, 202, 162));
        g.fillRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
        g.setColor(Color.RED);
        g.fillRect((int)this.pointX1, this.y1, (int)this.pointX2 - (int)this.pointX1, this.y2 - this.y1 - this.axisborder_size * 2);
        g2.drawLine((int)this.pointX1, this.y1, (int)this.pointX1, this.y2 - this.axisborder_size * 2);
        g2.drawLine((int)this.pointX2, this.y1, (int)this.pointX2, this.y2 - this.axisborder_size * 2);
        g.setColor(new Color(208, 202, 162));
        g.fillPolygon(this.xPoints, this.yPoints, this.xPoints.length);
        int i = 0;
        while (i < this.xPoints.length - 1) {
            g2.setColor(Color.BLACK);
            g2.drawLine(this.xPoints[i], this.yPoints[i], this.xPoints[i + 1], this.yPoints[i + 1]);
            ++i;
        }
        this.drawGraphLines(g, rowNum);
        double mu = this.dist.getNormalDist().getMean();
        double sigma = this.dist.getNormalDist().getStandardDeviation();
        this.dist.getNormalDist();
        double prob1 = NormalDist.cdf((double)mu, (double)sigma, (double)this.pointX1Val);
        this.dist.getNormalDist();
        double prob2 = NormalDist.cdf((double)mu, (double)sigma, (double)this.pointX2Val);
        double probability = prob2 - prob1;
        if (probability < 0.0) {
            probability = 0.0;
        }
        g.setColor(Color.BLACK);
        String probS = String.valueOf(String.format("%.2f", probability * 100.0)) + "%";
        int probX = (int)(this.pointX1 + (this.pointX2 - this.pointX1) / 2.0) - g.getFontMetrics().stringWidth(probS) / 2;
        int probY = this.highestCurveY - 10;
        g.drawString(probS, probX, probY);
    }

    public void step() {
    }

    public void setPointX1(double value) {
        double p;
        this.pointX1Val = (double)this.min + value;
        this.pointX1 = p = (double)(this.x1 + this.axisborder_size) + (double)((this.x2 - this.x1 - this.axisborder_size) / (this.max - this.min)) * value;
    }

    public void setPointX2(double value) {
        double p;
        this.pointX2Val = (double)this.min + value;
        this.pointX2 = p = (double)(this.x1 + this.axisborder_size) + (double)((this.x2 - this.x1 - this.axisborder_size) / (this.max - this.min)) * value;
    }
}

