package com.defiend.definedfegin;

import com.defiend.definedfegin.service.TestFeign;
import com.defiend.definedfegin.util.FeignRegistar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import javax.swing.*;

@SpringBootApplication
@Import(FeignRegistar.class)
public class DefinedFeginApplication {

    public static void main(String[] args) {
       ConfigurableApplicationContext context = SpringApplication.run(DefinedFeginApplication.class, args);
        TestFeign testFeign = context.getBean(TestFeign.class);
        System.out.println(testFeign.getSomeThing());
    }

}
