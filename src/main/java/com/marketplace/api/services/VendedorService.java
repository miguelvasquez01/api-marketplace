package com.marketplace.api.services;

import com.marketplace.api.entities.Vendedor;
import com.marketplace.api.exceptions.ArchivoNotFoundException;
import com.marketplace.api.exceptions.ArchivoVacioException;
import com.marketplace.api.exceptions.VendedorDuplicadoException;
import com.marketplace.api.exceptions.VendedorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VendedorService {

    @Autowired
    private XMLSerializador xmlSerializador = new XMLSerializador();

    private final String ruta = "archivosSerializados/vendedores.xml";
    private static final Logger LOGGER = Logger.getLogger(VendedorService.class.getName());

    static {
        LoggerConfigService.getInstance().configureLogger(LOGGER);
    }

    public Vendedor getVendedorById(String documento) {
        LOGGER.log(Level.INFO, "Obteniendo vendedor por documento: " + documento);

        List<Vendedor> vendedores = xmlSerializador.deserializarLista(ruta);
        Optional<Vendedor> vendedor = vendedores.stream()
                .filter(v -> v.getCedula().equals(documento)).findFirst();

        if (vendedor.isPresent()) {
            LOGGER.log(Level.INFO, "Vendedor encontrado: " + vendedor.get().getNombre());
        } else {
            LOGGER.log(Level.WARNING, "Vendedor no encontrado con documento: " + documento);
        }

        return vendedor.orElseThrow(() -> new VendedorNotFoundException(documento));
    }

    public List<Vendedor> getAllVendedores() {
        LOGGER.log(Level.INFO, "Obteniendo todos los vendedores");

        List<Vendedor> vendedores = xmlSerializador.deserializarLista(ruta);
        if (vendedores.isEmpty()) {
            LOGGER.log(Level.WARNING, "La lista de vendedores está vacía");
            throw new ArchivoVacioException("El archivo " + ruta + " está vacío");
        }

        LOGGER.log(Level.INFO, "Lista de vendedores obtenida correctamente");
        return vendedores;
    }

    public void guardarVendedor(Vendedor vendedor) {
        LOGGER.log(Level.INFO, "Guardando vendedor: " + vendedor.getNombre());

        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                LOGGER.log(Level.INFO, "Archivo no encontrado, creando nuevo archivo en: " + ruta);
                archivo.createNewFile();
            }

            List<Vendedor> vendedores = new ArrayList<>();
            if (archivo.length() > 0) {
                vendedores = xmlSerializador.deserializarLista(ruta);
                LOGGER.log(Level.INFO, "Archivo deserializado correctamente");
            }

            boolean documentoDuplicado = vendedores.stream()
                    .anyMatch(v -> v.getCedula().equals(vendedor.getCedula()));
            if (documentoDuplicado) {
                LOGGER.log(Level.WARNING, "Documento duplicado: " + vendedor.getCedula());
                throw new VendedorDuplicadoException(vendedor.getCedula());
            }

            vendedores.add(vendedor);
            xmlSerializador.serializarLista(ruta, vendedores);
            LOGGER.log(Level.INFO, "Vendedor guardado correctamente: " + vendedor.getNombre());

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al manipular el archivo: " + ruta, e);
            throw new RuntimeException("Error al guardar el vendedor", e);
        }
    }

    public boolean actualizarVendedor(String documento, Vendedor nuevoVendedor) {
        LOGGER.log(Level.INFO, "Actualizando vendedor con documento: " + documento);

        List<Vendedor> vendedores = xmlSerializador.deserializarLista(ruta);

        for (int i = 0; i < vendedores.size(); i++) {
            Vendedor vendedor = vendedores.get(i);

            if (vendedor.getCedula().equals(documento)) {
                vendedor.setNombre(nuevoVendedor.getNombre());
                vendedor.setApellidos(nuevoVendedor.getApellidos());
                vendedor.setDireccion(nuevoVendedor.getDireccion());
                vendedor.setProductos(nuevoVendedor.getProductos());

                xmlSerializador.serializarLista(ruta, vendedores);
                LOGGER.log(Level.INFO, "Vendedor actualizado correctamente: " + vendedor.getNombre());
                return true;
            }
        }

        LOGGER.log(Level.WARNING, "Vendedor no encontrado para actualizar con documento: " + documento);
        throw new VendedorNotFoundException(documento);
    }

    public boolean eliminarVendedor(String documento) {
        LOGGER.log(Level.INFO, "Eliminando vendedor con documento: " + documento);

        List<Vendedor> vendedores = xmlSerializador.deserializarLista(ruta);

        for (Vendedor vendedor : vendedores) {
            if (vendedor.getCedula().equals(documento)) {
                vendedores.remove(vendedor);
                xmlSerializador.serializarLista(ruta, vendedores);
                LOGGER.log(Level.INFO, "Vendedor eliminado correctamente: " + vendedor.getNombre());
                return true;
            }
        }

        LOGGER.log(Level.WARNING, "Vendedor no encontrado para eliminar con documento: " + documento);
        throw new VendedorNotFoundException(documento);
    }
}
