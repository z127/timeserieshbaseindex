package segmenttree;

import Item.HbaseIndexItem;

import java.util.ArrayList;

public class Node {
    IntervalTreeConstructor.Color color;           //红黑树结点的颜色类型,
    HbaseIndexItem HbaseIndexitem;
     Node parent;   //父节点
    Node childLeft;     //左孩子
    Node childRight;    //右孩子
    double leftpoint;
    double rightpoint;//区间
    ArrayList<Node> content;
    double max;               //附加信息，记录以该节点为根的子树中所有区间端点的最大值

    public Node(IntervalTreeConstructor.Color color, Node parent, Node childLeft, Node childRight, double leftpoint, double rightpoint, ArrayList<Node> content, double max) {
        this.color = color;
        this.parent = parent;
        this.childLeft = childLeft;
        this.childRight = childRight;
        this.leftpoint = leftpoint;
        this.rightpoint = rightpoint;
        this.content = content;
        this.max = max;
        this.content=new ArrayList<Node>();
    }


    public Node(double leftpoint) {
        this.leftpoint = leftpoint;
        this.content=new ArrayList<>();
    }

    public IntervalTreeConstructor.Color getColor() {
        return color;
    }

    public void setColor(IntervalTreeConstructor.Color color) {
        this.color = color;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getChildLeft() {
        return childLeft;
    }

    public void setChildLeft(Node childLeft) {
        this.childLeft = childLeft;
    }

    public Node getChildRight() {
        return childRight;
    }

    public void setChildRight(Node childRight) {
        this.childRight = childRight;
    }

    public double getLeftpoint() {
        return leftpoint;
    }

    public void setLeftpoint(double leftpoint) {
        this.leftpoint = leftpoint;
    }

    public double getRightpoint() {
        return rightpoint;
    }

    public void setRightpoint(double rightpoint) {
        this.rightpoint = rightpoint;
    }

    public ArrayList<Node> getContent() {
        return content;
    }

    public void setContent(ArrayList<Node> content) {
        this.content = content;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public Node() {
        content=new ArrayList<Node>();
    }
    public Node(double leftpoint,double rightpoint,HbaseIndexItem item) {
        this.leftpoint=leftpoint;
        this.rightpoint=rightpoint;
        this.content=new ArrayList<Node>();
        this.HbaseIndexitem=item;
    }

    public Node(double leftpoint, double rightpoint) {
        this.leftpoint = leftpoint;
        this.rightpoint = rightpoint;
    }
}
