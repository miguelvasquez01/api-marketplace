package com.marketplace.api;

import com.marketplace.api.entities.Producto;
import com.marketplace.api.entities.Vendedor;
import com.marketplace.api.services.VendedorService;
import com.marketplace.api.services.XMLSerializador;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
