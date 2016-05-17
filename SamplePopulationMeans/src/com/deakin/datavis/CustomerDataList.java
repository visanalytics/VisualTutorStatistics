/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

public class CustomerDataList {
    ArrayList<String> datasets;
    int x;
    int y;

    public CustomerDataList(int x, int y) {
        this.x = x;
        this.y = y;
        this.datasets = new ArrayList();
    }

    public void addListing(String listing) {
        this.datasets.add(0, listing);
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        int i = 0;
        while (i < this.datasets.size()) {
            int ySize = (g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent()) * 2;
            g.drawString(this.datasets.get(i), this.x, this.y + ySize * i);
            ++i;
        }
    }
}

