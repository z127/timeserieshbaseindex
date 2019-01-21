package segmenttree;

public class IntervalTreeConstructor {
    public static final double SENTINEL=-100000;
    public enum Color {
       Red,Black;
    }
    //求三个参数中的最大值
    public  static double GetMax(double high,double leftMax,double rightMax)
    {
        double temp=(leftMax>rightMax)?leftMax:rightMax;
        return (high>temp)?high:temp;
    }


    //左旋，结点x原来的右子树y旋转成x的父母
  public  static   void leftRotate(IntervalTree rbTree,Node x)
    {
        if(x.childRight!=rbTree.nil)
        {
            Node y=x.childRight;
            x.childRight=y.childLeft;
            if(y.childLeft!=rbTree.nil)
            {
                y.childLeft.parent=x;
            }
            y.parent=x.parent;
            if(x.parent==rbTree.nil)    //空树，将y设为根
            {
                rbTree.root=y;
            }
            else if(x==x.parent.childLeft)   //x为左子树，将y放在x父节点的左子树
            {
                x.parent.childLeft=y;
            }
            else
            {
                x.parent.childRight=y;
            }
            y.childLeft=x;
            x.parent=y;

            //以下为区间树与红黑树左旋调整的差异，即要调整结点max的大小,
            //且必须先计算x的max，在计算y的max
            x.max=GetMax(x.rightpoint,x.childLeft.rightpoint,x.childRight.rightpoint);
            y.max=GetMax(y.rightpoint,y.childLeft.rightpoint,y.childRight.rightpoint);
        }
        else
        {
            System.out.println("Error: can't left rotate,because no rigth child!");
        }
    }


  public static   void rightRotate(IntervalTree rbTree,Node x)
    {
        if(x.childLeft!=rbTree.nil)
        {
            Node y=x.childLeft;
            x.childLeft=y.childRight;
            if(y.childRight!=rbTree.nil)
            {
                y.childRight.parent=x;
            }

            y.parent=x.parent;
            if(x.parent==rbTree.nil)
            {
                rbTree.root=y;
            }
            else if(x==x.parent.childLeft)
            {
                x.parent.childLeft=y;
            }
            else
            {
                x.parent.childRight=y;
            }
            y.childRight=x;
            x.parent=y;

            //以下为区间树与红黑树左旋调整的差异，即要调整结点max的大小
            //且必须先计算x的max，在计算y的max
            x.rightpoint=GetMax(x.rightpoint,x.childLeft.rightpoint,x.childRight.rightpoint);
            y.rightpoint=GetMax(y.rightpoint,y.childLeft.rightpoint,y.childRight.rightpoint);
        }
        else
        {
            System.out.println("Error: can't right rotate,because no left child!");
        }

    }





    //插入结点
    public  static void RBInsert(IntervalTree rbTree,Node newnode)
    {
        if(rbTree.root==null)
        {//当根为空时，单独处理，直接插入到根结点中
            rbTree.root=new Node();
            rbTree.nil=new Node();
            rbTree.root.childLeft=rbTree.nil;
            rbTree.root.childRight=rbTree.nil;
            rbTree.root.parent=rbTree.nil;
            rbTree.root.leftpoint=newnode.leftpoint;      //设置区间低端点
            rbTree.root.rightpoint=newnode.rightpoint;    //设置区间高端点
            rbTree.root.max=newnode.rightpoint;      //初始根的max设为自己的high
            rbTree.root.color= Color.Black;            //根节点color设为黑

            rbTree.nil.parent=rbTree.root;
            rbTree.nil.childLeft=rbTree.root;
            rbTree.nil.childRight=rbTree.root;
            rbTree.nil.leftpoint=rbTree.nil.rightpoint=SENTINEL;  //将nil的区间设为哨兵
            rbTree.nil.color=Color.Black;     //nil结color也设为黑
            rbTree.root.max=0;          //nil节点的max设为0，便于其他节点max的维护

        }
        else
        {//如果树不为空，那么从根节点开始，从上往下查找插入点
            Node y=rbTree.nil;     //y用于当前扫描结点x的父节点
            Node x=rbTree.root;    //从根节点开始扫描
            while(x!=rbTree.nil)    //查找插入位置,以低端点为排序键值
            {
                if(newnode.leftpoint==x.leftpoint && newnode.rightpoint < x.rightpoint)
                {
                    x.content.add(x);
                    return;
                }
                y=x;
                x=newnode.leftpoint<x.leftpoint ? x.childLeft: x.childRight;
            }
            Node z=new Node();       //new一个Node结点空间
            z.color=Color.Red;           //新插入的color设为红色
            z.leftpoint=newnode.leftpoint;
            z.rightpoint=newnode.rightpoint;
            z.childLeft=z.childRight=rbTree.nil;
            z.max=GetMax(newnode.rightpoint,z.childLeft.max,z.childRight.max);
            z.parent=y;
            if(newnode.leftpoint<y.leftpoint)
                y.childLeft=z;
            else
                y.childRight=z;

            RBInsertFixUp(rbTree,z);   //插入后对树进行调整
        }
    }




