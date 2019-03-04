package traditionalway;

import Item.HbaseIndexItem;
import PCAM.Pcam;
import PCAM.ReadCSV;

import java.util.ArrayList;
import java.util.TreeMap;

public class SegmentUtils {
    public static ArrayList<HbaseIndexItem> computeOriginalSegment(double[] values, int count)
    {
        double toleranceToCompute=0;
        int SegmentSize=  values.length/count;
        ArrayList<HbaseIndexItem > listSegment=new ArrayList<HbaseIndexItem>();
        double  min=Double.MAX_VALUE;
        double  max=Double.MIN_VALUE;
        int lastInterval=0;
        int length=0;
        int idCount=0;
        for(int i=0;i<values.length;i++) {
            min=Math.min(min,values[i]);
            max=Math.max(max,values[i]);
            if(i%SegmentSize==0 && i>0)
            {
                length=i-lastInterval;
               HbaseIndexItem item=new HbaseIndexItem(idCount,lastInterval,i,max,min,length);
               lastInterval=i;
               idCount++;
               listSegment.add(item);
               min=Double.MAX_VALUE;
                max=Double.MIN_VALUE;
            }
        }
        HbaseIndexItem item=new HbaseIndexItem(idCount,lastInterval,values.length,Math.max(max,values[values.length-1]),Math.min(min,values[values.length-1]),values.length-lastInterval+1,toleranceToCompute);
        //计算辅助信息
        listSegment.add(item);
        return listSegment;
    }
}
