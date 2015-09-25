package com.lightsoft.microwave.lightmanager;

/**
 * Created by Microwave on 16.02.2015.
 */
public class Type implements Comparable<Type> {

    public String type;
    public int freq;

    public Type(String nType, int freq) {
        type = nType;
        this.freq = freq;
    }


    public int compareTo(Type sec){
        if(sec.freq < freq)
            return 1;
        if(sec.freq == freq)
            return 0;
        return -1;
    }
}
