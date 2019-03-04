package hbasequery;

public class ResultAnswer {
    String rowkey;
    String value;
    String columnfamily;
    String comlumn;

    public ResultAnswer(String rowkey, String value, String columnfamily, String comlumn) {
        this.rowkey = rowkey;
        this.value = value;
        this.columnfamily = columnfamily;
        this.comlumn = comlumn;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColumnfamily() {
        return columnfamily;
    }

    public void setColumnfamily(String columnfamily) {
        this.columnfamily = columnfamily;
    }

    public String getComlumn() {
        return comlumn;
    }

    public void setComlumn(String comlumn) {
        this.comlumn = comlumn;
    }

    @Override
    public String toString() {
        return "ResultAnswer{" +
                "rowkey='" + rowkey + '\'' +
                ", value='" + value + '\'' +
                ", columnfamily='" + columnfamily + '\'' +
                ", comlumn='" + comlumn + '\'' +
                '}';
    }
}
