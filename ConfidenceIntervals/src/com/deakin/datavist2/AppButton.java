/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import com.deakin.datavist2.AppButtonClickInterface;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class AppButton {
    int x1;
    int y1;
    int x2;
    int y2;
    Color buttonCol;
    String label;
    AppButtonClickInterface onClick;

    public AppButton(String label, int x1, int y1, int x2, int y2, Color buttonCol, AppButtonClickInterface onClickMethod) {
        this.label = label;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.buttonCol = buttonCol;
        this.onClick = onClickMethod;
    }

    public void click() {
        this.onClick.onClick();
    }

    public boolean contains(int x, int y) {
        Rectangle r = new Rectangle(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
        if (r.contains(x, y)) {
            return true;
        }
        return false;
    }

    public void paint(Graphics g, Color fontCol) {
        Color backCol = new Color((int)((double)this.buttonCol.getRed() * 0.5), (int)((double)this.buttonCol.getGreen() * 0.5), (int)((double)this.buttonCol.getBlue() * 0.5));
        g.setColor(backCol);
        g.fillRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
        g.setColor(this.buttonCol);
        int disp = 2;
        g.fillRect(this.x1 + disp, this.y1 + disp, this.x2 - this.x1 - disp * 2, this.y2 - this.y1 - disp * 2);
        g.setColor(fontCol);
        int x = this.x1 + ((this.x2 - this.x1) / 2 - g.getFontMetrics().stringWidth(this.label) / 2);
        int sH = g.getFontMetrics().getAscent() + g.getFontMetrics().getDescent() + g.getFontMetrics().getLeading();
        int y = this.y1 + ((this.y2 - this.y1) / 2 + sH / 4);
        g.drawString(this.label, x, y);
    }
}