    //插入后调整树，以维持红黑树的5条性质
   public static void RBInsertFixUp(IntervalTree rbTree,Node z)
    {
        Node y;      //用于记录z的叔叔结点
        while(z.parent.color==Color.Red)   //因为插入的结点是红色的，所以只可能违背性质4,即假如父结点也是红色的，要做调整
        {
            if(z.parent.parent.childLeft==z.parent)  //如果要插入的结点z是其父结点的左子树
            {
                y=z.parent.parent.childRight;         // y设置为z的叔父结点
                if(y.color==Color.Red)                   //case 1: y的颜色为红色
                {
                    z.parent.parent.color=Color.Red;
                    y.color=Color.Black;
                    z.parent.color=Color.Black;
                    z=z.parent.parent;
                }
                else
                {
                    if(z==z.parent.childRight)    //case 2: y的颜色为黑色，并且z是z的父母的右结点，则z左旋转
                    {
                        z=z.parent;
                        leftRotate(rbTree,z);
                    }
                    z.parent.parent.color=Color.Red;     //case 3: 如果y的颜色为黑色，并且z是z的父母的左结点
                    z.parent.color=Color.Black;
                    rightRotate(rbTree,z.parent.parent);
                }
            }
            else    //与前一种情况对称，要插入的结点z是其父结点的右子树,注释略去
            {
                y=z.parent.parent.childLeft;
                if(y.color==Color.Red)
                {
                    z.parent.parent.color=Color.Red;
                    y.color=Color.Black;
                    z.parent.color=Color.Black;
                    z=z.parent.parent;
                }
                else
                {
                    if(z.parent.childLeft==z)
                    {
                        z=z.parent;
                        rightRotate(rbTree,z);
                    }
                    z.parent.parent.color=Color.Red;
                    z.parent.color=Color.Black;
                    leftRotate(rbTree,z.parent.parent);
                }
            }
        }
        rbTree.root.color=Color.Black;   //最后如果上升为rbTree的根的话，把根的颜色设为黑色
    }




   public static void IntervalT_InorderWalk(Node x)
    {
        if(x.leftpoint!=SENTINEL)
        {
            IntervalT_InorderWalk(x.childLeft);
            System.out.print("[   "+x.leftpoint+"   "+x.rightpoint+"   ]");
            if(x.color==Color.Red)
                System.out.println("     Red       "+ x.max);
            else
                System.out.println("    Black      "+ x.max);
            IntervalT_InorderWalk(x.childRight);
        }
    }



    //查找与给定区间重叠的区间
    Node IntervalSearch(IntervalTree  rbTree,double left,double right)
    {
        Node x=rbTree.root;    //从根开始查找
        while(x!=rbTree.nil&&!(left<=x.rightpoint&&right>=x.leftpoint))
        {//若x不等于nil节点且x与interval不重叠，则进行判断
            if(x.childLeft!=rbTree.nil&&x.childLeft.max>=left)
                x=x.childLeft;       //到x的左子树中继续查找
            else
                x=x.childRight;      //左子树必查不到，到右子树查
        }
        return x;    //x=nil或者x与interval重叠
    }



}
