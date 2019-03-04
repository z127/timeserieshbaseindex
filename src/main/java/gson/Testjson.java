package gson;

import Item.HbaseIndexItem;
import Item.QueryItem;
import PCAM.Pcam;
import PCAM.ReadCSV;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import hbasequery.HBaseUtils;
import segmenttree.IntervalTree;
import segmenttree.IntervalTreeConstructor2;
import segmenttree.Node;
import segmenttree.NumberException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static hbasequery.HBaseUtils.formattedString;

public class Testjson {
    public static void main(String[] args) throws IOException {
        String filePath="D:\\DataProject\\DataGenerate\\temperature.csv";
        String writeToDes="D:\\DataProject\\DataJson\\HbaseIndex.json";
      // JsonGenerate(filePath,writeToDes);
        List<HbaseIndexItem> list= parseJson(writeToDes);
      IntervalTree tree=  GenerateTree(list);
        double leftpoint =  9.5;
        double rightpoint =  10;
      QueryResult(tree,leftpoint,rightpoint);
        //System.out.println(list.toString());
    }

    private static void QueryResult(IntervalTree T, double leftPoint,double rightPoint) throws IOException {
        //查询开始
        Long start=System.currentTimeMillis();
        System.out.println("查询开始的时间 "+start);
        System.out.println("The interval tree is:");
        // IntervalTreeConstructor2.IntervalT_InorderWalk(T.getRoot());
        System.out.println("The root of the tree is: " + T.getRoot().getLeftpoint() + "   " + T.getRoot().getRightpoint());
        System.out.println("left child" + T.getRoot().getChildLeft().getLeftpoint() + " " + T.getRoot().getChildLeft().getRightpoint()+" max "+T.getRoot().getChildLeft().getMax());
        System.out.println("left child" + T.getRoot().getChildRight().getLeftpoint() + " " + T.getRoot().getChildRight().getRightpoint()+" max "+T.getRoot().getChildRight().getMax());
        System.out.println("left child" + T.getRoot().getChildRight().getChildLeft().getLeftpoint() + " " + T.getRoot().getChildRight().getChildLeft().getRightpoint()+"  max "+T.getRoot().getChildRight().getChildLeft().getMax());
        System.out.println("/*-------------------------------------------------------------*/");
        System.out.println("/*--------------------Searching Interval Tree------------------*/");
        //34.995, min=34.687
        ArrayList<Node> nodeList=new ArrayList<Node>();
        Node rootNode=T.getRoot();
        try {
            IntervalTreeConstructor2.RecursiveIntervalSearch(rootNode, leftPoint, rightPoint,nodeList);
        } catch (NumberException e) {
            e.printStackTrace();
        }
        if (nodeList.size() > 0) {
            System.out.println("查询出区间有 " +nodeList.size()+" 个");
            Node node = nodeList.get(0);
            System.out.println("The overlap interval is:");
            System.out.println("[ " + node.getLeftpoint() + "  " + node.getRightpoint() + " ]");
            System.out.println(node.getColor());
            System.out.println("max " + node.getMax());
            System.out.println("node size" + node.getContent().size());
            ArrayList<QueryItem> listQueryItem= generateQueryItem(nodeList);
            System.out.println("查询的item有"+listQueryItem.size());
            Long generateTime=System.currentTimeMillis();
            System.out.println("查询获取rowkey的时间 "+generateTime);
            System.out.println("获取索引花了 "+(generateTime-start)+" 毫秒");
            HBaseUtils.getInstance().queryDataArrayListUsingRowKey("cmop",listQueryItem);
            Long queryEndTime=System.currentTimeMillis();
            System.out.println("查询花了 "+(queryEndTime-generateTime)+" 毫秒");
           /* for (int i = 0; i < nodeList.size(); i++) {
                Node node = nodeList.get(i);
                System.out.println("The overlap interval is:");
                System.out.println("[ " + node.getLeftpoint() + "  " + node.getRightpoint() + " ]");
                System.out.println(node.getColor());
                System.out.println("max " + node.getMax());
                System.out.println("node " + node.getContent().size());
            }*/


        }else

        {
            System.out.println("The searching interval doesn't exist in the tree.");
        }
    }

    private static ArrayList<QueryItem> generateQueryItem(ArrayList<Node> nodeList) {
        ArrayList<QueryItem> listQueryItem = new ArrayList<QueryItem>();
        for(int i=0;i<nodeList.size();i++) {
            Node node = nodeList.get(i);
            if (node.getContent().size() > 0) {
                for (Node item : node.getContent())
                    //item.getHbaseIndexitem().getStart();
                    listQueryItem.add(new QueryItem(formattedString(item.getHbaseIndexitem().getStart()), formattedString(item.getHbaseIndexitem().getEnd())));
            }
            listQueryItem.add(new QueryItem(formattedString(node.getHbaseIndexitem().getStart()), formattedString(node.getHbaseIndexitem().getEnd())));
        }
        return listQueryItem;

    }

    private static IntervalTree GenerateTree(List<HbaseIndexItem> listTem) {
        Node A[]= IntervalTreeConstructor2.transformToNode(listTem);
        int len = A.length;
        IntervalTree T = new IntervalTree();
        T.setRoot(new Node(IntervalTreeConstructor2.SENTINEL));
        for (int i = 0; i < len; i++)
            IntervalTreeConstructor2.IntervalT_Insert(T, A[i]);
        return T;
    }

    private static List<HbaseIndexItem>  parseJson(String writeToDes) throws IOException {
        Gson gson=new Gson();
        File file=new File(writeToDes);
        FileInputStream fis;
        fis = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
        List<HbaseIndexItem> listHbaseIndex = new ArrayList<HbaseIndexItem>();
        reader.beginArray();
        while (reader.hasNext()) {
            HbaseIndexItem message = gson.fromJson(reader, HbaseIndexItem.class);
            listHbaseIndex.add(message);
        }
        reader.endArray();
        reader.close();
        return listHbaseIndex;

    }

    public  static  void JsonGenerate(String filePath,String WritetoDes) throws IOException {
        Long start=System.currentTimeMillis();
        System.out.println("开始解析文件"+start);
        ArrayList<String> arrTemperature= ReadCSV.readCsv(filePath);
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        //计算均值，标准差，最小值，最大值
        double[] statistics= ReadCSV.computeStatistics(v1);
        //划分范围，最大值，最小值
        ArrayList<HbaseIndexItem> listTem=new Pcam().computeHbaseSegmentAccordingSegment(v1,statistics[0],statistics[1],statistics[1],3);
        for(HbaseIndexItem item:listTem)
        {
            System.out.println(item.toString());
        }
        JsonUtils.writeJsonStream(WritetoDes,listTem);
        Long end=System.currentTimeMillis();
        System.out.println("结束解析"+end);
        System.out.println("解析了　"+arrTemperature.size()+"　条数据, 生成　"+listTem.size()+"　条索引，一共花了 "+(end-start)+" 毫秒");

    }

}
