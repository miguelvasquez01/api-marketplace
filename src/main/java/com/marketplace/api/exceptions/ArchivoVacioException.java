package com.marketplace.api.exceptions;

public class ArchivoVacioException extends RuntimeException {

    public ArchivoVacioException(String archivo) {
        super("El archivo " + archivo + " esta vacío o malformado");
    }
}
