package TestWay.readSrockFile;

import PCAM.ReadCSV;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadStockFile {
    public static void main(String[] args) throws IOException {
       /* ArrayList<String>    listFiles=getFiles("D:\\DataProject\\DataStock\\alldata");
        for(int i=0;i<listFiles.size();i++)
        {   ArrayList<String> list=  readStockFile(listFiles.get(i));
     ArrayList<String>  listWriteContent=parseStock(list,false);
        ReadCSV.writeCsv("D:\\DataProject\\DataGenerate\\stock\\500wStock.csv",listWriteContent);;
        }
        */
       //ReadCSV.computeLine("D:\\DataProject\\DataGenerate\\stock\\500wStock.csv");
        ArrayList<String> list=ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\stock\\600wStock.csv");
        ArrayList<String> list2=new ArrayList<>();
        for(int i=0;i<4000000;i++)
        {
            list2.add(list.get(i));
        }
       // ReadCSV.writeCsv("D:\\DataProject\\DataGenerate\\stock\\400wStock.csv",list2);
    }



    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
              System.out.println("文     件：" + tempList[i]);
                files.add(tempList[i].toString());
            }
            if (tempList[i].isDirectory()) {
                System.out.println("文件夹：" + tempList[i]);
            }
        }
        return files;
    }


    public  static  ArrayList<String>  readStockFile(String filepath)
    {
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
                allString.add(everyLine);
                i++;
            }
            System.out.println("csv表格中所有行数：" + allString.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;

    }


    public  static   ArrayList<String> parseStock(ArrayList<String> list ,boolean printLog)
    {
        ArrayList<String> listNodeNumder=new ArrayList<String>();
        for(int i=0;i<list.size();i++)
        {
           String    item=list.get(i);
            if(i==0)
            {
                continue;
            }
            String[] arr=item.split(",");
            //一档挂单数
            String    nodeNumber=arr[10];
            if(printLog==true)
            {
                System.out.println("一档挂单数: "+ nodeNumber);
            }
            listNodeNumder.add(nodeNumber);
        }

        return  listNodeNumder;

    }

}
