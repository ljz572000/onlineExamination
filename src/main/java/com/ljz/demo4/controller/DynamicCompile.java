package com.ljz.demo4.controller;


import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Locale.CHINESE;

public class DynamicCompile {

    private static Map<String, JavaFileObject> fileObjects = new ConcurrentHashMap<>();
    public void compile(String code) throws FileNotFoundException {




//        String code = "import java.sql.*; \n public class Test {\n" + "\tpublic static void hello(String name,String name2){\n"
//                + "\t\tSystem.out.println(\"hello \"+name+name2);\n" + "\t}\n" + "}";

        //ToolProvider.getSystemJavaCompiler(); 调用系统编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //DiagnosticCollector   提供一种在列表中收集诊断的简便方法。
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        //File manager for tools operating on Java&trade;
        //把诊断收集器作为变量加入编译器中
        //文件管理器将使用给定的诊断侦听器来生成任何非致命的诊断程序。
        //将使用适当的例外情况发出致命错误信号。
        JavaFileManager javaFileManager =
                new MyJavaFileManager(compiler.getStandardFileManager(collector, CHINESE, null));



        //将给定的正则表达式编译为模式。
//        此私有构造函数用于创建所有模式。
        // 首先找到类名
        Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s*");

        Matcher matcher = CLASS_PATTERN.matcher(code);
        //java.util.regex.Matcher[pattern=class\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\s* region=0,122 lastmatch=]
        String cls;
        // 声明类名



        if (matcher.find()) {
            cls = matcher.group(1);
            //  打印输出  Test
        } else {
            throw new IllegalArgumentException("No such class name in " + code);
        }


        //  上面程序找到类名



        // 将类名和代码输入MyJavaFileObject
        // MyJavaFileObject
        JavaFileObject javaFileObject = new MyJavaFileObject(cls, code);
        // 创建 .java 文件




        List<String> options = new ArrayList<>();
        options.add("-target");
        options.add("1.8");
        // options -target <发行版>              生成特定 VM 版本的类文件
        //在其他实例都已经准备完毕后, 构建编译任务.
        Boolean result = compiler
                .getTask(null,
                        javaFileManager,
                        collector,
                        options,
                        null,
                        Arrays.asList(javaFileObject))
                .call();



        //通过Java的类加载机制（ClassLoader）来动态加载某个class文件到内存当中的，
        // 从而只有class文件被载入到了内存之后，才能被其它class所引用。
        // 所以ClassLoader就是用来动态加载class文件到内存当中用的。
        // https://blog.csdn.net/xyang81/article/details/7292380
        ClassLoader classloader = new MyClassLoader();

        //Class是一个可参数化的类，因此您可以使用语法Class <T>，其中T是一个类型。
        // 通过编写Class <？>，您将声明一个可以是任何类型的Class对象（？是通配符）。
        // 类类型是包含有关类的元信息的类型。
        //
        //通过指定他的特定类型来引用泛型类型总是很好的做法，
        // 通过使用Class <？>你尊重这种做法（你知道Class可以参数化）
        // 但是你并没有限制你的参数有一个 具体类型。

        Class<?> clazz = null;

        try {
            clazz = classloader.loadClass(cls);
            // clazz.toString()  Test
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Method method = null;




        try {
            //后面代表源程序中的变量String.class
            method = clazz.getMethod("hello");

            // 类动态加载类创建hello 方法
            // method.toString()  public static void Test.hello(java.lang.String)
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }



//开始编译
        PrintStream ps=
                new PrintStream(
                        new BufferedOutputStream(new FileOutputStream("output.txt"))
                );//字节打印流
        System.setOut(ps);//改变输出到output.txt文件
        System.setErr(ps);//改变输出到output.txt文件


        try {

            method.invoke(clazz.newInstance());

        } catch (IllegalAccessException e) {

            e.printStackTrace();
        } catch (InvocationTargetException e) {

            e.printStackTrace();
        } catch (InstantiationException e) {

            e.printStackTrace();
        }

        //将输出重新输出到eclipse控制台
        //  运行结束
        // 关闭PrintStream
        if(ps!=null)
            ps.close();
        System.setOut(
                new PrintStream
                        (new BufferedOutputStream
                                (new FileOutputStream(FileDescriptor.out))
                                ,true));
        System.setErr(new PrintStream
                (new BufferedOutputStream
                        (new FileOutputStream(FileDescriptor.out))
                        ,true));


    }

    public static class MyClassLoader extends ClassLoader {

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            JavaFileObject fileObject = fileObjects.get(name);
//            System.out.println(fileObjects);
//            {Test=com.company.ljz.DynamicCompile$MyJavaFileObject[String:///Test.class]}
            if (fileObject != null) {
                byte[] bytes = ((MyJavaFileObject) fileObject).getCompiledBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }


            try {
                return ClassLoader.getSystemClassLoader().loadClass(name);
            } catch (Exception e) {

                return super.findClass(name);
            }


        }
    }


    //为JavaFileObject中的大多数方法提供简单的实现。
    //此类设计为子类，并用作JavaFileObject实现的基础。
    // 只要遵守JavaFileObject的常规协定，
    // 子类就可以覆盖此类的任何方法的实现和规范。
    public static class MyJavaFileObject extends SimpleJavaFileObject {
        private String source;
        private ByteArrayOutputStream outPutStream;

        public MyJavaFileObject(String name, String source) {
//           MyJavaFileObject    MyJavaFileObject(URI uri, Kind kind)
            // "String:///"  Test.java
            // Kind.SOURCE.extension  =====>>>   .java
            // SOURCE
            // 创建Test.java 文件
            super(URI.create("String:///" + name + Kind.SOURCE.extension), Kind.SOURCE);
            this.source = source;
        }

        public MyJavaFileObject(String name, Kind kind) {
            super(URI.create("String:///" + name + kind.extension), kind);
            source = null;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            if (source == null) {
                throw new IllegalArgumentException("source == null");
            }
            return source;
        }

        public OutputStream openOutputStream() throws IOException {
            outPutStream = new ByteArrayOutputStream();
            return outPutStream;
        }

        public byte[] getCompiledBytes() {
            return outPutStream.toByteArray();
        }
    }

    public static class MyJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        protected MyJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind)
                throws IOException {
            JavaFileObject javaFileObject = fileObjects.get(className);
            if (javaFileObject == null) {
                super.getJavaFileForInput(location, className, kind);
            }
            return javaFileObject;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location,
                                                   String qualifiedClassName,
                                                   JavaFileObject.Kind kind,
                                                   FileObject sibling) throws IOException {
            JavaFileObject javaFileObject = new MyJavaFileObject(qualifiedClassName, kind);
            fileObjects.put(qualifiedClassName, javaFileObject);
            return javaFileObject;
        }
    }




}
