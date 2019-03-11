package segmenttree.optimizedalgorithm;

import Item.HbaseIndexItem;
import segmenttree.Node;
import segmenttree.NumberException;

import java.util.*;

public class IntervalTreeConstructorNew {
    public static final double SENTINEL=Double.MAX_VALUE;
    public  static   int  computecount=0;
    public  static  int bianliCount=0;
    public static  int TreeNodeCount=0;
    public enum Color {
       Red,Black;
    }
    //求三个参数中的最大值
    public  static double GetMax(double high,double leftMax,double rightMax)
    {
        double temp=(leftMax>rightMax)?leftMax:rightMax;
        return (high>temp)?high:temp;
    }

    //求三个参数中的最大值
    public  static double GetMin(double min,double leftMin,double rightMin)
    {
        double temp=(leftMin<rightMin)?leftMin:rightMin;
        return ( min<temp)? min:temp;
    }


    public static NewNode[] transformToNode(List<HbaseIndexItem> list) {
         NewNode[] arrNode=new NewNode[list.size()];
         HashSet<Double> mySet=new HashSet<Double>();
        for(int i=0;i<list.size();i++)
        {
            HbaseIndexItem item=list.get(i);
            arrNode[i]=new NewNode(item.getMin());
            //System.out.println(arrNode[i].getHbaseIndexitem().toString());
            mySet.add(item.getMin());
        }


        return  arrNode;
    }

    public static HbaseIndexItem[] transformToArray(List<HbaseIndexItem> list) {
        HbaseIndexItem[] arrNode=new HbaseIndexItem[list.size()];
        list.toArray(arrNode);
        return  arrNode;
    }





    public static void  RecursiveIntervalSearch(NewNode T, double left, double right, ArrayList<HbaseIndexItem> listOverlap) throws NumberException {
        if(left>right)
        {
            throw new NumberException(left,right);
        }

        if(T.getLeftpoint()!=SENTINEL )
        {
            ArrayList<HbaseIndexItem> list=overlapLeftAndRight(T,left,right);
            if(list!=null)
            {
                listOverlap.addAll(list);
            }
        }
        if(T.getChildLeft().getLeftpoint()!=SENTINEL&& T.getChildLeft().getMax()>=left)
        {
            RecursiveIntervalSearch(T.getChildLeft(),left,right,listOverlap);
        }
        if(T.getChildRight().getLeftpoint()!=SENTINEL && T.getChildRight().getMax()>=left)
        {
            RecursiveIntervalSearch(T.getChildRight(),left,right,listOverlap);
        }

    }

    public static void  RecursiveIntervalMaxSearch(NewNode T, double max, ArrayList<HbaseIndexItem> listOverlap) throws NumberException {


        if(T.getLeftpoint()!=SENTINEL )
        {
            ArrayList<HbaseIndexItem> list=overlapMax(T,max);
            if(list!=null)
            {
                listOverlap.addAll(list);
            }
        }
        if(T.getChildLeft().getLeftpoint()!=SENTINEL&& T.getChildLeft().getMax()>max)
        {
            RecursiveIntervalMaxSearch(T.getChildLeft(),max,listOverlap);
        }
        if(T.getChildRight().getLeftpoint()!=SENTINEL&&  T.getChildRight().getMax()>max )
        {
            RecursiveIntervalMaxSearch(T.getChildRight(),max,listOverlap);
        }

    }

    public static void  RecursiveIntervalMinSearch(NewNode T, double min, ArrayList<HbaseIndexItem> listOverlap) throws NumberException {


        if(T.getLeftpoint()!=SENTINEL )
        {
            if(T.getLeftpoint()==15.0)
            {
                System.out.println("等于 15");
            }
            ArrayList<HbaseIndexItem> list=overlapMin(T,min);
            if(list!=null)
            {
                listOverlap.addAll(list);
            }
        }
        if(T.getChildLeft().getLeftpoint()!=SENTINEL&& T.getChildLeft().getMin()<min)
        {
            RecursiveIntervalMinSearch(T.getChildLeft(),min,listOverlap);
        }
        if(T.getChildRight().getLeftpoint()!=SENTINEL&&  T.getChildRight().getMin()<min )
        {
            RecursiveIntervalMinSearch(T.getChildRight(),min,listOverlap);
        }

    }

