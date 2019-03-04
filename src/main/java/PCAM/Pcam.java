package PCAM;

import Item.HbaseIndexItem;
import com.sun.org.apache.xml.internal.dtm.DTMAxisTraverser;
import org.apache.commons.math.stat.descriptive.moment.Kurtosis;

import javax.swing.text.Segment;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executor;

public class Pcam {
        public  static boolean computedetails=false;

    /**
     * 计算
     * @param values
     * @return
     */
    public ArrayList<HbaseIndexItem> computeHbaseSegment(double[] values,double tolerance)
    {

        ArrayList<HbaseIndexItem > listSegment=new ArrayList<HbaseIndexItem>();
        double  min=values[0];
        double  max=values[0];
        int lastInterval=0;
       for(int i=0;i<values.length;i++)
       {
           if(Math.max(max,values[i])-Math.min(min,values[i])>2*tolerance)
           {
            HbaseIndexItem item=new HbaseIndexItem(lastInterval,i,max,min,i-lastInterval);
            ComputeMean(values,item,tolerance,Pcam.computedetails);
            listSegment.add(item);
            lastInterval=i;
            min=values[i];
            max=values[i];
           }else {
            min=Math.min(min,values[i]);
            max=Math.max(max,values[i]);
           }
       }
        HbaseIndexItem item=new HbaseIndexItem(lastInterval,values.length,Math.max(max,values[values.length-1]),Math.min(min,values[values.length-1]),values.length-lastInterval);
        ComputeMean(values,item,tolerance,Pcam.computedetails);
        listSegment.add(item);
        return listSegment;
    }


    /**
     * 根据误差，进行分段。
     * @param values
     * @param mean
     * @param tolarence
     * @param variance
     * @return
     */

    public ArrayList<HbaseIndexItem> computeHbaseSegmentAccordingMeanAndStv(double[] values,double mean,double tolarence,double variance)
    {
        double toleranceToCompute=tolarence;
        ArrayList<HbaseIndexItem > listSegment=new ArrayList<HbaseIndexItem>();
        double  min=values[0];
        double  max=values[0];
        int lastInterval=0;
        int length=values.length;
        for(int i=0;i<values.length;i++) {
            if(((i+0.0)/values.length)%0.1==0)
            {
                System.out.println(" percent "+i/values.length);
            }
          /*  if ( Math.abs(values[i] - mean) >variance )
            {
                toleranceToCompute=tolarence/3;
            }else {
                toleranceToCompute=tolarence;
            }*/
                if (Math.max(max, values[i]) - Math.min(min, values[i]) > 2 * toleranceToCompute) {
                    HbaseIndexItem item = new HbaseIndexItem(lastInterval, i, max, min,i-lastInterval);
                  // ComputeMean(values, item, toleranceToCompute,Pcam.computedetails);
                    modifyBlock(item, listSegment,length,5);
                    listSegment.add(item);
                    lastInterval = i;
                    min = values[i];
                    max = values[i];
                } else {
                    min = Math.min(min, values[i]);
                    max = Math.max(max, values[i]);
                }
        }
        HbaseIndexItem item=new HbaseIndexItem(lastInterval,values.length,Math.max(max,values[values.length-1]),Math.min(min,values[values.length-1]),length-lastInterval);
        //计算错误率
        //ComputeMean(values,item,toleranceToCompute,Pcam.computedetails);
        listSegment.add(item);
        return listSegment;
    }

    private void modifyBlock(HbaseIndexItem item, ArrayList<HbaseIndexItem > list,int length,int count) {
            int percent=length/1000;
            int itemMin=0;
            if(percent>0)
            {
                itemMin=Math.min(count,itemMin);
            }else {
                itemMin=count;
            }
            if(item.getLength()>itemMin || list.size()==0)
            {
                return;
            }
            HbaseIndexItem preItem=list.get(list.size()-1);
            preItem.setEnd(preItem.getEnd()+item.getLength());
            item.setEnd(item.getEnd()+count);
    }









    /**
     * 根据MeanAndVariance误差，进行分段。
     * @param values
     * @param mean
     * @param variance
     * @param count
     * @return
     */

