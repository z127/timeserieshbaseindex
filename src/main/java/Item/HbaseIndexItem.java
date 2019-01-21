package Item;

import APCA.ApcaSegment;

public class HbaseIndexItem {
    int id;
    int start; //inclusive
    int end; //exclusive
    double min;
    double max;
    double mean;
    double error;
    String itemString;
    int length;
    double tolerance;

    @Override
    public String toString() {
        return "HbaseIndexItem{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", min=" + min +
                ", max=" + max +
                ", mean=" + mean +
                ", error=" + error +
                ", itemString='" + itemString + '\'' +
                ", length=" + length +
                ", tolerance=" + tolerance +
                '}';
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public double getMax() {
        return max;
    }

    public HbaseIndexItem(int id, int start, int end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public HbaseIndexItem(int id, int start, int end, double max, double min, int length, double tolerance) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.max = max;
        this.min = min;
        this.length = length;
        this.tolerance = tolerance;
    }

    public HbaseIndexItem(int start, int end, double max, double min, int length, double tolerance) {
        this.start = start;
        this.end = end;
        this.max = max;
        this.min = min;
        this.length = length;
        this.tolerance = tolerance;
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

    public String getItemString() {
        return itemString;
    }

    public void setItemString(String itemString) {
        this.itemString = itemString;
    }

    public HbaseIndexItem() {

    }

    public HbaseIndexItem(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public HbaseIndexItem(int start, int end,double max,double min) {
        this.start = start;
        this.end = end;
        this.max=max;
        this.min=min;
    }







    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }







}
