package segmenttree;

import PCAM.ReadCSV;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static segmenttree.ParseCmop.ParseDate;
import static segmenttree.TesIntervalTree.ParseDirFile;

public class TestDir {
    public static void main(String[] args) throws IOException {
        List<String> rawCompList= ParseDirFile("D:\\DataProject\\DataWater");
        // ArrayList<String> rawCompList= ParseCmop.readCmopCsv("D:\\DataWater\\saturn01.0.F.CT_2008_12_PD0.csv");
        ArrayList<String> arrTemperature=parseRawCompListToString(rawCompList,false);
        writeCsv("D:\\DataProject\\DataGenerate\\temperature.csv",arrTemperature);
    }


    public static ArrayList<String>  parseRawCompListToString(List<String> list, boolean printLog) {
        ArrayList<String> arrTemperature=new ArrayList<String>();
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
            arrTemperature.add(arritem[3]);
            if(i%100000==0)
            {System.out.println("count "+i);}
        }

        return  arrTemperature;
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
}
