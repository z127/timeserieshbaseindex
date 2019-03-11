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

    public static ArrayList<String> readSatteliteCsv(String filepath) {
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
        String arr[];
        try {
            while ((line = br.readLine()) != null) // 读取到的内容给line变量
            {
                everyLine = line;
               //  System.out.println(everyLine);
               arr=everyLine.split(",");
               // System.out.println(arr.toString());
              //  System.out.println(arr[0]);
                int m=34;
                if(!arr[m].equals("0.000000"))
                {
                    allString.add(arr[m]);
                }else {
                    System.out.println("等于0"+arr[m]);
                }
                i++;
            }
            System.out.println("csv表格中所有行数：" + allString.size());
           //writeCsv("D:\\DataProject\\DataGenerate\\weixing\\3000weixing.csv", allString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;

    }

    public static ArrayList<String> computeLine(String filepath) {
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
        String arr[];
        try {
            while ((line = br.readLine()) != null) // 读取到的内容给line变量
            {
                i++;

            }
            System.out.println("计算行数,csv表格中所有行数：" + i);
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
            /*if(values[i]>1000)
            {
                System.out.println("序号是"+i+" "+values[i]);
            }*/
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
     * @param Errorrate
     * @param count
     * @return
     */
    public static TreeMap<Double,Double> splitStandardDiviationPointSegment(double mean,double variance,double Errorrate,int count) {

        TreeMap<Double,Double> sortedMap=new TreeMap<Double, Double>( );
       // System.out.println("mean "+ mean+" Errorrate "+Errorrate+" variance "+ variance);
        //总的差距超不过 mean+3variance
        double m=6.0/count;
        for(int i=0;i<count;i++)
        {
           double  parameter=variance*(i-count/2)*m;
           sortedMap.put(mean+parameter,caseTolerance(i,count/2 ,Errorrate));
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
     * @param Errorrate
     * @return
     */
    private static Double caseTolerance(double i,double count,double Errorrate) {
       if(Math.abs(i-count)>=1)
       {
         double m=  (Math.abs(i-count)+1);
          // System.out.println(" i "+ i + " count "+ count+"  i - count "+Math.abs(i-count));
           return  Errorrate/m;
       }else {
           return  Errorrate;
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


    public static TreeMap split2StandardDiviationPointSegment(double mean, double variance, double errorrate, int countLevel, double[] values) {
        double[] errorValue=new double[countLevel];
        double minValue=Double.MAX_VALUE;
        double maxValue=Double.MIN_VALUE;
        for(double i=0;i<countLevel;i++)
        {
            errorValue[(int)i]=Math.min(errorrate*(1-i/countLevel),errorrate*0.2);
        }
       double    standVariance=computeStandardDiviation(values);
        double total=0;
        for(int i=0;i<values.length;i++)
        {
            minValue=Math.min(minValue,values[i]);
            maxValue=Math.max(maxValue,values[i]);
            total+=values[i];
        }
        System.out.println("minValue "+minValue+" MaxValue "+maxValue);
        System.out.println("均值 "+mean);
        /**
         * 最大值，最小值抽取
         */
        double[] splitLevel=new double[countLevel];
        double chaZhi=(maxValue-minValue)/ countLevel;
        System.out.println("chazhi"+chaZhi);
        TreeMap<Double,Integer> map=new TreeMap<>();
        double lastValue=0;
        for(int i=0;i< countLevel;i++)
        {
            if(i==0)
            {   lastValue=minValue;
                map.put(minValue,0);}
            else
            {
                lastValue=lastValue+chaZhi;
               map.put(lastValue,0);
            }
        }
        for(int i=0;i<values.length;i++)
        {
            Object[]  arr= map.keySet().toArray();
            int lastIndex=0;
            for(int j=0;j<arr.length;j++)
            {
                if(values[i]>=(double)arr[arr.length-1])
                {
                    map.put((double)arr[arr.length-1],map.get(arr[arr.length-1])+1);
                    break;
                }
                if(values[i]<(double)arr[j])
                {
                   map.put((double)arr[j-1],map.get(arr[j-1])+1);
                   break;
                }else if(values[i]==(double)arr[j]){
                    map.put((double)arr[j],map.get(arr[j])+1);
                    break;
                }
            }
        }
        System.out.println(map.toString());
        return  map;
    }

    public static   boolean kurtosis(double[] values,int start, int end)
    {
        double variance=0;
        double mean=computeMean(values,start,end);
        double fourPower=0;
        if(start>end-100)
        {
            return  false;
        }
      for(int i=start;i<end;i++)
      {
         fourPower += Math.pow((values[i] - mean), 4);
          variance += Math.pow((values[i] - mean), 2);
      }
      double result=(end-start) * fourPower / Math.pow(variance, 2);
        System.out.println("kurtosis : "+result);
        if(result>3)
        {  System.out.println("3");
            return true;
        }
        return
                false;
    }

    /**
     * end
     * @param values
     * @param start
     * @param end
     * @return
     */
    public static double  computeMean(double[] values,int start, int end) {
        double total=0;
        for(int i=start;i<end;i++)
        {
            total+=values[i];
        }
        return  total/(end-start);
    }

}
