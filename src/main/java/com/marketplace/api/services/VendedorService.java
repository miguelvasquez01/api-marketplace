package com.marketplace.api.services;

import com.marketplace.api.entities.Vendedor;
import com.marketplace.api.exceptions.ArchivoNotFoundException;
import com.marketplace.api.exceptions.ArchivoVacioException;
import com.marketplace.api.exceptions.VendedorDuplicadoException;
import com.marketplace.api.exceptions.VendedorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VendedorService {

    @Autowired
    private XMLSerializador xmlSerializador;

    private final String ruta = "archivosSerializados/vendedores.xml";

    public Vendedor getVendedorById(String documento) {
        List<Vendedor> vendedores = xmlSerializador.deserializarLista(ruta);
        Optional<Vendedor> vendedor = vendedores.stream()
                .filter(v -> v.getCedula().equals(documento)).findFirst();
        return vendedor.orElseThrow(() -> new VendedorNotFoundException(documento));
    }

    public List<Vendedor> getAllVendedores() {

        return xmlSerializador.deserializarLista(ruta);
    }

    public void guardarVendedor(Vendedor vendedor) {
        try {

            File archivo = new File(ruta);
            if(!archivo.exists()){
                archivo.createNewFile();
            }

            List<Vendedor> vendedores = new ArrayList<>();
            if(archivo.length() > 0) {
                vendedores = xmlSerializador.deserializarLista(ruta);
            }

            boolean documentoDuplicado = vendedores.stream()
                    .anyMatch(v -> v.getCedula().equals(vendedor.getCedula()));
            if(documentoDuplicado) {
                throw new VendedorDuplicadoException(vendedor.getCedula());
            }
            vendedores.add(vendedor);

            xmlSerializador.serializarLista(ruta, vendedores);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean actualizarVendedor(String documento, Vendedor nuevoVendedor) {

        List<Vendedor> vendedores = xmlSerializador.deserializarLista(ruta);

        for (int i = 0; i < vendedores.size(); i++) {
            Vendedor vendedor = vendedores.get(i);

            if(vendedor.getCedula().equals(documento)) {

                vendedor.setNombre(nuevoVendedor.getNombre());
                vendedor.setApellidos(nuevoVendedor.getApellidos());
                vendedor.setDireccion(nuevoVendedor.getDireccion());

                xmlSerializador.serializarLista(ruta, vendedores);
                return true;
            }
        }
        throw new VendedorNotFoundException(documento);
    }

    public boolean eliminarVendedor(String documento) {

        List<Vendedor> vendedores = xmlSerializador.deserializarLista(ruta);

        for (int i = 0; i < vendedores.size(); i++) {
            Vendedor vendedor = vendedores.get(i);

            if(vendedor.getCedula().equals(documento)) {
                vendedores.remove(vendedor);

                xmlSerializador.serializarLista(ruta, vendedores);
                return true;
            }
        }
        throw new VendedorNotFoundException(documento);
    }
}
