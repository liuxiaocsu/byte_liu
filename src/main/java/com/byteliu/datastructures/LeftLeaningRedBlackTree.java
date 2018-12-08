package com.byteliu.datastructures;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * 类描述:
 *
 * @author yugu.lx 2018/9/29 下午5:43
 */
public class LeftLeaningRedBlackTree <Key extends Comparable<Key>, Value>{

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node{
        private  Key key;
        private Value value;
        private Node right, left;
        private int count;
        private boolean color;

        public Node(Key key, Value value, int count){
            this.key = key;
            this.value = value;
            this.count = count;
            this.color = RED;
        }
    }

    private Node root;

    public int size(){
        return size(root);
    }

    private boolean isRed(Node node){
        if(node == null) return false;
        return node.color == RED;
    }

    private int size(Node x){
        if(x == null) return 0;
        return x.count;
    }

    //How many keys lesser than this key are there?
    public int rank(Key key){
        return rank(key, root);
    }

    private int rank(Key key, Node x){
        if(x==null)return 0;
        int compare = key.compareTo(x.key);
        if(compare<0)return rank(key, x.left);
        if(compare>0)return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left);
    }

    public void put(Key key, Value value){
        root = put(root, key, value);
        root.color = BLACK;
    }

    private Node put(Node node, Key key, Value value){
        if(node == null)return new Node(key, value, 1);

        if(isRed(node.left) && isRed(node.left.left))node = splitFourNode(node);

        int compare = key.compareTo(node.key);
        if(compare>0)node.right = put(node.right, key, value);
        else if(compare<0)node.left = put(node.left, key, value);
        else node.value = value;

        if(isRed(node.right)) node = rotateLeft(node);

        node.count = 1 + size(node.right) + size(node.left);
        return node;
    }

    private Node splitFourNode(Node node){
        node = rotateRight(node);
        assert(isRed(node)):"Node was not red during split four node operation";
        flipColors(node);
        assert(!isRed(node.right)):"Node's right is not black";
        assert(!isRed(node.left)):"Node's left is not black";
        return node;
    }

    private Node rotateLeft(Node node){
        assert isRed(node.right):"Node.right is not Red - Rotate Left";

        Node t = node.right;
        node.right = t.left;
        t.left = node;
        t.color = node.color;
        node.color = RED;
        return t;
    }

    private Node rotateRight(Node node){
        assert isRed(node.left):"Node.left is not Red - Rotate Right";

        Node t = node.left;
        node.left = t.right;
        t.right = node;
        t.color = node.color;
        node.color = RED;
        return t;
    }

    private void flipColors(Node node){

        assert !isRed(node):"Assertion failed: parent node is not Black";
        assert isRed(node.right):"Assertion failed: node.right is not Red";
        assert isRed(node.left):"Assertion failed: node.left is not Red";
        node.color = !node.color;
        node.right.color = !node.right.color;
        node.left.color = !node.left.color;
    }

    private Node moveRedLeft(Node node){
        Node x = node.right;
        flipColors(node);
        if(isRed(x.left)){
            node.right = rotateRight(x);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node){
        flipColors(node);
        if(isRed(node.left.left)){
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    public Value get(Key key){
        Node x = root;
        while(x!=null){
            int compare = key.compareTo(x.key);
            if(compare>0)x = x.right;
            else if(compare<0)x = x.left;
            else return x.value;
        }
        return null;
    }


    private Node findMin(Node node){
        while(node.left!=null) node = node.left;
        return node;
    }

    public void deleteMin(){
        root = deleteMin(root);
        if(root!=null) root.color = BLACK;
    }

    private Node deleteMin(Node node){
        if(node.left == null)return null;
        if(!isRed(node.left) && !isRed(node.left.left)){node = moveRedLeft(node);}
        node.left = deleteMin(node.left);
        if(isRed(node.right))node = rotateLeft(node);
        node.count = 1 + size(node.left) + size(node.right);
        return node;
    }

    public void deleteMax(){
        root = deleteMax(root);
        if(root!=null) root.color = BLACK;
    }

    private Node deleteMax(Node node){
        if(node.right == null){
            if(node.left!=null) node.left.color = BLACK;
            return node.left;
        }

        if(isRed(node.left)) node = rotateRight(node);
        if(!isRed(node.right) && !isRed(node.right.left)){node = moveRedRight(node);}
        node.right = deleteMax(node.right);
        node.count = 1+size(node.left)+size(node.right);
        return node;
    }

    public void delete(Key key){
        if(get(key)==null){System.out.println("Key not found");return;}
        root = delete(root, key);
        if(root!=null) root.color = BLACK;
    }

    private Node delete(Node node, Key key){
        int cmp = key.compareTo(node.key);
        if(cmp < 0){
            if(!isRed(node.left) && !isRed(node.left.left))
                node = moveRedLeft(node);
            node.left = delete(node.left, key);
        }else{
            if(isRed(node.left)) node = rotateRight(node);

            if(key.compareTo(node.key) == 0 && (node.right == null))
                return null;

            if(!isRed(node.right) && !isRed(node.right.left))
                node = moveRedRight(node);

            if(key.compareTo(node.key) == 0){
                Node min = findMin(node.right);
                node.key = min.key;
                node.value = min.value;
                node.right = deleteMin(node.right);
            }else{
                node.right = delete(node.right, key);
            }
        }
        if(isRed(node.right)) node = rotateLeft(node);
        node.count = size(node.left) + size(node.right) + 1;

        return node;
    }

    public Iterable<Key> iterator(){
        Queue<Key> queue = new LinkedList<Key>();
        inorder(root, queue);
        return queue;
    }

    private void inorder(Node node, Queue<Key> queue){
        if(node == null)return;
        inorder(node.left, queue);
        queue.add(node.key);
        inorder(node.right, queue);
    }

    public Key floor(Key key){
        Node x = floor(root , key);
        if(x ==  null) return null;
        return x.key;
    }

    private Node floor(Node node, Key key){
        if(node == null) return null;
        int compare = key.compareTo(node.key);

        if(compare == 0)return node;
        if(compare<0)return floor(node.left, key);

        Node t = floor(node.right, key);
        if(t!=null) return t;
        else return node;
    }

    public Key ceiling(Key key){
        Node x = ceiling(root, key);
        if(x == null) return null;
        return x.key;
    }

    private Node ceiling(Node node, Key key){
        if(node == null) return null;
        int compare = key.compareTo(node.key);

        if(compare==0)return node;
        if(compare>0)return ceiling(node.right, key);

        Node t = ceiling(node.left, key);
        if(t!=null) return t;
        else return node;
    }
    public void printTree(){
        System.out.println();
        for(Key k:iterator()){
            System.out.print(k+" ");
        }
        System.out.println();
    }

    //Test
    public static void main(String[] args){
        LeftLeaningRedBlackTree<Integer, String> llrbt = new LeftLeaningRedBlackTree();
        Scanner scan = new Scanner(System.in);

        //Display a Console Interface
        while (true) {
            System.out.println("\n1.- Add items\n"
                    + "2.- Delete items\n"
                    + "3.- Floor\n"
                    + "4.- Ceiling\n"
                    + "5.- DeleteMin\n"
                    + "6.- DeleteMax\n"
                    + "7.- Print tree\n");
            int choice = scan.nextInt();

            int item;

            switch (choice) {
                case 1:
                    item = scan.nextInt();
                    while (item != -999) {
                        llrbt.put(item, "");
                        item = scan.nextInt();
                    }
                    llrbt.printTree();
                    break;
                case 2:
                    System.out.println("Enter value to delete: ");
                    item = scan.nextInt();
                    llrbt.delete(item);
                    llrbt.printTree();
                    break;
                case 3:
                    item = scan.nextInt();
                    System.out.println(llrbt.floor(item));
                    break;
                case 4:
                    item = scan.nextInt();
                    System.out.println(llrbt.ceiling(item));
                    break;
                case 5:
                    llrbt.deleteMin();
                    llrbt.printTree();
                    break;
                case 6:
                    llrbt.deleteMax();
                    llrbt.printTree();
                    break;
                case 7:
                    llrbt.printTree();
                    break;
            }
        }
    }
}
