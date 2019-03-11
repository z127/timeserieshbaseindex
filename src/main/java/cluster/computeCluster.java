package cluster;

import Item.HbaseIndexItem;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class computeCluster {

    public  static  ClusterModel minModel=new ClusterModel();

    public static void main(String[] args) throws IOException {
            List<HbaseIndexItem> list=getArrayListHbaseIndex();
             Compute(list,4);

    }

        public  static  List<HbaseIndexItem> getArrayListHbaseIndex()
            {
                HbaseIndexItem[] list={new HbaseIndexItem(1,1,2),new HbaseIndexItem(2,5,6),
                        new HbaseIndexItem(3,7,8),new HbaseIndexItem(4,9,10),new HbaseIndexItem(5,15,16),new HbaseIndexItem(6,17,18),
                        new HbaseIndexItem(7,19,20),new HbaseIndexItem(12,70,80),new HbaseIndexItem(8,35,37),new HbaseIndexItem(9,25,26),new HbaseIndexItem(10,27,28),new HbaseIndexItem(11,29,30),new HbaseIndexItem(12,38,39)};
                List<HbaseIndexItem> listItem=new ArrayList<HbaseIndexItem>();
                    for(int i=0;i<list.length;i++)
                    {
                     listItem.add(list[i]);
                    }
                    return  listItem;
            }


    public  static  void Compute(List<HbaseIndexItem> list,int size)
    {
        ArrayList<DataPoint> listDataPoint=new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
           HbaseIndexItem itemHBaseIndexItem=list.get(i);
           double left=itemHBaseIndexItem.getStart();
           double right=itemHBaseIndexItem.getEnd();
           double[] dimension={left,right};
            listDataPoint.add(new DataPoint(dimension,itemHBaseIndexItem,"["+left+","+right+"]"));
        }
        List<Cluster> clusters = startCluster(listDataPoint, size);
    // 输出聚类的结果，两个类簇中间使用----隔开
        System.out.println();
        System.out.println("结果输出---：");
        int i=1;
        double min=Integer.MAX_VALUE;
        double max=-Integer.MAX_VALUE;
        double length=0;
        for (Cluster cl : clusters) {
            List<DataPoint> tempDps = cl.getDataPoints();
            //System.out.println("第 "+i+" 类-------");
            for (DataPoint tempdp : tempDps) {
              //  System.out.println(tempdp.getDataPointName());
                min=Math.min(min,tempdp.getDimension()[0]);
                max=Math.max(max,tempdp.getDimension()[1]);
              //  System.out.println("当前轮长度"+(max-min));
            }
            length+=max-min;
         //   System.out.println("当前块总长度 "+(max-min));
            min=Integer.MAX_VALUE;
            max=-Integer.MAX_VALUE;
            i++;
        }
        System.out.println("层次聚类总块数 "+ size+ " 层次聚类命中数据 "+length);
        System.out.println("=========================================================");
    }


    private static List<Cluster> startCluster(ArrayList<DataPoint> dp, double size) {
// 声明cluster类，存放类名和类簇中含有的样本
        List<Cluster> finalClusters;
        // 初始化类簇，开始时认为每一个样本都是一个类簇并将初始化类簇赋值给最终类簇
        List<Cluster> originalClusters = initialCluster(dp);
        finalClusters = originalClusters;
        // flag为判断标志
        boolean flag = true;
        int it = 1;
        while (flag)
        {
            //System.out.println("第" + it + "次迭代");
            // 临时表量，存放类簇间余弦相似度的最大值
            double min = 0x7ffffff;
            // mergeIndexA和mergeIndexB表示每一次迭代聚类最小的两个类簇，也就是每一次迭代要合并的两个类簇
            int mergeIndexA = 0;
            int mergeIndexB = 0;
            for (int i = 0; i < finalClusters.size() - 1; i++) {
                for (int j = i + 1; j < finalClusters.size(); j++) {
                    // 得到任意的两个类簇
                    Cluster clusterA = finalClusters.get(i);
                    Cluster clusterB = finalClusters.get(j);
                    // 得到这两个类簇中的样本
                    List<DataPoint> dataPointsA = clusterA.getDataPoints();
                    List<DataPoint> dataPointsB = clusterB.getDataPoints();
                    /*
					 * 定义临时变量tempDis存储两个类簇的大小，这里采用的计算两个类簇的距离的方法是
					 * 得到两个类簇中所有的样本的距离的和除以两个类簇中的样本数量的积，其中两个样本 之间的距离用的是余弦相似度。
					 * 注意：这个地方的类簇之间的距离可以 换成其他的计算方法
					 */
                    double tempDis = 0x7ffffff;
                    for (int m = 0; m < dataPointsA.size(); m++) {
                        for (int n = 0; n < dataPointsB.size(); n++) {
                            //找出两个聚类最短的距离
                            tempDis = Math.min( getDistance(dataPointsA.get(m).getDimension(), dataPointsB.get(n).getDimension()),tempDis);
                        }
                    }
                    //tempDis = tempDis / (dataPointsA.size() * dataPointsB.size());
                    if (tempDis <= min) {
                        min = tempDis;
                        mergeIndexA = i;
                        mergeIndexB = j;
                    }
                }
                }
            if (finalClusters.size()<=size) {
                flag = false;
            } else {
                finalClusters = mergeCluster(finalClusters, mergeIndexA, mergeIndexB);
            }

        }
        return  finalClusters;
    }

    /**
     * 合并聚类
     * @param finalClusters
     * @param mergeIndexA
     * @param mergeIndexB
     * @return
     */
    private static List<Cluster> mergeCluster(List<Cluster> finalClusters, int mergeIndexA, int mergeIndexB) {
        if (mergeIndexA != mergeIndexB) {
            // 将cluster[mergeIndexB]中的DataPoint加入到 cluster[mergeIndexA]
            Cluster clusterA = finalClusters.get(mergeIndexA);
            Cluster clusterB = finalClusters.get(mergeIndexB);

            List<DataPoint> dpA = clusterA.getDataPoints();
            List<DataPoint> dpB = clusterB.getDataPoints();

            for (DataPoint dp : dpB) {
                DataPoint tempDp = new DataPoint();
                tempDp.setDataPointName(dp.getDataPointName());
                tempDp.setDimension(dp.getDimension());
                tempDp.setItem(dp.getItem());
                tempDp.setCluster(clusterA);
                dpA.add(tempDp);
            }
            clusterA.setDataPoints(dpA);
            finalClusters.remove(mergeIndexB);
        }
        return finalClusters;
    }

    // 初始化类簇
    private  static List<Cluster>  initialCluster(ArrayList<DataPoint> dpoints) {
        // 声明存放初始化类簇的链表
        List<Cluster> originalClusters = new ArrayList<Cluster>();

        for (int i = 0; i < dpoints.size(); i++) {
            // 得到每一个样本点
            DataPoint tempDataPoint = dpoints.get(i);
            // 声明一个临时的用于存放样本点的链表
            List<DataPoint> tempDataPoints = new ArrayList<DataPoint>();
            // 链表中加入刚才得到的样本点
            tempDataPoints.add(tempDataPoint);
            // 声明一个类簇，并且将给类簇设定名字、增加样本点
            Cluster tempCluster = new Cluster();
            tempCluster.setClusterName("Cluster " + String.valueOf(i));
            tempCluster.setDataPoints(tempDataPoints);
            // 将样本点的类簇设置为tempCluster
            tempDataPoint.setCluster(tempCluster);
            // 将新的类簇加入到初始化类簇链表中
            originalClusters.add(tempCluster);
        }
      /*  for (int i = 0; i < dpoints.size(); i++) {
           System.out.println(dpoints.get(i).getCluster());
        }*/
        return originalClusters;
    }


    private static  double getDistance(double[] one, double[] two) {// 计算两点间的欧氏距离
        double val = 0;
        if(one[0]<two[0])
        {
            return  Math.abs(one[1]-two[0]);
        }else {

            return  Math.abs(one[0]-two[1]);
        }

    }







}
