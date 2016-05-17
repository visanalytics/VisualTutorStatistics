/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

public class CustomComboBox {
    int x1;
    int y1;
    int x2;
    int y2;
    String[] answers;
    int current_answer_index = -1;
    String correct_answer;
    Rectangle disp_rect;
    Rectangle select_rect;
    int select_rect_width = 20;
    boolean selecting = false;
    int clicked_x = 0;
    int clicked_y = 0;

    public CustomComboBox(int x1, int y1, int x2, int y2, String[] answers, String correct_answer) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.select_rect_width = y2 - y1;
        this.disp_rect = new Rectangle(x1, y1, x2 - x1 - this.select_rect_width, y2 - y1);
        this.select_rect = new Rectangle(x1 + (x2 - x1 - this.select_rect_width), y1, this.select_rect_width, y2 - y1);
        this.answers = answers;
        this.correct_answer = correct_answer;
    }

    public void setAnswers(String[] values) {
        this.answers = values;
    }

    public void setCorrectAnswer(String value) {
        this.correct_answer = value;
    }

    public void resetAnswer() {
        this.current_answer_index = -1;
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(this.x1 - 2, this.y1 - 2, this.x2 - this.x1 + 4, this.y2 - this.y1 + 4);
        if (this.current_answer_index != -1) {
            if (this.answers[this.current_answer_index] == this.correct_answer) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.RED);
            }
        } else {
            g.setColor(Color.WHITE);
        }
        g.fillRect(this.disp_rect.x, this.disp_rect.y, this.disp_rect.width, this.disp_rect.height);
        g.setColor(Color.BLACK);
        if (this.current_answer_index != -1) {
            int string_x = this.disp_rect.x + this.disp_rect.width / 2 - g.getFontMetrics().stringWidth(this.answers[this.current_answer_index]) / 2;
            int string_y = this.disp_rect.y + this.disp_rect.height / 2 + g.getFontMetrics().getHeight() / 2;
            g.drawString(this.answers[this.current_answer_index], string_x, string_y);
        }
        g.setColor(Color.GRAY);
        g.fillRect(this.select_rect.x, this.select_rect.y, this.select_rect.width, this.select_rect.height);
        int[] xs = new int[]{(int)((double)this.select_rect.x + (double)this.select_rect.width * 0.1), (int)((double)this.select_rect.x + (double)this.select_rect.width * 0.5), (int)((double)this.select_rect.x + (double)this.select_rect.width * 0.9)};
        int[] ys = new int[]{(int)((double)this.select_rect.y + (double)this.select_rect.height * 0.9), (int)((double)this.select_rect.y + (double)this.select_rect.height * 0.1), (int)((double)this.select_rect.y + (double)this.select_rect.height * 0.9)};
        Polygon p = new Polygon(xs, ys, 3);
        g.setColor(Color.BLACK);
        g.fillPolygon(p);
        if (this.selecting) {
            int i = 0;
            while (i < this.answers.length) {
                Rectangle temp_rect = new Rectangle(this.disp_rect.x, this.disp_rect.y - this.disp_rect.height * (i + 1), this.disp_rect.width, this.disp_rect.height);
                g.setColor(Color.BLACK);
                g.fillRect(temp_rect.x - 2, temp_rect.y - 2, temp_rect.width + 4, temp_rect.height + 4);
                g.setColor(Color.WHITE);
                g.fillRect(temp_rect.x, temp_rect.y, temp_rect.width, temp_rect.height);
                int sx = temp_rect.x + temp_rect.width / 2 - g.getFontMetrics().stringWidth(this.answers[i]) / 2;
                int sy = temp_rect.y + temp_rect.height / 2 + g.getFontMetrics().getHeight() / 2;
                g.setColor(Color.BLACK);
                g.drawString(this.answers[i], sx, sy);
                if (temp_rect.contains(this.clicked_x, this.clicked_y)) {
                    this.current_answer_index = i;
                    this.selecting = false;
                }
                ++i;
            }
        }
    }

    public void step() {
    }

    public void onClick(int x, int y) {
        this.clicked_x = x;
        this.clicked_y = y;
        if (this.select_rect.contains(x, y)) {
            this.selecting = !this.selecting;
        }
    }
}

