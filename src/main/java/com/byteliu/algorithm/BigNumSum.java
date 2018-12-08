/*
 * Copyright 2018 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.byteliu.algorithm;

/**
 * 类描述: 大正整数求和
 *
 * @author yugu.lx 2018/12/8 3:20 PM
 */
public class BigNumSum {

    public static void main(String args[]) {
        char c = '7';
        int i = c;
        //如何把 char ‘7’ 转为 int 7, 不能直接转化，那样得到是‘7’的Ascii值，即int 55
        System.out.println(i);
        //可以采用如下方式进行操作把 char ‘7’ 转为 int 7；‘0’的Ascii值是48
        int ii = c - '0';
        System.out.println(ii);
        System.out.println(new BigNumSum().bigNumSum("426709752318", "29"));
    }

    private String bigNumSum(String bigNum1, String bigNum2) {
        char[] num1Chars = new StringBuilder(bigNum1).reverse().toString().toCharArray();
        char[] num2Chars = new StringBuilder(bigNum2).reverse().toString().toCharArray();
        int[] result = new int[Math.max(num1Chars.length, num1Chars.length) + 1];

        for (int i = 0; i < result.length; i++) {
            if (i < num1Chars.length) {
                result[i] += num1Chars[i] - '0';
                carryCover(result, i);
            }
            if (i < num2Chars.length) {
                result[i] += num2Chars[i] - '0';
                carryCover(result, i);
            }
        }

        return transferToIntString(result);
    }


    /**
     * 进位操作
     *
     * @param result
     * @param i
     */
    private void carryCover(int[] result, int i) {
        if (i == result.length - 1) {
            return;
        }
        if (result[i] >= 10) {
            result[i] -= 10;
            result[i + 1] += 1;
        }
    }

    /**
     * 把数组转化成字符串
     *
     * @param array
     * @return
     */
    private String transferToIntString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = array.length - 1; i >= 0; i--) {
            //如果最高位为0的话则去掉
            if (i == array.length - 1 && array[i] == 0) {
                continue;
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

}

