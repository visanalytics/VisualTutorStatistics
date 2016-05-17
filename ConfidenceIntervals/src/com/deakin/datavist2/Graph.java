/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  umontreal.iro.lecuyer.probdist.NormalDist
 *  umontreal.iro.lecuyer.probdist.WeibullDist
 */
package com.deakin.datavist2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Iterator;
import umontreal.iro.lecuyer.probdist.NormalDist;
import umontreal.iro.lecuyer.probdist.WeibullDist;

public class Graph {
    int x1;
    int y1;
    int x2;
    int y2;
    int axisborder_size;
    int l5 = 0;
    int l10 = 0;
    int l15 = 0;
    int l20 = 0;
    int l25 = 0;
    int l30 = 0;
    int l35 = 0;
    int l40 = 0;
    int l45 = 0;
    int l50 = 0;
    int l55 = 0;
    int l60 = 0;
    ArrayList<Integer> ls = new ArrayList();
    ArrayList<Integer> timesIA = new ArrayList();
    Color cFront;
    Color cBack;
    int mean;
    int median;
    int mode;
    WeibullDist dist;
    NormalDist normalDist;
    boolean show_curve;
    int min;
    int max;
    int distance;
    int size;

    public Graph(int drawX1, int drawY1, int drawX2, int drawY2, ArrayList<Integer> timesIA, int mean, Color cFront, Color cBack, boolean show_curve, int min, int max, int distance) {
        this.timesIA = timesIA;
        this.x1 = drawX1;
        this.y1 = drawY1;
        this.x2 = drawX2;
        this.y2 = drawY2;
        this.axisborder_size = (this.x2 - this.x1) / 20;
        this.cFront = cFront;
        this.cBack = cBack;
        this.mean = mean;
        this.min = min;
        this.max = max;
        this.distance = distance;
        this.show_curve = show_curve;
        if (timesIA.size() == 12) {
            this.initColumnsDataSorted(timesIA);
            this.dist = this.generateWeibullDistSorted(timesIA);
            this.normalDist = this.generateNormalDistSorted(timesIA);
        } else {
            this.initColumnsDataRaw(timesIA, min, max, distance);
            this.dist = this.generateWeibullDistRaw(timesIA);
            this.normalDist = this.generateNormalDistRaw(timesIA);
        }
    }

    public WeibullDist getWeibullDist() {
        return this.dist;
    }

    public NormalDist getNormalDist() {
        return this.normalDist;
    }

    private WeibullDist generateWeibullDistRaw(ArrayList<Integer> times) {
        double[] tms = new double[times.size()];
        int i = 0;
        while (i < times.size()) {
            tms[i] = times.get(i).intValue();
            ++i;
        }
        WeibullDist d = WeibullDist.getInstanceFromMLE((double[])tms, (int)tms.length);
        return d;
    }

    private NormalDist generateNormalDistRaw(ArrayList<Integer> times) {
        double[] tms = new double[times.size()];
        int i = 0;
        while (i < times.size()) {
            tms[i] = times.get(i).intValue();
            ++i;
        }
        NormalDist d = NormalDist.getInstanceFromMLE((double[])tms, (int)tms.length);
        return d;
    }

