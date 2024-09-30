package com.marketplace.api.exceptions;

public class ProductoDuplicadoException extends RuntimeException {

    public ProductoDuplicadoException(String codigo) {
        super("El producto con código " + codigo + " ya existe en el sistema");
    }
}
