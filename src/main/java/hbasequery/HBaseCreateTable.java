package hbasequery;

public class HBaseCreateTable {
    public static void main(String[] args) throws Exception {
        String[] family={"cf1"};
         //   HBaseUtils.creatTable("cmop1000double",family);
        HBaseUtils.dropTable("cmop1000double");
    }
}
