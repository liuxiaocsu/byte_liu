package com.byteliu.datastructures;

import java.util.Comparator;

/**
 * topn指的是从已经存在的数组中，找出最大（或最小）的前n个元素。
 topn算法实现思路（找最大的n个元素）
 1：取出数组的前n个元素，创建长度为n的最小堆。
 2：从n开始循环数组的剩余元素，如果元素(a)比最小堆的根节点大，将a设置成最小堆的根节点，并让堆保持最小堆的特性。
 3：循环完成后，最小堆中的所有元素就是需要找的最大的n个元素。
 */

/**
 * 堆数据结构应用实例
 *
 * @author yugu.lx 2018/12/8 3:29 PM
 *
 */
public class HeapApp {

    public static int[] toPrimitive(Integer array[]) {
        if (array == null)
            return null;
        if (array.length == 0)
            return new int[0];
        int result[] = new int[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i].intValue();

        return result;
    }

    /**
     * 1：取出数组的前n个元素，创建长度为n的最小堆。
     * 2：从n开始循环数组的剩余元素，如果元素(a)比最小堆的根节点大，将a设置成最小堆的根节点，并让堆保持最小堆的特性。
     * 3：循环完成后，最小堆中的所有元素就是需要找的最大的n个元素。
     *
     * @param array
     * @param n
     * @return
     */
    public static int[] topn(int[] array, int n) {
        if (n >= array.length) {
            return array;
        }
        Integer[] topn = new Integer[n];
        for (int i = 0; i < topn.length; i++) {
            topn[i] = array[i];
        }

        Heap<Integer> heap = new Heap<Integer>(topn, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                // 生成最大堆使用o1-o2,生成最小堆使用o2-o1
                return o2 - o1;
            }
        });
        for (int i = n; i < array.length; i++) {
            int value = array[i];
            int min = heap.root();
            if (value > min) {
                heap.setRoot(value);
            }
        }
//      heap.sort();
        return toPrimitive(topn);
    }

    public static void main(String[] args) {
        int[] array = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 11, 9, 10, 100 };
        array = new int[] { 101, 2, 3, 4, 5, 6, 7, 8, 11, 9, 10, 100 };
        int[] result = topn(array, 5);
        for (int integer : result) {
            System.out.print(integer + ",");
        }
    }

}




class Heap<T> {

    /**
     * 以数组形式存储堆元素
     */
    private T[] heap;

    /**
     * 用于比较堆中的元素。c.compare(根,叶子) > 0。
     * 使用相反的Comparator可以创建最大堆、最小堆。
     */
    private Comparator<? super T> c;

    public Heap(T[] a, Comparator<? super T> c) {
        this.heap = a;
        this.c = c;
        buildHeap();
    }

    /**
     * 返回值为i/2
     *
     * @param i
     * @return
     */
    private int parent(int i) {
        return (i - 1) >> 1;
    }

    /**
     *
     * 返回指定节点的left子节点数组索引。相当于2*(i+1)-1
     *
     *
     * @param i
     * @return
     */
    private int left(int i) {
        return ((i + 1) << 1) - 1;
    }

    /**
     * 返回指定节点的right子节点数组索引。相当于2*(i+1)
     *
     * @param i
     * @return
     */
    private int right(int i) {
        return (i + 1) << 1;
    }

    /**
     * 堆化
     *
     * @param i
     *           堆化的起始节点
     */
    private void heapify(int i) {
        heapify(i, heap.length);
    }

    /**
     * 堆化，
     *
     * @param i
     * @param size 堆化的范围
     */
    private void heapify(int i, int size) {
        int l = left(i);
        int r = right(i);
        int next = i;
        if (l < size && c.compare(heap[l], heap[i]) > 0)
            next = l;
        if (r < size && c.compare(heap[r], heap[next]) > 0)
            next = r;
        if (i == next)
            return;
        swap(i, next);
        heapify(next, size);
    }

    /**
     * 对堆进行排序
     */
    public void sort() {
        // buildHeap();
        for (int i = heap.length - 1; i > 0; i--) {
            swap(0, i);
            heapify(0, i);
        }
    }

    /**
     * 交换数组值
     *
     * @param i
     * @param j
     */
    private void swap(int i, int j) {
        T tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    /**
     * 创建堆
     */
    private void buildHeap() {
        for (int i = (heap.length) / 2 - 1; i >= 0; i--) {
            heapify(i);
        }
    }

    public void setRoot(T root) {
        heap[0] = root;
        heapify(0);
    }

    public T root() {
        return heap[0];
    }

    /**
     * 取出最大元素并从堆中删除最大元素。
     *
     * @param
     * @param a
     * @param comp
     * @return
     */
    // public T extractMax(T[] a, Comparator<? super T> comp) {
    // if (a.length == 0) {
    // throw new
    // IllegalArgumentException("can not extract max element in empty heap");
    // }
    // T max = a[0];
    // a[0] = a[a.length - 1];
    // heapify(0, a.length - 1);
    // return max;
    // }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Integer[] temp = null;
        temp = new Integer[] { 5, 2, 4, 6, 1, 3, 2, 6 };
        temp = new Integer[] { 16, 14, 8, 7, 9, 3, 2, 4, 1 };

        Comparator<Integer> comp = new Comparator<Integer>() {

            public int compare(Integer o1, Integer o2) {
                //生成最大堆使用o1-o2,生成最小堆使用o2-o1
                return o1 - o2;
            }
        };
        //创建最大堆
        Heap<Integer> heap = new Heap<Integer>(temp, comp);
        // heap.buildHeap();
        for (int i : temp) {
            System.out.print(i + " ");
        }
        System.out.println();

        heap.sort();
        for (int i : temp) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

}
