package com.xxl.springsecurity.utils.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.HashSet;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
     HashSet<Integer> hashSet=new HashSet<>();

    //初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] values = constraintAnnotation.values();
        for (int value : values) {
            hashSet.add(value);
        }
    }
    //判断是否校验成功

    /**
     *
     * @param value 需要校验的值
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {

        return hashSet.contains(value);
    }
}
