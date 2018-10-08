package io.github.lingalone;

import io.github.lingalone.bean.TestBean;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created with IntelliJ IDEA.
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/10/8
 */

public class ClassLoaderTests {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        ClassLoaderTests classLoaderTests = new ClassLoaderTests();
//        try {
//            //查看当前系统类路径中包含的路径条目
//            System.out.println(System.getProperty("java.class.path"));
//            //调用加载当前类的类加载器（这里即为系统类加载器）加载TestBean
//            Class typeLoaded = Class.forName("io.github.lingalone.bean.TestBean");
//
//            //查看被加载的TestBean类型是被那个类加载器加载的
//            System.out.println(typeLoaded.getClassLoader());
//
//            typeLoaded = Class.forName("junit.runner.BaseTestRunner");
//            System.out.println(typeLoaded.getClassLoader());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        ClassLoader myClassLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name)
                    throws ClassNotFoundException {
                try {
                    String filename = name.substring(name.lastIndexOf(".") + 1)
                            + ".class";
                    InputStream is = getClass().getResourceAsStream(filename);
                    if (is == null) {
                        return super.loadClass(name);   // 递归调用父类加载器
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (Exception e) {
                    throw new ClassNotFoundException(name);
                }
            }
        };

        Object obj = myClassLoader.loadClass("io.github.lingalone.ClassLoaderTests")
                .newInstance();

        obj = myClassLoader.loadClass("io.github.lingalone.ClassLoaderTests")
                .newInstance();
        System.out.println(obj.getClass().getClassLoader());
        System.out.println(classLoaderTests.getClass().getClassLoader());
        System.out.println(obj instanceof io.github.lingalone.ClassLoaderTests);
        System.out.println(classLoaderTests instanceof io.github.lingalone.ClassLoaderTests);

        Class<?> aClass = Class.forName("io.github.lingalone.ClassLoaderTests");
        System.out.println(aClass.getClassLoader());

        URL[] extURLs = ((URLClassLoader) ClassLoader.getSystemClassLoader().getParent()).getURLs();
        for (int i = 0; i < extURLs.length; i++) {
            System.out.println(extURLs[i]);
        }


    }



}
