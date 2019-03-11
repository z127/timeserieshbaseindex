package traditionalway;

import Item.HbaseIndexItem;
import PCAM.ReadCSV;
import benchmark.ErrorRate;
import hbasequery.HBaseUtils;
import hbasequery.HbaseQuery;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<String> arrTemperature= ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\2000Wtemperature.csv");
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        ArrayList<HbaseIndexItem> list= SegmentUtils.computeOriginalSegment(v1,      181512);
        for(int i=0;i<list.size();i++)
        {System.out.println(list.get(i).toString());}
      ArrayList<HbaseIndexItem>  listResult=ErrorRate.computeTraditionalErrorRate( 1.4434,  4.8907,list,arrTemperature.size());
        HbaseQuery.queryDataScanList("cmop2000",listResult);
    }
}