    private static ArrayList<HbaseIndexItem> overlapLeftAndRight(NewNode t, double left, double right ) {
        if(t.leftpoint>right)
        {
            computecount++;
            return null;
        }
        ArrayList<HbaseIndexItem> listItem=new ArrayList<HbaseIndexItem>();
        TreeMap<Double, ArrayList<HbaseIndexItem>> treeMap=t.getRightpoint();
        Integer integ = null;
        //
        Double rightKeyPoint=0.0;
        boolean isfalse=true;
        Iterator iter = treeMap.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            // 获取key
            rightKeyPoint = (double)entry.getKey();
            //没有数据
            //当前左端点小于查询的端点
            if(isfalse && rightKeyPoint < left )
            {
                computecount++;
                continue;
            }else {
                isfalse=false;
                listItem.addAll((ArrayList<HbaseIndexItem>)treeMap.get(rightKeyPoint));
            }
            // 获取value
        }

        if(listItem.size()>0)
        {
            return  listItem;
        }else {
            return null;
        }


    }


    private static ArrayList<HbaseIndexItem> overlapMax(NewNode t, double maxpoint) {
        ArrayList<HbaseIndexItem> listItem=new ArrayList<HbaseIndexItem>();
        TreeMap<Double, ArrayList<HbaseIndexItem>> treeMap=t.getRightpoint();
        Integer integ = null;
        Double key=0.0;
        Iterator iter = treeMap.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            // 获取key
            key = (double)entry.getKey();
            //当前左端点小于查询的端点，
            if(key < maxpoint)
            {
                continue;
            }else {
                listItem.addAll((ArrayList<HbaseIndexItem>)treeMap.get(key));
            }
            // 获取value
        }

        if(listItem.size()>0)
        {
            return  listItem;
        }else {
            return null;
        }

    }


    private static ArrayList<HbaseIndexItem> overlapMin(NewNode t, double minpoint) {
        ArrayList<HbaseIndexItem> listItem=new ArrayList<HbaseIndexItem>();
        TreeMap<Double, ArrayList<HbaseIndexItem>> treeMap=t.getRightpoint();
        Integer integ = null;
        Double key=0.0;
        Iterator iter = treeMap.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            // 获取key
            key = (double)entry.getKey();
            //当前左端点小于查询的端点，
            if(t.leftpoint > minpoint)
            {
                continue;
            }else {
                listItem.addAll((ArrayList<HbaseIndexItem>)treeMap.get(key));
            }
            // 获取value
        }

        if(listItem.size()>0)
        {
            return  listItem;
        }else {
            return null;
        }

    }

    public  static  void IntervalT_InsertFixup(NewIntervalTree T, NewNode newNode) {
        while (newNode.parent.color == IntervalTreeConstructorNew.Color.Red) {
            if (newNode.parent == newNode.parent.parent.childLeft) {
                NewNode y = newNode.parent.parent.childRight;
                if (y.color == IntervalTreeConstructorNew.Color.Red) {
                    newNode.parent.color =  IntervalTreeConstructorNew.Color.Black;            //case 1
                    y.color =  IntervalTreeConstructorNew.Color.Black;                    //case 1
                    newNode.parent.parent.color =  IntervalTreeConstructorNew.Color.Red;      //case 1
                    newNode = newNode.parent.parent;               //case 1
                } else {
                    if (newNode == newNode.parent.childRight) {
                        newNode = newNode.parent;                    //case 2
                        Left_Rotate(T, newNode);               //case 2
                    }
                    newNode.parent.color = IntervalTreeConstructorNew.Color.Black;             //case 3
                    newNode.parent.parent.color =  IntervalTreeConstructorNew.Color.Red;       //case 3
                    Right_Rotate(T, newNode.parent.parent);  //case 3
                }
            } else {//a me as then clause with "right" and "left" exchanged
                NewNode y = newNode.parent.parent.childLeft;
                if (y.color == IntervalTreeConstructorNew.Color.Red) {
                    newNode.parent.color = IntervalTreeConstructorNew.Color.Black;
                    y.color = IntervalTreeConstructorNew.Color.Black;
                    newNode.parent.parent.color = IntervalTreeConstructorNew.Color.Red;
                    newNode = newNode.parent.parent;
                } else {
                    if (newNode == newNode.parent.childLeft) {
                        newNode = newNode.parent;
                        Right_Rotate(T, newNode);
                    }
                    newNode.parent.color = IntervalTreeConstructorNew.Color.Black;
                    newNode.parent.parent.color = IntervalTreeConstructorNew.Color.Red;
                    Left_Rotate(T, newNode.parent.parent);
                }
            }
        }
        T.root.color = IntervalTreeConstructorNew.Color.Black;      //turn the root to BLACK
    }


    //插入结点
    public  static void RBInsert(NewIntervalTree rbTree, NewNode newNode, HbaseIndexItem item )
    {
        NewNode z = new NewNode();
        z.leftpoint = newNode.leftpoint;
        z.color = IntervalTreeConstructorNew.Color.Red;
        z.max=item.getMax();
        z.parent = new NewNode(SENTINEL);
        z.childLeft = new NewNode(SENTINEL);
        z.childRight = new NewNode(SENTINEL);
        NewNode y = new NewNode(SENTINEL);        //y is the parent of x
        NewNode x = rbTree.root;
        //判断左端点是否为空，为空代表当前树是空结点
        while (x.leftpoint != SENTINEL) {
            x.max = Math.max(x.max, z.max);     //Maintaining the max value of each node from z up to root
            y = x;
            if (z.leftpoint < x.leftpoint)
                x = x.childLeft;
            else if(z.leftpoint==x.leftpoint)
            {
                x.addItem(item);
                return;
            }else
            {
                x = x.childRight;
            }
        }
        z.parent = y;   //link new node's parent node to y(y's child is NIL)
        if (y.leftpoint == SENTINEL)
            rbTree.root = z;
        else if (z.leftpoint < y.leftpoint)
            y.childLeft = z;
        else
            y.childRight = z;
        z.addItem(item);
        IntervalT_InsertFixup(rbTree, z);
    }





    public  static  void Right_Rotate(NewIntervalTree T, NewNode x) {
        NewNode y = x.childLeft;      //set y
        x.childLeft = y.childRight;   //link x's left tree into y's right subtree;
        if (y.childRight.getLeftpoint() != SENTINEL)
            y.childRight.parent = x;
        y.parent = x.parent;    //link x's parent to y
        if (x.parent.getLeftpoint() == SENTINEL)
            T.root = y;
        else if (x == x.parent.childLeft)
            x.parent.childLeft = y;
        else
            x.parent.childRight = y;

        y.childRight = x;         //put x on y's right
        x.parent = y;
        //Maintaining additional information
        y.max = x.max;
        y.min=x.min;
        x.max = GetMax(x.max, x.childLeft.max, x.childRight.max);
        x.min = GetMin(x.min, x.childLeft.min, x.childRight.min);
    }

    public  static  void Left_Rotate(NewIntervalTree T, NewNode x) {
        NewNode y = x.childRight;    //set y
        x.childRight = y.childLeft;       //turn y's left subtree into x's right subtree
        if (y.childLeft.getLeftpoint() != SENTINEL)
            y.childLeft.parent = x;

        y.parent = x.parent;     //link x's parent to y;
        if (x.parent.leftpoint == SENTINEL)
            T.root = y;
        else if (x == x.parent.childLeft)
            x.parent.childLeft = y;
        else
            x.parent.childRight = y;

        y.childLeft = x;               //put x on y's left
        x.parent = y;

        //maitaining additional information
        y.max = x.max;
        y.min=x.min;
        x.max = GetMax(x.getMax(), x.childLeft.max, x.childRight.max);
        x.min = GetMin(x.getMin(), x.childLeft.min, x.childRight.min);
    }

   public static void IntervalT_InorderWalk(NewNode x)
    {
        if(x.leftpoint!=SENTINEL)
        {
            TreeNodeCount++;
            IntervalT_InorderWalk(x.childLeft);
           // System.out.print("[   "+x.leftpoint+" "+x.rightPointToString()+"   ]"  );
          /*  for(Map.Entry<Double, ArrayList<HbaseIndexItem>> entry: x.rightpoint.entrySet())
            {
               bianliCount+=entry.getValue().size();
            }*/
            /*if(x.color== Color.Red)
                System.out.println("     Red       "+ x.max);
            else
                System.out.println("    Black      "+ x.max);*/
            IntervalT_InorderWalk(x.childRight);
        }
    }





}
