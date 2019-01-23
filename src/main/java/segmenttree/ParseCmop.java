package segmenttree;

import Item.HbaseIndexItem;
import PCAM.Pcam;
import PCAM.ReadCSV;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParseCmop {
    public  static boolean printLog=false;
    public static void main(String[] args) {
        ArrayList<String> rawCompList= readCmopCsv("D:\\DataWater\\saturn01.0.F.CT_2008_12_PD0.csv");
        double[] arrTemperature=parseRawCompList(rawCompList,printLog);
        //计算均值，标准差，最小值，最大值
        double[] statistics= ReadCSV.computeStatistics(arrTemperature);
        //划分范围，最大值，最小值
        ArrayList<HbaseIndexItem> list=new Pcam().computeHbaseSegmentAccordingSegment(arrTemperature,statistics[0],statistics[1],3);


        for(HbaseIndexItem item:list)
        {
            System.out.println(item.toString());
        }

        System.out.println(" mean "+statistics[0]);
        System.out.println(" standard Deviation "+statistics[1]);
        System.out.println("HbaseItem Size "+ list.size());
        System.out.println("raw data size"+rawCompList.size());
    }

    public static double[] parseRawCompList(List<String> list,boolean printLog) {
                double[] arrTemperature=new double[list.size()];
                for(int i=0;i<list.size();i++)
                {
                    String item=list.get(i);
                    String[] arritem=item.split(",");
                    String date=ParseDate(arritem[0]);
                    if(printLog==true)
                    {System.out.println(" time "+arritem[0]);
                    System.out.println(" transformed time "+date);
                    System.out.println("water_salinity "+arritem[1]);
                    System.out.println("water_electrical_conductivity "+arritem[2]);
                    System.out.println("water_temperature "+arritem[3]);}
                    arrTemperature[i]=Double.parseDouble(arritem[1]);
                }

                return  arrTemperature;
    }

    public static String ParseDate(String timestamp) {
       String[]  arrtime=timestamp.split(" ");
        String[] date=arrtime[0].split("/");
        String[] time=arrtime[1].split(":");
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<date.length;i++)
        {
            sb.append(date[i]);
        }
        for(int i=0;i<time.length;i++)
        {
            sb.append(time[i]);
        }
     //   System.out.println("time "+ sb.toString());
        return  sb.toString();
    }


    public static ArrayList<String> readCmopCsv(String filepath) {
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
        ArrayList<String> allString = new ArrayList<>();
        try {
            int i=0;
            while ((line = br.readLine()) != null) // 读取到的内容给line变量
            {
                everyLine = line;
                i++;
                if(i<4)
                {
                    System.out.println(everyLine);
                    continue;
                }

                allString.add(everyLine);
            }
            System.out.println("csv表格中所有行数：" + allString.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;

    }
}
