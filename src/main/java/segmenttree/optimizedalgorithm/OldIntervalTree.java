package segmenttree.optimizedalgorithm;

import Item.HbaseIndexItem;
import segmenttree.NumberException;

import java.util.ArrayList;

public class OldIntervalTree {
    public static void main(String[] args) throws NumberException {
            NewNode A[] = {new NewNode(16),new NewNode(16),new NewNode(16),new NewNode(16),new NewNode(8), new NewNode(25), new NewNode(5),new NewNode(15),
                new NewNode(17), new NewNode(26), new NewNode(0), new NewNode(6), new NewNode(19)};

        HbaseIndexItem A1[] = {new  HbaseIndexItem(16, 21),new HbaseIndexItem(16,18), new HbaseIndexItem(16,19),new HbaseIndexItem(16,19),new  HbaseIndexItem(8, 9), new HbaseIndexItem(25,30), new  HbaseIndexItem(5, 8), new  HbaseIndexItem(15, 23),new  HbaseIndexItem(17, 19),
                new  HbaseIndexItem(26,26), new  HbaseIndexItem(0, 3), new  HbaseIndexItem(6, 10), new  HbaseIndexItem(19, 20)};
        int len = A.length;
        NewIntervalTree T = new NewIntervalTree();
        T.setRoot(new NewNode(IntervalTreeConstructorNew.SENTINEL));
        double leftpoint =  17;
        double rightpoint =  18;
        System.out.println("多少条"+len);
        for (int i = 0; i < len; i++) {
            if(overlap(A1[i],leftpoint,rightpoint))
            {

                System.out.println("overlap"+A1[i].getMin()+" "+A1[i].getMax());
            }
            IntervalTreeConstructorNew.RBInsert(T, A[i], A1[i]);
        }
        NewNode rootNode=T.getRoot();
        ArrayList<HbaseIndexItem> nodeList=new ArrayList<HbaseIndexItem>();
        IntervalTreeConstructorNew.RecursiveIntervalSearch(rootNode, leftpoint, rightpoint,nodeList);
        IntervalTreeConstructorNew.IntervalT_InorderWalk(T.getRoot());
        System.out.println("原来有多少条"+A.length);
        System.out.println("遍历出来的count"+IntervalTreeConstructorNew.bianliCount);
        if (nodeList.size() > 0) {
            System.out.println("查询区间是: ["+leftpoint+","+rightpoint+"] ,查询出区间有 " + nodeList.size() + " 个");
            for (int i = 0; i < nodeList.size(); i++) {
                HbaseIndexItem node = nodeList.get(i);
                System.out.println("The overlap interval is:");
                System.out.println("[ " + node.getMin() + "  "+ node.getMax()+ " ]");
                System.out.println("max " + node.getMax());
            }
            System.out.println("The root of the tree is: " + T.getRoot().getLeftpoint() + "   " + T.getRoot().getRightpoint());
            System.out.println("left child" + T.getRoot().getChildLeft().getLeftpoint() + " " + T.getRoot().getChildLeft().getRightpoint()+" max "+T.getRoot().getChildLeft().getMax());
            System.out.println("left child" + T.getRoot().getChildRight().getLeftpoint() + " " + T.getRoot().getChildRight().getRightpoint()+" max "+T.getRoot().getChildRight().getMax());
            System.out.println("left child" + T.getRoot().getChildRight().getChildLeft().getLeftpoint() + " " + T.getRoot().getChildRight().getChildLeft().getRightpoint()+"  max "+T.getRoot().getChildRight().getChildLeft().getMax());
            System.out.println("/*-------------------------------------------------------------*/");
            System.out.println("/*--------------------Searching Interval Tree------------------*/");
        }


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
