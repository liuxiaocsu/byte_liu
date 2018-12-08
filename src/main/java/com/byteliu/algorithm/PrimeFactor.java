package com.byteliu.algorithm;


/**
 * 将一个正整数分解质因数
 * @author yugu.lx 2018/12/8 3:26 PM
 */
public class PrimeFactor {

    public static void main(String[] args) {
        int n = 24;
        decompose(n);
    }

    private static void decompose(int n) {
        System.out.print(n + "=");
        for (int i = 2; i < n + 1; i++) {
            while (n % i == 0 && n != i) {
                n /= i;
                System.out.print(i + "*");
            }
            if (n == i) {
                System.out.println(i);
                break;
            }
        }
    }




}
