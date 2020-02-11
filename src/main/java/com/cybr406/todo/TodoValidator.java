package com.cybr406.todo;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class TodoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Todo.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {

        ValidationUtils.rejectIfEmptyOrWhitespace(
                e, "author", "author.required", "Author is a required field.");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                e, "details", "details.required", "Details is a required field.");
    }

}
