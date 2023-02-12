package com.topjava.restaurant_voting.util;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@UtilityClass
public class ValidationUtils {

    public static <T> T assertExistence(Optional<T> opt, String exceptionMassage) throws NotExistException {
        if (opt.isEmpty()) {
            throw new NotExistException(exceptionMassage);
        }
        return opt.get();
    }

    public static <T> T assertExistence(T entity, String exceptionMassage) throws NotExistException {
        if (entity == null) {
            throw new NotExistException(exceptionMassage);
        }
        return entity;
    }

    public static <T> void assertNotExistence(Optional<T> opt, String exceptionMassage) throws AlreadyExistException {
        if (opt.isPresent()) {
            throw new AlreadyExistException(exceptionMassage);
        }
    }

}
