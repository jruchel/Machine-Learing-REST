package com.jruchel.mlrest.validation;


import com.jruchel.mlrest.config.ApplicationContextHolder;
import com.jruchel.mlrest.config.Properties;
import com.jruchel.mlrest.exceptions.ValidationException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class Validator<A extends Annotation, E> implements ConstraintValidator<A, E> {

    protected Properties properties;

    @Override
    public void initialize(A constraintAnnotation) {
        properties = ApplicationContextHolder.getContext().getBean(Properties.class);
    }

    @SneakyThrows
    @Override
    public boolean isValid(E value, ConstraintValidatorContext context) {
        StringBuilder error = new StringBuilder();
        boolean allValid = true;
        Method[] methods = Arrays.stream(getClass().getDeclaredMethods()).filter(m -> m.getName().contains("Constraint_")).toArray(Method[]::new);
        for (Method m : methods) {
            try {
                m.setAccessible(true);
                String result = String.valueOf(m.invoke(this, value));
                if (!result.equals("true")) {
                    allValid = false;
                    error.append(result).append("\n");
                }
                m.setAccessible(false);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return false;
            }

        }

        if (!allValid) throw new ValidationException(error.toString());

        return true;
    }
}
