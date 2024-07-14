package com.somecompany.transferservice.validator;

import java.util.Optional;

@FunctionalInterface
public interface ConditionValidator <T>  {
    Optional<String> validate(T t);
}
