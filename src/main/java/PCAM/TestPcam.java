package PCAM;

import Item.HbaseIndexItem;
import benchmark.ErrorRate;

import java.util.ArrayList;

public class TestPcam {
    public static void main(String[] args) {
       double[] v = {21, 10, 1, 0, 5, 12, 14, 3, 200,15, 12, 4, 3, 26, 1, 10, 24, 13, 9, 13,120, 0, 28, 28, 26, 10, 100,11, 2, 0, 5, 1, 18, 13, 6, 8, 28, 3, 28, 15, 15, 3, 16, 22, 0, 12, 3, 23, 21, 16, 17, 2, 26, 5, 14, 16, 13, 13, 7, 2, 9, 4, 22, 4, 29, 9, 27, 16, 6, 2, 20, 9, 8, 5, 12, 27, 23, 27, 13, 20, 16, 23, 16, 0, 22, 23, 9, 0, 12, 28, 17, 10, 0, 19, 17, 7, 11, 25, 23, 17, 27, 9, 29, 111, 124, 104, 123,27, 106, 102, 121, 102, 110, 120, 102, 25,105, 116, 118, 129, 119, 30,119, 119, 121, 106, 115, 127, 102, 117, 104, 113, 106, 117, 125, 123, 104, 104, 101, 116, 126, 114, 119, 128, 105, 121, 110, 107, 124, 107, 109, 122, 119, 125, 116, 108, 112, 128, 127, 116, 116, 127, 106, 106, 121, 122, 115, 118, 122, 108, 105, 100, 125, 112, 122, 129, 115, 117, 121, 103, 116, 105, 114, 126, 121, 106, 114, 115, 100, 123, 108, 123, 70,116, 114, 104, 120,50, 102, 100, 106, 121, 123, 110, 118, 113, 122, 109};
        double[] v1 = {21, 10, 1, 0, 5, 12, 14, 3, 200,15, 12, 4, 3, 26, 1, 10, 24, 13, 9, 13,8, 0, 28, 28, 26, 10, 100,11, 2, 0, 5, 1, 18, 13, 6, 8, 28, 3, 28, 15, 15, 3, 16, 22, 0, 12, 3, 23, 21, 16, 17, 2, 26, 5, 14, 16, 13, 13, 7, 2, 9, 4, 22, 4, 29, 9, 27, 16, 6, 2, 20, 9, 8, 5, 12, 27, 23, 27, 13, 20, 16, 23, 16, 0, 22, 23, 9, 0, 12,200,230,240, 28, 17, 10, 7, 11, 25, 23, 17, 27, 9, 29, 111, 124, 104, 123,160, 106, 102, 121, 102, 110, 120, 102, 150,105, 116,150,160,130,120 };

        //double[] v = {1, 2, 1,5,6,7,8,9,10,5,6,8};
      // ArrayList<String> listString= ReadCSV.readCsv("C:\\\\Users\\\\zqj\\\\Downloads\\\\UCR_TS_Archive_2015\\\\ECG5000\\\\ecg.csv");
       //double[] v1= ReadCSV.transferToDouble(listString.get(0));
    //    System.out.println("message "+v1.length);
       double[] statistics= ReadCSV.computeDistribution(v);
      //  double[] v2= ReadCSV.transform(v1,10);
        Pcam pcam=new Pcam();
     //ArrayList<HbaseIndexItem> list=pcam.computeHbaseSegment(v1,2*statistics[1]);
        double mean=statistics[0];
        double standVariance=statistics[1];
        double tolarence=50;
        ArrayList<HbaseIndexItem> list=pcam.computeHbaseSegmentAccordingMeanAndStv(v1,mean,tolarence,
                standVariance);
        System.out.println(" mean "+statistics[0]);
        System.out.println(" standard Deviation "+statistics[1]);
        System.out.println("HbaseItem Size "+ list.size());
       for(HbaseIndexItem item:list)
        {
            System.out.println(item.toString());
        }

      //  ErrorRate.computeErrorRate(8.5,list);

       // ReadCSV.computeDistribution(v);

    }


}