    public ArrayList<HbaseIndexItem> computeHbaseSegmentAccordingSegment(double[] values, double mean, double variance,double errorrate,int count)
    {
        double toleranceToCompute=0;
        TreeMap map=ReadCSV.splitStandardDiviationPointSegment(mean,variance,errorrate,count);
        ArrayList<HbaseIndexItem > listSegment=new ArrayList<HbaseIndexItem>();
        double lasttolerance=Double.MAX_VALUE;
        double  min=values[0];
        double  max=values[0];
        int lastInterval=0;
        int idCount=0;
        for(int i=0;i<values.length;i++) {
          /*  if(((i+0.0)/values.length)%0.1==0)
            {
                System.out.println(" percent "+(i+0.0)/values.length);
            }*/
            toleranceToCompute=generateTolerance(map,values[i]);
            toleranceToCompute=Math.min(toleranceToCompute,lasttolerance);
            if (Math.max(max, values[i]) - Math.min(min, values[i]) > 2 * toleranceToCompute  ) {
                HbaseIndexItem item = new HbaseIndexItem(idCount,lastInterval, i, max, min,i-lastInterval,toleranceToCompute);
               //计算辅助信息
             // ComputeMean(values, item, toleranceToCompute,Pcam.computedetails);
                lasttolerance=Double.MAX_VALUE;
                listSegment.add(item);
                lastInterval = i;
                min = values[i];
                max = values[i];
                idCount++;
            } else {
                lasttolerance= toleranceToCompute;
                min = Math.min(min, values[i]);
                max = Math.max(max, values[i]);
            }

        }
        HbaseIndexItem item=new HbaseIndexItem(idCount,lastInterval,values.length,Math.max(max,values[values.length-1]),Math.min(min,values[values.length-1]),values.length-lastInterval+1,toleranceToCompute);
        //计算辅助信息
         //ComputeMean(values,item,toleranceToCompute,Pcam.computedetails);
        listSegment.add(item);
        return listSegment;
    }

    /**
     * 计算出对应的tolerance
     * @param map
     * @param value
     * @return
     */
    public double generateTolerance(TreeMap<Double,Double> map, double value) {
       Object[]  m= map.keySet().toArray();
       int lastIndex=0;
       for(int i=0;i<m.length;i++)
       {
           if ((Double)m[i]>=value )
           {
               return  map.get(m[i]);
           }
       }
       //返回最大值
        return map.get(m[m.length-1]);

    }


    /**
     * 根据MeanAndVariance误差，进行分段。
     * @param values
     * @param mean
     * @param variance
     * @param count
     * @return
     */

    public ArrayList<HbaseIndexItem> compute2HbaseSegmentAccordingSegment(double[] values, double mean, double variance,double errorrate,int count)
    {
        double toleranceToCompute=0;
        TreeMap map=ReadCSV.split2StandardDiviationPointSegment(mean,variance,errorrate,count,values);
        ArrayList<HbaseIndexItem > listSegment=new ArrayList<HbaseIndexItem>();
        double lasttolerance=Double.MAX_VALUE;
        double  min=values[0];
        double  max=values[0];
        int lastInterval=0;
        int idCount=0;
           System.out.println("tolerance : "+toleranceToCompute);
        for(int i=0;i<values.length;i++) {
          /*  if(((i+0.0)/values.length)%0.1==0)
            {
                System.out.println(" percent "+(i+0.0)/values.length);
            }*/
           // toleranceToCompute=generateTolerance(map,values[i]);
            toleranceToCompute=1.0;
            toleranceToCompute=Math.min(toleranceToCompute,lasttolerance);
         //   System.out.println(toleranceToCompute);
            if (Math.max(max, values[i]) - Math.min(min, values[i]) > 2 * toleranceToCompute )
            {
                HbaseIndexItem item = new HbaseIndexItem(idCount,lastInterval, i, max, min,i-lastInterval,toleranceToCompute);
                //计算辅助信息
                // ComputeMean(values, item, toleranceToCompute,Pcam.computedetails);
                lasttolerance=Double.MAX_VALUE;
                listSegment.add(item);
                lastInterval = i;
                min = values[i];
                max = values[i];
                idCount++;
            } else {
                lasttolerance= toleranceToCompute;
                min = Math.min(min, values[i]);
                max = Math.max(max, values[i]);
            }

        }
        HbaseIndexItem item=new HbaseIndexItem(idCount,lastInterval,values.length,Math.max(max,values[values.length-1]),Math.min(min,values[values.length-1]),values.length-lastInterval+1,toleranceToCompute);
        //计算辅助信息
        //ComputeMean(values,item,toleranceToCompute,Pcam.computedetails);
        listSegment.add(item);
        return listSegment;
    }


    /**
     *
     * @param values
     * @param item
     * @param tolerance
     * @return
     */
    private void ComputeMean(double[] values,HbaseIndexItem item,double tolerance,boolean bool) {
        if(bool==false)
        {
           return;
        }
        double totalValue=0;
        double ErrorValue=0;
        int start=item.getStart();
        int end=item.getEnd();
        String content=null;
        for(int i=start;i<end;i++)
        {
            content=content+","+values[i];
            ErrorValue+=Math.abs(values[i]-tolerance);
            totalValue+=values[i];
        }
        int valueCount=end-start;
        item.setError(ErrorValue/valueCount);
        item.setMean(totalValue/valueCount);
        item.setItemString(content);

    }




}
