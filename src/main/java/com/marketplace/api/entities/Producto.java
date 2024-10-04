package com.marketplace.api.entities;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class Producto implements Serializable {

    private String nombre;
    private int codigo;
    private String imagen;
    private String categoria;
    private double precio;
    private EstadoProducto estado;
    private LocalDate fechaPublicacion;
    private List<Comentario> comentarios;
    private int meGustas;
}
