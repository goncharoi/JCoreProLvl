package lesson07.tests;

import lesson07.annotations.AfterSuite;
import lesson07.annotations.BeforeSuite;
import lesson07.annotations.TestMethod;

public class Test01 {
    @TestMethod(priority = 1)
    public static void Method01(){
        System.err.println("Execution of Test01->Method01");
    }
    @TestMethod(priority = 3)
    public static void Method02(){
        System.err.println("Execution of Test01->Method02 pr.3");
    }
    @TestMethod(priority = 2)
    public static void Method03(){
        System.err.println("Execution of Test01->Method03 pr.2");
    }
    @TestMethod(priority = 2)
    public static void Method04(){
        System.err.println("Execution of Test01->Method04 pr.2");
    }

    public static void JustMethod(){ // просто метод, который не должен быть вызван
        System.err.println("Execution of Test01->JustMethod");
    }

    @TestMethod(priority = 3)
    public static void Method05(){
        System.err.println("Execution of Test01->Method05 pr.3");
    }
    @TestMethod(priority = 4)
    public static void Method06(){
        System.err.println("Execution of Test01->Method06");
    }

    @BeforeSuite
    public static void MethodBS(){
        System.err.println("Execution of Test01->MethodBS");
    }

// если раскомментировать, будет RuntimeException
//    @BeforeSuite
//    public static void MethodBS2(){
//        System.err.println("Execution of Test01->MethodBS2");
//    }

    @AfterSuite
    public static void MethodAS(){
        System.err.println("Execution of Test01->MethodAS");
    }
}
