package segmenttree;

public class OldIntervalTreeGenerate {
    public static void main(String[] args) {
            Node A[] = {new Node(8, 9),new Node(8,10),new Node(10, 15),  new Node(25, 30), new Node(5, 8),new Node(15, 23),
                new Node(17, 19)};
        Node A1[] = {new Node(16, 21),new Node(8, 9),  new Node(25, 30), new Node(5, 8),new Node(15, 23),
                new Node(17, 19),new  Node(26,26),new Node(0,3),new Node(6,10),new Node(19,20)};
        int len = A.length;
        IntervalTree T = new IntervalTree();
        T.setRoot(new Node(IntervalTreeConstructor2.SENTINEL));
        for (int i = 0; i < len; i++)
            IntervalTreeConstructor2.IntervalT_Insert(T, A[i]);
        System.out.println("The interval tree is:");
        IntervalTreeConstructor2.IntervalT_InorderWalk(T.getRoot());
        System.out.println("The root of the tree is: " + T.getRoot().getLeftpoint() + "   " + T.getRoot().getRightpoint()+" max "+T.getRoot().getMax()+" color "+ T.getRoot().getColor());
        System.out.println("left child" + T.getRoot().getChildLeft().getLeftpoint() + " " + T.getRoot().getChildLeft().getRightpoint()+" max "+T.getRoot().getChildLeft().getMax()+" color "+ T.getRoot().getChildLeft().getColor());
        System.out.println("right child" + T.getRoot().getChildRight().getChildRight().getChildLeft().getLeftpoint() + " " + T.getRoot().getChildRight().getChildRight().getChildLeft().getRightpoint()+" max "+T.getRoot().getChildRight().getMax());
        System.out.println("/*-------------------------------------------------------------*/");
        System.out.println("/*--------------------Searching Interval Tree------------------*/");
    }
}
