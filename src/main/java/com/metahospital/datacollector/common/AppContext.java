package com.metahospital.datacollector.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.util.Collection;

/**
 * 持有Spring进程的ApplicationContext，可用于非Spring Bean中获取Spring Bean的实例。
 */
public class AppContext implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AppContext.context = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return context.getBean(name, requiredType);
    }

    public static <T> T getBeanByClassName(String className, Class<T> requiredType) {
        final String shortName = ClassUtils.getShortName(className);
        final String beanName = Introspector.decapitalize(shortName);
        if (!context.containsBean(beanName)) {
            return null;
        }
        return getBean(beanName, requiredType);
    }

    public static <T> Collection<T> getBeansOfType(Class<T> type) {
        return context.getBeansOfType(type).values();
    }
}
