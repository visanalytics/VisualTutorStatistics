/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import com.deakin.datavist2.AppletMan;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.io.PrintStream;

public class ControlPanelBar
extends Canvas
implements MouseListener {
    Image offScreen;
    Graphics bufferGraphics;
    PanelButton pb1;
    PanelButton pb2;
    boolean top;
    boolean bottom;
    AppletMan man;
    boolean drawing = true;
    String title;

    public ControlPanelBar(AppletMan man, boolean top, boolean bottom, String title) {
        this.addMouseListener(this);
        this.setSize(800, 50);
        this.man = man;
        this.top = top;
        this.bottom = bottom;
        this.title = title;
        if (top) {
            this.pb1 = new PanelButton("Previous", 0, 0, this.getWidth() / 8, this.getHeight());
            this.pb2 = new PanelButton("Next", this.getWidth() - this.getWidth() / 8, 0, this.getWidth(), this.getHeight());
        }
    }

    public void init() {
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
        Color backC = new Color(196, 189, 151);
        g.setColor(backC);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (this.top && this.drawing) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Dialog", 1, 16));
            g.drawString(this.title, this.getWidth() / 2 - g.getFontMetrics().stringWidth(this.title) / 2, g.getFontMetrics().getHeight());
            this.pb1.paint(g);
            this.pb2.paint(g);
        }
    }

    @Override
    public void disable() {
        this.drawing = false;
        this.removeMouseListener(this);
    }

    @Override
    public void enable() {
        this.drawing = true;
        this.addMouseListener(this);
    }

    public String containsPoint(int x, int y) {
        String result = this.pb1.contains(x, y) ? "Previous" : (this.pb2.contains(x, y) ? "Next" : "null");
        return result;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Clicked");
        if (this.pb1 != null && this.pb2 != null) {
            String p1 = this.containsPoint(e.getX(), e.getY());
            if (p1 == "Previous") {
                this.man.previousActivity();
            } else if (p1 == "Next") {
                this.man.nextActivity();
            }
            System.out.println(p1);
        }
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

    private class PanelButton {
        int x1;
        int y1;
        int x2;
        int y2;
        String label;

        public boolean contains(int x, int y) {
            Rectangle r = new Rectangle(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1);
            if (r.contains(new Point(x, y))) {
                return true;
            }
            return false;
        }

        public PanelButton(String label, int x1, int y1, int x2, int y2) {
            this.label = label;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public void paint(Graphics g) {
            Font lFont = g.getFont();
            Font nFont = new Font("Dialog", 1, 18);
            g.setFont(nFont);
            Color butC = new Color(181, 175, 139);
            g.setColor(butC);
            g.fillRoundRect(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1, 20, 20);
            g.setColor(Color.BLACK);
            g.drawString(this.label, this.x1, this.y1 + (this.y2 - this.y1) / 2);
            g.setFont(lFont);
        }
    }

}

