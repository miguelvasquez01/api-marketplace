package com.marketplace.api.controllers;

import com.marketplace.api.entities.Producto;
import com.marketplace.api.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/getProductoById/{codigo}")
    public Producto getProductoById(@PathVariable Integer codigo) {
        return productoService.getProductoById(codigo);
        //ResponseEntity.ok()
    }

    @GetMapping("/getAllProductos")
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    @PostMapping("/guardarProducto")
    public void guardarProducto(@RequestBody Producto producto) {
        productoService.guardarProducto(producto);
    }

    @PutMapping("/actualizarProducto/{codigo}")
    public boolean actualizarProducto(@PathVariable Integer codigo, @RequestBody Producto producto) {
        return productoService.actualizarProducto(codigo, producto);
    }

    @DeleteMapping("/eliminarProducto/{codigo}")
    public boolean eliminarProducto(@PathVariable Integer codigo) {
        return productoService.eliminarProducto(codigo);
    }
}
