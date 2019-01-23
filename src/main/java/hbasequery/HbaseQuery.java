package hbasequery;

import PCAM.ReadCSV;

import java.io.IOException;
import java.util.ArrayList;

public class HbaseQuery {
    public static void main(String[] args) throws IOException {
        //HBaseUtils.getInstance().get("hbase_student");
        ArrayList<String> arrTemperature= ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\temperature.csv");
       /* double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }*/
        HBaseUtils.getInstance().bigInsertTestImport("cmop",arrTemperature);
    }
}
