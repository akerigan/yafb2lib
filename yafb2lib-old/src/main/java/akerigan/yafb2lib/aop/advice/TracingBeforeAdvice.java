package akerigan.yafb2lib.aop.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * Date: 21.09.2008
 * Time: 21:03:43
 *
 * @author Vlad Vinichenko(akerigan@gmail.com)
 */
public class TracingBeforeAdvice implements MethodBeforeAdvice {

    public void before(Method method, Object[] args, Object target)
            throws Throwable {
        System.out.println("Just before method call...");
    }

}
