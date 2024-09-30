package com.marketplace.api.services;

import com.marketplace.api.entities.Vendedor;
import com.marketplace.api.exceptions.ArchivoNotFoundException;
import com.marketplace.api.exceptions.VendedorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private XMLSerializador xmlSerializador;

    private final String ruta = "archivosSerializados/vendedores.xml";

    public Vendedor autenticarVendedor(LoginRequest loginRequest) {

        String nombre = loginRequest.getNombre();
        String cedula = loginRequest.getCedula();
        List<Vendedor> vendedores = xmlSerializador.deserializarLista(ruta);
        Optional<Vendedor> vendedor = vendedores.stream()
                .filter(v -> v.getNombre().equals(nombre) && v.getCedula().equals(cedula))
                .findFirst();

        return vendedor.orElseThrow(() -> new VendedorNotFoundException(cedula));

    }
}
