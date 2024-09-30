package com.marketplace.api.exceptions;

public class VendedorDuplicadoException extends RuntimeException {

    public VendedorDuplicadoException(String documento) {
        super("El vendedor con documento " + documento + " ya existe en el sistema");
    }
}
