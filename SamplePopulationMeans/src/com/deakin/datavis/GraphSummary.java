/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.AppButton;
import com.deakin.datavis.AppButtonClickInterface;
import com.deakin.datavis.AppletMan;
import com.deakin.datavis.ControlPanelBar;
import com.deakin.datavis.DistributionGraphVisColor;
import com.deakin.datavis.Graph;
import com.deakin.datavis.GraphScrollList;
import com.deakin.datavis.InstanceData;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;

public class GraphSummary
extends Activity
implements MouseMotionListener,
MouseListener {
    int windowHeight;
    int windowWidth;
    Image offScreen;
    Graphics bufferGraphics;
    ArrayList graphs = new ArrayList();
    GraphScrollList gsl;
    boolean dragging = false;
    DistributionGraphVisColor dgv;
    AppButton selectAllButton;
    boolean selectButtonClicked = false;
    boolean drawHoverInfo = false;
    int drawHoverX;
    int drawHoverY;
    int lastX;
    int lastY;

    @Override
    public void init(AppletMan m) {
        super.init(m);
        this.windowWidth = this.getSize().width;
        this.windowHeight = this.getSize().height;
        this.setBackground(Color.black);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    public void start() {
        int rSeed = 123456789;
        Random random = new Random();
        ArrayList timesIA = new ArrayList();
        double size = 10.0;
        this.appMan.pb1.title = "Plot sample means to build sampling distribution. (Click on samples)";
        InstanceData data = this.appMan.getInstanceData();
        ArrayList usertimes = data.getCollectedCustomerArrivalTimesArray();
        ArrayList<ArrayList<Integer>> samples_times = data.getSamplesList();
        ArrayList<Integer> samples_means = data.getSamplesMeans();
        this.gsl = new GraphScrollList(0, 0, this.windowWidth / 2, this.windowHeight, samples_times, samples_means);
        this.dgv = new DistributionGraphVisColor((int)((double)(this.windowWidth / 2) + (double)(this.windowWidth / 2) * 0.1), (int)((double)this.windowHeight * 0.05), (int)((double)(this.windowWidth / 2) + (double)(this.windowWidth / 2) * 0.9), (int)((double)this.windowHeight * 0.8), 25, 34, 1);
        this.selectAllButton = new AppButton("Select All", this.gsl.x2 + 20, (int)((double)this.windowHeight * 0.95), this.gsl.x2 + (int)((double)this.windowHeight * 0.3), this.windowHeight, new Color(208, 202, 162), new AppButtonClickInterface(){

            @Override
            public void onClick() {
                int i = 0;
                while (i < GraphSummary.this.gsl.getGraphList().size()) {
                    if (!GraphSummary.this.gsl.graph_selected[i]) {
                        GraphSummary.this.dgv.addData((int)GraphSummary.this.gsl.getGraphList().get(i).getMean(), GraphSummary.this.gsl.getGraphList().get((int)i).cFront);
                    }
                    ++i;
                }
                GraphSummary.this.gsl.selectAllGraphs();
                GraphSummary.this.dgv.initAndShowWeibullCurve();
                GraphSummary.this.selectButtonClicked = true;
            }
        });
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
        this.gsl.paint(g);
        this.dgv.paint(g);
        this.selectAllButton.paint(g, Color.BLACK);
        this.paintInfo(g);
    }

    private void paintInfo(Graphics g) {
        if (this.selectButtonClicked) {
            g.setFont(new Font("Dialog", 0, 12));
            StringBuilder sb = new StringBuilder("Irrespective of the shape of the data, sampling distribution of sample mean is approximately normal.");
            int i = 0;
            int mLineWidth = (this.dgv.x2 - this.dgv.x1) / g.getFontMetrics().charWidth('q');
            while ((i = sb.indexOf(" ", i + mLineWidth)) != -1) {
                sb.replace(i, i + 1, "\n");
            }
            String parsStr = sb.toString();
            g.setFont(new Font("Dialog", 0, 14));
            int lineNum = this.getLineNumber(parsStr);
            int pWidth = this.maxLineWidth(g, parsStr);
            g.setColor(Color.WHITE);
            this.drawString(g, parsStr, this.dgv.x1 + (this.dgv.x2 - this.dgv.x1) / 2 - pWidth / 2, this.dgv.y2);
            if (this.drawHoverInfo) {
                g.setFont(new Font("Dialog", 0, 16));
                String str = "Because of the Central Limit Theorem (CLT)";
                g.setColor(Color.BLUE);
                int width = g.getFontMetrics().stringWidth(str) + 2;
                int height = g.getFontMetrics().getHeight() + 2;
                g.fillRect(this.drawHoverX - width - 2, this.drawHoverY - height - 2, width + 4, height + 4);
                g.setColor(Color.WHITE);
                g.fillRect(this.drawHoverX - width - 1, this.drawHoverY - height - 1, width + 2, height + 2);
                g.setColor(Color.BLACK);
                g.drawString(str, this.drawHoverX - width + 1, this.drawHoverY - height + (int)((double)g.getFontMetrics().getHeight() * 0.75) + 1);
            }
        }
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
        this.dgv.update();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!this.dragging && this.gsl.scrollContains(e.getX(), e.getY())) {
            this.lastY = e.getY();
            this.lastX = e.getX();
            this.dragging = true;
            return;
        }
        if (this.gsl.scrollContains(e.getX(), e.getY())) {
            this.gsl.drag(this.lastX, this.lastY, e.getX(), e.getY());
            this.lastX = e.getX();
            this.lastY = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int eY;
        Rectangle rect = new Rectangle(this.dgv.x1, this.dgv.y2, this.dgv.x2, (int)((double)this.windowHeight * 0.95));
        int eX = e.getX();
        if (rect.contains(eX, eY = e.getY())) {
            this.drawHoverX = eX;
            this.drawHoverY = eY;
        } else {
            this.drawHoverInfo = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Graph gr = this.gsl.click(e.getX(), e.getY());
        if (gr != null) {
            this.dgv.addData((int)gr.getMean(), gr.cFront);
        }
        if (this.selectAllButton.contains(e.getX(), e.getY())) {
            this.selectAllButton.click();
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        this.dragging = false;
    }

}

