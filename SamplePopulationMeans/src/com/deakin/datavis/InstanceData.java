/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

import java.util.ArrayList;

public class InstanceData {
    static final long DEFAULT_SAMPLE_STARTTIME = 36000;
    static final long DEFAULT_SAMPLE_ENDTIME = 36900;
    long sample_starttime;
    ArrayList customer_arrivaltimes;
    ArrayList customer_collectedtimes;
    ArrayList<ArrayList<Integer>> samples_list;
    ArrayList<Integer> samples_means;
    ArrayList<String> questions;

    public InstanceData(long sample_starttime, ArrayList customers_arrivaltimes, ArrayList<ArrayList<Integer>> samples_list, ArrayList<Integer> samples_means) {
        this.samples_list = samples_list;
        this.sample_starttime = sample_starttime;
        this.customer_arrivaltimes = customers_arrivaltimes;
        this.customer_collectedtimes = new ArrayList();
        this.samples_means = samples_means;
    }

    public ArrayList<ArrayList<Integer>> getSamplesList() {
        return this.samples_list;
    }

    public ArrayList<Integer> getSamplesMeans() {
        return this.samples_means;
    }

    public long getSampleStartTime() {
        return this.sample_starttime;
    }

    public ArrayList getCustomerArrivalTimesArray() {
        return this.customer_arrivaltimes;
    }

    public ArrayList getCollectedCustomerArrivalTimesArray() {
        return this.customer_collectedtimes;
    }

    public void setCollectedCustomerArrivalTimesArray(ArrayList times) {
        this.customer_collectedtimes = times;
    }

    public ArrayList<String> getQuestions() {
        return this.questions;
    }
}

