/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

public class QuestionTextBox {
    public static final int NO_ENTRY = 9999;
    int x;
    int y;
    int width;
    int height;
    double current_answer;
    double correct_answer;
    String current_answerS;
    String correct_answerS;
    int line_delay = 0;
    int max_line_delay = 10;
    boolean show_line;
    boolean selected = false;
    int correctState = 0;

    public QuestionTextBox(int x, int y, int width, int height, double initialAnswer, double correctAnswer) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (initialAnswer == 9999.0) {
            this.current_answer = 0.0;
            this.current_answerS = "";
        } else {
            this.current_answer = initialAnswer;
            this.current_answerS = String.valueOf(this.current_answer);
        }
        this.correct_answer = correctAnswer;
        this.correct_answerS = String.valueOf(this.correct_answer);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        int disp = 2;
        g.setColor(Color.BLACK);
        g.fillRect(this.x - this.width / 2, this.y - this.height / 2, this.width, this.height);
        if (this.correctState == 0) {
            g.setColor(Color.WHITE);
        } else if (this.correctState == 1) {
            g.setColor(Color.RED);
        } else if (this.correctState == 2) {
            g.setColor(Color.ORANGE);
        } else if (this.correctState == 3) {
            g.setColor(Color.GREEN);
        }
        g.fillRect(this.x - this.width / 2 + disp, this.y - this.height / 2 + disp, this.width - disp * 2, this.height - disp * 2);
        if (this.line_delay >= this.max_line_delay) {
            this.show_line = !this.show_line;
            this.line_delay = 0;
        } else {
            ++this.line_delay;
        }
        if (this.show_line && this.selected) {
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(Color.BLACK);
            g2.drawLine(this.x + (g.getFontMetrics().stringWidth(this.current_answerS) / 2 + 1), this.y - g.getFontMetrics().getHeight() / 2, this.x + (g.getFontMetrics().stringWidth(this.current_answerS) / 2 + 1), this.y + g.getFontMetrics().getHeight() / 2);
        }
        g.setColor(Color.BLACK);
        g.drawString(this.current_answerS, this.x - g.getFontMetrics().stringWidth(this.current_answerS) / 2, this.y + g.getFontMetrics().getHeight() / 4);
    }

    public void backspace() {
        StringBuilder sb = new StringBuilder(this.current_answerS);
        if (this.current_answerS.length() <= 0) {
            this.current_answerS = "";
            this.current_answer = 0.0;
        } else {
            sb.deleteCharAt(sb.length() - 1);
            this.current_answerS = sb.toString();
            if (this.current_answerS.length() <= 0) {
                this.current_answerS = "";
                this.current_answer = 0.0;
            } else {
                this.current_answer = Double.parseDouble(this.current_answerS);
            }
        }
    }

    public void append(char value) {
        StringBuilder sb = new StringBuilder(this.current_answerS);
        if (this.current_answerS.length() < 5) {
            sb.append(value);
            this.current_answerS = sb.toString();
            this.current_answer = Double.parseDouble(this.current_answerS);
        }
    }

    public boolean contains(int x, int y) {
        Rectangle bounds = new Rectangle(this.x - this.width / 2, this.y - this.height / 2, this.width, this.height);
        if (bounds.contains(x, y)) {
            return true;
        }
        return false;
    }

    public void verifyAnswer() {
        this.correctState = 0;
        this.correctState = this.correct_answer == this.current_answer || Math.abs(this.correct_answer - this.current_answer) < 0.5 ? 3 : (Math.abs(this.correct_answer - this.current_answer) < 2.0 ? 2 : 1);
    }
}

