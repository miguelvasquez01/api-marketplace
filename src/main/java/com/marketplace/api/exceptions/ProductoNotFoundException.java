package com.marketplace.api.exceptions;

public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(String codigo) {
        super("El producto con código " + codigo + " no ha sido encontrado");
    }
}
