/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import java.awt.Color;
import java.awt.Graphics;

public class BoundsVis {
    int x1;
    int y1;
    int x2;
    int y2;
    double val1;
    double val2;
    double min;
    double max;

    public BoundsVis(int x1, int y1, int x2, int y2, double min, double max, double initVal1, double initVal2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.min = min;
        this.max = max;
        this.val1 = initVal1;
        this.val2 = initVal2;
    }

    public void setUpper(double val) {
        this.val1 = val < 0.0 ? 0.0 : (val > 1.0 ? 1.0 : val);
    }

    public void setLower(double val) {
        this.val2 = val < 0.0 ? 0.0 : (val > 1.0 ? 1.0 : val);
    }

    public void paint(Graphics g) {
        int rectRound = 15;
        g.setColor(new Color(79, 129, 189, 255));
        g.fillRoundRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1, rectRound, rectRound);
        g.setColor(Color.RED);
        g.fillRoundRect(this.x1, (int)((double)this.y1 + (double)(this.y2 - this.y1) * (1.0 - this.val1)), this.x2 - this.x1, (int)((double)(this.y2 - this.y1) * (- this.val2 - this.val1)), rectRound, rectRound);
        int stringDisp = 3;
        g.setColor(Color.WHITE);
        g.drawString(String.format("%.0f", this.max), this.x2 + stringDisp, this.y1);
        g.drawString(String.format("%.0f", this.min), this.x2 + stringDisp, this.y2);
        g.drawString(String.format("%.2f", this.val1), this.x2 + stringDisp, (int)((double)this.y1 + (double)(this.y2 - this.y1) * (1.0 - this.val1)));
        g.drawString(String.format("%.2f", this.val2), this.x2 + stringDisp, (int)((double)this.y1 + (double)(this.y2 - this.y1) * (1.0 - this.val2)));
    }
}

