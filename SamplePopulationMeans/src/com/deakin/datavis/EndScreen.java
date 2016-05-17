/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.AppletMan;
import com.deakin.datavis.ControlPanelBar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;

public class EndScreen
extends Activity
implements MouseListener {
    int windowHeight;
    int windowWidth;
    Image offScreen;
    Graphics bufferGraphics;

    @Override
    public void init(AppletMan m) {
        super.init(m);
        this.windowWidth = this.getSize().width;
        this.windowHeight = this.getSize().height;
        this.setBackground(Color.black);
        this.addMouseListener(this);
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    public void start() {
        this.appMan.pb1.drawing = false;
        this.appMan.pb2.drawing = false;
    }

    public void stop() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void update(Graphics g) {
        if (this.offScreen == null) {
            this.offScreen = this.createImage(this.getSize().width, this.getSize().height);
            this.bufferGraphics = this.offScreen.getGraphics();
        }
        this.bufferGraphics.setColor(this.getBackground());
        this.bufferGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
        this.bufferGraphics.setColor(this.getForeground());
        this.paint(this.bufferGraphics);
        g.drawImage(this.offScreen, 0, 0, this);
    }

    @Override
    public void paint(Graphics g) {
        Color backCol = new Color(55, 96, 146, 255);
        g.setColor(backCol);
        g.fillRect(0, 0, this.windowWidth, this.windowHeight);
        g.setFont(new Font("Serif", 2, 104));
        g.setColor(Color.WHITE);
        g.drawString("Thank You !", this.windowWidth / 2 - g.getFontMetrics().stringWidth("Thank You !") / 2, this.windowHeight / 3 + g.getFontMetrics().getHeight() / 4);
        StringBuilder sb = new StringBuilder("Congratulations. You have successfully completed a demonstration on sampling distribution.");
        int i = 0;
        while ((i = sb.indexOf(" ", i + 75)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        String parsStr = sb.toString();
        g.setFont(new Font("Dialog", 0, 18));
        int lineNum = this.getLineNumber(parsStr);
        int pWidth = this.maxLineWidth(g, parsStr);
        this.drawString(g, parsStr, this.windowWidth / 2 - pWidth / 2, this.windowHeight / 2);
        sb = new StringBuilder("Don\u2019t forget to fill in the quiz");
        i = 0;
        while ((i = sb.indexOf(" ", i + 75)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        parsStr = sb.toString();
        g.setFont(new Font("Dialog", 0, 18));
        pWidth = this.maxLineWidth(g, parsStr);
        this.drawString(g, parsStr, this.windowWidth / 2 - pWidth / 2, this.windowHeight / 2 + g.getFontMetrics().getHeight() * lineNum);
        g.setFont(new Font("Dialog", 0, 22));
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

    @Override
    public void step() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }
}

