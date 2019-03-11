package TestWay;

import Item.HbaseIndexItem;
import PCAM.ReadCSV;
import benchmark.ErrorRate;
import cluster.computeCluster;
import hbasequery.HbaseQuery;
import traditionalway.SegmentUtils;

import java.util.ArrayList;

public class StaticWay {
    public static void main(String[] args) {
        ArrayList<String> arrTemperature= ReadCSV.readCsv(DynamicWay.path);
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        ArrayList<HbaseIndexItem> list= SegmentUtils.computeOriginalSegment(v1,                     1398285);
        for(int i=0;i<list.size();i++)
        {System.out.println(list.get(i).toString());}
       ArrayList<HbaseIndexItem>  listResult=ErrorRate.computeTraditionalErrorRate( DynamicWay.leftpoint,  DynamicWay.rightpoint,list,arrTemperature.size());
        computeCluster.Compute(listResult,DynamicWay.clusterSize);
      //  HbaseQuery.queryDataScanList(DynamicWay.tableName,listResult);
    }
}
