package hbasequery;

import PCAM.ReadCSV;

import java.io.IOException;
import java.util.ArrayList;

public class HBaseInsert {
    public static void main(String[] args) throws IOException {

         ArrayList<String> arrTemperature= ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\1000Wtemperature.csv");
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        HBaseUtils.getInstance().bigInsertTestImportBinary("cmop1000",arrTemperature);
    }
}
