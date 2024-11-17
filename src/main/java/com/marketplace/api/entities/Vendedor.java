package com.marketplace.api.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Vendedor implements Serializable {

    private String nombre;
    private String apellidos;
    private String cedula;
    private String direccion;
    private List<Producto> productos;
    private ArrayList<Vendedor> contactos;
    private ArrayList<Resena> reseñas;
    private ArrayList<Vendedor> solicitudesDeContacto;
    private Muro muro;

    public Vendedor() { //Los vendedores se crean en el cliente
    }
}
