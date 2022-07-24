package org.cjoakim.cosmos.altgraph.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This is the entry-point to this Spring Web Application, as denoted by the
 * @SpringBootApplication annotation.
 *
 * Chris Joakim, Microsoft, July 2022
 */

@SpringBootApplication
@ComponentScan(basePackages = { "org.cjoakim.cosmos.altgraph" })
public class WebApplication {

    public static void main(String[] args) {

         SpringApplication.run(WebApplication.class, args);
    }
}
