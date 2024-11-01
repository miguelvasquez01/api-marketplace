package com.marketplace.api.services;

import com.marketplace.api.entities.Vendedor;
import com.marketplace.api.exceptions.ArchivoNotFoundException;
import com.marketplace.api.exceptions.VendedorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AuthService {

    @Autowired
    private XMLSerializador xmlSerializador;

    private final String ruta = "archivosSerializados/vendedores.xml";
    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());

    static {
        LoggerConfigService.getInstance().configureLogger(LOGGER);
    }

    public Vendedor autenticarVendedor(LoginRequest loginRequest) {

        String nombre = loginRequest.getNombre();
        String cedula = loginRequest.getCedula();
        LOGGER.log(Level.INFO,"Autenticando vendedor");
        List<Vendedor> vendedores = xmlSerializador.deserializarLista(ruta);
        LOGGER.log(Level.INFO,"Archivo deserializado correctamente");

        Optional<Vendedor> vendedor = vendedores.stream()
                .filter(v -> v.getNombre().equals(nombre) && v.getCedula().equals(cedula))
                .findFirst();

        LOGGER.log(Level.INFO,"Vendedor autenticado exitosamente");

        return vendedor.orElseThrow(() -> new VendedorNotFoundException(cedula));

    }
}
