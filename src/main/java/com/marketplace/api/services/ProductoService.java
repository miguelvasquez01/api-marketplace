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
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductoService {

    @Autowired
    private XMLSerializador xmlSerializador;

    private final String ruta = "archivosSerializados/productos.xml";
    private static final Logger LOGGER = Logger.getLogger(ProductoService.class.getName());

    static {
        LoggerConfigService.getInstance().configureLogger(LOGGER);
    }

    // Excepciones archivo no encontrado y producto no encontrado
    public Producto getProductoById(int codigo) {
        LOGGER.log(Level.INFO, "Obteniendo producto por ID: " + codigo);

        List<Producto> productos = xmlSerializador.deserializarLista(ruta);
        Optional<Producto> producto = productos.stream().filter(p -> p.getCodigo() == codigo).findFirst();

        if (producto.isPresent()) {
            LOGGER.log(Level.INFO, "Producto encontrado: " + producto.get().getNombre());
        } else {
            LOGGER.log(Level.WARNING, "Producto no encontrado con ID: " + codigo);
        }

        return producto.orElseThrow(() -> new ProductoNotFoundException(codigo + ""));
    }

    // Excepciones archivo no encontrado y archivo vacío
    public List<Producto> getAllProductos() {
        LOGGER.log(Level.INFO, "Obteniendo todos los productos");

        List<Producto> productos = xmlSerializador.deserializarLista(ruta);
        if (productos.isEmpty()) {
            LOGGER.log(Level.WARNING, "La lista de productos está vacía");
            throw new ArchivoVacioException("El archivo " + ruta + " está vacío");
        }

        LOGGER.log(Level.INFO, "Lista de productos obtenida correctamente");
        return productos;
    }

    // Excepciones archivo no encontrado y código duplicado
    public void guardarProducto(Producto producto) {
        LOGGER.log(Level.INFO, "Guardando producto: " + producto.getNombre());

        try {
            File archivo = new File(ruta);

            if (!archivo.exists()) {
                LOGGER.log(Level.INFO, "Archivo no encontrado, creando nuevo archivo en: " + ruta);
                archivo.createNewFile();
            }

            List<Producto> productos = new ArrayList<>();
            if (archivo.length() > 0) {
                productos = xmlSerializador.deserializarLista(ruta);
                LOGGER.log(Level.INFO, "Archivo deserializado correctamente");
            }

            boolean codigoDuplicado = productos.stream()
                    .anyMatch(p -> p.getCodigo() == producto.getCodigo());
            if (codigoDuplicado) {
                LOGGER.log(Level.WARNING, "Código de producto duplicado: " + producto.getCodigo());
                throw new ProductoDuplicadoException(producto.getCodigo() + "");
            }

            productos.add(producto);
            xmlSerializador.serializarLista(ruta, productos);
            LOGGER.log(Level.INFO, "Producto guardado correctamente: " + producto.getNombre());

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al manipular el archivo: " + ruta, e);
            throw new RuntimeException("Error al guardar el producto", e);
        }
    }

    // Excepciones archivo no encontrado y producto no encontrado
    public boolean actualizarProducto(int codigo, Producto nuevoProducto) {
        LOGGER.log(Level.INFO, "Actualizando producto con ID: " + codigo);

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
                LOGGER.log(Level.INFO, "Producto actualizado correctamente: " + producto.getNombre());
                return true;
            }
        }

        LOGGER.log(Level.WARNING, "Producto no encontrado para actualizar con ID: " + codigo);
        throw new ProductoNotFoundException(codigo + "");
    }

    // Excepciones archivo no encontrado y producto no encontrado
    public boolean eliminarProducto(int codigo) {
        LOGGER.log(Level.INFO, "Eliminando producto con ID: " + codigo);

        List<Producto> productos = xmlSerializador.deserializarLista(ruta);

        for (Producto producto : productos) {
            if (producto.getCodigo() == codigo) {
                productos.remove(producto);
                xmlSerializador.serializarLista(ruta, productos);
                LOGGER.log(Level.INFO, "Producto eliminado correctamente: " + producto.getNombre());
                return true;
            }
        }

        LOGGER.log(Level.WARNING, "Producto no encontrado para eliminar con ID: " + codigo);
        throw new ProductoNotFoundException(codigo + "");
    }
}