    private NormalDist generateNormalDistSorted(ArrayList<Integer> times) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        int i = 0;
        while (i < times.size()) {
            int n = 0;
            while (n < times.get(i)) {
                arr.add(i * this.distance + 1);
                ++n;
            }
            ++i;
        }
        return this.generateNormalDistRaw(arr);
    }

    private WeibullDist generateWeibullDistSorted(ArrayList<Integer> times) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        int i = 0;
        while (i < times.size()) {
            int n = 0;
            while (n < times.get(i)) {
                arr.add(i * this.distance + 1);
                ++n;
            }
            ++i;
        }
        return this.generateWeibullDistRaw(arr);
    }

    private void initColumnsDataSorted(ArrayList<Integer> arr) {
        this.ls = arr;
        Iterator<Integer> iterator = arr.iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            this.size += i;
        }
    }

    private void initColumnsDataRaw(ArrayList<Integer> arr, int min, int max, int distance) {
        this.ls = new ArrayList((max - min) / distance);
        int i = 0;
        while (i < (max - min) / distance) {
            this.ls.add(0);
            ++i;
        }
        i = 0;
        while (i < arr.size()) {
            int n = 0;
            while (n < (max - min) / distance) {
                if (arr.get(i) < min + (n + 1) * distance) {
                    this.ls.set(n, this.ls.get(n) + 1);
                    break;
                }
                ++n;
            }
            ++i;
        }
        this.size = arr.size();
    }

    public int getMean() {
        return this.mean;
    }

    public void paint(Graphics g, int rowNum) {
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
        int disp = 1;
        int i2 = 0;
        while (i2 < this.ls.size()) {
            int tx = this.x1 + this.axisborder_size + w * i2;
            int ty = this.y2 - this.axisborder_size - h * this.ls.get(i2);
            g.setColor(Color.BLACK);
            g.fillRect(tx, ty, w + 1, this.y2 - this.axisborder_size - ty);
            g.setColor(this.cFront);
            g.fillRect(tx + disp, ty + disp, w + 1 - disp * 2, this.y2 - this.axisborder_size - ty - disp * 2);
            ++i2;
        }
        if (this.show_curve) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(2.0f));
            g2.setColor(Color.WHITE);
            Point lastPoint = null;
            double wx = (double)columnNum * (double)w / ((double)this.max - (double)this.min);
            int i3 = 0;
            while (i3 < this.max - this.min) {
                double xVal = i3;
                double yVal = this.dist.density(xVal - (double)(this.distance / 2)) * (double)this.distance * (double)this.size;
                double x = (double)(this.x1 + this.axisborder_size) + xVal * wx;
                double y = (double)(this.y2 - this.axisborder_size) - (double)h * yVal;
                if (lastPoint == null) {
                    lastPoint = new Point((int)x, (int)y);
                } else {
                    g2.drawLine(lastPoint.x, lastPoint.y, (int)x, (int)y);
                    lastPoint = new Point((int)x, (int)y);
                }
                ++i3;
            }
        }
    }

    public void paint(Graphics g, int x1, int y1, int x2, int y2, Color cBack) {
        this.axisborder_size = (x2 - x1) / 20;
        g.setColor(cBack);
        g.fillRect(x1 + this.axisborder_size / 2, y1, x2 - x1 - this.axisborder_size, y2 - y1 - this.axisborder_size / 2 + 1);
        int columnNum = 12;
        int rowNum = 12;
        int w = (x2 - x1 - this.axisborder_size + 1) / columnNum;
        int h = (y2 - this.axisborder_size - y1) / rowNum;
        g.setColor(Color.BLACK);
        g.drawLine(x1 + this.axisborder_size, y1, x1 + this.axisborder_size, y2 - this.axisborder_size);
        g.drawLine(x1 + this.axisborder_size, y2 - this.axisborder_size, x1 + this.axisborder_size + w * columnNum, y2 - this.axisborder_size);
        int i = 0;
        while (i <= columnNum) {
            int xb = x1 + this.axisborder_size + w * i;
            g.drawLine(xb, y2 - this.axisborder_size / 2, xb, y2 - this.axisborder_size);
            ++i;
        }
        i = 0;
        while (i <= rowNum) {
            int yl = y1 + h * i;
            g.drawLine(x1 + this.axisborder_size / 2, yl, x1 + this.axisborder_size, yl);
            ++i;
        }
        int disp = 1;
        int i2 = 0;
        while (i2 < this.ls.size()) {
            int tx = x1 + this.axisborder_size + w * i2;
            int ty = y2 - this.axisborder_size - h * this.ls.get(i2);
            g.setColor(Color.BLACK);
            g.fillRect(tx, ty, w + disp, y2 - this.axisborder_size - ty);
            g.setColor(this.cFront);
            g.fillRect(tx + disp, ty + disp, w + 1 - disp * 2, y2 - this.axisborder_size - ty - disp * 2);
            ++i2;
        }
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2.0f));
        if (this.show_curve) {
            g2.setColor(Color.WHITE);
            Point lastPoint = null;
            double wx = (double)columnNum * (double)w / ((double)this.max - (double)this.min);
            int i3 = 0;
            while (i3 < this.max - this.min) {
                double xVal = i3;
                double yVal = this.dist.density(xVal - (double)(this.distance / 2)) * (double)this.distance * (double)this.size;
                double x = (double)(x1 + this.axisborder_size) + xVal * wx;
                double y = (double)(y2 - this.axisborder_size) - (double)h * yVal;
                if (lastPoint == null) {
                    lastPoint = new Point((int)x, (int)y);
                } else {
                    g2.drawLine(lastPoint.x, lastPoint.y, (int)x, (int)y);
                    lastPoint = new Point((int)x, (int)y);
                }
                ++i3;
            }
        }
        double ratio = (double)(x2 - x1 - this.axisborder_size + 1) / (double)(this.max - this.min);
        int lx = (int)((double)(x1 + this.axisborder_size + 1) + (double)this.mean * ratio);
        g2.setColor(Color.BLACK);
        g2.drawLine(lx, y1, lx, y2 - this.axisborder_size);
    }
}

