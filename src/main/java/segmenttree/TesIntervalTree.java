package segmenttree;

import Item.HbaseIndexItem;
import PCAM.Pcam;
import PCAM.ReadCSV;
import segmenttree.IntervalTree;
import segmenttree.IntervalTreeConstructor;
import segmenttree.IntervalTreeConstructor2;
import segmenttree.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TesIntervalTree {

    public static void main(String[] args) throws NumberException {
       // List<String> rawCompList= ParseDirFile("D:\\DataWater");
       // ArrayList<String> rawCompList= ParseCmop.readCmopCsv("D:\\DataWater\\saturn01.0.F.CT_2008_12_PD0.csv");
        ArrayList<String> arrTemperature=ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\temperature.csv");
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        //计算均值，标准差，最小值，最大值
        double[] statistics= ReadCSV.computeStatistics(v1);
        //划分范围，最大值，最小值
        ArrayList<HbaseIndexItem> listTem=new Pcam().computeHbaseSegmentAccordingSegment(v1,statistics[0],statistics[1],3);
        for(HbaseIndexItem item:listTem)
        {
            System.out.println(item.toString());
        }

        System.out.println(" mean "+statistics[0]);
        System.out.println(" standard Deviation "+statistics[1]);
        System.out.println(" HbaseItem Size "+ listTem.size());
        System.out.println(" raw data size "+v1.length);
        Node A[]= IntervalTreeConstructor2.transformToNode(listTem);
       /* Node A[] = {new Node(16, 21), new Node(8, 9), new Node(5, 8), new Node(17, 21),new Node(5, 9),
                new Node(25, 30), new Node(5, 8), new Node(15, 23), new Node(17, 19), new Node(26, 26), new Node(0, 3), new Node(6, 10), new Node(19, 20)};
        */
        int len = A.length;
        IntervalTree T = new IntervalTree();
        T.setRoot(new Node(IntervalTreeConstructor2.SENTINEL));
        for (int i = 0; i < len; i++)
            IntervalTreeConstructor2.IntervalT_Insert(T, A[i]);

        System.out.println("The interval tree is:");
       // IntervalTreeConstructor2.IntervalT_InorderWalk(T.getRoot());
        System.out.println("The root of the tree is: " + T.getRoot().getLeftpoint() + "   " + T.getRoot().getRightpoint());
        System.out.println("left child" + T.getRoot().getChildLeft().getLeftpoint() + " " + T.getRoot().getChildLeft().getRightpoint()+" max "+T.getRoot().getChildLeft().getMax());
        System.out.println("left child" + T.getRoot().getChildRight().getLeftpoint() + " " + T.getRoot().getChildRight().getRightpoint()+" max "+T.getRoot().getChildRight().getMax());
        System.out.println("left child" + T.getRoot().getChildRight().getChildLeft().getLeftpoint() + " " + T.getRoot().getChildRight().getChildLeft().getRightpoint()+"  max "+T.getRoot().getChildRight().getChildLeft().getMax());
        System.out.println("/*-------------------------------------------------------------*/");
        System.out.println("/*--------------------Searching Interval Tree------------------*/");
        double leftpoint =  9.5;
        double rightpoint =  10;
        //34.995, min=34.687
        ArrayList<Node> nodeList=new ArrayList<Node>();
        Node rootNode=T.getRoot();
      IntervalTreeConstructor2.RecursiveIntervalSearch(rootNode, leftpoint, rightpoint,nodeList);
        if (nodeList.size() > 0) {
            System.out.println("查询出区间有 " +nodeList.size()+" 个");
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
       public  static List ParseDirFile(String FilPath)
        {
            ArrayList<String> list =new ArrayList<String>();
            String path = FilPath;		//要遍历的路径
            String m=null;
            ArrayList<String> compList=null;
            File file = new File(path);		//获取其file对象
            File[] fs = file.listFiles();
            //遍历path下的文件和目录，放在File数组中
            for(File f:fs){					//遍历File[]数组
                if(!f.isDirectory())		//若非目录(即文件)，则打印
                    m=f.getAbsolutePath();
                   compList=ParseCmop.readCmopCsv(m);
                list.addAll(compList);
            }

            System.out.println("list size" +list.size());
            return  list.subList(30000000,list.size());
        }

}
