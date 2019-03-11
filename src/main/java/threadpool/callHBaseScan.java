package threadpool;

import Item.HbaseIndexItem;
import hbasequery.HbaseQuery;
import hbasequery.ResultAnswer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class callHBaseScan  implements Callable<List<ResultAnswer>> {
    public List<HbaseIndexItem> list;
    public   String tableName;

    public callHBaseScan( List<HbaseIndexItem> list, String tableName) {
        this.list = list;
        this.tableName=tableName;
    }
    @Override
    public  List<ResultAnswer> call() throws Exception {
        System.out.println("块的长度"+list.size());
       List<ResultAnswer> resultList= HbaseQuery.queryPartDataScanList(tableName,list);
        return  resultList;
    }
}



