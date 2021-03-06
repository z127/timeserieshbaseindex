package segmenttree.optimizedalgorithm;

import PCAM.ReadCSV;

import java.util.ArrayList;

public class TestDataDistribution {
    public static void main(String[] args) {
        ArrayList<String> arrTemperature= ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\1000Wtemperature.csv");
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        int count =0;
        double leftpoint=-3.15;
        double rightpoint=-2.4;
        for(int i=0;i<v1.length;i++)
        {
            if(v1[i]> leftpoint&&v1[i]< rightpoint)
            {
                count++;
            }
        }
        System.out.println(count);
    }

    public static  int   TestDistribution( double leftpoint, double rightpoint,String path) {
        ArrayList<String> arrTemperature= ReadCSV.readCsv(path);
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        int count =0;

        for(int i=0;i<v1.length;i++)
        {
            if(v1[i]> leftpoint&&v1[i]< rightpoint)
            {
                count++;
            }
        }
        //System.out.println(count);
        return count;


    }
}
