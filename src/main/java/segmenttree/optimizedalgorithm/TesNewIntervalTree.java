package segmenttree.optimizedalgorithm;

import Item.HbaseIndexItem;
import PCAM.Pcam;
import PCAM.ReadCSV;
import benchmark.ErrorRate;
import segmenttree.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TesNewIntervalTree {

    public static void main(String[] args) throws NumberException {
       // List<String> rawCompList= ParseDirFile("D:\\DataWater");
       // ArrayList<String> rawCompList= ParseCmop.readCmopCsv("D:\\DataWater\\saturn01.0.F.CT_2008_12_PD0.csv");
        ArrayList<String> arrTemperature=ReadCSV.readCsv("D:\\DataProject\\DataGenerate\\stock\\600wStock.csv");
        double[] v1=new double[arrTemperature.size()];
        for(int i=0;i<arrTemperature.size();i++)
        {
            v1[i]=Double.parseDouble(arrTemperature.get(i));
        }
        //计算均值，标准差，最小值，最大值
        double[] statistics= ReadCSV.computeStatistics(v1);
        //划分范围，最大值，最小值
        ArrayList<HbaseIndexItem> listTem=new Pcam().computeHbaseSegmentAccordingSegment(v1,statistics[0],statistics[1],20,4);
       /*for(HbaseIndexItem item:listTem)
        {
            System.out.println(item.toString());
        }*/

        System.out.println(" mean "+statistics[0]);
        System.out.println(" standard Deviation "+statistics[1]);
        System.out.println(" HbaseItem Size "+ listTem.size());
        System.out.println(" raw data size "+v1.length);
        NewNode[] A= IntervalTreeConstructorNew.transformToNode(listTem);
        HbaseIndexItem[] A1=IntervalTreeConstructorNew.transformToArray(listTem);
      /*  NewNode A[] = {new NewNode(16),new NewNode(16),new NewNode(16),new NewNode(16),new NewNode(8), new NewNode(25), new NewNode(5),new NewNode(15),
                new NewNode(17), new NewNode(26), new NewNode(0), new NewNode(6), new NewNode(19)};

        HbaseIndexItem A1[] = {new  HbaseIndexItem(16, 21),new HbaseIndexItem(16,18), new HbaseIndexItem(16,19),new HbaseIndexItem(16,19),new  HbaseIndexItem(8, 9), new HbaseIndexItem(25,30), new  HbaseIndexItem(5, 8), new  HbaseIndexItem(15, 23),new  HbaseIndexItem(17, 19),
                new  HbaseIndexItem(26,26), new  HbaseIndexItem(0, 3), new  HbaseIndexItem(6, 10), new  HbaseIndexItem(19, 20)};
        */
        int len = A.length;
        NewIntervalTree T = new NewIntervalTree();
        T.setRoot(new NewNode(IntervalTreeConstructorNew.SENTINEL));

        double leftpoint = 400;
        double rightpoint = 500;
        int mingzhongcount=0;
        for (int i = 0; i < len; i++) {
            if(overlap(A1[i],leftpoint,rightpoint))
            {
               // System.out.println("overlap"+A1[i].getMin()+" "+A1[i].getMax());
                mingzhongcount++;
            }
            IntervalTreeConstructorNew.RBInsert(T, A[i], A1[i]);
        }
        System.out.println("The interval tree is:");
        IntervalTreeConstructorNew.IntervalT_InorderWalk(T.getRoot());
        //System.out.println("遍历出来的count"+IntervalTreeConstructorNew.bianliCount);
      //  System.out.println("The root of the tree is: " + T.getRoot().getLeftpoint() + "   " + T.getRoot().getRightpoint());
     //   System.out.println("left child" + T.getRoot().getChildLeft().getLeftpoint() + " " + T.getRoot().getChildLeft().getRightpoint()+" max "+T.getRoot().getChildLeft().getMax());
     //   System.out.println("left child" + T.getRoot().getChildRight().getLeftpoint() + " " + T.getRoot().getChildRight().getRightpoint()+" max "+T.getRoot().getChildRight().getMax());
     //   System.out.println("left child" + T.getRoot().getChildRight().getChildLeft().getLeftpoint() + " " + T.getRoot().getChildRight().getChildLeft().getRightpoint()+"  max "+T.getRoot().getChildRight().getChildLeft().getMax());
     //   System.out.println("/*-------------------------------------------------------------*/");
     //   System.out.println("/*--------------------Searching Interval Tree------------------*/");
       // 34.995, min=34.687
        ArrayList<HbaseIndexItem> nodeList=new ArrayList<HbaseIndexItem>();
        NewNode rootNode=T.getRoot();
        double starttime=System.currentTimeMillis();
        System.out.println("start time"+System.currentTimeMillis());
      IntervalTreeConstructorNew.RecursiveIntervalSearch(rootNode, leftpoint, rightpoint,nodeList);
        System.out.println("end time"+System.currentTimeMillis());
        double endtime=System.currentTimeMillis();
        System.out.println("耗时："+(endtime-starttime));
        System.out.println("总共有区间　"+A.length+" 查询范围 : "+leftpoint+" "+rightpoint+"　一共有 "+IntervalTreeConstructorNew.TreeNodeCount+" 个节点 "+",比较了　"+IntervalTreeConstructorNew.computecount+"次。 "+"命中count: "+mingzhongcount+"　遍历出来的count: "+nodeList.size());
         if (nodeList.size() > 0) {
            System.out.println("查询出区间有 " +nodeList.size()+" 个");
            int length=0;
              for (int i = 0; i <nodeList.size(); i++) {
                  length+=nodeList.get(i).getLength();
              }
              System.out.println("length"+length);
             int  totalcount=32857494;
              System.out.println("数据冗余率 "+ length);
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


         public static boolean overlap(HbaseIndexItem item,double leftpoint ,double rightpoint)
        {
            if(item.getMax() < leftpoint || item.getMin() > rightpoint)
            {
                return false;
            }
            return  true;
        }
}
