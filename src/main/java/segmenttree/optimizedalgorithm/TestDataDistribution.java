package segmenttree.optimizedalgorithm;

import PCAM.ReadCSV;

import java.util.ArrayList;

public class TestDataDistribution {
    public static void main(String[] args) {
        ArrayList<String> arrTemperature= ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\3000Wtemperature.csv");
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        int count =0;
        for(int i=0;i<v1.length;i++)
        {
            if(v1[i]>  5.40625&&v1[i]< 12.742749999999997)
            {
                count++;
            }
        }
        System.out.println(count);
    }
}
