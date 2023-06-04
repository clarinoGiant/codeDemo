package com.test.spring;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ZhouyuApplicationContext {

    public static final String SCOPE_SINGLETON = "singleton";

    private Class configClass;

    public Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    public Map<String, Object> singleObjectMap = new HashMap<>();


    public ZhouyuApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan annotationAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);

            String path = annotationAnnotation.value();  //  扫描com.test.service
            path = path.replace(".", "/");  // 得到相对路径

            ClassLoader classLoader = ZhouyuApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);   // 得到绝对路径
            File file = new File(resource.getFile());

//            System.out.println(file);

            // 1. 扫描得到beanDefinition定义
            if (file.isDirectory()) {
                File[] files = file.listFiles();

                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    if (fileName.endsWith(".class")) {
                        // 通过反射得到类，进一步获取到是否有component注解
                        String className = fileName.substring(fileName.indexOf("com\\test"), fileName.indexOf(".class"));
                        className = className.replace("\\", ".");  // com.test.service.App
                        try {
                            Class<?> clazz = classLoader.loadClass(className);

                            if (clazz.isAnnotationPresent(Component.class)) {
                                Component component = clazz.getAnnotation(Component.class);
                                String beanName = component.value();

                                // 建立beanDefinition
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);

                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    beanDefinition.setScope(clazz.getAnnotation(Scope.class).value());
                                } else {
                                    beanDefinition.setScope(SCOPE_SINGLETON);
                                }

                                beanDefinitionMap.put(beanName, beanDefinition);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            // 进创建单例Bean
            for (String beanName : beanDefinitionMap.keySet()) {
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

                if (beanDefinition.getScope().equals(SCOPE_SINGLETON)) {
                    Object bean = createBean(beanName, beanDefinition);
                    singleObjectMap.put(beanName, bean);
                }
            }
        }
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        if (beanDefinition == null) {
            throw new NullPointerException();
        }

        String scope = beanDefinition.getScope();
        if (scope.equals(SCOPE_SINGLETON)) {
            Object bean = singleObjectMap.get(beanName);
            if (bean == null) {
                bean = createBean(beanName, beanDefinition);
                singleObjectMap.put(beanName, bean);
            }
            return bean;
        } else {
            return createBean(beanName, beanDefinition);
        }
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {

        Class clazz = beanDefinition.getType();

        // 反射，依赖无参构造方法的存在
        try {
            Object instance = clazz.getConstructor().newInstance();
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }
}
