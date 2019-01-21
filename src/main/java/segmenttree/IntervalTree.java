package segmenttree;

import java.util.ArrayList;

public class IntervalTree {
  public   Node root;            //根节点
    public  Node nil=new Node(IntervalTreeConstructor.Color.Black,null,null,null,-1,-1,new ArrayList<>(),-1);             //哨兵结点,避免讨论结点的边界情况

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Node getNil() {
        return nil;
    }

    public void setNil(Node nil) {
        this.nil = nil;
    }
}
