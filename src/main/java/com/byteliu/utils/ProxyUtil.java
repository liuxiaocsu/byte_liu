package com.byteliu.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 类描述:
 *
 * @author yugu.lx 2018/9/20 下午5:03
 */
public class ProxyUtil {

    public static <T> T getProxy(T t, Class<? super T> ifClazz, InvocationHandler invocation) {

        if (!ifClazz.isInterface() || !ifClazz.isAssignableFrom(t.getClass())) {
            throw new RuntimeException("unmatch interface ");
        }
        Object ojb = Proxy.newProxyInstance(invocation.getClass().getClassLoader(), new Class[]{ifClazz}, invocation);
        return (T)ojb;
    }


    /**特意在网上查了一下，发现自己的知识点还是比较薄弱，同时也手动测试了自己的一些猜想，如下
     Work.class == new Work().getClass() 是相等的，所以同理可得：Work.class.getClassLoader() == new Work().getClass().getClassLoader() 也相等
     然而，有一个坑就是我上图片写的 Work work = new Hello();
     这里 System.out.println(work.getClass.getName（），输出为 "Hello";
     以前一直以为父类指向子类或“强转”后实例的类型是会转变的，但测试后发现Class并没有变化，所以我猜测类型是不会变的。
     本人以为 new Work().getClass()是Work的Class对象，殊不知获得的是子类Hello的Class对象。


     回到主题：这次出现异常的主要原因不是方法的第一个参数出现问题，而是第二个参数!
     Class.getInterfaces() 主要作用是获取某类所实现的接口所有接口，返回值Class<?>[] ，而我。。。哈哈竟然是获取接口的接口，所以出错了，有兴趣的朋友可以遍历输出所有接口的Class。



     另外，我在上网查问题的时候也发现了一个朋友遇到过同样的异常，但他的异常原因是：代理类没有实现接口，而是实现了某一基类。大家要注意了哦。
     */
    public static void main(String args[]) {
        Caculation caculation = new CaculationImpl();

        Caculation proxy = ProxyUtil.getProxy(caculation, Caculation.class, new LogInvocationHandler(caculation));

//        caculation.sum(1000);
        proxy.sum(100000);

//        Class<?> caller = null;
//         caller = Reflection.getCallerClass();

        Class c = caculation.getClass();
        Class c1 = CaculationImpl.class;
        Class c2 = Caculation.class;

        ClassLoader cl = c.getClassLoader();
        ClassLoader cl1 = c1.getClassLoader();
        ClassLoader cl2 = c2.getClassLoader();

        Class[] a = c.getInterfaces();
        Class[] a1 = c1.getInterfaces();
        Class[] a2 = c2.getInterfaces();

        InvocationHandler invocationHandler = new LogInvocationHandler(caculation);
//        Caculation proxy2 = (Caculation)Proxy.newProxyInstance(caculation.getClass().getClassLoader(), new Class[]{Caculation.class}, invocationHandler);
//        Caculation proxy2 = (Caculation)Proxy.newProxyInstance(caculation.getClass().getClassLoader(), caculation.getClass().getInterfaces(), invocationHandler);
//        Caculation proxy2 = (Caculation)Proxy.newProxyInstance(CaculationImpl.class.getClassLoader(), CaculationImpl.class.getInterfaces(), invocationHandler);
//        Caculation proxy2 = (Caculation)Proxy.newProxyInstance(Caculation.class.getClassLoader(), Caculation.class.getInterfaces(), invocationHandler);
        Object obj = Proxy.newProxyInstance(CaculationImpl.class.getClassLoader(), caculation.getClass().getInterfaces(), invocationHandler);
        Caculation proxy2 = (Caculation)obj;
        proxy2.sum(1000);

    }
}

class LogInvocationHandler implements InvocationHandler{

    Object proxy;

    public LogInvocationHandler(Object proxy){
        this.proxy = proxy;
    }


    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        System.out.println("start...");
        long start = System.currentTimeMillis();
        Object ob =  method.invoke(proxy,args);
        System.out.println("end...");
        System.out.println("cost:"+(System.currentTimeMillis()-start));
        //默认return null 会造成空指针异常
//        return null;
//        return object;   会ClassCastException
//        return proxy;    会ClassCastException
        return ob;
    }
}

interface Caculation{
    int sum(int num);
}

class CaculationImpl implements Caculation{

    public int sum(int num) {
        int sum = 0;
        for(int i=0;i<num+1;i++){
            sum += i;
        }
        return sum;
    }

//    public String toString(){
//        return String.valueOf(this.hashCode());
//    }
}

