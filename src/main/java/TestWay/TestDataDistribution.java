package TestWay;

import PCAM.ReadCSV;

import java.util.ArrayList;

public class TestDataDistribution {
    public static void main(String[] args) {
        ArrayList<String> arrTemperature= ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\salinity\\3000Wsalinity.csv");
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        int count =0;
        double leftpoint=31.8;
        double rightpoint=32;
        for(int i=0;i<v1.length;i++)
        {
            if(v1[i]> leftpoint&&v1[i]< rightpoint)
            {
                System.out.println("Num "+i+"Value "+v1[i]);
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
