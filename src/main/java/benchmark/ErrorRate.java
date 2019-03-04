package benchmark;

import Item.HbaseIndexItem;
import hbasequery.HBaseUtils;
import segmenttree.Node;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ErrorRate {

        public  static   ArrayList<HbaseIndexItem> computeTraditionalErrorRate(double leftpoint ,double rightpoint , ArrayList<HbaseIndexItem > list,int size)
        {
           ArrayList<HbaseIndexItem> listResult=new ArrayList<HbaseIndexItem>();
            System.out.println("segment size : "+ list.size());
            long start=System.currentTimeMillis();
            System.out.println(" start time : "+ start);
            int count=0;
            int length=0;
            double totalLength=0;
            for(int i=0;i<list.size();i++)
            {
                if(Overlap(list.get(i),leftpoint,rightpoint))
                {
                    count++;
                    length+=list.get(i).getLength();
                    listResult.add(list.get(i));
                }
                if(i==(list.size()-1))
                {
                    totalLength=(double) list.get(i).getEnd();
                }
            }
            System.out.println("静态划分 数据总量 : "+size+" 查询范围 : ["+leftpoint+","+rightpoint+"] ,命中块数 "+count+"总共快数 "+list.size()+"命中数据数 "+length);

            System.out.println(count);
            long end=System.currentTimeMillis();
            System.out.println();
            System.out.println("end time:" + end);
            System.out.println("compute time : "+(end-start));
            return  listResult;
        }

    public  static  double computeHbaseIndexItemErrorRate( ArrayList<HbaseIndexItem> list,double[] value,int leftpoint,int rightpoint)
    {
        System.out.println("segment size : "+ list.size());
       double count=list.size();
       int totalcorrectCount=0;
       int totalSize=0;
        double totalLength=0;
        ArrayList<HbaseIndexItem> listsplit=new ArrayList<HbaseIndexItem>();;
        ArrayList<Integer> listInteger=new ArrayList<Integer>();
        Iterator<HbaseIndexItem> it = list.iterator();
        System.out.println("之前listsize"+list.size());
       while (it.hasNext())
       {   HbaseIndexItem item=it.next();
            int correctCount=ComputeOccupation(value,item.getStart(),item.getEnd(),leftpoint,rightpoint);
            DecimalFormat df3  = new DecimalFormat("##0.00000");
             double percent   =Double.parseDouble(df3.format((correctCount * 1.0) / ((double) item.getLength())));
            if(item.getLength()>1000 && percent<0.1) {
           //     System.out.println("start -end" + item.getStart() + "," + item.getEnd() + " ItemSize " + item.getLength() + "rightCount: " + correctCount + "errorCountPercent " +percent );
                        it.remove();
                listsplit.addAll(computeSubSequenceIndexItemErrorRate(value,item,leftpoint,rightpoint,listsplit));
            }
        //    System.out.println("listsplit "+listsplit.size());
            totalcorrectCount+=correctCount;
            totalSize+=item.getLength();
        }
        for(int i=0;i<listInteger.size();i++)
        {
            list.remove(listInteger.get(i));
        }
        System.out.println("删除后listsize"+list.size());
        list.addAll(listsplit);
        System.out.println("之后listsize"+list.size());
        for (int i=0;i<list.size();i++)
        {
            count+=list.get(i).getLength();
        }
        System.out.println("命中数据"+count);
        DecimalFormat df2  = new DecimalFormat("###.00000");
        System.out.println(df2.format(totalcorrectCount/totalLength));
        return totalcorrectCount/totalLength;
    }



    private static  ArrayList<HbaseIndexItem> computeSubSequenceIndexItemErrorRate(double[] value,HbaseIndexItem item,double leftpoint,double rightpoint,List<HbaseIndexItem> list) {
        ArrayList<HbaseIndexItem> itemList=new ArrayList<HbaseIndexItem>();
        int start=item.getStart();
        int end=item.getEnd();
        int indexstart=0;
        int indexend=0;
        int count=0;
        double rate=0;
        double Lastindexend=0;
        for(int i=start;i<end;i++) {
            if(OverlapPoint(value[i],leftpoint,rightpoint))
            {
                count++;
                if(indexstart==0) {
                    indexstart = i;
                    indexend=i+1;
                }else {
                    indexend=i;
                }
                if(indexstart!=0)
                {rate=(count*1.0)/(indexend-indexstart);}
            }else {
             //   System.out.println("rate 现在 "+rate+" indexstart "+indexstart +" indexend "+indexend);
                if(rate< 0.5 && indexstart!=0)
                {
                    System.out.println("我现在开始添加");
                    itemList.add(new HbaseIndexItem(0,indexstart,indexend,0,0,indexend-indexstart,0));
                    count=0;
                    indexstart=0;
                    indexend=0;
                }
            }
        }
        System.out.println("size "+list.size());
        itemList.add(new HbaseIndexItem(0,indexstart,indexend,0,0,indexend-indexstart,0));
        return itemList;
    }

    private static int ComputeOccupation(double[] value, int start, int end, int leftpoint, int rightpoint) {
            int count =0;
            for(int i=start;i<end;i++)
        {
            if(value[i]<rightpoint && value[i]>leftpoint)
            {
                            count++;
            }
        }
        return count;

    }

    public static boolean  Overlap(HbaseIndexItem item, double left, double right)
    {
        if(item.getMax() < left || item.getMin() > right)     // a & b do not overlap
            return false;
        return true;
    }
    public static boolean  OverlapPoint(double num, double left, double right)
    {
        if(num<right&&num>left)
        {
            return  true;
        }
        return  false;
    }


}

