package com.codetutr.config.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;

import com.codetutr.populatorHelper.AbstractPopulatingConverter;
import com.codetutr.populatorHelper.Populator;

public class AppConfig_Converter implements BeanDefinitionRegistryPostProcessor{
 
    public static final String TARGET_CLASS = "targetClass";
    public static final String POPULATORS = "populators";
    public static final String POPULATE_METHOD = "populate";
    public static final int TARGET_PARAM = 1;
 
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Map<String, List<BeanDefinition>> converterNameToPopulators =
                Stream.of(beanDefinitionRegistry.getBeanDefinitionNames())
                        .map(beanDefinitionRegistry::getBeanDefinition)
                        .filter(getBeanDefinition -> null != getBeanDefinition.getBeanClassName())
                        .filter(this::doesBeanImplementPopulatorInterface)
                        .filter(this::doesBeanClassHaveConverterAnnotation)
                        .collect(Collectors.groupingBy(this::getConverterName));
 
        converterNameToPopulators.forEach((converterName, populators) ->
                createAndRegisterConverter(converterName, populators, beanDefinitionRegistry));
    }
 
    private boolean doesBeanImplementPopulatorInterface(BeanDefinition beanDefinition) {
        Class<?> beanClass = getBeanClass(beanDefinition);
        return Stream.of(beanClass.getInterfaces()).anyMatch(Populator.class::equals);
    }
 
    private boolean doesBeanClassHaveConverterAnnotation(BeanDefinition beanDefinition) {
        Class<?> beanClass = getBeanClass(beanDefinition);
        return Stream.of(beanClass.getAnnotations()).map(Annotation::annotationType).anyMatch(ConverterName.class::equals);
    }
 
    private String getConverterName(BeanDefinition populatorBeanDefinition) {
    	ConverterName converterAnnotation = getBeanClass(populatorBeanDefinition).getAnnotation(ConverterName.class);
        return converterAnnotation.value();
    }
 
    private void createAndRegisterConverter(String converterName, List<BeanDefinition> populators, BeanDefinitionRegistry beanDefinitionRegistry) {
        BeanDefinition converter = createConverterBeanDefinition(populators);
        beanDefinitionRegistry.registerBeanDefinition(converterName, converter);
    }
 
    private BeanDefinition createConverterBeanDefinition(List<BeanDefinition> populators) {
        GenericBeanDefinition converterBeanDefinition = new GenericBeanDefinition();
        converterBeanDefinition.setBeanClass(AbstractPopulatingConverter.class);
        converterBeanDefinition.setPropertyValues(createBeanProperties(populators));
        return converterBeanDefinition;
    }
 
    private MutablePropertyValues createBeanProperties(List<BeanDefinition> populators) {
        MutablePropertyValues properties = new MutablePropertyValues();
        properties.addPropertyValue(POPULATORS, createPopulatorsList(populators));
        properties.addPropertyValue(TARGET_CLASS, getPopulatorTargetClass(populators.get(0)));
        return properties;
    }
 
    private ManagedList<BeanDefinition> createPopulatorsList(List<BeanDefinition> populators) {
        ManagedList<BeanDefinition> populatorReferences = new ManagedList<>();
        populatorReferences.setElementTypeName(Populator.class.getName());
        populatorReferences.addAll(populators);
        return populatorReferences;
    }
 
    private Class<?> getPopulatorTargetClass(BeanDefinition populatorBeanDefinition) {
        Method[] declaredMethods = getBeanClass(populatorBeanDefinition).getDeclaredMethods();
        return Stream.of(declaredMethods)
                .filter(method -> POPULATE_METHOD.equals(method.getName()))
                .findFirst()
                .map(Method::getParameters)
                .map(parameters -> parameters[TARGET_PARAM])
                .map(Parameter::getType)
                .get();
    }
 
    private Class<?> getBeanClass(BeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        return toClass(className);
    }
 
    private  Class<?> toClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
 
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // no operation
    }
 
}
