package threadpool;

import Item.HbaseIndexItem;
import hbasequery.ResultAnswer;
import javafx.concurrent.Task;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestFutureTask2   {



    public static  void threadCompute(String tableName, ArrayList<HbaseIndexItem> list)
    {
        int count=10;
        // 用Executors来管理线程
        ExecutorService exec = Executors.newFixedThreadPool(count);
        // 用Future来接受返回值
        List<Future<List<ResultAnswer>>> results = new ArrayList<Future<List<ResultAnswer>>>();
        int length=list.size();
        int start=0;
        int end=0;
        int lastEnd=0;
        int split=list.size()/count;
       Long startTime=System.currentTimeMillis();
        System.out.println();
        for(int i=0; i<count; i++){
            // 将返回值放入Future<String>类型的List中去
             start=lastEnd+1;
             end=start+split;
             if(end>=length)
             {
                 end=length-1;
             }
             if(lastEnd==0)
             {
                 start=0;
             }
            List<HbaseIndexItem> subList=list.subList(start,end);
            results.add(exec.submit(new callHBaseScan(subList,tableName)));
            lastEnd=end;
        }
        try {
            System.out.println("task运行结果"+results.get(0).get().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("所有任务执行完毕");
        Long endTime=System.currentTimeMillis();
        System.out.println("总耗时 "+ (endTime-startTime));
        // 得到返回值
        exec.shutdown();
    }
}
