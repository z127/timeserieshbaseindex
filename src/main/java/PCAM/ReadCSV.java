package PCAM;

import java.io.*;
import java.util.*;

public class ReadCSV {
    public static ArrayList<String> readCsv(String filepath) {
        File csv = new File(filepath); // CSV文件路径
        csv.setReadable(true);//设置可读
        csv.setWritable(true);//设置可写
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        int i=0;
        ArrayList<String> allString = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) // 读取到的内容给line变量
            {
                everyLine = line;
              //  System.out.println(everyLine);
                 allString.add(everyLine);
                i++;
            }
            System.out.println("csv表格中所有行数：" + allString.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;

    }



    public static void writeCsv(String filepath,ArrayList<String> m) throws IOException {
        File csv = new File(filepath); // CSV文件路径
        csv.setReadable(true);//设置可读
        csv.setWritable(true);
        if(!csv.exists())
        {
            System.out.println("不存在创建文件");
            csv.createNewFile();
        }else {
            System.out.println("文件存在");
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csv,true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";
        try {
            for(int i=0;i<m.size();i++)
            {
                line=m.get(i);
               // System.out.println("newLine"+line);
                bw.write(line+"\n");
            }
           bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 转成浮点
     * @param row
     * @return
     */
    public static double[] transferToDouble(String row)
    {
      row=row.substring(1,row.length());
        String[]  m=row.split(",");
        double[] array = new double[m.length];
       for(int i=0;i<m.length;i++)
       {
          array[i]=Double.parseDouble(m[i]);
       }
        System.out.println("array Length : "+array.length);
        return  array;
    }


    /**
     *
     * @param values
     * @return
     */
    public static double[] computeDistribution(double[] values) {
        double totalNum=0;
        double minValue=values[0];
        double maxValue=values[0];
        for(int i=0;i<values.length;i++)
        {
            minValue=Math.min(minValue,values[i]);
            maxValue=Math.max(maxValue,values[i]);
            totalNum+=values[i];
        }
        double meanValue=totalNum/values.length;
        double standardDiviation=computeStandardDiviation(values);
        System.out.println(" standardDiviation "+ standardDiviation);
        System.out.println(" minValue "+minValue);
        System.out.println(" maxValue "+maxValue);
        System.out.println(" meanvalue " + meanValue);
        double[] Maxsplit=splitStandardDiviationPoint(meanValue,standardDiviation);
        TreeMap<Double,Integer> valueMap=new  TreeMap<Double,Integer>();
        for(int i=0;i< values.length;i++)
        {
            double num=whichLevel(values[i],Maxsplit);
            valueMap.put(num,valueMap.getOrDefault(num,0)+1);
        }

        ComputeValueCount(values, Maxsplit);
        double[] statistics=new double[3];
        statistics[0]=meanValue;
        statistics[1]=standardDiviation;
        return statistics ;


    }


    /**
     *
     * @param values
     * @return
     */
    public static double[] computeStatistics(double[] values) {
        double totalNum=0;
        double minValue=values[0];
        double maxValue=values[0];
        for(int i=0;i<values.length;i++)
        {
            minValue=Math.min(minValue,values[i]);
            maxValue=Math.max(maxValue,values[i]);
            totalNum+=values[i];
        }
        double meanValue=totalNum/values.length;
        double standardDiviation=computeStandardDiviation(values);
        System.out.println(" standardDiviation "+ standardDiviation);
        System.out.println(" minValue "+minValue);
        System.out.println(" maxValue "+maxValue);
        System.out.println(" meanvalue " + meanValue);
        double[] m=new double[4];
        m[0]=meanValue;
        m[1]=standardDiviation;
        m[2]=minValue;
        m[3]=maxValue;
        return m;
    }





    /**
     *
     * @param values
     * @param maxsplit
     */
    private static void ComputeValueCount(double[] values, double[] maxsplit) {
        TreeMap<Double, Integer> valueMap =new  TreeMap<Double, Integer>() ;
        for(int i=0;i< values.length;i++)
        {
            double num=whichLevel(values[i], maxsplit);
            valueMap.put(num,valueMap.getOrDefault(num,0)+1);
        }

        for(Double key:valueMap.keySet())
        {
            System.out.println("Key: "+key+" Value: "+valueMap.get(key));
        }
    }

    private static double whichLevel(double value, double[] maxsplit) {
        for(int i=0;i<maxsplit.length;i++)
        {
            if(value<=maxsplit[i])
            {
                return maxsplit[i];
            }

        }
        return  maxsplit[maxsplit.length-1];
    }

       private static double[] SplitPoint(double first,double second,double third,double variance) {
        double firstLength=second-first;
        double secondLength=third-second;
        double[] array=new double[10];
        for(int i=0;i<10;i++)
        {
            if(i<5)
            {
                array[i]=first+firstLength*i/5;
            }else {
                array[i]=second+secondLength*i/5;
            }
            System.out.println(" split "+i+" "+array[i]);
        }

        return  array;
    }
    private static double[] splitStandardDiviationPoint(double mean,double variance) {
        double[] array=new double[10];
        for(int i=0;i<array.length;i++)
        {
            array[i]=mean+variance*(i-5);
            System.out.println(" split "+i+" "+array[i]);
        }
        return  array;
    }

    /**
     * Segment
     * @param mean
     * @param variance
     * @param count
     * @return
     */
    public static TreeMap<Double,Double> splitStandardDiviationPointSegment(double mean,double variance,int count) {
        TreeMap<Double,Double> sortedMap=new TreeMap<Double, Double>( );
        System.out.println("mean "+ mean+" variance "+variance);
        double m=6.0/count;
        for(int i=0;i<count;i++)
        {
           double  parameter=variance*(i-count/2)*m;
           sortedMap.put(mean+parameter,caseTolerance(i,count/2 ,variance));
        }
       Set<Double> set=sortedMap.keySet();
       /* int i=0;
        for(double item:set)
        {
            System.out.println(" i "+i +" key "+item+"  value "+sortedMap.get(item));
            i++;
        }*/
        return sortedMap;
    }

    /**
     * 获取误差
     * @param i
     * @param count
     * @param variance
     * @return
     */
    private static Double caseTolerance(double i,double count,double variance) {
       if(Math.abs(i-count)>=1)
       {
         double m=  (Math.abs(i-count)+1);
          // System.out.println(" i "+ i + " count "+ count+"  i - count "+Math.abs(i-count));
           return  variance/m;
       }else {
           return  variance;
       }
    }


    public static double computeStandardDiviation(double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        return Math.sqrt(dVar/m);
    }


    /**
     * 纵向转化
     * @param values
     * @param segments
     * @return
     */

    public static double[] transform(double[] values,int segments) {

        return  null;
    }


}
