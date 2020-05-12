package com.enough.common.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @program: app-parent
 * @description:
 * @author: lidong
 * @create: 2019/04/16
 */
public class ClassUtils {

    /**
     * 从包package中获取所有的Class
     *
     * @param pagekageName 包名
     * @param recursive    是否递归
     * @return a {@link Set} object.
     */
    public static Set <Class <?>> getClasses(String pagekageName, boolean recursive) {
        // 第一个class类的集合
        Set <Class <?>> classes = new LinkedHashSet <Class <?>>();
        // 获取包的名字 并进行替换
        String packageName = pagekageName;
        String packageDirName = packageName.replace('.', '/');
        Enumeration <URL> dirs;
        try {
            dirs = ClassLoaderUtil.contextLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration <JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName a {@link String} object.
     * @param packagePath a {@link String} object.
     * @param recursive   a boolean.
     * @param classes     a {@link Set} object.
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set <Class <?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取到类的接口和父类
     *
     * @param clazz
     * @param classList
     */
    public static void getClassSuperClassAndInterface(Class clazz, List <Class <?>> classList) {
        if (ArrayUtils.isNotEmpty(clazz.getInterfaces())) {
            for (Class <?> in : clazz.getInterfaces()) {
                classList.add(in);
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass().getClassLoader() != null) {
            classList.add(clazz.getSuperclass());
            getClassSuperClassAndInterface(clazz.getSuperclass(), classList);
        }
    }

    /**
     * 检查某个类是否是clazz的子类
     *
     * @param clazz
     * @param childClass
     * @return
     */
    public static boolean checkChildClassInstanceofClazz(Class clazz, Class childClass) {
        if (clazz.isAssignableFrom(childClass)) {
            return true;
        }
        if (childClass.getSuperclass() != null) {
            return checkChildClassInstanceofClazz(clazz, childClass.getSuperclass());
        } else {
            return false;
        }
    }

}
