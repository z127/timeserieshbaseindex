package segmenttree.optimizedalgorithm;

import segmenttree.IntervalTreeConstructor;
import segmenttree.Node;

import java.util.ArrayList;

public class NewIntervalTree {
  public   NewNode root;            //根节点
    public  NewNode nil=new NewNode(IntervalTreeConstructorNew.Color.Black,Double.MIN_VALUE);             //哨兵结点,避免讨论结点的边界情况

    public NewNode getRoot() {
        return root;
    }

    public void setRoot(NewNode root) {
        this.root = root;
    }

    public NewNode getNil() {
        return nil;
    }

    public void setNil(NewNode nil) {
        this.nil = nil;
    }
}
