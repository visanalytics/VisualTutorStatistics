/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import com.deakin.datavis.Activity;
import com.deakin.datavis.ControlPanelBar;
import com.deakin.datavis.EndScreen;
import com.deakin.datavis.GraphSummary;
import com.deakin.datavis.InstanceData;
import com.deakin.datavis.Main;
import com.deakin.datavis.MainScreen;
import com.deakin.datavis.Screen5;
import com.deakin.datavis.Screen6;
import com.deakin.datavis.ViewMainQuestions;
import com.deakin.datavis.ViewQuestions2;
import com.deakin.datavis.World;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AppletMan
extends Applet
implements Runnable {
    Image offScreen;
    Graphics bufferGraphics;
    AtomicReference<Activity> curActivity;
    Main main;
    GraphSummary graphSum;
    ViewMainQuestions vmQ;
    Class<? extends Activity>[] activities;
    boolean paused = false;
    int activity_index = 0;
    ControlPanelBar pb1;
    ControlPanelBar pb2;
    InstanceData instData;
    Thread runnableThread;

    @Override
    public void init() {
        this.setSize(800, 500);
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.requestFocus();
        this.setLayout(new BorderLayout());
        System.out.println("Before doc base");
        System.out.println("DOCUMENT BASE: " + this.getDocumentBase());
        System.out.println("After doc base");
        System.out.println("Before xml parse");
        this.parseXml(this.instData);
        System.out.println("After xml parse");
        Panel mPanel = new Panel();
        mPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 3;
        c.fill = 1;
        c.gridy = 0;
        this.pb1 = new ControlPanelBar(this, true, false, "");
        mPanel.add(this.pb1);
        this.add("North", mPanel);
        Panel mPanel2 = new Panel();
        mPanel2.setLayout(new GridBagLayout());
        c.gridy = 2;
        this.pb2 = new ControlPanelBar(this, false, true, "");
        mPanel2.add(this.pb2);
        this.add("South", mPanel2);
        Panel mPanel1 = new Panel();
        mPanel1.setLayout(new GridBagLayout());
        c.gridy = 1;
        this.activities = new Class[]{MainScreen.class, Main.class, ViewMainQuestions.class, ViewQuestions2.class, GraphSummary.class, Screen5.class, Screen6.class, EndScreen.class};
        this.curActivity = new AtomicReference();
        try {
            this.curActivity.set(this.activities[this.activity_index].newInstance());
            this.curActivity.get().init(this);
            this.curActivity.get().start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        mPanel1.add(this.curActivity.get());
        this.add("Center", mPanel1);
        this.paused = false;
    }

    private void changeActivity(Class<? extends Activity> var) {
        this.paused = true;
        Panel c = (Panel)this.curActivity.get().getParent();
        c.remove(this.curActivity.get());
        try {
            this.curActivity.set(var.newInstance());
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.curActivity.get().init(this);
        this.curActivity.get().start();
        c.add(this.curActivity.get());
        this.paused = false;
    }

    public void nextActivity() {
        if (this.activity_index + 1 < this.activities.length) {
            ++this.activity_index;
            this.pb1.title = "";
            this.changeActivity(this.activities[this.activity_index]);
        }
    }

    public void previousActivity() {
        if (this.activity_index - 1 >= 0) {
            --this.activity_index;
            this.pb1.title = "";
            this.changeActivity(this.activities[this.activity_index]);
        }
    }

    public InstanceData getInstanceData() {
        return this.instData;
    }

    @Override
    public void start() {
        this.runnableThread = new Thread(this);
        this.runnableThread.start();
    }

    @Override
    public void run() {
        do {
            if (!this.paused) {
                this.repaint();
                this.step();
            }
            try {
                long before = System.currentTimeMillis();
                Thread.sleep(40);
                long after = System.currentTimeMillis();
                long obs_dist = after - before;
                if (!this.curActivity.get().getClass().equals(Main.class) || this.paused) continue;
                Main m = (Main)this.curActivity.get();
                double rt = (double)obs_dist / m.world.getTimeRatio();
                m.world.incTime(rt);
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

    public void step() {
        this.curActivity.get().step();
        this.curActivity.get().repaint();
        this.pb1.repaint();
        this.pb2.repaint();
    }

    @Override
    public void paint(Graphics g) {
    }

    @Override
    public void stop() {
    }

    @Override
    public void destroy() {
    }

    private void parseXml(InstanceData iD) {
        ArrayList<Long> customer_arrivaltimes = new ArrayList<Long>();
        long sample_starttime = 0;
        long sample_endtime = 0;
        try {
            String filUrl = "/res/xml/data_hist_add.xml";
            URL url = new URL(this.getDocumentBase(), filUrl);
            File rawXFile = new File(filUrl);
            InputStream is = this.getClass().getResourceAsStream(filUrl);
            DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuild = dbFact.newDocumentBuilder();
            Document doc = dBuild.parse(is);
            doc.getDocumentElement().normalize();
            NodeList sampList = doc.getElementsByTagName("sample");
            ArrayList<ArrayList<Integer>> samples_times = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> samples_means = new ArrayList<Integer>();
            Random r = new Random();
            int my_sample_index = r.nextInt(sampList.getLength());
            int i = 0;
            while (i < sampList.getLength()) {
                Node n = sampList.item(i);
                if (n.getNodeType() == 1) {
                    String[] indiv;
                    Element e = (Element)n;
                    String timeStartS = e.getElementsByTagName("start_time").item(0).getTextContent();
                    long timeStart = Long.parseLong(timeStartS);
                    if (i == my_sample_index) {
                        sample_starttime = timeStart;
                    }
                    String meanS = e.getElementsByTagName("mean").item(0).getTextContent();
                    double meanF = Double.parseDouble(meanS);
                    int meanI = (int)Math.floor(meanF);
                    samples_means.add(meanI);
                    ArrayList<Integer> hist_data = new ArrayList<Integer>();
                    String line = e.getElementsByTagName("histogram").item(0).getTextContent();
                    String[] arrstring = indiv = line.split(",");
                    int n2 = arrstring.length;
                    int n3 = 0;
                    while (n3 < n2) {
                        String d = arrstring[n3];
                        int data_point = Integer.parseInt(d);
                        hist_data.add(data_point);
                        ++n3;
                    }
                    samples_times.add(hist_data);
                }
                ++i;
            }
            System.out.println(samples_times.size());
            System.out.println(samples_means.size());
            NodeList cusList = doc.getElementsByTagName("customer");
            int i2 = 0;
            while (i2 < cusList.getLength()) {
                Element e;
                String timeS;
                long timeL;
                Node n = cusList.item(i2);
                if (n.getNodeType() == 1 && (timeL = Long.parseLong(timeS = (e = (Element)n).getElementsByTagName("time").item(0).getTextContent())) >= sample_starttime) {
                    customer_arrivaltimes.add(timeL);
                }
                ++i2;
            }
            this.instData = iD = new InstanceData(sample_starttime, customer_arrivaltimes, samples_times, samples_means);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

