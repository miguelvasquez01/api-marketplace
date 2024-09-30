package com.marketplace.api.entities;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Vendedor {

    private String nombre;
    private String apellidos;
    private String cedula;
    private String direccion;
    private ArrayList<Producto> productos;
    private ArrayList<Vendedor> contactos;
    private ArrayList<Resena> reseñas;
    private ArrayList<Vendedor> solicitudesDeContacto;
    private Muro muro;
}
