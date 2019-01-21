package PCAM;

import java.util.ArrayList;
import java.util.Random;

public class TestMean {
    public static void main(String[] args) {
        System.out.println("Math.random()=" + Math.random());// 结果是个double类型的值，区间为[0.0,1.0）
        ArrayList<Integer> list=new ArrayList<Integer>();
        for(int i=0;i<100;i++)
        {int num = (int) (Math.random() * 30);
            list.add(num);
        }
        for(int i=0;i<100;i++)
        {int num =100+ (int) (Math.random() * 30);
            list.add(num);
        }
        System.out.println(list.toString());
        // 注意不要写成(int)Math.random()*3，这个结果为0，因为先执行了强制转换
        System.out.println("Mean=" + computeMean(list));
        System.out.println("std=" + computeStandardDiviation(list));
    }




    public static double computeStandardDiviation(ArrayList<Integer> x) {
        int m=x.size();
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x.get(i);
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x.get(i)-dAve)*(x.get(i)-dAve);
        }
        return Math.sqrt(dVar/m);
    }

    public static double computeMean(ArrayList<Integer> x) {
        int m=x.size();
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x.get(i);
        }
        double dAve=sum/m;//求平均值
        return  dAve;
    }
}
