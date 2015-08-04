package com.panton;

import java.io.File;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static BigDecimal totalBytes = new BigDecimal(0);
    public static int num_files = 0;
    public static List<BigDecimal> fileSizeList = new ArrayList<BigDecimal>();
    public static void main(String[] args) {

        String rootPath = args[0];
        load(new File(rootPath));
        System.out.println("Total File Size:" + getBRepresentation(totalBytes));
        System.out.println("Total Files: " + num_files);
        System.out.println("Mean File Size:" + getBRepresentation(totalBytes.divide(new BigDecimal(1464), 5, BigDecimal.ROUND_CEILING)));

        Collections.sort(fileSizeList);
        System.out.println(Arrays.toString(fileSizeList.toArray()));
    }

    public static File[] get_files(File dirInfo) {
        File[] files = dirInfo.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }

        });

        return files;
    }

    public static File[] get_sub_dirs(File  dirInfo) {
         File[] dirs = dirInfo.listFiles(new FilenameFilter() {
             @Override
             public boolean accept(File current, String name) {
                 return new File(current, name).isDirectory();
             }

         });

        return dirs;
    }

    public static String getBRepresentation(BigDecimal size) {
        double dSize = size.doubleValue();
        if(dSize / (1000 * 1000 * 1000 ) >= 1) {
            return toGB(size).toString() + " GB";
        }else if (dSize /(1000 * 1000) >= 1) {
            return toMB(size).toString() + " MB";
        }else {
            return toKB(size).toString() + " KB";
        }
    }
    public static String getBRepresentation(long size) {
        return getBRepresentation(new BigDecimal(size));
    }

    public static BigDecimal toMB(BigDecimal size) {
        return  size.divide(new BigDecimal (1000 * 1000));
    }
    public static BigDecimal toGB(BigDecimal size) {
        return  size.divide(new BigDecimal(1000 * 1000 * 1000));
    }
    public static BigDecimal toKB(BigDecimal size) {
        return  size.divide(new BigDecimal(1000 ));
    }


    public static void load(File currentDir) {

        File[] files = get_files(currentDir);
        System.out.println("****In Folder: " + currentDir.getName() + "****");
        BigDecimal fSize = null;
        if(files != null) {
            for(File f: files) {
                fSize = new BigDecimal(f.length());
                num_files++;
                totalBytes = totalBytes.add(fSize);
                fileSizeList.add(fSize);
                System.out.println("-" + f.getName() + "," + getBRepresentation(fSize));
            }
        }

        File[] sub_dirs = get_sub_dirs(currentDir);
        if(sub_dirs != null) {
            for(File f: sub_dirs) {
                System.out.println(" " + f.getName());
                load(f);

            }
        }
    }
}
