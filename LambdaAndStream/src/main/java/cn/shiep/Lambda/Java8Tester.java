package cn.shiep.Lambda;

/**
 * @Author yuanbao
 * @Date 2023/5/12
 * @Description
 */
public class Java8Tester {
    public static void main(String[] args) {
        Java8Tester tester = new Java8Tester();
        /**
         * MathOperation是interface类型的，是不能实例化的，但是这里把函数传递进方法了，等于说
         * 这个interface每次被创建时，就给他实现一次了
         */
        //函数作为一个方法的参数传递进方法中，这里就是把a + b函数作为参数传进了function
        MathOperation addition = (int a, int b) -> a + b;
        //甚至可以不声明类型
        MathOperation subtraction = (a,b) -> a - b;
        //也可以写成{}的形式，我感觉这里有点像匿名类
        MathOperation multiplication = (a,b) -> {
            return a * b;
        };

        System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
        System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
        System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));

        GreetingService greetingService1 = message -> {
            System.out.println("Hello" + message);
        };

        GreetingService greetingService2 = message -> System.out.println("Hello " + message);

        greetingService1.sayMessage("使用括号");
        greetingService2.sayMessage("不使用括号");
    }

    interface MathOperation{
        int operation(int a,int b);
    }

    interface GreetingService{
        void sayMessage(String message);
    }

    private int operate(int a, int b, MathOperation mathOperation){
        return mathOperation.operation(a,b);
    }
}
