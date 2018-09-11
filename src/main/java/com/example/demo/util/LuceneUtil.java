package com.example.demo.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.lang.reflect.Method;
import java.nio.file.Paths;

/**
 * 工具类
 *
 * @author AdminTC
 */
public class LuceneUtil {

    private static Directory directory;
    private static Version version;
    private static StandardAnalyzer standardAnalyzer;

    static {
        try {
            directory = FSDirectory.open(Paths.get("F:\\IndexDBDBDB"));
            version = Version.LUCENE_7_1_0;
            standardAnalyzer = new StandardAnalyzer();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Directory getDirectory() {
        return directory;
    }

    public static Version getVersion() {
        return version;
    }

    public static StandardAnalyzer getStandardAnalyzer() {
        return standardAnalyzer;
    }

    /**
     * 不让外界new该帮助类(私有化)
     */
    private LuceneUtil() {
    }

    /**
     * 将JavaBean转成Document对象
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Document javabean2document(Object obj) throws Exception {
        //创建Document对象
        Document document = new Document();
        //获取obj引用的对象字节码
        Class clazz = obj.getClass();
        //通过对象字节码获取私有的属性
        java.lang.reflect.Field[] reflectFields = clazz.getDeclaredFields();
        //迭代
        for (java.lang.reflect.Field reflectField : reflectFields) {
            //强力反射
            reflectField.setAccessible(true);
            //获取属性名，id/age/content
            String name = reflectField.getName();
            //人工拼接方法名
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            //获取方法，例如：getId()/getAge()/getContent()
            Method method = clazz.getMethod(methodName, null);
            //执行方法
            String value = method.invoke(obj, null).toString();
            //加入到Document对象中去，这时javabean的属性与document对象的属性相同
            document.add(new Field(name, value, TextField.TYPE_STORED));
        }
        //返回document对象
        return document;
    }

    /**
     * 将Document对象转成JavaBean对象
     *
     * @param document
     * @param clazz
     * @return
     * @throws Exception
     */
    public static Object document2javabean(Document document, Class clazz) throws Exception {
        Object obj = clazz.newInstance();
        java.lang.reflect.Field[] reflectFields = clazz.getDeclaredFields();
        for (java.lang.reflect.Field reflectField : reflectFields) {
            reflectField.setAccessible(true);
            // 获取相应的字段列表//id/age/content
            String name = reflectField.getName();
            // 获取字段对应的值
            String value = document.get(name);
            //封装javabean对应的属性中去，通过setXxx()方法
            BeanUtils.setProperty(obj, name, value);
        }
        return obj;
    }


}




