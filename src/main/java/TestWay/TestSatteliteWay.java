package TestWay;

import PCAM.ReadCSV;

public class TestSatteliteWay {
    public static void main(String[] args) {
        ReadCSV.readSatteliteCsv("D:\\DataProject\\DataGenerate\\weixing\\final_1.csv");
        ReadCSV.computeLine("D:\\DataProject\\DataGenerate\\weixing\\3000Weixing.csv");

    }
}
