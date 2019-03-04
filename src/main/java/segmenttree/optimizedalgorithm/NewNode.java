package segmenttree.optimizedalgorithm;

import Item.HbaseIndexItem;
import segmenttree.IntervalTreeConstructor;
import segmenttree.Node;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class NewNode {
    IntervalTreeConstructorNew.Color color;           //红黑树结点的颜色类型,
    NewNode parent;   //父节点
    NewNode childLeft;     //左孩子
    NewNode childRight;    //右孩子
    double leftpoint;
    TreeMap<Double,ArrayList<HbaseIndexItem>> rightpoint;//区间
    double max;               //附加信息，记录以该节点为根的子树中所有区间端点的最大值
    double min;

    public NewNode(IntervalTreeConstructorNew.Color black, double i) {
        this.color=black;
        this.leftpoint=i;
    }


    public String rightPointToString()
    {
        Set<Double> m=rightpoint.keySet();
        String start="{";
        String end="}";
        String content="";
        for(Double item:m)
        {
           content+=item+",";
        }
        return  start+content+end;
    }


    public  void addItem(HbaseIndexItem item)
    {
        this.setMax(Math.max(this.getMax(),item.getMax()));
        this.setMin(Math.min(this.getMin(),item.getMin()));
        if(this.rightpoint.get(item.getMax())!=null)
        {
                this.getRightpoint().get(item.getMax()).add(item);
        }else {
            ArrayList<HbaseIndexItem> listItem=new ArrayList<HbaseIndexItem>();
            listItem.add(item);
            this.getRightpoint().put(item.getMax(),listItem);
        }
    }


    public NewNode() {
        this.rightpoint=new TreeMap<Double,ArrayList<HbaseIndexItem>>();
    }




    public IntervalTreeConstructorNew.Color getColor() {
        return color;
    }

    public void setColor(IntervalTreeConstructorNew.Color color) {
        this.color = color;
    }

    public NewNode getParent() {
        return parent;
    }

    public void setParent(NewNode parent) {
        this.parent = parent;
    }

    public NewNode getChildLeft() {
        return childLeft;
    }

    public void setChildLeft(NewNode childLeft) {
        this.childLeft = childLeft;
    }

    public NewNode getChildRight() {
        return childRight;
    }

    public void setChildRight(NewNode childRight) {
        this.childRight = childRight;
    }

    public double getLeftpoint() {
        return leftpoint;
    }

    public void setLeftpoint(double leftpoint) {
        this.leftpoint = leftpoint;
    }

    public TreeMap<Double, ArrayList<HbaseIndexItem>> getRightpoint() {
        return rightpoint;
    }

    public void setRightpoint(TreeMap<Double, ArrayList<HbaseIndexItem>> rightpoint) {
        this.rightpoint = rightpoint;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public NewNode(double leftpoint) {
        this.leftpoint=leftpoint;
        this.rightpoint=new TreeMap<Double,ArrayList<HbaseIndexItem>>();
        this.max=Double.MIN_VALUE;
        this.min=Double.MAX_VALUE;
    }









}
