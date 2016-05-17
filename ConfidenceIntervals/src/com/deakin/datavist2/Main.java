/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import com.deakin.datavist2.Activity;
import com.deakin.datavist2.AppletMan;
import com.deakin.datavist2.Clock;
import com.deakin.datavist2.CustomerDataList;
import com.deakin.datavist2.DistributionGraphVis;
import com.deakin.datavist2.Door;
import com.deakin.datavist2.InstanceData;
import com.deakin.datavist2.MoodScroll;
import com.deakin.datavist2.Person;
import com.deakin.datavist2.ProportionBar;
import com.deakin.datavist2.World;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Main
extends Activity
implements MouseListener,
MouseMotionListener {
    int windowHeight;
    int windowWidth;
    public static int STATE_COLLECTING = 0;
    public static int STATE_DATA_COLLECTED = 1;
    public int STATE = 0;
    Thread runnableThread;
    boolean running = true;
    Image offScreen;
    Graphics bufferGraphics;
    Clock stopWatch;
    CustomerDataList datalist;
    DistributionGraphVis dgv;
    World world;
    Door door;
    MoodScroll mScroll;
    ProportionBar propBar;
    ArrayList people = new ArrayList();
    long rSeed;
    Image[] personImages;
    Image[] moodImages;
    int clickCircleStage = 30;
    boolean clicked = false;
    ArrayList customerarrival_times;
    ArrayList<String> customermoods;
    int lastX = 0;
    int lastY = 0;

    @Override
    public void init(AppletMan m) {
        super.init(m);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.windowWidth = this.getSize().width;
        this.windowHeight = this.getSize().height;
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    public void start() {
        Random random = new Random();
        this.world = new World(0, 50, 600, 400, 8, 6, random);
        this.door = this.createDoor();
        this.datalist = new CustomerDataList(this.windowWidth - 260, 325);
        this.dgv = new DistributionGraphVis(this.windowWidth - 260, 292, this.windowWidth, this.windowHeight, 0, 60, 5);
        this.stopWatch = new Clock(this.world, this, this.dgv);
        this.world.time = this.appMan.getInstanceData().getSampleStartTime();
        this.stopWatch.setTime(this.world.time);
        this.stopWatch.speed2button.click();
        this.customerarrival_times = this.appMan.getInstanceData().getCustomerArrivalTimesArray();
        this.customermoods = this.appMan.getInstanceData().getCustomerMoodsArray();
        Image pImage1 = this.getImage(this.getDocumentBase(), "res/images/person.png");
        Image pImage2 = this.getImage(this.getDocumentBase(), "res/images/person_2.png");
        Image pImage3 = this.getImage(this.getDocumentBase(), "res/images/person_3.png");
        Image pImage4 = this.getImage(this.getDocumentBase(), "res/images/person_4.png");
        Image pImage5 = this.getImage(this.getDocumentBase(), "res/images/person_5.png");
        Image pImage6 = this.getImage(this.getDocumentBase(), "res/images/person_6.png");
        Image pImage7 = this.getImage(this.getDocumentBase(), "res/images/person_7.png");
        Image pImage8 = this.getImage(this.getDocumentBase(), "res/images/person_8.png");
        Image pImage9 = this.getImage(this.getDocumentBase(), "res/images/person_9.png");
        Image pImage10 = this.getImage(this.getDocumentBase(), "res/images/person_10.png");
        this.personImages = new Image[]{pImage1, pImage2, pImage3, pImage4, pImage5, pImage6, pImage7, pImage8, pImage9, pImage10};
        Image moodHappy = this.getImage(this.getDocumentBase(), "res/images/happy_face.png");
        Image moodUnhappy = this.getImage(this.getDocumentBase(), "res/images/unhappy_face.png");
        Image moodNeutral = this.getImage(this.getDocumentBase(), "res/images/neutral_face.png");
        this.moodImages = new Image[]{moodHappy, moodUnhappy, moodNeutral};
        this.mScroll = new MoodScroll(this.appMan, (int)((double)this.windowWidth - 227.5), 65, (int)((double)this.windowWidth - 32.5), 325, this.moodImages);
        this.propBar = new ProportionBar(this.mScroll.x1, this.mScroll.y2, this.mScroll.x2, this.mScroll.y2 + 26, 0.0, 0.0);
        this.createPerson();
        if (this.appMan.getInstanceData().getCollectedCustomerMoodsArray().size() >= 30) {
            this.removeMouseListener(this);
            this.STATE = STATE_DATA_COLLECTED;
        } else {
            this.STATE = STATE_COLLECTING;
        }
    }

    public void stop() {
    }

    @Override
    public void destroy() {
        this.running = false;
        this.runnableThread = null;
    }

    public void run() {
        do {
            this.repaint();
            this.step();
            try {
                long before = System.currentTimeMillis();
                Thread.sleep(40);
                long after = System.currentTimeMillis();
                long obs_dist = after - before;
                double rt = (double)obs_dist / this.world.getTimeRatio();
                this.world.incTime(rt);
                continue;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
        } while (true);
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
        this.door.paintBackground(g);
        int i = 0;
        while (i < this.people.size()) {
            Person p = (Person)this.people.get(i);
            p.paint(this, g);
            ++i;
        }
        this.door.paintForeground(g);
        this.drawPane(g);
        this.datalist.paint(g);
        this.mScroll.paint(this, g);
        this.propBar.paint(g);
        this.paintClick(g);
        this.paintRatioString(g);
        if (this.STATE == STATE_DATA_COLLECTED) {
            this.drawStateCollectedOverlay(g);
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
        String s2 = String.valueOf((int)happyNum);
        g.setColor(Color.WHITE);
        g.drawString(s, this.propBar.x1 - g.getFontMetrics().stringWidth(s) / 2, this.propBar.y2 + g.getFontMetrics().getHeight());
        g.drawString(s2, this.propBar.x1 - g.getFontMetrics().stringWidth(s2) / 2, this.propBar.y2 + g.getFontMetrics().getHeight() * 2);
        String s3 = "Unhappy";
        String s4 = String.valueOf((int)unhappyNum);
        g.setColor(Color.WHITE);
        g.drawString(s3, this.propBar.x2 - g.getFontMetrics().stringWidth(s3) / 2, this.propBar.y2 + g.getFontMetrics().getHeight());
        g.drawString(s4, this.propBar.x2 - g.getFontMetrics().stringWidth(s4) / 2, this.propBar.y2 + g.getFontMetrics().getHeight() * 2);
    }

    private void paintClick(Graphics g) {
        if (this.clicked) {
            if (this.clickCircleStage <= 0) {
                this.clickCircleStage = 30;
                this.clicked = false;
            } else {
                g.setColor(Color.RED);
                g.fillOval(this.lastX - this.clickCircleStage / 2, this.lastY - this.clickCircleStage / 2, this.clickCircleStage, this.clickCircleStage);
                this.clickCircleStage -= 3;
            }
        }
    }

    private void drawStateCollectedOverlay(Graphics g) {
        Font lastFont = g.getFont();
        g.setFont(new Font("Dialog", 1, 24));
        g.setColor(new Color(255, 255, 255, 120));
        g.fillRect(0, 0, this.windowWidth, (int)((double)g.getFontMetrics().getHeight() * 1.5));
        String s = "Sample Collected, Continue Collecting or Continue to Analysis";
        g.setColor(Color.BLACK);
        g.drawString(s, this.windowWidth / 2 - g.getFontMetrics().stringWidth(s) / 2, g.getFontMetrics().getHeight());
        g.setFont(lastFont);
    }

    private void drawPane(Graphics g) {
        Color c = new Color(37, 63, 96, 255);
        g.setColor(c);
        g.fillRect(this.windowWidth - 260, 0, 260, this.windowHeight);
    }

    @Override
    public void step() {
        this.stopWatch.update();
        int i = 0;
        while (i < this.people.size()) {
            Person p = (Person)this.people.get(i);
            p.update();
            if (p.done) {
                this.people.remove(p);
            }
            ++i;
        }
        this.door.update();
        this.dgv.update();
        this.spawnPeople();
        this.spawnCustomers();
        ArrayList<String> arm = this.appMan.instData.customer_collected_moods;
        double happyNum = 0.0;
        double unhappyNum = 0.0;
        int i2 = 0;
        while (i2 < arm.size()) {
            String m = arm.get(i2);
            if (m == "MoodHappy") {
                happyNum += 1.0;
            }
            if (m == "MoodUnhappy") {
                unhappyNum += 1.0;
            }
            ++i2;
        }
        if (unhappyNum + happyNum > 0.0) {
            this.propBar.setVal1(happyNum / (happyNum + unhappyNum));
            this.propBar.setVal2(unhappyNum / (happyNum + unhappyNum));
        }
        this.checkState();
    }

    private void checkState() {
        if (this.STATE == STATE_COLLECTING && this.appMan.getInstanceData().getCollectedCustomerMoodsArray().size() >= 30) {
            this.STATE = STATE_DATA_COLLECTED;
        }
    }

    public void createPerson() {
        double md = this.door.xm * 0.5;
        double[] posX = new double[]{md - 1.0, md - 0.5, md, md + 0.5, md + 1.0};
        double origX = posX[this.world.getRandom().nextInt(posX.length)];
        String mood = "MoodNeutral";
        int rRadius = this.world.getRandom().nextInt(11) + 10;
        if (this.world.random.nextBoolean()) {
            Person p = new Person(this, this.world, this.door, origX, -1.0, origX, this.world.metresHeight, rRadius, false, false, this.personImages[this.world.getRandom().nextInt(this.personImages.length)], mood, this.moodImages[0]);
            this.people.add(p);
        } else {
            Person p = new Person(this, this.world, this.door, origX, this.world.metresHeight, origX, -1.0, rRadius, false, false, this.personImages[this.world.getRandom().nextInt(this.personImages.length)], mood, this.moodImages[0]);
            this.people.add(p);
        }
    }

    public Door createDoor() {
        return new Door(this.world, this, this.world.metresWidth * 0.45, this.world.metresHeight * 0.3, this.world.metresWidth * 0.475, this.world.metresHeight * 0.7);
    }

    public void spawnPeople() {
        if (this.world.getTime() - this.stopWatch.getLastSpawnTime() >= 6.0) {
            this.createPerson();
            this.stopWatch.setLastSpawnTime(this.world.getTime());
        }
    }

    public void spawnCustomers() {
        boolean movingLeft;
        double endY;
        double endX;
        double origY;
        InstanceData data = this.appMan.getInstanceData();
        double md = this.door.xm * 0.5;
        double[] posX = new double[]{md - 1.0, md - 0.5, md, md + 0.5, md + 1.0};
        double origX = posX[this.world.getRandom().nextInt(posX.length)];
        if (this.world.getRandom().nextBoolean()) {
            origY = 0.0;
            movingLeft = false;
            endX = origX;
            endY = this.door.getY1() + (this.door.getY2() - this.door.getY1()) * this.world.getRandom().nextDouble();
        } else {
            origY = this.world.metresHeight + this.world.pixelsToMetres(this.personImages[0].getHeight(this));
            movingLeft = true;
            endX = origX;
            endY = this.door.getY2() - (this.door.getY2() - this.door.getY1()) * this.world.getRandom().nextDouble();
        }
        int i = 0;
        while (i < this.customerarrival_times.size()) {
            long l = (Long)this.customerarrival_times.get(i);
            String mood = this.customermoods.get(i);
            double timemoving = this.calcTimeFromDistanceSpeed(origX, origY, endX, endY);
            if ((int)this.world.getTime() == (int)l) {
                System.out.println("WT: " + this.world.getTime() + "    T:" + l + "    M: " + mood);
                this.createPerson(mood, origX, origY, endX, endY, movingLeft);
                this.customerarrival_times.remove(i);
                this.customermoods.remove(i);
                break;
            }
            ++i;
        }
    }

    private void createPerson(String mood, double origX, double origY, double endX, double endY, boolean movingLeft) {
        int rRadius = this.world.getRandom().nextInt(11) + 10;
        Random r = new Random();
        int iPointer = 0;
        iPointer = mood == "MoodHappy" ? 0 : 1;
        Person p = new Person(this, this.world, this.door, origX, origY, endX, endY, rRadius, movingLeft, true, this.personImages[this.world.getRandom().nextInt(this.personImages.length)], mood, this.moodImages[iPointer]);
        this.people.add(p);
    }

    @Override
    public URL getDocumentBase() {
        return this.appMan.getDocumentBase();
    }

    @Override
    public Image getImage(URL url, String path) {
        return this.appMan.getImage(url, path);
    }

    private double calcTimeFromDistanceSpeed(double origX, double origY, double endX, double endY) {
        double x3 = (endX - origX) * (endX - origX);
        double y3 = (endY - origY) * (endY - origY);
        double c = Math.sqrt(x3 + y3);
        double s = 0.3;
        double tt = c / s;
        return tt;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int eX = e.getX();
        int eY = e.getY();
        this.stopWatch.onClick(e);
        int i = 0;
        while (i < this.people.size()) {
            Person p = (Person)this.people.get(i);
            if (p.contains(eX, eY, (int)((double)this.personImages[0].getHeight(this)/* * 0.6*/)) && p.showMood()) {
                if (!p.getMood().equals("MoodNeutral")) {
                    this.mScroll.addMood(p.getMood());
                    this.appendMoodsArray(p.getMood());
                }
                this.clicked = true;
                this.clickCircleStage = 30;
                this.lastX = eX;
                this.lastY = eY;
            }
            ++i;
        }
    }

    public void addTimeListing(String listing) {
        this.datalist.addListing(listing);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }
}

