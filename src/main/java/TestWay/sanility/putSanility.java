package TestWay.sanility;

import PCAM.ReadCSV;
import segmenttree.ParseCmop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class putSanility {
    public static void main(String[] args) throws IOException {


        //TreeMap map=ReadCSV.splitStandardDiviationPointSegment(statistics[0],statistics[1],errorrate,count);

        //WriteSanility();
       splitWay();
    }

    public  static void  WriteSanility() throws IOException {
       // ArrayList<String>       list=ParseCmop.readCmopCsv("D:\\DataProject\\DataWater\\saturn01.0.F.CT_2011_02_PD1.csv");
      //  ArrayList<String>       listSalinity=ParseCmop.parseRawCompListSalinity(list,false);
        ArrayList<String> list=ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\salinity\\1000Wsalinity.csv");
        Iterator<String> it = list.iterator();
        while(it.hasNext()){
            String str = it.next();
           // System.out.println(str);
            if(Double.parseDouble(str)>35){
               it.remove();
                System.out.println(str);
            }
        }
        ReadCSV.writeCsv("D:\\DataProject\\DataGenerate\\salinity\\kkk1000Wsalinity.csv",list);
        ReadCSV.computeLine("D:\\DataProject\\DataGenerate\\salinity\\kkk1000Wsalinity.csv");
    }


    public static void   splitWay()
    {
        String path="D:\\DataProject\\DataGenerate\\stock\\400wStock.csv";
        ArrayList<String> arrTemperature= ReadCSV.readCsv(path);
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        //计算均值，标准差，最小值，最大值
        double[] statistics= ReadCSV.computeStatistics(v1);
        double min=statistics[2];
        double max=300;
       // double max=statistics[3];
        int count=100;
        double split=(max-min)/count;
        double acc=min;
        ArrayList<Double> list=new ArrayList<Double>();
        TreeMap<Double,Integer> map=new TreeMap<Double, Integer>();
        for(int i=0;i<count;i++)
        {
            //acc=i*split;
            list.add(acc+i*split);
            map.put(acc+i*split,0);
        }
        list.add(max);
        map.put(max,0);
        for(int i=0;i<v1.length;i++)
        {
            for(int j=1;j<list.size();j++)
            {
                if(v1[i]<list.get(j))
                {
                    int  valueCount=map.get(list.get(j-1));
                    valueCount++;
                    map.put(list.get(j-1),valueCount);
                    break;
                }

            }

        }
        System.out.println(map.toString());
    }
}
