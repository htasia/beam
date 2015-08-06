package com.panton;

import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * Created by roylee on 8/4/15.
 */
public class Frequency {
    BigDecimal upperBound;
    BigDecimal lowerBound;
    int id;
    int count;
    public Frequency(int id, int lowerBound, double upperBound) {
        this.id = id;
        this.upperBound = new BigDecimal(upperBound);
        this.lowerBound = new BigDecimal(lowerBound);
        this.count = 0;
    }
    public int get() {
        return count;
    }
    public int incr() {
        this.count ++;
        return this.count;
    }
    @Override
    public String toString(){
        return  MessageFormat.format("{0} {1}", getBoundString(), count);
    }
    public String getBoundString() {
        return  MessageFormat.format("{0} - {1}: ", Util.getBRepresentation(lowerBound), Util.getBRepresentation(upperBound));
    }

}
