package com.marketplace.api.services;

import com.marketplace.api.entities.Producto;
import com.marketplace.api.entities.Vendedor;
import com.marketplace.api.entities.EstadoProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ReporteService {

    @Autowired
    private VendedorService vendedorService;

    private static final String RUTA_REPORTES = "reportes/";
    private static final Logger LOGGER = Logger.getLogger(ReporteService.class.getName());

    static {
        LoggerConfigService.getInstance().configureLogger(LOGGER);
        // Crear directorio de reportes si no existe
        new File(RUTA_REPORTES).mkdirs();
    }

    public String generarReporteCompleto() {
        LOGGER.log(Level.INFO, "Generando reporte completo");
        String nombreArchivo = RUTA_REPORTES + "reporte_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            List<Vendedor> vendedores = vendedorService.getAllVendedores();

            // Encabezado
            writer.println("===========================================");
            writer.println("        REPORTE DE MARKETPLACE");
            writer.println("===========================================");
            writer.println("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            writer.println("===========================================\n");

            // Resumen general
            writer.println("RESUMEN GENERAL");
            writer.println("-------------------------------------------");
            writer.printf("Total de vendedores registrados: %d%n", vendedores.size());
            int totalProductos = vendedores.stream()
                    .mapToInt(v -> v.getProductos() != null ? v.getProductos().size() : 0)
                    .sum();
            writer.printf("Total de productos en el sistema: %d%n%n", totalProductos);

            // Detalle por vendedor
            writer.println("DETALLE POR VENDEDOR");
            writer.println("-------------------------------------------");

            for (Vendedor vendedor : vendedores) {
                writer.printf("%s %s - %s%n",
                        vendedor.getNombre(),
                        vendedor.getApellidos(),
                        vendedor.getCedula());

                List<Producto> productos = vendedor.getProductos();
                if (productos != null && !productos.isEmpty()) {
                    writer.println("Productos publicados:");
                    for (int i = 0; i < productos.size(); i++) {
                        Producto p = productos.get(i);
                        writer.printf("  %d- %s $%,.0f (%s) - %s%n",
                                i + 1,
                                p.getNombre(),
                                p.getPrecio(),
                                p.getCategoria(),
                                p.getEstado());
                    }

                    // Estadísticas del vendedor
                    writer.printf("\nEstadísticas:%n");
                    writer.printf("  - Total productos: %d%n", productos.size());
                    if (vendedor.getContactos() != null) {
                        writer.printf("  - Total contactos: %d%n", vendedor.getContactos().size());
                    }
                    if (vendedor.getSolicitudesDeContacto() != null) {
                        writer.printf("  - Solicitudes pendientes: %d%n",
                                vendedor.getSolicitudesDeContacto().size());
                    }

                } else {
                    writer.println("  -sin productos-");
                }
                writer.println("\n-------------------------------------------\n");
            }

            LOGGER.log(Level.INFO, "Reporte completo generado en: " + nombreArchivo);
            return nombreArchivo;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al generar el reporte", e);
            throw new RuntimeException("Error al generar el reporte: " + e.getMessage());
        }
    }
}