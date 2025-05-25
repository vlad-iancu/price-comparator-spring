package com.example.price_comparator;

import java.util.Map;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SpringBootApplication
public class PriceComparatorApplication {

	public static void main(String[] args) {
		
		var context = SpringApplication.run(PriceComparatorApplication.class, args);
		Map<String, Object> adviceBeans = context.getBeansWithAnnotation(ExceptionHandler.class);

        System.out.println("🔎 Registered @ControllerAdvice beans:");
        adviceBeans.forEach((name, bean) ->
            System.out.println(" - " + name + " (" + bean.getClass().getName() + ")")
        );
	}

}
