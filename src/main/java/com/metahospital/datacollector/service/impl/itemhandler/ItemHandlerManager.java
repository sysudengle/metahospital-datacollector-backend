package com.metahospital.datacollector.service.impl.itemhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ItemHandlerManager implements BeanPostProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(ItemHandlerManager.class);

    private final Map<Integer, IItemHandler> type2RuleHandler = new HashMap<>();

    public IItemHandler getItemHandler(int itemType) {
        return type2RuleHandler.get(itemType);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        boolean assignableFrom = IItemHandler.class.isAssignableFrom(bean.getClass());
        if (assignableFrom) {
            IItemHandler handler = (IItemHandler) bean;
            LOGGER.debug("add ItemHandler, type:{}, handler:{}", handler.getItemType(), bean.getClass());
            type2RuleHandler.put(handler.getItemType(), handler);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
