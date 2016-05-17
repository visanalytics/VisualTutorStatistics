/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import java.awt.Color;
import java.awt.Graphics;

public class ProportionBar {
    int x1;
    int y1;
    int x2;
    int y2;
    double val1;
    double val2;

    public ProportionBar(int x1, int y1, int x2, int y2, double initVal1, double initVal2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.val1 = initVal1;
        this.val2 = initVal2;
    }

    public void setVal1(double val) {
        this.val1 = val;
    }

    public void setVal2(double val) {
        this.val2 = val;
    }

    public void paint(Graphics g) {
        int rectRound = 15;
        g.setColor(Color.BLACK);
        g.fillRoundRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1, rectRound, rectRound);
        g.setColor(Color.GREEN);
        g.fillRoundRect(this.x1, this.y1, (int)((double)(this.x2 - this.x1) * this.val1), this.y2 - this.y1, rectRound, rectRound);
        g.setColor(Color.RED);
        g.fillRoundRect(this.x1 + (int)((double)(this.x2 - this.x1) * this.val1), this.y1, (int)((double)(this.x2 - this.x1) * this.val2), this.y2 - this.y1, rectRound, rectRound);
    }
}

