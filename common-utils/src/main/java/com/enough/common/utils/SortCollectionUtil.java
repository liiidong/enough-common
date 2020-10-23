package com.enough.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author lidong
 * @apiNote 通用工具类之按对象中某属性排序 *
 * @program datacube-server
 * @since 2020/08/19
 */
@Slf4j
public class SortCollectionUtil {
    public static final String DESC = "desc";
    public static final String ASC = "asc";

    /**
     * 对list中的元素按升序排列.
     *
     * @param list  排序集合
     * @param field 排序字段
     * @return
     */
    public static <T> List <T> sort(List <T> list, final String field) {
        return sort(list, field, null);
    }

    public static <T> List <T> sort(List <T> list, Field field) {
        return sort(list, field.getName(), null);
    }

    public static <T> List <T> sort(List <T> list, Field field, final String sort) {
        return sort(list, field.getName(), sort);
    }

    /**
     * 对Set排序，set必须实现Comparable接口
     *
     * @param set set
     * @param <T>
     * @return 返回新集合
     */
    public static <T extends Comparable <T>> Set <T> sort(Set <T> set) {
        Set <T> newSet = new TreeSet <>(Comparator.naturalOrder());
        newSet.addAll(set);
        return newSet;
    }

    /**
     * 对Set排序，set必须实现Comparable接口
     *
     * @param set  set
     * @param sort 升降序
     * @param <T>
     * @return 返回新集合
     */
    public static <T extends Comparable <T>> Set <T> sort(Set <T> set, final String sort) {
        Set <T> newSet;
        if (sort != null && sort.toLowerCase().equals(DESC)) {
            newSet = new TreeSet <>(Comparator.reverseOrder());
        } else {
            newSet = new TreeSet <>(Comparator.naturalOrder());
        }
        newSet.addAll(set);
        return newSet;
    }

    /**
     * 对list中的元素进行排序.
     *
     * @param list  排序集合
     * @param field 排序字段
     * @param sort  排序方式: SortList.DESC(降序) SortList.ASC(升序).
     * @return
     */
    public static <T> List <T> sort(List <T> list, final String field, final String sort) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        innerSort(list, field, sort);
        return list;
    }

    /**
     * 对list中的元素按fields和sorts进行排序,
     * fields[i]指定排序字段,sorts[i]指定排序方式.如果sorts[i]为空则默认按升序排列.
     *
     * @param list
     * @param fields
     * @param sorts
     * @return
     */
    public static <T> List <T> sort(List <T> list, String[] fields, String[] sorts) {
        if (fields != null && fields.length > 0) {
            for (int i = fields.length - 1; i >= 0; i--) {
                final String field = fields[i];
                String tmpSort = ASC;
                if (sorts != null && sorts.length > i && sorts[i] != null) {
                    tmpSort = sorts[i];
                }
                final String sort = tmpSort;
                innerSort(list, field, sort);
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private static void innerSort(List <?> list, String field, String sort) {
        list.sort((Comparator) (a, b) -> {
            int ret = 0;
            try {
                Field f = a.getClass().getDeclaredField(field);
                f.setAccessible(true);
                Class <?> type = f.getType();
                if (type == int.class) {
                    ret = Integer.compare(f.getInt(a), f.getInt(b));
                } else if (type == double.class) {
                    ret = Double.compare(f.getDouble(a), f.getDouble(b));
                } else if (type == long.class) {
                    ret = Long.compare(f.getLong(a), f.getLong(b));
                } else if (type == float.class) {
                    ret = Float.compare(f.getFloat(a), f.getFloat(b));
                } else if (type == Date.class) {
                    ret = ((Date) f.get(a)).compareTo((Date) f.get(b));
                } else if (isImplementsOf(type, Comparable.class)) {
                    ret = ((Comparable) f.get(a)).compareTo(f.get(b));
                } else {
                    ret = String.valueOf(f.get(a)).compareTo(String.valueOf(f.get(b)));
                }

            } catch (Exception e) {
                log.error("排序异常", e);
            }

            if (sort != null && sort.toLowerCase().equals(DESC)) {
                return -ret;
            } else {
                return ret;
            }
        });
    }

    /**
     * 默认按正序排列
     *
     * @param list
     * @param method
     * @return
     */
    public static List <?> sortByMethod(List <?> list, final String method) {
        return sortByMethod(list, method, null);
    }

    @SuppressWarnings("unchecked")
    public static List <?> sortByMethod(List <?> list, final String method, final String sort) {
        methodInnerSort(list, method, sort);
        return list;
    }

    public static List <?> sortByMethod(List <?> list, final String[] methods, final String sorts[]) {
        if (methods != null && methods.length > 0) {
            for (int i = methods.length - 1; i >= 0; i--) {
                final String method = methods[i];
                String tmpSort = ASC;
                if (sorts != null && sorts.length > i && sorts[i] != null) {
                    tmpSort = sorts[i];
                }
                final String sort = tmpSort;
                methodInnerSort(list, method, sort);
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private static void methodInnerSort(List <?> list, String method, String sort) {
        list.sort((Comparator) (a, b) -> {
            int ret = 0;
            try {
                Method m = a.getClass().getMethod(method, null);
                m.setAccessible(true);
                Class <?> type = m.getReturnType();
                if (type == int.class) {
                    ret = ((Integer) m.invoke(a, null)).compareTo((Integer) m.invoke(b, null));
                } else if (type == double.class) {
                    ret = ((Double) m.invoke(a, null)).compareTo((Double) m.invoke(b, null));
                } else if (type == long.class) {
                    ret = ((Long) m.invoke(a, null)).compareTo((Long) m.invoke(b, null));
                } else if (type == float.class) {
                    ret = ((Float) m.invoke(a, null)).compareTo((Float) m.invoke(b, null));
                } else if (type == Date.class) {
                    ret = ((Date) m.invoke(a, null)).compareTo((Date) m.invoke(b, null));
                } else if (isImplementsOf(type, Comparable.class)) {
                    ret = ((Comparable) m.invoke(a, null)).compareTo(m.invoke(b, null));
                } else {
                    ret = String.valueOf(m.invoke(a, null)).compareTo(String.valueOf(m.invoke(b, null)));
                }

            } catch (Exception e) {
                log.error("排序异常", e);
            }
            if (sort != null && sort.toLowerCase().equals(DESC)) {
                return -ret;
            } else {
                return ret;
            }
        });
    }

    /**
     * 判断对象实现的所有接口中是否包含szInterface
     *
     * @param clazz
     * @param szInterface
     * @return
     */
    public static boolean isImplementsOf(Class <?> clazz, Class <?> szInterface) {
        boolean flag = false;

        Class <?>[] face = clazz.getInterfaces();
        for (Class <?> c : face) {
            if (c == szInterface) {
                flag = true;
            } else {
                flag = isImplementsOf(c, szInterface);
            }
        }

        if (!flag && null != clazz.getSuperclass()) {
            return isImplementsOf(clazz.getSuperclass(), szInterface);
        }

        return flag;
    }
}
