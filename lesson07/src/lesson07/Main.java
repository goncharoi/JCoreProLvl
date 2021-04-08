package lesson07;

import lesson07.annotations.AfterSuite;
import lesson07.annotations.BeforeSuite;
import lesson07.annotations.TestMethod;
import lesson07.tests.Test01;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        start(Test01.class);
    }

    private static void start(Class<?> icTestClass) {
        List<Method> ltTestMethods = new ArrayList<>();
        List<Method> ltBeforeMethods = new ArrayList<>();
        List<Method> ltAfterMethods = new ArrayList<>();

        for (Method loMethod: icTestClass.getDeclaredMethods()) {
            if (loMethod.getAnnotation(TestMethod.class) != null)
                ltTestMethods.add(loMethod);

            if (loMethod.getAnnotation(BeforeSuite.class) != null)
                if (ltBeforeMethods.size() > 0)
                    throw new RuntimeException();
                else
                    ltBeforeMethods.add(loMethod);

            if (loMethod.getAnnotation(AfterSuite.class) != null)
                if (ltAfterMethods.size() > 0)
                    throw new RuntimeException();
                else
                    ltAfterMethods.add(loMethod);
        }

        if (ltBeforeMethods.size() > 0)
            invokeStaticMethod(ltBeforeMethods.get(0));

        ltTestMethods.sort(Comparator.comparingInt((Method o) -> o.getAnnotation(TestMethod.class).priority()));
        for (Method loMethod: ltTestMethods)
            invokeStaticMethod(loMethod);

        if (ltAfterMethods.size() > 0)
            invokeStaticMethod(ltAfterMethods.get(0));
    }

    private static void invokeStaticMethod(Method ioMethod){
        try {
            ioMethod.invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
