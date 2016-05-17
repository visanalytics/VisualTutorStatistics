/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.AppletMan;
import com.deakin.datavis.Clock;
import com.deakin.datavis.ControlPanelBar;
import com.deakin.datavis.CustomerDataList;
import com.deakin.datavis.DistributionGraphVis;
import com.deakin.datavis.Door;
import com.deakin.datavis.InstanceData;
import com.deakin.datavis.Person;
import com.deakin.datavis.World;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class Main
extends Activity
implements MouseListener {
    int windowHeight;
    int windowWidth;
    public static int STATE_COLLECTING = 0;
    public static int STATE_DATA_COLLECTED_INVALID = 1;
    public static int STATE_DATA_COLLECTED_VALID = 2;
    public static int STATE_READING = 2;
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
    ArrayList people = new ArrayList();
    long rSeed;
    Image[] personImages;
    ArrayList customerarrival_times;

    @Override
    public void init(AppletMan m) {
        super.init(m);
        this.addMouseListener(this);
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
        this.customerarrival_times = (ArrayList)this.appMan.getInstanceData().getCustomerArrivalTimesArray().clone();
        this.appMan.pb1.title = "Click the red square when a person enters the store";
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
        int i = 0;
        while (i < this.appMan.getInstanceData().getCollectedCustomerArrivalTimesArray().size()) {
            int t = (Integer)this.appMan.getInstanceData().getCollectedCustomerArrivalTimesArray().get(i);
            this.stopWatch.addTimeDifListing(t);
            ++i;
        }
        this.checkState();
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
        this.stopWatch.paint(g);
        this.datalist.paint(g);
        this.dgv.paint(g);
        if (this.STATE == STATE_DATA_COLLECTED_INVALID || this.STATE == STATE_DATA_COLLECTED_VALID) {
            this.drawStateCollectedOverlay(g);
        }
    }

    private void drawStateCollectedOverlay(Graphics g) {
        Font lastFont = g.getFont();
        g.setFont(new Font("Dialog", 1, 24));
        g.setColor(new Color(255, 255, 255, 120));
        g.fillRect(0, 0, this.windowWidth, (int)((double)g.getFontMetrics().getHeight() * 1.5));
        String s = "";
        if (this.STATE == STATE_DATA_COLLECTED_VALID) {
            s = "Sample Collected - Continue to Analysis";
        } else if (this.STATE == STATE_DATA_COLLECTED_INVALID) {
            s = "Invalid Sample - Analyse new sample";
        }
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
        this.checkState();
    }

    private void checkState() {
        if (this.STATE == STATE_COLLECTING && this.appMan.getInstanceData().getCollectedCustomerArrivalTimesArray().size() >= 30) {
            double mean = 0.0;
            InstanceData data = this.appMan.getInstanceData();
            int i = 0;
            while (i < data.getCollectedCustomerArrivalTimesArray().size()) {
                int dataPoint = (Integer)data.getCollectedCustomerArrivalTimesArray().get(i);
                mean += (double)dataPoint;
                ++i;
            }
            mean = Math.round(mean / (double)data.getCollectedCustomerArrivalTimesArray().size());
            ArrayList<Integer> tempVals = new ArrayList<Integer>();
            long lastTime = data.getSampleStartTime();
            long totalForMean = 0;
            int m = 0;
            for (Object l : data.getCustomerArrivalTimesArray()) {
                if (m > data.getCollectedCustomerArrivalTimesArray().size()) break;
                long i2 = (Long)l;
                long difVal = i2 - lastTime;
                tempVals.add((int)difVal);
                lastTime = i2;
                totalForMean += difVal;
                ++m;
                System.out.println(difVal);
            }
            long meanTime = totalForMean / (long)data.getCollectedCustomerArrivalTimesArray().size();
            if (Math.abs((double)meanTime - mean) > 10.0) {
                data.setCollectedCustomerArrivalTimesArray(tempVals);
                this.STATE = STATE_DATA_COLLECTED_INVALID;
            } else {
                this.STATE = STATE_DATA_COLLECTED_VALID;
            }
            this.removeMouseListener(this);
        }
    }

    public void createPerson() {
        double md = this.world.metresHeight / 2.0;
        double[] posLeftY = new double[]{md, md - 1.0, md - 2.0};
        double[] posRightY = new double[]{md - 0.75, md - 0.5, md - 1.5, md - 2.5};
        double origY = this.world.metresHeight / 2.0 + (double)((this.world.getRandom().nextInt(10) - 5) / 5);
        double origYL = posLeftY[this.world.getRandom().nextInt(posLeftY.length)];
        double origYR = posRightY[this.world.getRandom().nextInt(posRightY.length)];
        int rRadius = this.world.getRandom().nextInt(11) + 10;
        if (this.world.getRandom().nextBoolean()) {
            Person p = new Person(this.world, this.door, this.world.pixelsToMetres(- this.personImages[0].getWidth(this)), origYL, this.world.metresWidth, origYL, rRadius, false, false, this.personImages[this.world.getRandom().nextInt(this.personImages.length)]);
            this.people.add(p);
        } else {
            Person p = new Person(this.world, this.door, this.world.metresWidth + this.world.pixelsToMetres(this.personImages[0].getWidth(this)), origYR, -1.0, origYR, rRadius, true, false, this.personImages[this.world.getRandom().nextInt(this.personImages.length)]);
            this.people.add(p);
        }
    }

    public Door createDoor() {
        return new Door(this.world, this, this.world.metresWidth / 2.0 - 1.0, this.world.metresHeight * 0.6, this.world.metresWidth / 2.0 + 1.0, this.world.metresHeight * 0.75 + 0.1);
    }

    public void spawnPeople() {
        if (this.world.getTime() - this.stopWatch.getLastSpawnTime() >= 6.0) {
            this.createPerson();
            this.stopWatch.setLastSpawnTime(this.world.getTime());
        }
    }

    public void spawnCustomers() {
        double origY;
        double origX;
        double endX;
        boolean movingLeft;
        InstanceData data = this.appMan.getInstanceData();
        double md = this.world.metresHeight / 2.0;
        double[] posLeftY = new double[]{md, md - 1.0, md - 2.0};
        double[] posRightY = new double[]{md + 0.5, md - 0.5, md - 1.5, md - 2.5};
        double origYL = posLeftY[this.world.getRandom().nextInt(posLeftY.length)];
        double origYR = posRightY[this.world.getRandom().nextInt(posRightY.length)];
        if (this.world.getRandom().nextBoolean()) {
            origY = origYL;
            movingLeft = false;
            origX = this.world.pixelsToMetres(-20);
            endX = this.door.xm;
        } else {
            origY = origYR;
            movingLeft = true;
            origX = this.world.metresWidth + this.world.pixelsToMetres(20);
            endX = this.door.xm2;
        }
        double endY = origY;
        for (Object l : this.customerarrival_times) {
            long i = (Long)l;
            double timemoving = this.calcTimeFromDistanceSpeed(origX, origY, endX, endY);
            if ((int)this.world.getTime() != (int)((double)i - timemoving)) continue;
            this.createPerson(origX, origY, endX, endY, movingLeft);
            this.customerarrival_times.remove(l);
            break;
        }
    }

    private void createPerson(double origX, double origY, double endX, double endY, boolean movingLeft) {
        int rRadius = this.world.getRandom().nextInt(11) + 10;
        Person p = new Person(this.world, this.door, origX, origY, endX, endY, rRadius, movingLeft, true, this.personImages[this.world.getRandom().nextInt(this.personImages.length)]);
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
        double s = 0.2;
        double tt = c / s;
        return tt;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.stopWatch.onClick(e);
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
}

