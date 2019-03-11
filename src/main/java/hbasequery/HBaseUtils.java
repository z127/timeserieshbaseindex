package hbasequery;


import Item.QueryItem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class HBaseUtils {
    HBaseAdmin admin=null;
    Configuration configuration =null;
    private HBaseUtils(){
        configuration=new Configuration();
        configuration.set("hbase.zookeeper.quorum","s101:2181,s102:2181,s103:2181");
        configuration.set("hbase.rootdir","hdfs://s101:8020/hbase");
        try {
            admin = new HBaseAdmin(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static volatile HBaseUtils instance = null;
    public static synchronized HBaseUtils getInstance(){
        if(null == instance){
            instance = new HBaseUtils();
        }
        return instance;
    }


    /**
     * 根据表名获取到 Htable 实例
     */
    public HTable getHTable(String tableName){
        HTable table = null;
        try {
            table = new HTable(configuration,tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }


    /**
     * 添加一条记录到 Hbase 表 70 30 128 32 核 200T 8000
     * @param tableName Hbase 表名
     * @param rowkey Hbase 表的 rowkey * @param cf Hbase 表的 columnfamily * @param column Hbase 表的列
     * @param value 写入 Hbase 表的值
     */
    public void put(String tableName,String rowkey,String cf,String column,String value){
        HTable table = getHTable(tableName);
        Put put = new Put(Bytes.toBytes(rowkey));
        put.add(Bytes.toBytes(cf), Bytes.toBytes(column), Bytes.toBytes(value));
        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    public static void querytable(String tablename) throws IOException {
        Configuration conf= HBaseConfiguration.create();
        Connection con=null;
        //get the hbase table instance



        HTable table = new HTable(conf,tablename);
        try {
            con = ConnectionFactory.createConnection(conf);
            List<HRegionInfo>   list=MetaScanner.listAllRegions(conf,con,false);
            System.out.println("list"+ list);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            Table querytable = con.getTable(hbasetablename);
            querytable.close();
            System.out.println("tableName : "+tablename);
            System.out.println("Data : ");

            Scan scan = new Scan();
            ResultScanner rsscan = table.getScanner(scan);
            for(Result rs : rsscan){
                System.out.println(Bytes.toString(rs.getRow()));
                for(Cell cell : rs.rawCells()){
                    System.out.println(
                            " family: "+Bytes.toString( CellUtil.cloneFamily(cell))
                                    +" -> "+
                                " column : "  +Bytes.toString(CellUtil.cloneQualifier(cell))
                                    +" -> "+
                                 " value : "  + Bytes.toString(CellUtil.cloneValue(cell))
                                    +" -> "+
                                  " timestamp : " + cell.getTimestamp()
                    );
                  String regioninfo=  Bytes.toString( CellUtil.cloneFamily(cell))+":"+Bytes.toString(CellUtil.cloneQualifier(cell));
                    if("info:regioninfo".equals(regioninfo)) {
                        HRegionInfo hRegionInfo = HRegionInfo.parseFromOrNull(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                        System.out.println("startKey " + Bytes.toString(hRegionInfo.getStartKey()) + " endKey " + Bytes.toString(hRegionInfo.getEndKey())+"RegionInfo "+Bytes.toString(hRegionInfo.getRegionName()));
                    }
                    }

                System.out.println("------------------------------");
            }
            rsscan.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            con.close();
        }
    }


    public static void get(String tablename) throws IOException {
        Configuration conf= HBaseConfiguration.create();
        Connection con=null;
        try {
            con = ConnectionFactory.createConnection(conf);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            Table querytable = con.getTable(hbasetablename);
            ResultScanner rs = null;
            Scan scan = new Scan();
            rs = querytable.getScanner(scan);
            querytable.close();
            System.out.println("tableName : "+tablename);
            System.out.println("Data : ");

            for (Result r : rs) {
                Cell[]    cellArr=r.rawCells();
                for(Cell cell:cellArr)
                {
                    //rowArray
                   byte[]  rowArr=cell.getRowArray();
                   //获取列族
                    String family = new String(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
                    System.out.println(cell.getTimestamp()+"  "+new String(cell.getValue(),"utf-8"));  //都转换为utf-8，中文会乱码
                    //获取rowkey
                    String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                    //获取列名
                    String column= Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
                    String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    System.out.println("row :"+row+" family :"+family+" column :"+column+" value : "+value);
                   // System.out.println(new String(rowArr));
                }
            }
            rs.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            con.close();
        }
    }


    public static void queryDataUsingRowKey(String tablename,String startrowkey,String endrowkey) throws IOException {
        Configuration conf= HBaseConfiguration.create();
        Connection con=null;
        try {
            con = ConnectionFactory.createConnection(conf);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            Table querytable = con.getTable(hbasetablename);
            ResultScanner rs = null;
            Scan scan = new Scan();
            scan.setStartRow(Bytes.toBytes(startrowkey));
            scan.setStopRow(Bytes.toBytes(endrowkey));
            Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator(".*"));
            scan.setFilter(filter);
            rs = querytable.getScanner(scan);
            querytable.close();
            System.out.println("tableName : "+tablename);
            System.out.println("Data : ");
           // double max=0;
         //   double min=Double.MAX_VALUE;
            for (Result r : rs) {
                Cell[]    cellArr=r.rawCells();
                for(Cell cell:cellArr)
                {
                    //rowArray
                    byte[]  rowArr=cell.getRowArray();
                    //获取列族
                    String family = new String(cell.getFamilyArray(),cell.getFamilyOffset(),cell.getFamilyLength());
                   // System.out.println(cell.getTimestamp()+"  "+new String(cell.getValue(),"utf-8"));  //都转换为utf-8，中文会乱码
                    //获取rowkey
                    String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                    //获取列名
                    String column= Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
                    String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    System.out.println("row :"+row+" family :"+family+" column :"+column+" value : "+value);
                   // return;
                  /*   max=Double.parseDouble(value)>max?Double.parseDouble(value):max;
                    min=Double.parseDouble(value)<min?Double.parseDouble(value):min;
                    System.out.println("max"+max);
                    System.out.println("min"+min);*/
                    // System.out.println(new String(rowArr));
                }
            }
            rs.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            con.close();
        }
    }


    public static void queryDataArrayListUsingRowKey(String tablename, ArrayList<QueryItem> queryItems) throws IOException {
        Configuration conf= HBaseConfiguration.create();
        Connection con=null;
        try {
            con = ConnectionFactory.createConnection(conf);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            Table querytable = con.getTable(hbasetablename);
            ResultScanner rs = null;
            Scan scan = new Scan();
            for(int i=0;i<queryItems.size();i++) {
                String startrowkey=queryItems.get(i).getStartKey();
                String endrowkey=queryItems.get(i).getEndKey();
                scan.setStartRow(Bytes.toBytes(startrowkey));
                scan.setStopRow(Bytes.toBytes(endrowkey));
               // Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(".*"));
              //  scan.setFilter(filter);
                rs = querytable.getScanner(scan);
                System.out.println("tableName : " + tablename);
                System.out.println("Data : ");
                // double max=0;
                //   double min=Double.MAX_VALUE;
                System.out.println(" i "+i);
               System.out.println("startKey"+ Bytes.toString(scan.getStartRow()));
              System.out.println("endKey"+ Bytes.toString(scan.getStartRow()));
                for (Result r : rs) {
                    Cell[] cellArr = r.rawCells();
                    for (Cell cell : cellArr) {
                        //rowArray
                        byte[] rowArr = cell.getRowArray();
                        //获取列族
                        String family = new String(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
                        // System.out.println(cell.getTimestamp()+"  "+new String(cell.getValue(),"utf-8"));  //都转换为utf-8，中文会乱码
                        //获取rowkey
                        String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                        //获取列名
                        String column = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                        String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                       System.out.println("count "+i +" row :" + row + " family :" + family + " column :" + column + " value : " + value);
                        // return;
                  /*
                   max=Double.parseDouble(value)>max?Double.parseDouble(value):max;
                    min=Double.parseDouble(value)<min?Double.parseDouble(value):min;
                    System.out.println("max"+max);
                    System.out.println("min"+min);*/
                        // System.out.println(new String(rowArr));
                    }
                }
            }
            rs.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            con.close();
        }
    }


    public static void queryDataArrayListUsing(String tablename, ArrayList<QueryItem> queryItems) throws IOException {
        Configuration conf= HBaseConfiguration.create();
        Connection con=null;
        try {
            con = ConnectionFactory.createConnection(conf);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            Table querytable = con.getTable(hbasetablename);
            ResultScanner rs = null;
            Scan scan = new Scan();
            for(int i=0;i<queryItems.size();i++) {
                String startrowkey=queryItems.get(i).getStartKey();
                String endrowkey=queryItems.get(i).getEndKey();
                scan.setStartRow(Bytes.toBytes(startrowkey));
                scan.setStopRow(Bytes.toBytes(endrowkey));
                // Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(".*"));
                //  scan.setFilter(filter);
                rs = querytable.getScanner(scan);
                System.out.println("tableName : " + tablename);
                System.out.println("Data : ");
                // double max=0;
                //   double min=Double.MAX_VALUE;
                System.out.println(" i "+i);
                System.out.println("startKey"+ Bytes.toString(scan.getStartRow()));
                System.out.println("endKey"+ Bytes.toString(scan.getStartRow()));
                for (Result r : rs) {
                    Cell[] cellArr = r.rawCells();
                    for (Cell cell : cellArr) {
                        //rowArray
                        byte[] rowArr = cell.getRowArray();
                        //获取列族
                        String family = new String(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
                        // System.out.println(cell.getTimestamp()+"  "+new String(cell.getValue(),"utf-8"));  //都转换为utf-8，中文会乱码
                        //获取rowkey
                        String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                        //获取列名
                        String column = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                        String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                        System.out.println("count "+i +" row :" + row + " family :" + family + " column :" + column + " value : " + value);
                        // return;
                  /*
                   max=Double.parseDouble(value)>max?Double.parseDouble(value):max;
                    min=Double.parseDouble(value)<min?Double.parseDouble(value):min;
                    System.out.println("max"+max);
                    System.out.println("min"+min);*/
                        // System.out.println(new String(rowArr));
                    }
                }
            }
            rs.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            con.close();
        }
    }

    public static void queryDataArrayListUsingRowKeyBatch(String tablename, ArrayList<QueryItem> queryItems) throws IOException {
        Configuration conf= HBaseConfiguration.create();
        List<Scan> batch = new ArrayList<Scan>();
        Connection con=null;
        try {
            con = ConnectionFactory.createConnection(conf);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            Table querytable = con.getTable(hbasetablename);
            ResultScanner rs = null;
            Scan scan = new Scan();
            for(int i=0;i<queryItems.size();i++) {
                scan.setStartRow(Bytes.toBytes(queryItems.get(i).getStartKey()));
                scan.setStopRow(Bytes.toBytes(queryItems.get(i).getEndKey()));
                Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(".*"));
                scan.setFilter(filter);
                rs = querytable.getScanner(scan);
                System.out.println("tableName : " + tablename);
                System.out.println("Data : ");
                // double max=0;
                //   double min=Double.MAX_VALUE;
                System.out.println(" i "+i);
                for (Result r : rs) {
                    Cell[] cellArr = r.rawCells();
                    for (Cell cell : cellArr) {
                        //rowArray
                        byte[] rowArr = cell.getRowArray();
                        //获取列族
                        String family = new String(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
                        // System.out.println(cell.getTimestamp()+"  "+new String(cell.getValue(),"utf-8"));  //都转换为utf-8，中文会乱码
                        //获取rowkey
                        String row = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
                        //获取列名
                        String column = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                        String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                        System.out.println("row :" + row + " family :" + family + " column :" + column + " value : " + value);
                        // return;
                  /*   max=Double.parseDouble(value)>max?Double.parseDouble(value):max;
                    min=Double.parseDouble(value)<min?Double.parseDouble(value):min;
                    System.out.println("max"+max);
                    System.out.println("min"+min);*/
                        // System.out.println(new String(rowArr));
                    }
                }
            }
            rs.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            con.close();
        }
    }



    public static void creatTable(String tableName, String[] familys)
            throws Exception {
        Configuration conf= HBaseConfiguration.create();
        HBaseAdmin admin = new HBaseAdmin(conf);
        if (admin.tableExists(tableName)) {
            System.out.println("table already exists!");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(tableName);
            for (int i = 0; i < familys.length; i++) {
                tableDesc.addFamily(new HColumnDescriptor(familys[i]));
            }
            admin.createTable(tableDesc);
            System.out.println("create table " + tableName + " ok.");
        }
    }



    public static void dropTable(String tableName) {
        try {
            Configuration conf= HBaseConfiguration.create();
            HBaseAdmin admin = new HBaseAdmin(conf);
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void truncateTable(String tableName) {
        try {
            Configuration conf= HBaseConfiguration.create();
            HBaseAdmin admin = new HBaseAdmin(conf);
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 大批量数据插入
     * @param tablename
     * @param list
     * @throws IOException
     */
    public static void bigInsertTestImport(String tablename,List<String> list) throws IOException {
        long start=System.currentTimeMillis();
        Configuration conf= HBaseConfiguration.create();
        Connection con=null;
        String Decimalformat="00000000000000";
        try {
            con = ConnectionFactory.createConnection(conf);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            HTable table = (HTable) con.getTable(hbasetablename);

            //不要自动清理缓冲区
            table.setAutoFlush(false);
            for(int i=0;i<list.size();i++) {
                String rowKey = new DecimalFormat(Decimalformat).format(i);
                //System.out.println("format: " + rowKey);
                Put put = new Put(Bytes.toBytes(rowKey));
                //关闭写前日志
                //put.setWriteToWAL(false);
                put.add(Bytes.toBytes("cf1"), Bytes.toBytes("temperature"), Bytes.toBytes(list.get(i)));
                table.put(put);
                if(i % 2000==0)
                {
                    System.out.println("format: " + rowKey);
                    table.flushCommits();
                }
            }
            //自动清理
            table.flushCommits();
            System.out.println(System.currentTimeMillis()-start);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            con.close();
        }
    }



    public static void bigInsertTestImportBinary(String tablename,List<String> list) throws IOException {
        long start=System.currentTimeMillis();
        Configuration conf= HBaseConfiguration.create();
        Connection con=null;
        String Decimalformat="00000000000000";
        try {
            con = ConnectionFactory.createConnection(conf);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            HTable table = (HTable) con.getTable(hbasetablename);

            //不要自动清理缓冲区
            table.setAutoFlush(false);
            for(int i=0;i<list.size();i++) {
                String rowKey = new DecimalFormat(Decimalformat).format(i);
                //System.out.println("format: " + rowKey);
                Put put = new Put(Bytes.toBytes(rowKey));
                //关闭写前日志
                //put.setWriteToWAL(false);
                put.add(Bytes.toBytes("cf1"), Bytes.toBytes("temperature"), Bytes.toBytes(Double.parseDouble(list.get(i))));
                table.put(put);
                if(i % 2000==0)
                {
                    System.out.println("format: " + rowKey);
                    table.flushCommits();
                }
            }
            //自动清理
            table.flushCommits();
            System.out.println(System.currentTimeMillis()-start);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            con.close();
        }
    }

    public static  String formattedString(int num)
    {
        String Decimalformat="00000000000000";
        String rowKey = new DecimalFormat(Decimalformat).format(num);
        return  rowKey;
    }




    public static void bigInsert(String tablename) throws IOException {
        long start=System.currentTimeMillis();
        Configuration conf= HBaseConfiguration.create();
        Connection con=null;

        try {
            con = ConnectionFactory.createConnection(conf);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            HTable table = (HTable) con.getTable(hbasetablename);

            //不要自动清理缓冲区
            table.setAutoFlush(false);
            String Decimalformat="0000000000";
            for(int i=500000;i<502000;i++) {
                String rowKey = new DecimalFormat(Decimalformat).format(i);
                System.out.println("format: " + rowKey);
                Put put = new Put(Bytes.toBytes(rowKey));
                //关闭写前日志
                put.setWriteToWAL(false);
                if(i%2==0) {
                    put.add(Bytes.toBytes("cf1"), Bytes.toBytes("gender"), Bytes.toBytes("man"));
                }else {
                    put.add(Bytes.toBytes("cf1"), Bytes.toBytes("gender"), Bytes.toBytes("woman"));
                }
                put.add(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes(valueOf(i+"tom")));
                put.add(Bytes.toBytes("cf2"), Bytes.toBytes("chinese"), Bytes.toBytes(valueOf(i+"chinese")));
                table.put(put);
                if(i % 2000==0)
                {
                    table.flushCommits();
                }
            }
            //自动清理
            table.flushCommits();
            System.out.println(System.currentTimeMillis()-start);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            con.close();
        }
    }
    private void ConFactoryWay() throws IOException {
        Configuration conf= HBaseConfiguration.create();
        Connection con    = ConnectionFactory.createConnection(conf);
        TableName[] names=con.getAdmin().listTableNames();
        System.out.println(" 123 "+names[0].getNameAsString());


    }


}
