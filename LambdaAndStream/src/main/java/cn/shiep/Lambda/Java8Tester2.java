package cn.shiep.Lambda;

/**
 * @Author yuanbao
 * @Date 2023/5/12
 * @Description lambda 表达式只能引用标记了 final 的外层局部变量，
 * 这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误。
 */
public class Java8Tester2 {
    final static String salutation = "Hello!";

    public static void main(String[] args) {
        // String a = "asdas";
        // GreetingService greetingService = message -> {
        //     // a = "asdasd";
        //     System.out.println(salutation + message);
        // };
        // greetingService.sayMessage("Runoob");

        int num = 1;
        Converter<Integer,String> s = (param) -> {
            System.out.println(param + num);
        };
        s.convert(2);
    }

    public interface Converter<T1,T2>{
        void convert(int i);
    }

    interface GreetingService{
        void sayMessage(String message);
    }
}
