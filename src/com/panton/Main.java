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
    public static int num_masterlist = 0;
    public static int num_folders = 1;
    public static List<BigDecimal> fileSizeList = new ArrayList<BigDecimal>();
    public static void main(String[] args) {

        String rootPath = args[0];
        load(new File(rootPath));
        Collections.sort(fileSizeList);
        System.out.println(Arrays.toString(fileSizeList.toArray()));
        BigDecimal total = totalBytes;
        BigDecimal mean = totalBytes.divide(new BigDecimal(num_files), 5, BigDecimal.ROUND_CEILING);
        BigDecimal min = fileSizeList.get(0);
        BigDecimal max = fileSizeList.get(num_files - 1);
        BigDecimal range = fileSizeList.get(num_files - 1).subtract(fileSizeList.get(0));
        BigDecimal median = fileSizeList.get(num_files / 2);
        BigDecimal mode = fileSizeList.get(num_files - 1);

        List<BigDecimal> test = new ArrayList<BigDecimal>();

        test.add(new BigDecimal(600));
        test.add(new BigDecimal(470));
        test.add(new BigDecimal(170));
        test.add(new BigDecimal(430));
        test.add(new BigDecimal(300));

        BigDecimal variance = calc_variance(mean, fileSizeList);


        System.out.println("Total File Size: " + Util.getBRepresentation(total));
        System.out.println("Total Files: " + num_files);
        System.out.println("Total Folders: " + num_folders);
        System.out.println("Total MasterLists: " + num_masterlist);

        System.out.println("Mean File Size:" + Util.getBRepresentation(mean));
        System.out.println("Min File Size: " + Util.getBRepresentation(min));
        System.out.println("Max File Size: " + Util.getBRepresentation(max));
        System.out.println("Range File Size: " + Util.getBRepresentation(range));

        System.out.println("Median File Size (Middle Sorted): " + Util.getBRepresentation(median));
        System.out.println("Mode File Size (Occurs most frequent): " + Util.getBRepresentation(mode));

        //variance
        System.out.println("Variance File Size: (The average of the squared differences from the Mean): " +
                Util.getBRepresentation(variance));
        System.out.println("Standard Deviation File Size: (how far from the normal): " +
                Util.getBRepresentation(sqrt(variance)));
        System.out.println("cumulative frequency distribution table");




        int check = 0;
        for(Frequency f: get_freq_dist(fileSizeList)) {
            System.out.println(f.toString());
            check += f.count;
        }
        System.out.println("Is N(freq_dist) and N(file) tally? " + (check == num_files));
    }


    public static List<Frequency> gen_freq_table(int lower, int upper, int upperUnit, int step, int dUnit) {


        List<Frequency> freq_table = new ArrayList<Frequency>();

        int idx = 0;
        double accuracy = 0.9999;
        while(lower <= (upper * upperUnit)) {


            if(idx > 0) {
                freq_table.add(new Frequency(idx, lower , lower + (step -1 + accuracy) * dUnit));
                lower = lower + (step * dUnit);

            }else {
                freq_table.add(new Frequency(idx, lower, (step + accuracy)  * dUnit));
                lower = (lower + step) * dUnit;

            }

            idx ++;
        }
        return freq_table;
    }


    public static List<Frequency> get_freq_dist(List<BigDecimal> sample) {//EQMS

        List<Frequency> freq_table = gen_freq_table(1, 25, Util.MB , 1, Util.MB);


        System.out.println("sample size:" + sample.size());
        int pass = 1;
        for(BigDecimal data: sample) {

            pass = 1;
            for(Frequency f: freq_table) {

                if(data.compareTo(f.lowerBound) >= 0 && data.compareTo(f.upperBound) <= 0){
                    f.incr();
                    break;
                   // System.out.println(f.getBoundString() + Util.getBRepresentation(data));
                }
                else if(pass >= freq_table.size()){
                    System.out.println("missing:>" +Util.getBRepresentation(data));
                }
                pass++;
            }

        }
        return freq_table;

    }

    public static BigDecimal sqrt(BigDecimal value) {
        BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
        return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));
    }

    public static BigDecimal calc_variance(BigDecimal mean, List<BigDecimal> sample) {

        BigDecimal result = new BigDecimal(0);
        for(BigDecimal data: sample) {
            result = result.add(data.subtract(mean).pow(2));
        }
        return result.divide(new BigDecimal(sample.size()), 5, BigDecimal.ROUND_CEILING); //avg of E(data-mean)^2
    }


    public static File[] get_files(File dirInfo, final String ext) {

        File[] files = dirInfo.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile() && name.toLowerCase().endsWith(ext);
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


    public static void load(File currentDir) {

        File[] files = get_files(currentDir, ".pdf");
        System.out.println("****In Folder: " + currentDir.getName() + "****");
        BigDecimal fSize = null;
        if(files != null) {
            for(File f: files) {
                fSize = new BigDecimal(f.length());
                num_files++;
                totalBytes = totalBytes.add(fSize);
                fileSizeList.add(fSize);
                System.out.println("-" + f.getName() + "," + Util.getBRepresentation(fSize));
            }
        }



        File[] masterlists = get_files(currentDir, ".xls");
        if(files != null) {
            for(File f: masterlists) {
                num_masterlist++;
            }
        }

        File[] sub_dirs = get_sub_dirs(currentDir);
        if(sub_dirs != null) {
            for(File f: sub_dirs) {
                num_folders++;
                System.out.println("folder: " + f.getName());
                load(f);

            }
        }
    }

}
