/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavist2;

import java.util.ArrayList;

public class InstanceData {
    static final long DEFAULT_SAMPLE_STARTTIME = 36000;
    static final long DEFAULT_SAMPLE_ENDTIME = 36900;
    long sample_starttime;
    ArrayList customer_arrivaltimes;
    ArrayList<String> customer_moods;
    ArrayList<String> customer_collected_moods;
    ArrayList<String> questions;
    int population_size;
    double population_unhappytohappy_ratio;

    public InstanceData(long sample_starttime, ArrayList customers_arrivaltimes, ArrayList<String> customer_moods) {
        this.sample_starttime = sample_starttime;
        this.customer_arrivaltimes = customers_arrivaltimes;
        this.customer_collected_moods = new ArrayList();
        this.customer_moods = customer_moods;
        this.population_size = customers_arrivaltimes.size();
        int unhappyNum = 0;
        int happyNum = 0;
        int i = 0;
        while (i < customer_moods.size()) {
            if (customer_moods.get(i) == "MoodHappy") {
                ++happyNum;
            } else if (customer_moods.get(i) == "MoodUnhappy") {
                ++unhappyNum;
            }
            ++i;
        }
        this.population_size = unhappyNum + happyNum;
        this.population_unhappytohappy_ratio = (double)unhappyNum / (double)this.population_size;
    }

    public int getPopulationSize() {
        return this.population_size;
    }

    public long getSampleStartTime() {
        return this.sample_starttime;
    }

    public ArrayList getCustomerArrivalTimesArray() {
        return this.customer_arrivaltimes;
    }

    public ArrayList<String> getCollectedCustomerMoodsArray() {
        return this.customer_collected_moods;
    }

    public void setCollectedCustomerMoodsArray(ArrayList<String> moods) {
        this.customer_collected_moods = moods;
    }

    public ArrayList<String> getCustomerMoodsArray() {
        return this.customer_moods;
    }

    public ArrayList<String> getQuestions() {
        return this.questions;
    }
}

