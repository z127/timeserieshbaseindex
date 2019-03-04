package segmenttree.optimizedalgorithm;

import Item.HbaseIndexItem;
import PCAM.Pcam;
import PCAM.ReadCSV;
import benchmark.ErrorRate;
import hbasequery.HBaseUtils;
import hbasequery.HbaseQuery;
import segmenttree.Node;

import java.util.ArrayList;

public class SequenceCompute {
    public static void main(String[] args) {
        ArrayList<String> arrTemperature= ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\3000Wtemperature.csv");
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        double leftpoint =  10.5;
        double rightpoint =11;
        //计算均值，标准差，最小值，最大值
        double[] statistics= ReadCSV.computeStatistics(v1);
      //  ReadCSV.kurtosis(v1,0,v1.length);
        double variance=statistics[1];
        int splitcount =10;
        //划分范围，最大值，最小值
        double i=1;
        int count=0;
        int  length=0;
        System.out.println("查询内容为"+leftpoint+" "+ rightpoint);
       // 当前误差为 1.0划分段数为 4命中块数 24574总共快数 281693命中数据数 5831701
        int lengthcount=0;
        ArrayList<HbaseIndexItem> listResult=new ArrayList<HbaseIndexItem>();
        while(i<=1){
            for(int j=11;j<12;j=j+2) {
                ArrayList<HbaseIndexItem> listTem = new Pcam().computeHbaseSegmentAccordingSegment(v1, statistics[0],statistics[1],1, 4);
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
                System.out.println("动态划分  查询内容:["+leftpoint+" "+ rightpoint+"] 当前误差为 "+i+"划分段数为 "+4+" 命中块数 "+count+"总共快数 "+listTem.size()+"命中数据数 "+length);
                System.out.println("有 "+lengthcount+" 条数据小于10");
                length=0;
                count=0;
                lengthcount=0;
            }
            i=i+0.1;
        }
        //HbaseQuery.queryDataScanList("cmop",listResult);
     //   ErrorRate.computeHbaseIndexItemErrorRate(listResult,v1,10,11);
    }

    public static boolean  Overlap(HbaseIndexItem item, double left, double right)
    {
        if(item.getMax() < left || item.getMin() > right)     // a & b do not overlap
            return false;
        return true;
    }
}
