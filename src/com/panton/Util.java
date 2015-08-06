package com.panton;

import java.math.BigDecimal;

/**
 * Created by roylee on 8/4/15.
 */
public class Util {
    public static final int KB = 1000;
    public static final int MB = KB * KB;
    public static final int GB = MB * KB;

    public static String getBRepresentation(BigDecimal size) {
        double dSize = size.doubleValue();
        if(dSize / (1000 * 1000 * 1000 ) >= 1) {
            return toGB(size).toString() + " GB";
        }else if (dSize /(1000 * 1000) >= 1) {
            return toMB(size).toString() + " MB";
        }else if (dSize/ (1000) >= 1){
            return toKB(size).toString() + " KB";
        }else {
            return dSize + " B";
        }
    }
    public static String getBRepresentation(long size) {
        return getBRepresentation(new BigDecimal(size));
    }

    public static BigDecimal toMB(BigDecimal size) {
        return  size.divide(new BigDecimal (MB));
    }
    public static BigDecimal toGB(BigDecimal size) {
        return  size.divide(new BigDecimal(GB));
    }
    public static BigDecimal toKB(BigDecimal size) {
        return  size.divide(new BigDecimal(KB ));
    }

}
