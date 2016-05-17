/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.Graph;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class GraphScrollList {
    int x1;
    int y1;
    int x2;
    int y2;
    int listY1;
    int column_num = 4;
    ArrayList<Graph> graphs;
    boolean[] graph_selected;

    public GraphScrollList(int x1, int y1, int x2, int y2, ArrayList<ArrayList<Integer>> times, ArrayList<Integer> means) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.graphs = this.initGraphList(times, means);
        this.graph_selected = new boolean[this.graphs.size()];
    }

    private ArrayList<Graph> initGraphList(ArrayList<ArrayList<Integer>> samples_times, ArrayList<Integer> means) {
        ArrayList<Graph> temp_graphs = new ArrayList<Graph>();
        int arrayIndex = 0;
        Random random = new Random();
        while (arrayIndex < samples_times.size()) {
            ArrayList<Integer> timesIA = samples_times.get(arrayIndex);
            int mean = means.get(arrayIndex);
            Color c2 = new Color(255, 255, 255, 120);
            Color c1 = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 255);
            Graph graph = new Graph(0, 0, 0, 0, timesIA, mean, c1, c2, false, 0, 60, 5);
            temp_graphs.add(graph);
            ++arrayIndex;
        }
        return temp_graphs;
    }

    public void paint(Graphics g) {
        int colNum = 0;
        int rowNum = 0;
        int i = 0;
        while (i < this.graphs.size()) {
            Graph gr = this.graphs.get(i);
            int x = this.x1 + (this.x2 - this.x1) / this.column_num * colNum;
            int y = this.listY1 + (this.x2 - this.x1) / this.column_num * rowNum;
            int xa = this.x1 + (this.x2 - this.x1) / this.column_num * (colNum + 1);
            int ya = this.listY1 + (this.x2 - this.x1) / this.column_num * (rowNum + 1);
            Color cBack = this.graph_selected[i] ? Color.GRAY : Color.WHITE;
            gr.paint(g, x, y, xa, ya, cBack);
            if (colNum + 1 > this.column_num - 1) {
                ++rowNum;
                colNum = 0;
            } else {
                ++colNum;
            }
            ++i;
        }
        this.paintScrollbar(g);
    }

    public ArrayList<Graph> getGraphList() {
        return this.graphs;
    }

    public void selectAllGraphs() {
        int i = 0;
        while (i < this.graph_selected.length) {
            this.graph_selected[i] = true;
            ++i;
        }
    }

    public Graph click(int x, int y) {
        int colNum = 0;
        int rowNum = 0;
        int i = 0;
        while (i < this.graphs.size()) {
            Graph gr = this.graphs.get(i);
            int xa = this.x1 + (this.x2 - this.x1) / this.column_num * colNum;
            int ya = this.listY1 + (this.x2 - this.x1) / this.column_num * rowNum;
            int xb = this.x1 + (this.x2 - this.x1) / this.column_num * (colNum + 1);
            int yb = this.listY1 + (this.x2 - this.x1) / this.column_num * (rowNum + 1);
            Rectangle r = new Rectangle(xa, ya, xb - xa, yb - ya);
            if (r.contains(x, y) && !this.graph_selected[i]) {
                this.graph_selected[i] = true;
                return gr;
            }
            if (colNum + 1 > this.column_num - 1) {
                ++rowNum;
                colNum = 0;
            } else {
                ++colNum;
            }
            ++i;
        }
        return null;
    }

    private void paintScrollbar(Graphics g) {
        double height = this.y2 - this.y1;
        double ratio = Math.ceil(this.graphs.size() / this.column_num) * (double)((this.x2 - this.x1) / this.column_num) / height;
        g.setColor(Color.BLACK);
        g.fillRect(this.x2, this.y1, 20, this.y2 - this.y1);
        double size = (double)(this.y2 - this.y1) / ratio;
        g.setColor(Color.ORANGE);
        g.fillRect(this.x2, (int)((double)this.y1 - (double)this.listY1 / ratio), 20, (int)size);
    }

    public void drag(int lastX, int lastY, int curX, int curY) {
        int dist = curY - lastY;
        double height = this.y2 - this.y1;
        double ratio = Math.ceil(this.graphs.size() / this.column_num) * (double)((this.x2 - this.x1) / this.column_num) / height;
        this.listY1 = (double)this.listY1 - (double)dist * ratio >= (double)this.y1 ? this.y1 : ((double)this.listY1 + Math.ceil(this.graphs.size() / this.column_num) * (double)((this.x2 - this.x1) / this.column_num) - (double)dist * ratio <= (double)this.y2 ? (int)(- Math.ceil(this.graphs.size() / this.column_num) * (double)((this.x2 - this.x1) / this.column_num) - (double)(this.y2 - this.y1)) : (int)((double)this.listY1 - (double)dist * ratio));
    }

    public boolean scrollContains(int x, int y) {
        Rectangle r = new Rectangle(this.x2, this.y1, 20, this.y2 - this.y1);
        if (r.contains(x, y)) {
            return true;
        }
        return false;
    }
}

