package com.enough.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @program: common-utils
 * @description: 通用的Builder模式构造器，不使用lombok的@Builder注解时，也可以使用类的builder模式
 * @author: lidong
 * @create: 2020/01/15
 */
public class CommonBuilder<T> {

    /**
     * 实例化器
     */
    private final Supplier <T> instantiator;

    private List <Consumer <T>> modifiers = new ArrayList <>();

    public CommonBuilder(Supplier <T> instantiator) {
        this.instantiator = instantiator;
    }

    public static <T> CommonBuilder <T> of(Supplier <T> instantiator) {
        return new CommonBuilder <>(instantiator);
    }

    public <P1> CommonBuilder <T> with(Consumer1 <T, P1> consumer, P1 p1) {
        Consumer <T> c = instance -> consumer.accept(instance, p1);
        modifiers.add(c);
        return this;
    }

    public <P1, P2> CommonBuilder <T> with(Consumer2 <T, P1, P2> consumer, P1 p1, P2 p2) {
        Consumer <T> c = instance -> consumer.accept(instance, p1, p2);
        modifiers.add(c);
        return this;
    }

    public <P1, P2, P3> CommonBuilder <T> with(Consumer3 <T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
        Consumer <T> c = instance -> consumer.accept(instance, p1, p2, p3);
        modifiers.add(c);
        return this;
    }

    public T build(){
        T value = instantiator.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }

    @FunctionalInterface
    public interface Consumer1<T, P1> {
        void accept(T t, P1 p1);
    }

    @FunctionalInterface
    public interface Consumer2<T, P1, P2> {
        void accept(T t, P1 p1, P2 p2);
    }

    @FunctionalInterface
    public interface Consumer3<T, P1, P2, P3> {
        void accept(T t, P1 p1, P2 p2, P3 p3);
    }
}
