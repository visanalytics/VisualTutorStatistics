/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

public class DistributionGraphVis {
    Column[] columns;
    ArrayList times = new ArrayList();
    int x1;
    int y1;
    int x2;
    int y2;
    int min;
    int max;
    int distance;

    public DistributionGraphVis(int x1, int y1, int x2, int y2, int min, int max, int distance) {
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

    public void addData(int val) {
        this.times.add(val);
    }

    public void update() {
        int[] dist = new int[this.columns.length];
        int i = 0;
        while (i < this.times.size()) {
            int time = (Integer)this.times.get(i);
            int c = 0;
            while (c < this.columns.length) {
                if (time < this.min + (c + 1) * this.distance) {
                    int[] arrn = dist;
                    int n = c;
                    arrn[n] = arrn[n] + 1;
                    break;
                }
                if (c == this.columns.length - 1 && time >= this.min + c * this.distance) {
                    int[] arrn = dist;
                    int n = c;
                    arrn[n] = arrn[n] + 1;
                    break;
                }
                ++c;
            }
            ++i;
        }
        i = 0;
        while (i < this.columns.length) {
            this.columns[i].setDataVal(dist[i]);
            ++i;
        }
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
    }

    private class Column {
        String label;
        int dataVal;
        int x1;
        int y1;
        int x2;
        int y2;

        public Column(String label, int x1, int y1, int x2, int y2) {
            this.dataVal = 0;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.label = label;
        }

        public int getDataVal() {
            return this.dataVal;
        }

        public void setDataVal(int val) {
            this.dataVal = val;
        }

        public void incDataVal(int val) {
            this.dataVal += val;
        }

        public void paint(Graphics g) {
            g.setColor(Color.WHITE);
            g.drawString(this.label, this.x1 + (this.x2 - this.x1) / 2 - g.getFontMetrics().stringWidth(this.label) / 2, this.y2);
            Color c = new Color(196, 189, 151, 255);
            g.setColor(c);
            int rHeight = (int)((double)(this.x2 - this.x1) / 2.5);
            int bench = this.y2 - 10 - rHeight;
            int i = 0;
            while (i < this.dataVal) {
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

