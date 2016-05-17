/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.io.PrintStream;

public class PieChart {
    int x;
    int y;
    int radius;
    double val1;
    double val2;

    public PieChart(int x, int y, int radius, double initVal1, double initVal2) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.val1 = initVal1;
        this.val2 = initVal2;
    }

    public void setVal1(double value) {
        this.val1 = value;
        this.val2 = 1.0 - this.val1;
    }

    public void setVal2(double value) {
        this.val2 = value;
        this.val1 = 1.0 - this.val2;
    }

    public void paint(Graphics g, boolean fillSector) {
        g.setColor(new Color(37, 64, 97, 255));
        g.fillOval(this.x - this.radius, this.y - this.radius, this.radius * 2, this.radius * 2);
        System.out.println(String.valueOf(this.val1) + "    " + this.val2);
        if (fillSector) {
            g.setColor(Color.GREEN);
            g.fillArc(this.x - this.radius, this.y - this.radius, this.radius * 2, this.radius * 2, (int)this.val1, (int)(360.0 * this.val2));
            g.setColor(Color.RED);
            g.fillArc(this.x - this.radius, this.y - this.radius, this.radius * 2, this.radius * 2, (int)this.val1, (int)(-360.0 * (1.0 - this.val2)));
        } else {
            g.setColor(Color.GREEN);
            g.drawArc(this.x - this.radius, this.y - this.radius, this.radius * 2, this.radius * 2, (int)this.val1, (int)(360.0 * this.val2));
            g.setColor(Color.RED);
            g.drawArc(this.x - this.radius, this.y - this.radius, this.radius * 2, this.radius * 2, (int)this.val1, (int)(-360.0 * (1.0 - this.val2)));
        }
    }

    public void paintLines(Graphics g) {
        Arc2D.Double arcV1 = new Arc2D.Double(this.x - this.radius, this.y - this.radius, this.radius * 2, this.radius * 2, (int)this.val1, (int)(360.0 * this.val2), 2);
        Arc2D.Double arcV2 = new Arc2D.Double(this.x - this.radius, this.y - this.radius, this.radius * 2, this.radius * 2, (int)this.val1, (int)(-360.0 * (1.0 - this.val2)), 2);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.draw(arcV1);
        g2.draw(arcV2);
    }
}

