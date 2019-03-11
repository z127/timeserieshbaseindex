package cluster;

import Item.HbaseIndexItem;

public class DataPoint {
    String dataPointName; // 样本点名
    Cluster cluster; // 样本点所属类簇
    private double dimension[]; // 样本点的维度
    private HbaseIndexItem item ;

    public DataPoint(){

    }
    public DataPoint(double[] dimension,String dataPointName ){
        this.dataPointName=dataPointName;
        this.dimension=dimension;
    }
    public DataPoint(double[] dimension,HbaseIndexItem item,String dataPointName){
        this.dimension=dimension;
        this.item=item;
        this.dataPointName=dataPointName;
    }



    public HbaseIndexItem getItem() {
        return item;
    }

    public void setItem(HbaseIndexItem item) {
        this.item = item;
    }

    public double[] getDimension() {
        return dimension;
    }

    public void setDimension(double[] dimension) {
        this.dimension = dimension;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public String getDataPointName() {
        return dataPointName;
    }

    public void setDataPointName(String dataPointName) {
        this.dataPointName = dataPointName;
    }
}
