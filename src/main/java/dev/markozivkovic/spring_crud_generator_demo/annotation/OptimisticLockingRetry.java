package dev.markozivkovic.spring_crud_generator_demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.persistence.OptimisticLockException;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Retryable(
    retryFor = {
        OptimisticLockException.class,
        ObjectOptimisticLockingFailureException.class
    },
    maxAttempts = 5,
    backoff = @Backoff(
        delay      = 100,
        maxDelay   = 1000,
        multiplier = 2
    )
)
public @interface OptimisticLockingRetry {

}