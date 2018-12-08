package com.byteliu.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 类描述:
 *
 * @author yugu.lx 2018/10/12 下午2:26
 */
public class UnsafeUtil {

    private static volatile Unsafe UNSAFE;

    public static Unsafe getUnsafe(){
        if(UNSAFE==null){
            synchronized (UnsafeUtil.class){
                if(UNSAFE==null){
                    try {
                        Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                        theUnsafeField.setAccessible(true);
                        UNSAFE = (Unsafe)theUnsafeField.get(Unsafe.class);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return UNSAFE;
    }


    public static void main(String args[]){
        Unsafe unsafe = UnsafeUtil.getUnsafe();
        UnsafeUtil.Student student = new UnsafeUtil().new Student();
        student.setAddress("12345add");
        student.setAge(28);
        student.setName("liu");

        try {
            long nameOffSet = unsafe.objectFieldOffset(student.getClass().getDeclaredField("name"));
            long ageOffSet = unsafe.objectFieldOffset(student.getClass().getDeclaredField("age"));
            long addressOffSet = unsafe.objectFieldOffset(student.getClass().getDeclaredField("address"));

            System.out.println("nameOFFSET-----"+nameOffSet);
            System.out.println("ageOFFSET-----"+ageOffSet);
            System.out.println("addressOFFSET-----"+addressOffSet);

            System.out.println("name-----"+student.getName());
            System.out.println("age-----"+student.getAge());
            System.out.println("address-----"+student.getAddress());

            unsafe.compareAndSwapInt(student,ageOffSet,28,29);
            unsafe.compareAndSwapObject(student,addressOffSet,"12345add--","addcsu");

            System.out.println("name-----"+student.getName());
            System.out.println("age-----"+student.getAge());
            System.out.println("address-----"+student.getAddress());

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }



    class Student{
        private int age;
        private String name;
        private String address;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }



}

