/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class QuestionPopup {
    int x1;
    int y1;
    int x2;
    int y2;
    String message;
    boolean show_popup;

    public QuestionPopup(int x1, int y1, int x2, int y2, String initialMessage) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.message = initialMessage;
        this.show_popup = false;
    }

    public void paint(Graphics g) {
        int disp = 10;
        Font lFont = g.getFont();
        g.setFont(new Font("Dialog", 1, (this.y2 - this.y1) / 2));
        String q = "?";
        g.setColor(Color.WHITE);
        g.drawString(q, this.x1 + (this.x2 - this.x1) / 2 - g.getFontMetrics().stringWidth(q) / 2, this.y1 + (int)((double)(this.y2 - this.y1) * 0.75));
        g.setFont(lFont);
        if (this.show_popup) {
            int dialogX1;
            int dialogX2;
            int dialogY2;
            int dialogY1;
            g.setFont(new Font("Dialog", 1, 18));
            StringBuilder sb = new StringBuilder(this.message);
            int i = 0;
            while ((i = sb.indexOf(" ", i + 20)) != -1) {
                sb.replace(i, i + 1, "\n");
            }
            String parsStr = sb.toString();
            int lineNum = this.getLineNumber(parsStr);
            int pWidth = this.maxLineWidth(g, parsStr);
            if (this.y1 - g.getFontMetrics().getHeight() * 3 > 0) {
                dialogY1 = this.y1 - g.getFontMetrics().getHeight() * lineNum;
                dialogY2 = this.y1;
            } else {
                dialogY1 = this.y2;
                dialogY2 = this.y2 + g.getFontMetrics().getHeight() * lineNum;
            }
            if (this.x1 - pWidth - disp * 2 < 0) {
                dialogX1 = this.x2;
                dialogX2 = this.x2 + disp * 2 + pWidth;
            } else {
                dialogX1 = this.x1 - disp * 2 - pWidth;
                dialogX2 = this.x1;
            }
            g.setColor(Color.ORANGE);
            g.fillRect(dialogX1, dialogY1, dialogX2 - dialogX1, dialogY2 - dialogY1);
            g.setColor(Color.BLACK);
            this.drawString(g, parsStr, dialogX1 + disp, dialogY1);
        }
        g.setFont(lFont);
    }

    private void drawString(Graphics g, String text, int x, int y) {
        String[] arrstring = text.split("\n");
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String line = arrstring[n2];
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
            ++n2;
        }
    }

    private int getLineNumber(String text) {
        int lines = 1;
        String[] arrstring = text.split("\n");
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String line = arrstring[n2];
            ++lines;
            ++n2;
        }
        return lines;
    }

    private int maxLineWidth(Graphics g, String text) {
        int mWidth = 0;
        String[] arrstring = text.split("\n");
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String line = arrstring[n2];
            int temp = g.getFontMetrics().stringWidth(line);
            if (temp > mWidth) {
                mWidth = temp;
            }
            ++n2;
        }
        return mWidth;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public boolean contains(int x, int y) {
        if (x > this.x1 && x < this.x2 && y > this.y1 && y < this.y2) {
            return true;
        }
        return false;
    }

    public void click() {
        this.show_popup = !this.show_popup;
    }
}

