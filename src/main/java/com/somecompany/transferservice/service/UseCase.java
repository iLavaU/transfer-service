package com.somecompany.transferservice.service;

@FunctionalInterface
public interface UseCase<T, V> {
    V execute(T input);
}
