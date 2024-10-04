package com.marketplace.api.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Resena implements Serializable {

    private Vendedor autor;
    private int calificacion;
    private String comentario;
    private LocalDateTime fecha;
}
