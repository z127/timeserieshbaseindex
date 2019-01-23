package gson;

import Item.HbaseIndexItem;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.List;

public class JsonUtils {
    public  static  void writeJsonStream(String filepath, List<HbaseIndexItem> messages) throws IOException {
        File csv = new File(filepath); // CSV文件路径
        Gson gson = new Gson();
        csv.setReadable(true);//设置可读
        csv.setWritable(true);
        if(!csv.exists())
        {
            System.out.println("不存在创建文件");
            csv.createNewFile();
        }else {
            System.out.println("文件存在");
        }
        FileOutputStream fos = null;
        try {
           fos =new FileOutputStream(csv,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
        writer.setIndent("  ");
        writer.beginArray();
        for (HbaseIndexItem item : messages) {
            gson.toJson(item, HbaseIndexItem.class, writer);
        }
        writer.endArray();
        writer.close();
    }

}
