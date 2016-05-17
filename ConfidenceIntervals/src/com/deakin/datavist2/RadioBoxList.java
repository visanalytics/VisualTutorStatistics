/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class RadioBoxList {
    public static final String VERTICAL = "Vertical";
    public static final String HORIZONTAL = "Horizontal";
    String orientation;
    int x1;
    int y1;
    int x2;
    int y2;
    String[] answers;
    String question;
    String correct_answer;
    RadioBox[] radioBoxes;
    boolean answered = false;

    public RadioBoxList(int x1, int y1, int x2, int y2, String orientation, String question, String[] answers, String correct_answer) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.orientation = orientation;
        this.answers = answers;
        this.correct_answer = correct_answer;
        this.radioBoxes = new RadioBox[answers.length];
        int i = 0;
        while (i < answers.length) {
            this.radioBoxes[i] = new RadioBox(x1 + 8, y2 - 18 * i, answers[i]);
            ++i;
        }
        StringBuilder sb = new StringBuilder(question);
        int i2 = 0;
        while ((i2 = sb.indexOf(" ", i2 + 50)) != -1) {
            sb.replace(i2, i2 + 1, "\n");
        }
        this.question = sb.toString();
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        this.drawString(g, this.question, this.x1, this.y1 + g.getFontMetrics().getHeight() * (this.getLineNumber(this.question) - 1));
        int i = 0;
        while (i < this.radioBoxes.length) {
            RadioBox rb = this.radioBoxes[i];
            rb.paint(g);
            ++i;
        }
    }

    public boolean onMouseClick(MouseEvent e) {
        int eX = e.getX();
        int eY = e.getY();
        int i = 0;
        while (i < this.radioBoxes.length) {
            if (this.radioBoxes[i].contains(eX, eY)) {
                this.radioBoxes[i].setState("StateChecked");
                int m = 0;
                while (m < this.radioBoxes.length) {
                    if (m != i) {
                        this.radioBoxes[m].setState("StateUnchecked");
                    }
                    ++m;
                }
                this.answered = true;
                return true;
            }
            ++i;
        }
        return false;
    }

    public boolean hasAnswered() {
        return this.answered;
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

    public void checkAnswers() {
        int i = 0;
        while (i < this.radioBoxes.length) {
            if (this.radioBoxes[i].STATE == "StateChecked" && !this.radioBoxes[i].label.equals(this.correct_answer)) {
                this.radioBoxes[i].setState("StateIncorrect");
            }
            if (this.radioBoxes[i].label.equals(this.correct_answer)) {
                this.radioBoxes[i].setState("StateCorrect");
            }
            ++i;
        }
    }

    private class RadioBox {
        static final String STATE_UNCHECKED = "StateUnchecked";
        static final String STATE_CHECKED = "StateChecked";
        static final String STATE_INCORRECT = "StateIncorrect";
        static final String STATE_CORRECT = "StateCorrect";
        String STATE;
        int x;
        int y;
        String label;
        boolean selected;
        static final int RADIO_RADIUS = 8;

        public RadioBox(int x, int y, String label) {
            this.selected = false;
            this.STATE = "StateUnchecked";
            this.x = x;
            this.y = y;
            this.label = label;
        }

        public void paint(Graphics g) {
            g.setColor(Color.GRAY);
            g.fillOval(this.x + 8, this.y, 16, 16);
            if (this.STATE == "StateChecked") {
                g.setColor(Color.WHITE);
            } else if (this.STATE == "StateUnchecked") {
                g.setColor(Color.BLACK);
            } else if (this.STATE == "StateCorrect") {
                g.setColor(Color.GREEN);
            } else if (this.STATE == "StateIncorrect") {
                g.setColor(Color.RED);
            }
            g.fillOval(this.x + 12, this.y + 4, 8, 8);
            g.setColor(Color.WHITE);
            RadioBoxList.this.drawString(g, this.label, this.x + 24, this.y);
        }

        public void setState(String value) {
            this.STATE = value;
        }

        public boolean contains(int x, int y) {
            Rectangle r = new Rectangle((int)((double)this.x + 12.0), (int)((double)this.y + 4.0), 16, 8);
            if (r.contains(x, y)) {
                return true;
            }
            return false;
        }
    }

}

