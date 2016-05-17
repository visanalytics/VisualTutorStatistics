/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import com.deakin.datavist2.Activity;
import com.deakin.datavist2.AppletMan;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;

public class MoodScroll {
    int x1;
    int y1;
    int x2;
    int y2;
    int listY1;
    int column_num = 9;
    ArrayList<MoodField> graphs;
    boolean[] faces_changed;

    public MoodScroll(int x1, int y1, int x2, int y2, ArrayList<String> moods, Image[] moodImages) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.listY1 = y1;
        this.graphs = this.initMoodFieldList(moods, moodImages);
        this.faces_changed = new boolean[this.graphs.size()];
    }

    public MoodScroll(AppletMan appMan, int x1, int y1, int x2, int y2, Image[] moodImages) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.listY1 = y1;
        ArrayList<String> tempmoods = new ArrayList<String>();
        int i = 0;
        while (i < 99) {
            tempmoods.add("MoodNeutral");
            ++i;
        }
        this.graphs = this.initMoodFieldList(tempmoods, moodImages);
        this.faces_changed = new boolean[this.graphs.size()];
    }

    private ArrayList<MoodField> initMoodFieldList(ArrayList<String> moods, Image[] moodImages) {
        ArrayList<MoodField> temp_graphs = new ArrayList<MoodField>();
        int arrayIndex = 0;
        Random random = new Random();
        while (arrayIndex < moods.size()) {
            String curMood = moods.get(arrayIndex);
            MoodField mf = new MoodField(0, 0, 0, 0, curMood, moodImages);
            temp_graphs.add(mf);
            ++arrayIndex;
        }
        return temp_graphs;
    }

    public void paint(Activity activ, Graphics g) {
        int colNum = 0;
        int rowNum = 0;
        int i = 0;
        while (i < this.graphs.size()) {
            MoodField gr = this.graphs.get(i);
            int x = this.x1 + (this.x2 - this.x1) / this.column_num * colNum;
            int y = this.listY1 + (this.x2 - this.x1) / this.column_num * rowNum;
            int xa = this.x1 + (this.x2 - this.x1) / this.column_num * (colNum + 1);
            int ya = this.listY1 + (this.x2 - this.x1) / this.column_num * (rowNum + 1);
            Color cBack = this.faces_changed[i] ? Color.GRAY : Color.WHITE;
            gr.paint(activ, g, x, y, xa, ya);
            if (colNum + 1 > this.column_num - 1) {
                ++rowNum;
                colNum = 0;
            } else {
                ++colNum;
            }
            ++i;
        }
    }

    private void drawPane(Graphics g, int height, int rowNum) {
        Color c = new Color(37, 63, 96, 255);
        g.setColor(c);
        g.fillRect(this.x1, this.y2, this.x2 - this.x1, height * rowNum);
    }

    public ArrayList<MoodField> getGraphList() {
        return this.graphs;
    }

    public void addMood(String mood) {
        int neutral_left = 0;
        int i = 0;
        while (i < this.faces_changed.length) {
            if (!this.faces_changed[i]) {
                ++neutral_left;
            }
            ++i;
        }
        if (neutral_left > 0) {
            Random r = new Random();
            int arrayIndex = r.nextInt(this.faces_changed.length);
            while (this.faces_changed[arrayIndex]) {
                arrayIndex = r.nextInt(this.faces_changed.length);
            }
            this.graphs.get((int)arrayIndex).STATE = mood;
            this.faces_changed[arrayIndex] = true;
        }
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

    private class MoodField {
        int x1;
        int y1;
        int x2;
        int y2;
        String STATE;
        Image[] moodImages;

        public MoodField(int x1, int y1, int x2, int y2, String MOOD, Image[] moods) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.STATE = MOOD;
            this.moodImages = moods;
        }

        public void paint(Activity activ, Graphics g, int x1, int y1, int x2, int y2) {
            g.setColor(Color.WHITE);
            g.fillRoundRect(x1, y1, x2 - x1, y2 - y1, 20, 20);
            if (this.STATE == "MoodHappy") {
                g.drawImage(this.moodImages[0], x1, y1, x2 - x1, y2 - y1, activ);
            }
            if (this.STATE == "MoodUnhappy") {
                g.drawImage(this.moodImages[1], x1, y1, x2 - x1, y2 - y1, activ);
            }
            if (this.STATE == "MoodNeutral") {
                g.drawImage(this.moodImages[2], x1, y1, x2 - x1, y2 - y1, activ);
            }
        }
    }

}

