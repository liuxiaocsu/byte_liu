package com.byteliu.datastructures;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * 类描述:
 *
 * @author yugu.lx 2018/11/7 6:01 PM
 */
public class BitSetDemo {


    /**
     * 将BitSet对象转化为ByteArray
     * @param bitSet
     * @return
     */
    public static byte[] bitSet2ByteArray(BitSet bitSet) {
        byte[] bytes = new byte[bitSet.size() / 8];
        for (int i = 0; i < bitSet.size(); i++) {
            int index = i / 8;
            int offset = 7 - i % 8;
            bytes[index] |= (bitSet.get(i) ? 1 : 0) << offset;
        }
        return bytes;
    }

    /**
     * 将ByteArray对象转化为BitSet
     * @param bytes
     * @return
     */
    public static BitSet byteArray2BitSet(byte[] bytes) {
        BitSet bitSet = new BitSet(bytes.length * 8);
        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 7; j >= 0; j--) {
                bitSet.set(index++, (bytes[i] & (1 << j)) >> j == 1 ? true
                        : false);
            }
        }
        return bitSet;

    }

    public static void main(String[]args){
        int A[]={23,44,1,2,34,56,11,7,9,25,1000000};
        bitSort(A);
        for(int i=0;i<A.length;i++)
        {
            System.out.print(A[i]+" ");
        }

        System.out.println(ff());


        String str = "我&的";
        char c = str.charAt(0);
        System.out.println(Character.isLetter(c));
        System.out.println(c);

//        if (!(c == '_' || Character.isLetter(c)) || str.indexOf(' ') != -1) {
//            str = "_" + str.replace(" ", "");
//        }

        if (str.contains("&")) {
            str = str.replaceAll("&", "_");
        }
        System.out.println(str);

    }

    public static int ff(){
        try{
            System.out.println("liu");
            return 1;
        }finally{
            return 2;
        }
    }


    public static void bitSort(int[]A)
    {
        int size=A.length;
        final int number_range=1000001;//这个数的大小必须大于数组A中元素的最大值，否则数组A中的后面部分元素不会改变，关键点在下面循环内代码  A[j++]=i 处
        BitSet bits=new BitSet(number_range);
        for(int i=0;i<size;i++)
        {
            bits.set(A[i]);
        }
        for(int i=0,j=0;i<number_range;i++)
        {
            if(bits.get(i))
            {
                A[j++]=i;
            }
        }
        System.out.println("bits的内存："+bits.size());//bits增长内存的方式：64,128,192，256,320.。。  即64bits=8bytes的倍数
        //解释：操作系统分配内存时以字节为单位，也就是说分配的内存必须是byte的整数倍，按照这个思路，似乎只需要7byte =56bit就够了，那为什么又是8byte呢?
        //这又是因为标准库在实现bitset时以一个字为基本单位分配内存的(不是字节)，也就是说分配的内存大小必定是4byte的整数倍。这里我们虽然浪费了十几个bit的空间，
        //但是比起之前用int类型的数组或者char类型的数组来作为标记，这个程序节约了很多内存的(50个int数据消耗内存200byte，而50个char数据消耗内存50byte)。
    }


    /**
     * 有1千万个随机数，随机数的范围在1到1亿之间。现在要求写出一种算法，将1到1亿之间没有在随机数中的数求出来？
     */
    public static void printNotContainRandomNum() {
        Random random=new Random();

        List<Integer> list=new ArrayList<Integer>();
        for(int i=0;i<10000000;i++)
        {
            int randomResult=random.nextInt(100000000);
            list.add(randomResult);
        }
        System.out.println("产生的随机数有");
        for(int i=0;i<list.size();i++)
        {
            System.out.println(list.get(i));
        }
        BitSet bitSet=new BitSet(100000000);
        for(int i=0;i<10000000;i++)
        {
            bitSet.set(list.get(i));
        }

        System.out.println("0~1亿不在上述随机数中有"+bitSet.size());
        for (int i = 0; i < 100000000; i++)
        {
            if(!bitSet.get(i))
            {
                System.out.println(i);
            }
        }
    }
}
