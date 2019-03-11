package TestWay;

import Item.HbaseIndexItem;
import PCAM.Pcam;
import PCAM.ReadCSV;
import cluster.computeCluster;
import hbasequery.HbaseQuery;
import segmenttree.optimizedalgorithm.TestDataDistribution;
import threadpool.TestFutureTask2;

import java.util.ArrayList;

public class DynamicWay {
    public static  String path="D:\\DataProject\\DataGenerate\\3000Wtemperature.csv";
    public static  double leftpoint=0;
    public static  double rightpoint=1;
    public  static  String tableName="cmop";
    public static  int clusterSize=20;

    public static void main(String[] args) {
        ArrayList<String> arrTemperature= ReadCSV.readCsv(path);
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        //计算均值，标准差，最小值，最大值
        double[] statistics= ReadCSV.computeStatistics(v1);
      //  ReadCSV.kurtosis(v1,0,v1.length);
        double variance=statistics[1];
        int splitcount =10;
        //划分范围，最大值，最小值
        int count=0;
        int  length=0;
        System.out.println("查询内容为"+leftpoint+" "+ rightpoint);
       // 当前误差为 1.0划分段数为 4命中块数 24574总共快数 281693命中数据数 5831701
        int lengthcount=0;
        ArrayList<HbaseIndexItem> listResult=new ArrayList<HbaseIndexItem>();
        double errorrate=0.5;
        double i=10;
        while(clusterSize<=50){
            for(int j=11;j<12;j=j+2) {
                ArrayList<HbaseIndexItem> listTem = new Pcam().computeHbaseSegmentAccordingSegment(v1, statistics[0],statistics[1],0.5, 4);
                for (int k = 0; k < listTem.size(); k++) {
                    if (Overlap(listTem.get(k), leftpoint, rightpoint)) {
                        count++;
                       // System.out.println(listTem.get(k).tomyString());
                        listResult.add(listTem.get(k));
                        length += listTem.get(k).getLength();
                        if(listTem.get(k).getLength()<5)
                        {
                            lengthcount++;
                        }
                    }
                }
                System.out.println("动态划分  查询内容:["+leftpoint+" "+ rightpoint+"] 当前误差为 "+0.5+"划分段数为 "+4+" 命中块数 "+count+"总共快数 "+listTem.size()+"命中数据数 "+length);
                System.out.println("有 "+lengthcount+" 条数据小于10");
                System.out.println("查询数据量"+ TestDataDistribution.TestDistribution(leftpoint,rightpoint,path) +" 命中数据数 "+length);
                computeCluster.Compute(listResult,clusterSize);
                length=0;
                count=0;
                lengthcount=0;
            }
           clusterSize=clusterSize+10;
           // i=i+10;
        }

       //HbaseQuery.queryDataScanList(tableName,listResult);
       //TestFutureTask2.threadCompute("weixing2000",listResult);
        //ErrorRate.computeHbaseIndexItemErrorRate(listResult,v1,10,11);
    }

    public static boolean  Overlap(HbaseIndexItem item, double left, double right)
    {
        if(item.getMax() < left || item.getMin() > right)     // a & b do not overlap
            return false;
        return true;
    }
}
