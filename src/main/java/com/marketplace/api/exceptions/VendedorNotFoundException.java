package com.marketplace.api.exceptions;

public class VendedorNotFoundException extends RuntimeException {

    public VendedorNotFoundException(String documento) {
        super("El vendedor con documento " + documento + " no ha sido encontrado");
    }
}
