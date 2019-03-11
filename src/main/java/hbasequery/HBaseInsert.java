package hbasequery;

import PCAM.ReadCSV;

import java.io.IOException;
import java.util.ArrayList;

public class HBaseInsert {
    public static void main(String[] args) throws IOException {
         ArrayList<String> arrTemperature= ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\weixing\\3000Weixing.csv");
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        HBaseUtils.getInstance().bigInsertTestImport("weixing3000",arrTemperature);
    }
}
