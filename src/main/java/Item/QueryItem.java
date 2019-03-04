package Item;

public class QueryItem {
    String startKey;
    String endKey;

    public QueryItem(String startKey, String endKey) {
        this.startKey = startKey;
        this.endKey = endKey;
    }

    public String getStartKey() {
        return startKey;
    }

    public void setStartKey(String startKey) {
        this.startKey = startKey;
    }

    public String getEndKey() {
        return endKey;
    }

    public void setEndKey(String endKey) {
        this.endKey = endKey;
    }

    @Override
    public String toString() {
        return "QueryItem{" +
                "startKey='" + startKey + '\'' +
                ", endKey='" + endKey + '\'' +
                '}';
    }
}
