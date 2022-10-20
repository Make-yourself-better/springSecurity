package com.xxl.springsecurity.utils.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//自定义校验
//        1）、编写一个自定义的校验注解 ListValue
//        2）、编写一个自定义的校验器  ListValueConstraintValidator
//        3）、关联自定义的校验器和自定义的校验注解
//@Documented
//@Constraint(validatedBy = {ListValueConstraintValidator.class,【可以使用多个不同的校验器进行校验】)//指定校验器
////使用的位置
//@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
//@Retention(RetentionPolicy.RUNTIME)//运行时机
//   在ConstraintValidator使用ctrl+h 查看校验器的 实现


@Documented
@Constraint(validatedBy = {ListValueConstraintValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ListValue {
    String message() default "{com.xxl.springsecurity.utils.valid.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int[] values()default { };

}
