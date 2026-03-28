package com.javaee2026.citruschat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Punto de entrada principal de la aplicación Citruschat. */
@SpringBootApplication
public class CitruschatApplication {

	/**
	 * Inicia la aplicación Spring Boot.
	 *
	 * @param args
	 *            argumentos de línea de comandos
	 */
	public static void main(final String[] args) {
		SpringApplication.run(CitruschatApplication.class, args);
	}

	/**
	 * Método de ejemplo para tratar de romper el stylecheck
	 */
	public void holiwixdjdfsklja() {
		var a = 1;
		var b = 2;
		String c = "hola";
		System.out.println(a + b + c);
		return;
	}
}
