/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  umontreal.iro.lecuyer.probdist.NormalDist
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.AppButton;
import com.deakin.datavis.AppButtonClickInterface;
import com.deakin.datavis.AppletMan;
import com.deakin.datavis.CurveDistributionInteractive;
import com.deakin.datavis.Graph;
import com.deakin.datavis.InstanceData;
import com.deakin.datavis.Question;
import com.deakin.datavis.QuestionTextBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import umontreal.iro.lecuyer.probdist.NormalDist;

public class Screen6
extends Activity
implements KeyListener,
MouseListener,
MouseMotionListener {
    int windowHeight;
    int windowWidth;
    Image offScreen;
    Graphics bufferGraphics;
    ArrayList graphs = new ArrayList();
    Graph samples_hist;
    AppButton checkAnswersBtn;
    QuestionTextBox q1;
    Question q1q;
    QuestionTextBox q2;
    Question q2q;
    QuestionTextBox q3;
    Question q3q;
    AtomicReference<QuestionTextBox> currentQBox;
    QuestionTextBox[] qBoxes;
    CurveDistributionInteractive cdi;
    CDISlider point1Slid;
    CDISlider point2Slid;

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
        int rSeed = 123456789;
        Random random = new Random();
        ArrayList timesIA = new ArrayList();
        double size = 10.0;
        InstanceData data = this.appMan.getInstanceData();
        ArrayList usertimes = data.getCollectedCustomerArrivalTimesArray();
        ArrayList<ArrayList<Integer>> samples_times = data.getSamplesList();
        ArrayList<Integer> samples_means = data.getSamplesMeans();
        int mean = 0;
        Iterator<Integer> iterator = samples_means.iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            mean += i;
        }
        this.samples_hist = new Graph(0, 0, 0, 0, samples_means, mean /= samples_means.size(), Color.YELLOW, new Color(208, 202, 162), true, 23, 35, 1);
        this.cdi = new CurveDistributionInteractive(0, 0, (int)((double)this.windowWidth * 0.45), (int)((double)this.windowHeight * 0.7), 23, 35, 3.0, 6.0, this.samples_hist);
        this.point1Slid = new CDISlider("Point 1:", 0, (int)((double)this.windowHeight * 0.7), (int)((double)this.windowWidth * 0.45), (int)((double)this.windowHeight * 0.85), 23, 35, 0.1, 3, 1);
        this.point2Slid = new CDISlider("Point 2:", 0, (int)((double)this.windowHeight * 0.85), (int)((double)this.windowWidth * 0.45), this.windowHeight, 23, 35, 0.1, 6, 1);
        int aW = this.point1Slid.abox.x2 - this.point1Slid.abox.x1;
        this.point1Slid.abox.x2 += aW;
        this.point1Slid.abox.x1 = this.point1Slid.abox.x2 - aW;
        this.point2Slid.abox.x2 = this.point1Slid.abox.x2;
        this.point2Slid.abox.x1 = this.point1Slid.abox.x1;
        String question1S = "How confident are you that a sample mean of inter-arrival time will fall between 20 and 40 seconds?";
        StringBuilder sb = new StringBuilder(question1S);
        int i = 0;
        while ((i = sb.indexOf(" ", i + 50)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        question1S = sb.toString();
        String question2S = "What is the risk a sample mean of inter-arrival time will be less than 32 seconds?";
        sb = new StringBuilder(question2S);
        i = 0;
        while ((i = sb.indexOf(" ", i + 50)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        question2S = sb.toString();
        String question3S = "What is the risk a sample mean of inter-arrival time will be more than 32 seconds?";
        sb = new StringBuilder(question3S);
        i = 0;
        while ((i = sb.indexOf(" ", i + 50)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        question3S = sb.toString();
        double mu = this.samples_hist.getNormalDist().getMean();
        double sigma = this.samples_hist.getNormalDist().getStandardDeviation();
        this.q1q = new Question(question1S, new String[0], String.valueOf(this.samples_hist.min));
        this.q1 = new QuestionTextBox((int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.3), (int)((double)this.windowWidth * 0.075), (int)((double)this.windowHeight * 0.075), 9999.0, 100.0);
        this.samples_hist.getNormalDist();
        this.q2q = new Question(question2S, new String[0], String.valueOf(NormalDist.cdf((double)mu, (double)sigma, (double)32.0) * 100.0));
        this.samples_hist.getNormalDist();
        this.q2 = new QuestionTextBox((int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.5), (int)((double)this.windowWidth * 0.075), (int)((double)this.windowHeight * 0.075), 9999.0, NormalDist.cdf((double)mu, (double)sigma, (double)32.0) * 100.0);
        this.samples_hist.getNormalDist();
        this.q3q = new Question(question3S, new String[0], String.valueOf((1.0 - NormalDist.cdf((double)mu, (double)sigma, (double)32.0)) * 100.0));
        this.samples_hist.getNormalDist();
        this.q3 = new QuestionTextBox((int)((double)this.windowWidth * 0.9), (int)((double)this.windowHeight * 0.7), (int)((double)this.windowWidth * 0.075), (int)((double)this.windowHeight * 0.075), 9999.0, (1.0 - NormalDist.cdf((double)mu, (double)sigma, (double)32.0)) * 100.0);
        this.qBoxes = new QuestionTextBox[]{this.q1, this.q2, this.q3};
        this.currentQBox = new AtomicReference<QuestionTextBox>(this.q1);
        this.currentQBox.get().selected = true;
        this.checkAnswersBtn = new AppButton("Check Answers", (int)((double)this.windowWidth * 0.68), (int)((double)this.windowHeight * 0.85), (int)((double)this.windowWidth * 0.82), (int)((double)this.windowHeight * 0.95), new Color(208, 202, 162), new AppButtonClickInterface(){

            @Override
            public void onClick() {
                QuestionTextBox[] arrquestionTextBox = Screen6.this.qBoxes;
                int n = arrquestionTextBox.length;
                int n2 = 0;
                while (n2 < n) {
                    QuestionTextBox qtb = arrquestionTextBox[n2];
                    qtb.verifyAnswer();
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
        this.samples_hist.paint(g, 30);
        this.paintQuestion(g);
        this.checkAnswersBtn.paint(g, Color.BLACK);
        this.point1Slid.paint(g);
        this.point2Slid.paint(g);
        this.cdi.paint(g, 30);
    }

    private void paintQuestion(Graphics g) {
        Font lastFont = g.getFont();
        String q1S = this.q1q.getQuestion();
        g.setColor(Color.WHITE);
        this.drawString(g, q1S, this.cdi.x2 + 20, this.q1.y - g.getFontMetrics().getHeight() * this.getLineNumber(q1S) / 2);
        this.q1.paint(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", 0, 16));
        this.drawString(g, "%", this.q1.x + this.q1.width / 2 + 2, this.q1.y - (int)((double)g.getFontMetrics().getHeight() * 0.75));
        g.setFont(lastFont);
        String q2S = this.q2q.getQuestion();
        g.setColor(Color.WHITE);
        this.drawString(g, q2S, this.cdi.x2 + 20, this.q2.y - g.getFontMetrics().getHeight() * this.getLineNumber(q2S) / 2);
        this.q2.paint(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", 0, 16));
        this.drawString(g, "%", this.q2.x + this.q2.width / 2 + 2, this.q2.y - (int)((double)g.getFontMetrics().getHeight() * 0.75));
        g.setFont(lastFont);
        String q3S = this.q3q.getQuestion();
        g.setColor(Color.WHITE);
        this.drawString(g, q3S, this.cdi.x2 + 20, this.q3.y - g.getFontMetrics().getHeight() * this.getLineNumber(q3S) / 2);
        this.q3.paint(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", 0, 16));
        this.drawString(g, "%", this.q3.x + this.q3.width / 2 + 2, this.q3.y - (int)((double)g.getFontMetrics().getHeight() * 0.75));
        g.setFont(lastFont);
        String inst = "Use the sliders under the graph to derive answers to the following questions.";
        StringBuilder sb = new StringBuilder(inst);
        int i = 0;
        while ((i = sb.indexOf(" ", i + 40)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        inst = sb.toString();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", 0, 18));
        this.drawString(g, inst, this.cdi.x2 + (this.windowWidth - this.cdi.x2) / 2 - this.maxLineWidth(g, inst) / 2, g.getFontMetrics().getHeight());
        g.setFont(lastFont);
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
        this.point1Slid.step();
        this.point2Slid.step();
        this.cdi.step();
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
        this.cdi.setPointX1(this.point1Slid.onMouseDragged(e));
        this.cdi.setPointX2(this.point2Slid.onMouseDragged(e));
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    private class CDISlider {
        int x1;
        int y1;
        int x2;
        int y2;
        double min;
        double max;
        double current;
        double distance;
        int offset;
        int circle_radius;
        String title;
        AnswerBox abox;

        public CDISlider(String title, int x1, int y1, int x2, int y2, int min, int max, double distance, int start, int answer) {
            this.offset = 2;
            this.circle_radius = 15;
            this.title = title;
            int bLength = (x2 - x1) / 8 * 6 / (max - min) * (max - min);
            int n = x1 + (x2 - x1) / 8;
            int x = n + (x2 - n - bLength) / 2;
            this.abox = new AnswerBox(x1 + (x2 - x1) / 8 * 7, y1, x + bLength + (x2 - x - bLength) / 2, y2, start, answer);
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.min = min;
            this.max = max;
            this.distance = distance;
            this.current = start;
            this.circle_radius = 15;
        }

        public void paint(Graphics g) {
            Color back1 = new Color(37, 64, 97, 255);
            g.setColor(back1);
            g.fillRect(this.x1, this.y1, (this.x2 - this.x1) / 8, this.y2 - this.y1);
            Color back2 = new Color(55, 96, 146, 255);
            g.setColor(back2);
            g.fillRect(this.x1 + (this.x2 - this.x1) / 8, this.y1, this.x2 - this.x1 - (this.x2 - this.x1) / 8, this.y2 - this.y1);
            g.setColor(Color.WHITE);
            g.drawString(this.title, this.x1 + (this.x2 - this.x1) / 8 / 2 - g.getFontMetrics().stringWidth(this.title) / 2, this.y1 + (this.y2 - this.y1) / 2);
            Color boxC = new Color(92, 146, 211, 255);
            g.setColor(boxC);
            double bLength = (double)((int)((double)(this.x2 - this.x1) * 0.75)) / (this.max - this.min) * (this.max - this.min);
            int n = this.x1 + (this.x2 - this.x1) / 8;
            int n2 = this.x1 + (this.x2 - this.x1) / 8 * 2;
            int x = (int)((double)n + ((double)(this.x2 - n2) - bLength) / 2.0);
            g.fillRect(x, this.y1 + (this.y2 - this.y1) / 2 - this.offset, (int)bLength, this.offset * 2);
            Color slidC = new Color(72, 111, 154, 255);
            g.setColor(slidC);
            int dist = (int)((double)((this.x2 - this.x1) / 8 * 6) / (this.max - this.min) * this.current);
            g.fillOval(x + dist - this.circle_radius, this.y1 + (this.y2 - this.y1) / 2 - this.circle_radius, this.circle_radius * 2, this.circle_radius * 2);
            this.abox.setCurrentAnswer(this.min + this.current);
            this.abox.paint(g);
        }

        public void step() {
        }

        public double onMouseDragged(MouseEvent e) {
            double x = e.getX();
            double y = e.getY();
            double bLength = (this.x2 - this.x1) / 8 * 6;
            double n = this.x1 + (this.x2 - this.x1) / 8;
            double n2 = this.x1 + (this.x2 - this.x1) / 8 * 2;
            double m = n + ((double)this.x2 - n2 - bLength);
            double pdist = bLength / ((this.max - this.min) / this.distance);
            if (y > (double)this.y1 && y < (double)this.y2) {
                this.current = x <= m ? 0.0 : (x > m + bLength ? this.max - this.min : (this.roundUp(x * 10.0, pdist * 10.0) - m * 10.0) / 100.0 / pdist);
                this.abox.setCurrentAnswer(this.min + this.current);
            }
            return this.current;
        }

        double roundUp(double x, double f) {
            return (int)(f * Math.ceil(x / f));
        }

        double round(double num, double closestto) {
            double temp = num % closestto;
            if (temp < closestto / 2.0) {
                return num - temp;
            }
            return num + closestto - temp;
        }

        public void verifyAnswer() {
            this.abox.verifyAnswer();
        }

        private class AnswerBox {
            int x1;
            int y1;
            int x2;
            int y2;
            double currentAnswer;
            int finalAnswer;
            private static final int STATE_UNVERIFIED = 0;
            private static final int STATE_VERIFIED = 1;
            int state;
            boolean correct;

            public AnswerBox(int x1, int y1, int x2, int y2, int startingAnswer, int finalAnswer) {
                this.correct = false;
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
                this.finalAnswer = finalAnswer;
                this.currentAnswer = startingAnswer;
                this.state = 0;
            }

            public void paint(Graphics g) {
                Color c;
                g.setColor(new Color(37, 63, 96));
                int radius = 15;
                g.fillRect(this.x1 + (this.x2 - this.x1) / 2 - radius * 2, this.y1 + (this.y2 - this.y1) / 2 - radius, radius * 4, radius * 2);
                int disp = 3;
                if (this.state == 1) {
                    c = this.currentAnswer == (double)this.finalAnswer ? Color.GREEN : (Math.abs((double)this.finalAnswer - this.currentAnswer) < 3.0 ? Color.ORANGE : Color.RED);
                    g.setColor(c);
                } else {
                    c = new Color(78, 128, 189, 255);
                }
                g.setColor(c);
                g.fillRect(this.x1 + (this.x2 - this.x1) / 2 - radius * 2 + disp, this.y1 + (this.y2 - this.y1) / 2 - radius + disp, radius * 4 - disp * 2, radius * 2 - disp * 2);
                if (this.currentAnswer == (double)this.finalAnswer && this.state == 1) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                String s = String.format("%.1f", this.currentAnswer);
                g.drawString(s, this.x1 + (this.x2 - this.x1) / 2 - g.getFontMetrics().stringWidth(s) / 2, this.y1 + (this.y2 - this.y1) / 2 + g.getFontMetrics().getHeight() / 2 / 2);
            }

            public void setCurrentAnswer(double value) {
                this.currentAnswer = value;
            }

            public int getCurrentAnswer() {
                return (int)this.currentAnswer;
            }

            public void verifyAnswer() {
                this.correct = this.currentAnswer == (double)this.finalAnswer;
                this.state = 1;
            }
        }

    }

}

