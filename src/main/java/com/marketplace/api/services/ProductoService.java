package com.marketplace.api.services;

import com.marketplace.api.entities.Producto;
import com.marketplace.api.exceptions.ArchivoVacioException;
import com.marketplace.api.exceptions.ProductoDuplicadoException;
import com.marketplace.api.exceptions.ProductoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private XMLSerializador xmlSerializador;

    private final String ruta = "archivosSerializados/productos.xml";

    //Excepciones archivo no encontrado y producto no encontrado
    public Producto getProductoById(int codigo) {
        List<Producto> productos = xmlSerializador.deserializarLista(ruta);
        Optional<Producto> producto = productos.stream().filter(p -> p.getCodigo() == codigo).findFirst();
        return producto.orElseThrow(() -> new ProductoNotFoundException(codigo+""));
        //Falta verificar que la lista no este vacía:getById, put, delete
    }    //El problemas es que se lanza primero la excepcion archivoVacio que ProductoNotFound

    //Excepciones archivo no encontrado y archivo vacío
    public List<Producto> getAllProductos() {

        return xmlSerializador.deserializarLista(ruta);
    }

    //Excepciones archivo no encontrado y codigo duplicado
    public void guardarProducto(Producto producto) {//Hacer que retorne el producto opcional
        try {
            //Comprobamos que haya archivo y lista
            File archivo = new File(ruta);

            if (!archivo.exists()) {
                archivo.createNewFile();  // Crear el archivo vacío
            }

            List<Producto> productos = new ArrayList<>();
            //Verificar si el archivo tiene contenido
            if (archivo.length() > 0) {
                productos = xmlSerializador.deserializarLista(ruta);
            }

            //Comprobar que el código del producto no se repita
            boolean codigoDuplicado = productos.stream()
                    .anyMatch(p -> p.getCodigo() == producto.getCodigo());
            if(codigoDuplicado) {
                throw new ProductoDuplicadoException(producto.getCodigo()+"");
            }
            productos.add(producto);

            xmlSerializador.serializarLista(ruta, productos);

        } catch (IOException e) {//Para la clase File
            e.printStackTrace();
        }
    }

    //Excepciones archivo no encontrado y producto no encontrado
    public boolean actualizarProducto(int codigo, Producto nuevoProducto) {
        List<Producto> productos = xmlSerializador.deserializarLista(ruta);

        for (int i = 0; i < productos.size(); i++) {
            Producto producto = productos.get(i);

            if (producto.getCodigo() == codigo) {

                producto.setNombre(nuevoProducto.getNombre());
                producto.setImagen(nuevoProducto.getImagen());
                producto.setCategoria(nuevoProducto.getCategoria());
                producto.setPrecio(nuevoProducto.getPrecio());
                producto.setEstado(nuevoProducto.getEstado());
                producto.setFechaPublicacion(nuevoProducto.getFechaPublicacion());

                xmlSerializador.serializarLista(ruta, productos);

                return true; //Retornar true si la actualización fue exitosa
            }
        }
        throw new ProductoNotFoundException(codigo+"");//Lanza excepción si no se encontró el producto
    }

    //Excepciones archivo no encontrado y producto no encontrado
    public boolean eliminarProducto(int codigo) {
        List<Producto> productos = xmlSerializador.deserializarLista(ruta);

        for (Producto producto1: productos) {//Puede dar error por remover con forEach
            if(producto1.getCodigo() == codigo){
                productos.remove(producto1);

                xmlSerializador.serializarLista(ruta, productos);

                return true;
            }
        }
        throw new ProductoNotFoundException(codigo+"");
    }
}
