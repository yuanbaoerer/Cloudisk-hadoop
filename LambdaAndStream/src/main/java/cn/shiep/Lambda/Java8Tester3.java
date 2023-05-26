package cn.shiep.Lambda;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author yuanbao
 * @Date 2023/5/12
 * @Description
 */
public class Java8Tester3 {

    public static void lambdaMaxAndMin(){
        List<Integer> primes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
        //mapToInt()将List中的每个元素转换为int类型，返回一个IntStream对象。
        //mapToInt() 方法接收一个 ToIntFunction 接口类型的参数，用于定义元素的转换规则。
        /**
         * 使用 IntStream 中的 summaryStatistics() 方法，
         * 将 IntStream 转换为一个 IntSummaryStatistics 对象，
         * 这个对象提供了一些有用的统计信息，如元素个数、总和、平均值、最大值和最小值等。
         * 这些统计信息可以使用 IntSummaryStatistics 中的方法进行获取。
         */
        IntSummaryStatistics stats = primes.stream().mapToInt((x) -> x)
                .summaryStatistics();
        System.out.println("Highest prime number in List : " + stats.getMax());
        System.out.println("Lowest prime number in List : " + stats.getMin());
        System.out.println("Sum of all prime numbers : " + stats.getSum());
        System.out.println("Average of all prime numbers : " + stats.getAverage());
    }

    /**
     * 使用Stream的distinct()方法过滤集合中重复元素。
     */
    public static void lambdaDistinct(){
        List<Integer> numbers = Arrays.asList(9,10,3,4,7,3,4);
        //首先将每一个元素平方，之后对集合中元素进行distinct去重，最后collect(Collectors.toList())一个新的集合
        List<Integer> distinct = numbers.stream().map(i -> i * i).distinct()
                .collect(Collectors.toList());
        System.out.println(distinct);
    }

    /**
     * 我们经常需要对集合中元素运用一定的功能，如表中的每个元素乘以或除以一个值等等.
     * 上面是将字符串转换为大写，然后使用逗号串起来。
     */
    public static void lambdaFunctions(){
        List<String> G7 = Arrays.asList("USA","Japan", "France", "Germany",
                "Italy", "U.K.","Canada");
        String G7Countries = G7.stream().map(x -> x.toUpperCase())
                .collect(Collectors.joining(","));
        System.out.println(G7Countries);
    }

    public static void lambdaFiltering(){
        List<String> strList = Arrays.asList("abc","ab","1234");
        List<String> filtered = strList.stream().filter(x -> x.length()>2)
                .collect(Collectors.toList());
        System.out.printf("Original List:%s, filtered list:%s\n",strList,filtered);
    }

    // 使用Lambda实现Map和Reduce
    public static void lambdaMap() {
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        for (Integer cost : costBeforeTax) {
            double price = cost + .12 * cost;
            System.out.println(price);
        }

        System.out.println("-----------------------");

        costBeforeTax.stream().map(cost -> cost + .12 * cost)
                .forEach(System.out::println);
    }

    //reduce() 是将集合中所有值结合进一个，Reduce类似SQL语句中的sum(), avg() 或count(),
    public static void lambdaReduce() {
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        double total = 0;
        for (Integer cost : costBeforeTax) {
            double price = cost + .12 * cost;
            total += price;
        }
        System.out.println("Total:" + total);

        double bill = costBeforeTax.stream().map(cost -> cost + .12*cost)
                .reduce((sum,cost) -> sum + cost)
                .get();
        System.out.println("Total:"+bill);
    }


    /**
     * 为了支持函数编程，Java 8加入了一个新的包java.util.function，
     * 其中有一个接口java.util.function.Predicate是支持Lambda函数编程：
     */
    public static void lambdaFuncProgram() {
        List languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
        System.out.println("Languages which starts with J :");
        filter(languages, (str) -> ((String) str).startsWith("C"));
    }

    public static void filter(List<String> names, Predicate condition) {
        // for (String name : names){
        //     if (condition.test(name)){
        //         System.out.println(name + " ");
        //     }
        // }
        /**
         * condition.test()用于判断给定的对象是否符合指定的条件
         * 换句话说，condition.test(name) 是用来测试给定的元素是否符合过滤条件的，
         * 它返回一个 boolean 类型的结果。如果返回值为 true，则表示该元素符合条件，应该被保留；
         * 如果返回值为 false，则表示该元素不符合条件，应该被过滤掉。
         */
        names.stream().filter((name) -> (condition.test(name)))
                .forEach((name) -> {
                    System.out.println(name + " ");
                });

        names.stream().filter(name -> name.startsWith("J")).forEach(System.out::println);
    }


    public static void lambdaList() {
        // 传统java
        List<String> features = Arrays.asList("Lambdas", "Default Method",
                "Stream API", "Date and Time API");
        for (String feature : features) {
            System.out.println(feature);
        }
        // Lambda
        List featuresLambda = Arrays.asList("Lambdas", "Default Method",
                "Stream API", "Date and Time API");
        featuresLambda.forEach(n -> System.out.println(n));

        /**
         * 更好地使用 Java 8 的方法引用功能
         * 方法引用由 ::（双冒号）运算符表示
         * 看起来类似于 C++ 的分数解析运算符
         */
        featuresLambda.forEach(System.out::println);
    }

    public static void main(String[] args) {
        // lambdaList();
        // lambdaFuncProgram();
        // lambdaMap();
        // lambdaReduce();
        // lambdaFiltering();
        // lambdaFunctions();
        // lambdaDistinct();
        lambdaMaxAndMin();
    }
}
