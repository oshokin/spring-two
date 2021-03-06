package ru.oshokin.store.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.oshokin.store.services.ShoppingCartService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

    @Before("execution(* ru.oshokin.store.services.ShoppingCartService.*(..))")
    public void beforeAnyCartMethodInvokation(JoinPoint point) {
        log.info("Cart method was invoked: {}.", point.getSignature().getName());
    }

    @Around("execution(public void ru.oshokin.store.services.ShoppingCartService.addToCart(javax.servlet.http.HttpSession, Long))" +
            "|| execution(public void ru.oshokin.store.services.ShoppingCartService.removeFromCart(javax.servlet.http.HttpSession, Long))" +
            "|| execution(public void ru.oshokin.store.services.ShoppingCartService.setProductCount(javax.servlet.http.HttpSession, Long, Long))")
    public void AfterCartQuantityChanged(ProceedingJoinPoint pjp) {
        try {
            Object[] methodArgs = pjp.getArgs();
            if (methodArgs.length < 1) {
                log.info("А куда подевался идентификатор сессии? Та-та-та по ягодицам!!!");
                return;
            }
            log.info("Procedemos con este metodo! A ver!");
            pjp.proceed();
            log.info("Y ahora vamos a recalcular esa mierda");
            ((ShoppingCartService) pjp.getThis()).recalculate((HttpSession) methodArgs[0]);
        } catch (Throwable t) {
            log.info("Никогда такого не было и вот опять... {}.", t.getMessage());
        }
    }

}
