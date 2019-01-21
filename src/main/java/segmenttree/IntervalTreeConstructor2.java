package segmenttree;

import Item.HbaseIndexItem;

import java.util.ArrayList;

public class IntervalTreeConstructor2 {
    public static final double SENTINEL = -100000;

    public static Node[] transformToNode(ArrayList<HbaseIndexItem> list) {
        Node[] arrNode=new Node[list.size()];
        for(int i=0;i<list.size();i++)
        {
            HbaseIndexItem item=list.get(i);
            arrNode[i]=new Node(item.getMin(),item.getMax(),item);
        }

        return  arrNode;
    }

    public enum Color {
        Red, Black;
    }

    public  static   double Max(double a, double b, double c) {
        if (a > b)
            return a > c ? a : c;
        else
            return b > c ? b : c;
    }

    public  static  void Left_Rotate(IntervalTree T, Node x) {
        Node y = x.childRight;    //set y
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
        x.max = Max(x.rightpoint, x.childLeft.max, x.childRight.max);


    }


    public  static  void Right_Rotate(IntervalTree T, Node x) {
        Node y = x.childLeft;      //set y

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
        x.max = Max(x.rightpoint, x.childLeft.max, x.childRight.max);
    }

    public  static void IntervalT_Insert(IntervalTree T, Node newNode) {
        Node z = new Node();
        z.leftpoint = newNode.leftpoint;
        z.rightpoint=newNode.rightpoint;
        z.max = newNode.rightpoint;
        z.color = IntervalTreeConstructor.Color.Red;
        z.parent = new Node(SENTINEL);
        z.childLeft = new Node(SENTINEL);
        z.childRight = new Node(SENTINEL);

        Node y = new Node(SENTINEL);        //y is the parent of x
        Node x = T.root;
        while (x.leftpoint != SENTINEL) {
            x.max = Math.max(x.max, z.max);     //Maintaining the max value of each node from z up to root
            y = x;
            if (z.leftpoint < x.leftpoint)
                x = x.childLeft;
            else if(z.leftpoint==x.leftpoint && z.rightpoint==x.rightpoint)
            {
                    x.getContent().add(newNode);
                    return;
            }else
                {
                x = x.childRight;
                }
        }
        z.parent = y;   //link new node's parent node to y(y's child is NIL)
        if (y.leftpoint == SENTINEL)
            T.root = z;
        else if (z.leftpoint < y.leftpoint)
            y.childLeft = z;
        else
            y.childRight = z;
        IntervalT_InsertFixup(T, z);
    }


    public static void IntervalT_InorderWalk(Node x)
    {
        if(x.leftpoint!=SENTINEL)
        {
            IntervalT_InorderWalk(x.childLeft);
            System.out.print("[   "+x.leftpoint+"   "+x.rightpoint+"   ]");
            if(x.color== IntervalTreeConstructor.Color.Red)
                System.out.println("     Red       "+ x.max);
            else
                System.out.println("    Black      "+ x.max);
            IntervalT_InorderWalk(x.childRight);
        }
    }


    public  static  void IntervalT_InsertFixup(IntervalTree T, Node newNode) {
        while (newNode.parent.color == IntervalTreeConstructor.Color.Red) {
            if (newNode.parent == newNode.parent.parent.childLeft) {
                Node y = newNode.parent.parent.childRight;
                if (y.color == IntervalTreeConstructor.Color.Red) {
                    newNode.parent.color = IntervalTreeConstructor.Color.Black;            //case 1
                    y.color = IntervalTreeConstructor.Color.Black;                    //case 1
                    newNode.parent.parent.color = IntervalTreeConstructor.Color.Red;      //case 1
                    newNode = newNode.parent.parent;               //case 1
                } else {
                    if (newNode == newNode.parent.childRight) {
                        newNode = newNode.parent;                    //case 2
                        Left_Rotate(T, newNode);               //case 2
                    }
                    newNode.parent.color = IntervalTreeConstructor.Color.Black;             //case 3
                    newNode.parent.parent.color = IntervalTreeConstructor.Color.Red;       //case 3
                    Right_Rotate(T, newNode.parent.parent);  //case 3
                }
            } else {//a me as then clause with "right" and "left" exchanged
                Node y = newNode.parent.parent.childLeft;
                if (y.color == IntervalTreeConstructor.Color.Red) {
                    newNode.parent.color = IntervalTreeConstructor.Color.Black;
                    y.color = IntervalTreeConstructor.Color.Black;
                    newNode.parent.parent.color = IntervalTreeConstructor.Color.Red;
                    newNode = newNode.parent.parent;
                } else {
                    if (newNode == newNode.parent.childLeft) {
                        newNode = newNode.parent;
                        Right_Rotate(T, newNode);
                    }
                    newNode.parent.color = IntervalTreeConstructor.Color.Black;
                    newNode.parent.parent.color = IntervalTreeConstructor.Color.Red;
                    Left_Rotate(T, newNode.parent.parent);
                }
            }
        }
        T.root.color = IntervalTreeConstructor.Color.Black;      //turn the root to BLACK
    }

   public static boolean  Overlap(Node x,double left,double right)
    {
        if(x.rightpoint < left || x.leftpoint > right)     // a & b do not overlap
            return false;
        return true;
    }

    /**
     *
     * @param T
     * @param left
     * @param right
     * @param listOverlap
     * @return
     */
   public static ArrayList<Node> intervalSearch(IntervalTree T,double left,double right,ArrayList<Node> listOverlap)
    {
        Node x=T.root;
        while(x.getLeftpoint()!=SENTINEL )
        {
             if(Overlap(x,left,right))
             {
                 listOverlap.add(x);
             }
            if(x.childLeft.getLeftpoint() !=SENTINEL && x.childLeft.getMax()>= left)
                x=x.childLeft;
            else
                x=x.childRight;
        }
        return listOverlap;
    }

    /**
     * 递归判断是否出错
     * @param T
     * @param left
     * @param right
     * @param listOverlap
     * @return
     */
    public static void  RecursiveIntervalSearch(Node T,double left,double right,ArrayList<Node> listOverlap) throws NumberException {
        if(left>right)
        {
            throw new NumberException(left,right);
        }

        if(T.getLeftpoint()!=SENTINEL &&Overlap(T,left,right))
        {
            listOverlap.add(T);
        }
       if(T.getChildLeft().getLeftpoint()!=SENTINEL&& T.getChildLeft().getMax()>=left)
       {
            RecursiveIntervalSearch(T.getChildLeft(),left,right,listOverlap);
       }
        if(T.getChildRight().getLeftpoint()!=SENTINEL&& !(left>T.getChildRight().getMax()) )
        {
            RecursiveIntervalSearch(T.getChildRight(),left,right,listOverlap);
        }

    }

}

