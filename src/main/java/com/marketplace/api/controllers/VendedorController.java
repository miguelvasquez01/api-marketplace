package com.marketplace.api.controllers;

import com.marketplace.api.entities.Vendedor;
import com.marketplace.api.services.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @GetMapping("/getVendedorById/{documento}")
    public Vendedor getVendedorById(@PathVariable String documento) {
        return vendedorService.getVendedorById(documento);
    }

    @GetMapping("/getAllVendedores")
    public List<Vendedor> getAllVendedores() {
        return vendedorService.getAllVendedores();
    }

    @PostMapping("/guardarVendedor")
    public void guardarVendedor(@RequestBody Vendedor vendedor) {
        vendedorService.guardarVendedor(vendedor);
    }

    @PutMapping("/actualizarVendedor/{documento}")
    public boolean actualizarVendedor(@PathVariable String documento, @RequestBody Vendedor vendedor) {
        return vendedorService.actualizarVendedor(documento, vendedor);
    }

    @DeleteMapping("/eliminarVendedor/{documento}")
    public boolean eliminarVendedor(@PathVariable String documento) {
        return vendedorService.eliminarVendedor(documento);
    }
}
