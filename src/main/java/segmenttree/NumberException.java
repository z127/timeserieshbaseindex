package segmenttree;

public class NumberException extends Exception
{
    double left;
    double right;
    public NumberException(double left,double right)//构造方法
    {
        super("左端点大于右端点 : "+left+" >" +right);//调用Exception(message:String)
    }
}
