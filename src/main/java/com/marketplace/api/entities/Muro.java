package com.marketplace.api.entities;

import java.io.Serializable;
import java.util.List;

public class Muro   implements Serializable {

    private String mensaje;
    private List<Comentario> comentarios;
    private int meGustas;
}
