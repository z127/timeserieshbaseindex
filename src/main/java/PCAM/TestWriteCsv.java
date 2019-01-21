package PCAM;

import java.util.ArrayList;

public class TestWriteCsv {
    public static void main(String[] args) {
        ArrayList<String> listString= ReadCSV.readCsv("C:\\\\Users\\\\zqj\\\\Downloads\\\\UCR_TS_Archive_2015\\\\ECG5000\\\\ECG5000_TEST");
       ReadCSV.writeCsv("C:\\Users\\zqj\\Downloads\\UCR_TS_Archive_2015\\ECG5000\\ecg.csv",listString);
    }
}
