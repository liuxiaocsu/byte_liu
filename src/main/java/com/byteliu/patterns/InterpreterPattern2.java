/*
 * Copyright 2018 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.byteliu.patterns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * 类描述: 解释器模式
 *
 * @author yugu.lx 2018/8/31 下午2:36
 */
public class InterpreterPattern2 {
    public static void main(String[] args){
        String text = "LOOP 2 PRINT 杨过 SPACE SPACE PRINT 小龙女 BREAK END PRINT 郭靖 SPACE SPACE PRINT 黄蓉";
        Context context = new Context(text);

        Node node = new ExpressionNode();
        node.interpret(context);
        node.execute();
    }
}


//环境类：用于存储和操作需要解释的语句，在本实例中每一个需要解释的单词可以称为一个动作标记(Action Token)或命令
class Context {
    private StringTokenizer tokenizer; //StringTokenizer类，用于将字符串分解为更小的字符串标记(Token)，默认情况下以空格作为分隔符
    private String currentToken; //当前字符串标记

    public Context(String text) {
        tokenizer = new StringTokenizer(text); //通过传入的指令字符串创建StringTokenizer对象
        nextToken();
    }

    //返回下一个标记
    public String nextToken() {
        if (tokenizer.hasMoreTokens()) {
            currentToken = tokenizer.nextToken();
        }
        else {
            currentToken = null;
        }
        return currentToken;
    }

    //返回当前的标记
    public String currentToken() {
        return currentToken;
    }

    //跳过一个标记
    public void skipToken(String token) {
        if (!token.equals(currentToken)) {
            System.err.println("错误提示：" + currentToken + "解释错误！");
        }
        nextToken();
    }

    //如果当前的标记是一个数字，则返回对应的数值
    public int currentNumber() {
        int number = 0;
        try{
            number = Integer.parseInt(currentToken); //将字符串转换为整数
        }
        catch(NumberFormatException e) {
            System.err.println("错误提示：" + e);
        }
        return number;
    }
}

//抽象节点类：抽象表达式
abstract class Node {
    public abstract void interpret(Context text); //声明一个方法用于解释语句
    public abstract void execute(); //声明一个方法用于执行标记对应的命令
}

//表达式节点类：非终结符表达式
class ExpressionNode extends Node {
    private ArrayList<Node> list = new ArrayList<Node>(); //定义一个集合用于存储多条命令

    public void interpret(Context context) {
        //循环处理Context中的标记
        while (true){
            //如果已经没有任何标记，则退出解释
            if (context.currentToken() == null) {
                break;
            }
            //如果标记为END，则不解释END并结束本次解释过程，可以继续之后的解释
            else if (context.currentToken().equals("END")) {
                context.skipToken("END");
                break;
            }
            //如果为其他标记，则解释标记并将其加入命令集合
            else {
                Node commandNode = new CommandNode();
                commandNode.interpret(context);
                list.add(commandNode);
            }
        }
    }

    //循环执行命令集合中的每一条命令
    public void execute() {
        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            ((Node)iterator.next()).execute();
        }
    }
}

//语句命令节点类：非终结符表达式
class CommandNode extends Node {
    private Node node;

    public void interpret(Context context) {
        //处理LOOP循环命令
        if (context.currentToken().equals("LOOP")) {
            node = new LoopCommandNode();
            node.interpret(context);
        }
        //处理其他基本命令
        else {
            node = new PrimitiveCommandNode();
            node.interpret(context);
        }
    }

    public void execute() {
        node.execute();
    }
}

//循环命令节点类：非终结符表达式
class LoopCommandNode extends Node {
    private int number; //循环次数
    private Node commandNode; //循环语句中的表达式

    //解释循环命令
    public void interpret(Context context) {
        context.skipToken("LOOP");
        number = context.currentNumber();
        context.nextToken();
        commandNode = new ExpressionNode(); //循环语句中的表达式
        commandNode.interpret(context);
    }

    public void execute() {
        for (int i=0;i<number;i++)
            commandNode.execute();
    }
}

//基本命令节点类：终结符表达式
class PrimitiveCommandNode extends Node {
    private String name;
    private String text;

    //解释基本命令
    public void interpret(Context context) {
        name = context.currentToken();
        context.skipToken(name);
        if (!name.equals("PRINT") && !name.equals("BREAK") && !name.equals ("SPACE")){
            System.err.println("非法命令！");
        }
        if (name.equals("PRINT")){
            text = context.currentToken();
            context.nextToken();
        }
    }

    public void execute(){
        if (name.equals("PRINT"))
            System.out.print(text);
        else if (name.equals("SPACE"))
            System.out.print(" ");
        else if (name.equals("BREAK"))
            System.out.println();
    }
}
