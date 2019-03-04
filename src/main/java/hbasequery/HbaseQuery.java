package hbasequery;

import Item.HbaseIndexItem;
import Item.QueryItem;
import PCAM.ReadCSV;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HbaseQuery {
    public static void main(String[] args) throws IOException {
        //HBaseUtils.getInstance().queryDataUsingRowKey("cmop1000","0000000000","0000200000" );
        queryDataFilter("cmop","3.","4." );
    }


        public  static String formatSeries(double num)
        {
            String Decimalformat="00000000000000";
            return new DecimalFormat(Decimalformat).format(num);
        }

    public static void queryTest() throws IOException {
        ArrayList<QueryItem > list=new ArrayList<>();
        String Decimalformat="00000000000000";
        Long  starttime=System.currentTimeMillis();
        System.out.println();
        for(int i=0;i<50;i++)
        {
            list.add(new QueryItem(new DecimalFormat(Decimalformat).format(i+i*200000),new DecimalFormat(Decimalformat).format(i+i*200000+200000)));
        }
        HBaseUtils.getInstance().queryDataArrayListUsingRowKey("cmop2000",list);
        Long endtime=System.currentTimeMillis();
        System.out.println("总共耗时 : "+(endtime-starttime));
    }

    public static void queryDataScanList(String tablename,ArrayList<HbaseIndexItem> listHbaseIndexItem) {
        Configuration conf= HBaseConfiguration.create();
        Connection con=null;
        try {
            con = ConnectionFactory.createConnection(conf);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            Table querytable = con.getTable(hbasetablename);
            ResultScanner rs = null;
            //Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator(".*"));
          //  scan.setFilter(filter);
            Scan scan = new Scan();
            ArrayList<ResultAnswer> listResult=new ArrayList<>();
            System.out.println("tableName : "+tablename);
            Long startTime=System.currentTimeMillis();
            System.out.println();
            for(int i=0;i<listHbaseIndexItem.size();i++)
            {
               // System.out.println("count"+i);
                listResult.addAll(getListResult(scan,querytable, listHbaseIndexItem.get(i)));
            }
            querytable.close();
            Long endTime=System.currentTimeMillis();
            System.out.println("向HBase查询时间"+(endTime-startTime));
            // double max=0;
            //   double min=Double.MAX_VALUE;
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            try {
                con.close();
            } catch (Exception e)
            {
               e.printStackTrace();
            }

        }
    }


    public static void queryDataFilter(String tablename,String startKey,String endKey) {
        Configuration conf= HBaseConfiguration.create();
        Connection con=null;
        try {
            con = ConnectionFactory.createConnection(conf);
            //Admin admin=con.getAdmin();
            TableName hbasetablename = TableName.valueOf(tablename);
            Table querytable = con.getTable(hbasetablename);
            ResultScanner rs = null;
            //Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator(".*"));
            //  scan.setFilter(filter);
            Scan scan = new Scan();
            ArrayList<ResultAnswer> listResult=new ArrayList<>();
            Long startTime=System.currentTimeMillis();
            System.out.println();
            List<Filter> filters = new ArrayList<Filter>();
            filters.add( new SingleColumnValueFilter(Bytes.toBytes("cf1"), //列族
                    Bytes.toBytes("temperature"), //列名
                    CompareFilter.CompareOp.EQUAL,new SubstringComparator(startKey))); //值
            filters.add( new SingleColumnValueFilter(Bytes.toBytes("cf1"),
                    Bytes.toBytes("temperature"),
                    CompareFilter.CompareOp.EQUAL,new SubstringComparator(endKey) ));
            FilterList filterList1 = new FilterList(FilterList.Operator.MUST_PASS_ONE,filters);
            scan.setFilter(filterList1);;

           //  Filter  filter=  new RowFilter(CompareFilter.CompareOp.EQUAL,new SubstringComparator("000400"));
            //SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(Bytes.toBytes("cf1"),Bytes.toBytes("temperature"), CompareFilter.CompareOp.EQUAL, new SubstringComparator("9.056"));
          //  singleColumnValueFilter.setFilterIfMissing(true);
          /* Filter filter=new SingleColumnValueFilter(Bytes.toBytes("cf1"), //列族
                   Bytes.toBytes("temperature"), //列名
                   CompareFilter.CompareOp.EQUAL,Bytes.toBytes("10.7" ));*/
            scan.setFilter(filterList1);
            rs = querytable.getScanner(scan);
            System.out.println("tableName : " + tablename);
            System.out.println("Data : ");
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
                    //System.out.println(" row :" + row + " family :" + family + " column :" + column + " value : " + value);
                    // return;
                }
            }
            Long endTime=System.currentTimeMillis();
            System.out.println("向HBase查询时间"+(endTime-startTime));
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            try {
                con.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }


    public  static   ArrayList<ResultAnswer> getListResult(Scan scan, Table querytable, HbaseIndexItem item) throws IOException {
        ArrayList<ResultAnswer> list = new ArrayList<ResultAnswer>();
        ResultScanner rs = null;
        scan.setStartRow(Bytes.toBytes(formatSeries(item.getStart())));
        scan.setStopRow(Bytes.toBytes (formatSeries(item.getEnd())));
      //  System.out.println("startKey"+ Bytes.toString(scan.getStartRow()));
     //   System.out.println("endKey"+ Bytes.toString(scan.getStopRow()));
        rs = querytable.getScanner(scan);
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
               // System.out.println("row :" + row + " family :" + family + " column :" + column + " value : " + value);
                list.add(new ResultAnswer(row, value, family, column));
            }
        }
        rs.close();
        return list;
    }
}
