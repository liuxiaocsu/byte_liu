/*
 * Copyright 2018 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.byteliu.patterns;

/**
 * 类描述: 状态模式
 *
 * @author yugu.lx 2018/9/6 下午5:04
 */
public class StatePattern {
    public static void main(String args[]) {
        Switch s1,s2;
        s1=new Switch("开关1");
        s2=new Switch("开关2");

        s1.on();
        s2.on();
        s1.off();
        s2.off();
        s2.on();
        s1.on();

//        s1.on();
//        s1.off();
//        s1.on();
//
//        s2.on();
//        s2.off();
//        s2.on();

    }
}


class Switch {
    /**定义三个状态对象   在有些情况下，多个环境对象可能需要共享同一个状态，
     * 如果希望在系统中实现多个环境对象共享一个或多个状态对象，那么需要将这些状态对象定义为环境类的静态成员对象。
     */
    private  State state,onState,offState;
    private String name;

    public Switch(String name) {
        this.name = name;
        onState = new OnState();
        offState = new OffState();
        this.state = onState;
    }

    public void setState(State state) {
        this.state = state;
    }

    public  State getState(String type) {
        if (type.equalsIgnoreCase("on")) {
            return onState;
        }
        else {
            return offState;
        }
    }

    //打开开关
    public void on() {
        System.out.print(name);
        state.on(this);
    }

    //关闭开关
    public void off() {
        System.out.print(name);
        state.off(this);
    }
}


abstract class State {
    public abstract void on(Switch s);
    public abstract void off(Switch s);
}

//打开状态
class OnState extends State {
    public void on(Switch s) {
        System.out.println("已经打开！");
    }

    public void off(Switch s) {
        System.out.println("关闭！");
        s.setState(s.getState("off"));
    }
}

//关闭状态
class OffState extends State {
    public void on(Switch s) {
        System.out.println("打开！");
        s.setState(s.getState("on"));
    }

    public void off(Switch s) {
        System.out.println("已经关闭！");
    }
}
