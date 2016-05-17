/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import com.deakin.datavist2.Activity;
import com.deakin.datavist2.AppButton;
import com.deakin.datavist2.AppButtonClickInterface;
import com.deakin.datavist2.AppletMan;
import com.deakin.datavist2.ControlPanelBar;
import com.deakin.datavist2.InstanceData;
import com.deakin.datavist2.MoodScroll;
import com.deakin.datavist2.ProportionBar;
import com.deakin.datavist2.Question;
import com.deakin.datavist2.QuestionTextBox;
import com.deakin.datavist2.RadioBoxList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class Screen2
extends Activity
implements KeyListener,
MouseListener,
MouseMotionListener {
    int windowHeight;
    int windowWidth;
    Image offScreen;
    Graphics bufferGraphics;
    AppButton checkAnswersBtn;
    QuestionTextBox q1;
    Question q1q;
    QuestionTextBox q2;
    Question q2q;
    QuestionTextBox q3;
    Question q3q;
    AtomicReference<QuestionTextBox> currentQBox;
    QuestionTextBox[] qBoxes;
    MoodScroll mScroll;
    ProportionBar propBar;
    RadioBoxList radioList1;
    RadioBoxList radioList2;
    Image[] moodImages;
    int changeScreenDelay = 0;
    int MAX_DELAY = 80;
    boolean changing_screens = false;

    @Override
    public void init(AppletMan m) {
        super.init(m);
        this.windowWidth = this.getSize().width;
        this.windowHeight = this.getSize().height;
        this.setBackground(Color.black);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    public void start() {
        this.appMan.pb1.title = "Explore the proportions";
        int rSeed = 123456789;
        Random random = new Random();
        ArrayList timesIA = new ArrayList();
        double size = 10.0;
        InstanceData data = this.appMan.getInstanceData();
        Image moodHappy = this.getImage(this.getDocumentBase(), "res/images/happy_face.png");
        Image moodUnhappy = this.getImage(this.getDocumentBase(), "res/images/unhappy_face.png");
        Image moodNeutral = this.getImage(this.getDocumentBase(), "res/images/neutral_face.png");
        this.moodImages = new Image[]{moodHappy, moodUnhappy, moodNeutral};
        this.mScroll = new MoodScroll(this.appMan, 32, 65, 227, 325, this.moodImages);
        ArrayList<String> arm = this.appMan.instData.customer_collected_moods;
        double happyNum = 0.0;
        double unhappyNum = 0.0;
        int i = 0;
        while (i < arm.size()) {
            String m = arm.get(i);
            if (m == "MoodHappy") {
                happyNum += 1.0;
            }
            if (m == "MoodUnhappy") {
                unhappyNum += 1.0;
            }
            ++i;
        }
        this.propBar = new ProportionBar(this.mScroll.x1, this.mScroll.y2, this.mScroll.x2, this.mScroll.y2 + 26, happyNum / (happyNum + unhappyNum), unhappyNum / (happyNum + unhappyNum));
        this.radioList1 = new RadioBoxList((int)((double)this.windowWidth * 0.5), (int)((double)this.windowHeight * 0.02), (int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.35), "Vertical", "The proportion of unhappy customers from your sample is " + String.format("%.2f", unhappyNum / (happyNum + unhappyNum) * 100.0) + "% ,\u00a0What would be the proportion of ALL unhappy customers who  visit the store?", new String[]{"Exactly the same as my sample proportion", "Approximately the same as my sample", "Not enough information to say"}, "Approximately the same as my sample");
        this.radioList2 = new RadioBoxList((int)((double)this.windowWidth * 0.5), (int)((double)this.windowHeight * 0.32), (int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.65), "Vertical", "What do we call the average error we will be making when we use the sample proportion as the population proportion?", new String[]{"Standard Deviation", "Sampling Error", "Standard Error"}, "Standard Error");
        i = 0;
        while (i < data.getCollectedCustomerMoodsArray().size()) {
            String mood = data.getCollectedCustomerMoodsArray().get(i);
            this.mScroll.addMood(mood);
            ++i;
        }
        String question1S = "How confident are you that a sample mean of inter-arrival time will fall between 20 and 40 seconds?";
        StringBuilder sb = new StringBuilder(question1S);
        int i2 = 0;
        while ((i2 = sb.indexOf(" ", i2 + 50)) != -1) {
            sb.replace(i2, i2 + 1, "\n");
        }
        question1S = sb.toString();
        String question2S = "What is the risk a sample mean of inter-arrival time will be less than 32 seconds?";
        sb = new StringBuilder(question2S);
        i2 = 0;
        while ((i2 = sb.indexOf(" ", i2 + 50)) != -1) {
            sb.replace(i2, i2 + 1, "\n");
        }
        question2S = sb.toString();
        String question3S = "What is the risk a sample mean of inter-arrival time will be more than 32 seconds?";
        sb = new StringBuilder(question3S);
        i2 = 0;
        while ((i2 = sb.indexOf(" ", i2 + 50)) != -1) {
            sb.replace(i2, i2 + 1, "\n");
        }
        question3S = sb.toString();
        this.q1q = new Question(question1S, new String[0], String.valueOf(31));
        this.q1 = new QuestionTextBox((int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.2), (int)((double)this.windowWidth * 0.075), (int)((double)this.windowHeight * 0.075), 30.0, 31.0);
        this.q2q = new Question(question2S, new String[0], String.valueOf(31));
        this.q2 = new QuestionTextBox((int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.4), (int)((double)this.windowWidth * 0.075), (int)((double)this.windowHeight * 0.075), 30.0, 31.0);
        this.q3q = new Question(question3S, new String[0], String.valueOf(31));
        this.q3 = new QuestionTextBox((int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.6), (int)((double)this.windowWidth * 0.075), (int)((double)this.windowHeight * 0.075), 30.0, 31.0);
        this.qBoxes = new QuestionTextBox[]{this.q1, this.q2, this.q3};
        this.currentQBox = new AtomicReference<QuestionTextBox>(this.q1);
        this.currentQBox.get().selected = true;
        this.checkAnswersBtn = new AppButton("Check Answers", (int)((double)this.windowWidth * 0.68), (int)((double)this.windowHeight * 0.85), (int)((double)this.windowWidth * 0.82), (int)((double)this.windowHeight * 0.95), new Color(208, 202, 162), new AppButtonClickInterface(){

            @Override
            public void onClick() {
                QuestionTextBox[] arrquestionTextBox = Screen2.this.qBoxes;
                int n = arrquestionTextBox.length;
                int n2 = 0;
                while (n2 < n) {
                    QuestionTextBox qtb = arrquestionTextBox[n2];
                    Screen2.this.radioList1.checkAnswers();
                    Screen2.this.radioList2.checkAnswers();
                    Screen2.this.changing_screens = true;
                    ++n2;
                }
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
        this.checkAnswersBtn.paint(g, Color.BLACK);
        this.mScroll.paint(this, g);
        this.propBar.paint(g);
        this.paintRatioString(g);
        this.radioList1.paint(g);
        if (this.radioList1.hasAnswered()) {
            this.radioList2.paint(g);
        }
    }

    private void paintRatioString(Graphics g) {
        ArrayList<String> arm = this.appMan.instData.customer_collected_moods;
        double happyNum = 0.0;
        double unhappyNum = 0.0;
        int i = 0;
        while (i < arm.size()) {
            String m = arm.get(i);
            if (m == "MoodHappy") {
                happyNum += 1.0;
            }
            if (m == "MoodUnhappy") {
                unhappyNum += 1.0;
            }
            ++i;
        }
        String s = "Happy";
        String s2 = String.valueOf(happyNum / (happyNum + unhappyNum));
        s2 = String.format("%.2f", happyNum / (happyNum + unhappyNum));
        g.setColor(Color.WHITE);
        g.drawString(s, this.propBar.x1 - g.getFontMetrics().stringWidth(s) / 2, this.propBar.y2 + g.getFontMetrics().getHeight());
        g.drawString(s2, this.propBar.x1 - g.getFontMetrics().stringWidth(s2) / 2, this.propBar.y2 + g.getFontMetrics().getHeight() * 2);
        String s3 = "Unhappy";
        String s4 = String.valueOf(unhappyNum / (happyNum + unhappyNum));
        s4 = String.format("%.2f", unhappyNum / (happyNum + unhappyNum));
        g.setColor(Color.WHITE);
        g.drawString(s3, this.propBar.x2 - g.getFontMetrics().stringWidth(s3) / 2, this.propBar.y2 + g.getFontMetrics().getHeight());
        g.drawString(s4, this.propBar.x2 - g.getFontMetrics().stringWidth(s4) / 2, this.propBar.y2 + g.getFontMetrics().getHeight() * 2);
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
        if (this.changing_screens) {
            if (this.changeScreenDelay >= this.MAX_DELAY) {
                this.appMan.nextActivity();
            } else {
                ++this.changeScreenDelay;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(String.valueOf(e.getKeyCode()) + "  +  " + 8);
        if (e.getKeyCode() == 8) {
            this.currentQBox.get().backspace();
        }
        switch (e.getKeyChar()) {
            case '0': {
                this.currentQBox.get().append('0');
                break;
            }
            case '1': {
                this.currentQBox.get().append('1');
                break;
            }
            case '2': {
                this.currentQBox.get().append('2');
                break;
            }
            case '3': {
                this.currentQBox.get().append('3');
                break;
            }
            case '4': {
                this.currentQBox.get().append('4');
                break;
            }
            case '5': {
                this.currentQBox.get().append('5');
                break;
            }
            case '6': {
                this.currentQBox.get().append('6');
                break;
            }
            case '7': {
                this.currentQBox.get().append('7');
                break;
            }
            case '8': {
                this.currentQBox.get().append('8');
                break;
            }
            case '9': {
                this.currentQBox.get().append('9');
                break;
            }
            case '.': {
                this.currentQBox.get().append('.');
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        QuestionTextBox[] arrquestionTextBox = this.qBoxes;
        int n = arrquestionTextBox.length;
        int n2 = 0;
        while (n2 < n) {
            QuestionTextBox q = arrquestionTextBox[n2];
            if (q.contains(x, y)) {
                this.currentQBox.get().selected = false;
                q.selected = true;
                System.out.println("Sleected");
                this.currentQBox.set(q);
            } else {
                q.selected = false;
                System.out.println("not select");
            }
            ++n2;
        }
        this.radioList1.onMouseClick(e);
        if (this.radioList1.hasAnswered()) {
            this.radioList2.onMouseClick(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int y;
        int x = e.getX();
        if (this.checkAnswersBtn.contains(x, y = e.getY())) {
            this.checkAnswersBtn.click();
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

}

