package com.marketplace.api.exceptions;

public class ArchivoNotFoundException extends RuntimeException {

    public ArchivoNotFoundException(String mensaje) {
        super("El archivo " + mensaje + " no ha sido encontrado");
    }
}
