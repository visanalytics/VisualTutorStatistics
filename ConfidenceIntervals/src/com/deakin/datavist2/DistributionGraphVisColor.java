/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  umontreal.iro.lecuyer.probdist.NormalDist
 */
package com.deakin.datavist2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import umontreal.iro.lecuyer.probdist.NormalDist;

public class DistributionGraphVisColor {
    Column[] columns;
    ArrayList times = new ArrayList();
    ArrayList bucCols = new ArrayList();
    int x1;
    int y1;
    int x2;
    int y2;
    int min;
    int max;
    int distance;
    NormalDist dist;
    boolean show_curve = false;

    public DistributionGraphVisColor(int x1, int y1, int x2, int y2, int min, int max, int distance) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.min = min;
        this.max = max;
        this.distance = distance;
        int offset = 2;
        this.initColumns(min, max, distance);
    }

    private void initColumns(int min, int max, int distance) {
        int colNum = (max - min) / distance;
        int offset = 2;
        this.columns = new Column[colNum];
        int i = 0;
        while (i < this.columns.length) {
            Column c;
            String name = Integer.toString(min + distance * (i + 1));
            if (i == this.columns.length - 1) {
                name = String.valueOf(name) + "+";
            }
            this.columns[i] = c = new Column(name, this.x1 + (this.x2 - this.x1) / colNum * i + offset, this.y1, this.x1 + (this.x2 - this.x1) / colNum * (i + 1) - offset, this.y2);
            ++i;
        }
    }

    public void initAndShowWeibullCurve() {
        double[] tms = new double[this.times.size()];
        int i = 0;
        while (i < this.times.size()) {
            tms[i] = ((Integer)this.times.get(i)).intValue();
            ++i;
        }
        this.dist = NormalDist.getInstanceFromMLE((double[])tms, (int)tms.length);
        this.show_curve = true;
    }

    public void addData(int val, Color col) {
        this.times.add(val);
        this.bucCols.add(col);
        int c = 0;
        while (c < this.columns.length) {
            if (val < this.min + (c + 1) * this.distance) {
                this.columns[c].addDataVal(col);
                break;
            }
            if (c == this.columns.length - 1 && val >= this.min + c * this.distance) {
                this.columns[c].addDataVal(col);
                break;
            }
            ++c;
        }
    }

    public void update() {
    }

    public void paint(Graphics g) {
        Color c = new Color(37, 63, 96, 255);
        g.setColor(c);
        g.fillRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
        Column[] arrcolumn = this.columns;
        int n = arrcolumn.length;
        int n2 = 0;
        while (n2 < n) {
            Column col = arrcolumn[n2];
            col.paint(g);
            ++n2;
        }
        if (this.show_curve) {
            int columnNum = this.columns.length;
            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke(new BasicStroke(3.0f));
            g2.setColor(Color.WHITE);
            Point lastPoint = null;
            double w = (this.x2 - this.x1) / columnNum;
            double wx = (double)columnNum * w / ((double)this.max - (double)this.min);
            int rHeight = (this.x2 - this.x1) / columnNum / 3;
            int bench = this.y2 - 10;
            int itNum = 40;
            int i = 0;
            while (i < itNum) {
                double xVal = (double)this.min + ((double)this.max - (double)this.min) / (double)itNum * (double)i;
                double yVal = this.dist.density(xVal - (double)(this.distance / 2)) * (double)this.distance * (double)this.times.size();
                double x = (double)this.x1 + (xVal - (double)this.min) * wx + 1.0;
                double y = (double)bench - (double)rHeight * yVal;
                if (lastPoint == null) {
                    lastPoint = new Point((int)x, (int)y);
                } else {
                    g2.drawLine(lastPoint.x, lastPoint.y, (int)x, (int)y);
                    lastPoint = new Point((int)x, (int)y);
                }
                ++i;
            }
        }
    }

    private class Column {
        String label;
        Color[] bucketColors;
        int bucket_index_num;
        int x1;
        int y1;
        int x2;
        int y2;

        public Column(String label, int x1, int y1, int x2, int y2) {
            this.bucketColors = new Color[99];
            this.bucket_index_num = 0;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.label = label;
        }

        public void addDataVal(Color color) {
            ++this.bucket_index_num;
            this.bucketColors[this.bucket_index_num - 1] = color;
        }

        public void paint(Graphics g) {
            g.setColor(Color.WHITE);
            g.drawString(this.label, this.x1 + (this.x2 - this.x1) / 2 - g.getFontMetrics().stringWidth(this.label) / 2, this.y2);
            Color c = new Color(196, 189, 151, 255);
            g.setColor(c);
            int rHeight = (this.x2 - this.x1) / 3;
            int bench = this.y2 - 10 - rHeight;
            int i = 0;
            while (i < this.bucket_index_num) {
                g.setColor(this.bucketColors[i]);
                if (bench - rHeight * i < this.y1 + rHeight) {
                    Color cv = new Color(225, 102, 102, 255);
                    g.setColor(cv);
                    g.fillRect(this.x1, bench - rHeight * i, this.x2 - this.x1, rHeight - rHeight / 3);
                    break;
                }
                g.fillRect(this.x1, bench - rHeight * i, this.x2 - this.x1, rHeight - rHeight / 3);
                ++i;
            }
        }
    }

}

