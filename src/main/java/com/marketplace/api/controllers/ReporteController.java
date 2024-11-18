package com.marketplace.api.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.marketplace.api.entities.Producto;
import com.marketplace.api.entities.Vendedor;
import com.marketplace.api.services.VendedorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private VendedorService vendedorService;

    private static final Logger LOGGER = Logger.getLogger(ReporteController.class.getName());

    @GetMapping("/generar/{vendedorId}/{mes}")
    public ResponseEntity<Resource> generarReporte(@PathVariable String vendedorId, @PathVariable String mes) {
        try {
            LOGGER.log(Level.INFO, "Generando reporte para vendedor: " + vendedorId + " del mes: " + mes);

            // Obtener el vendedor
            Vendedor vendedor = vendedorService.getVendedorById(vendedorId);

            // Filtrar productos por mes
            List<Producto> productosFiltrados = filtrarProductosPorMes(vendedor.getProductos(), mes);

            // Crear documento PDF
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            // Configurar fuentes
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font subtituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.open();

            // Título y fecha
            Paragraph titulo = new Paragraph("Reporte Mensual de Vendedor", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Mes: " + getNombreMes(mes), normalFont));
            document.add(new Paragraph("Fecha de generación: " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), normalFont));
            document.add(new Paragraph("\n"));

            // Información del vendedor
            document.add(new Paragraph("Información del Vendedor", subtituloFont));
            document.add(new Paragraph("Nombre: " + vendedor.getNombre() + " " + vendedor.getApellidos(), normalFont));
            document.add(new Paragraph("Cédula: " + vendedor.getCedula(), normalFont));
            document.add(new Paragraph("Dirección: " + vendedor.getDireccion(), normalFont));
            document.add(new Paragraph("\n"));

            // Productos del mes
            document.add(new Paragraph("Productos del Mes", subtituloFont));
            if (!productosFiltrados.isEmpty()) {
                double valorTotalProductos = 0;

                for (Producto producto : productosFiltrados) {
                    document.add(new Paragraph("- " + producto.getNombre(), normalFont));
                    document.add(new Paragraph("  Código: " + producto.getCodigo(), normalFont));
                    document.add(new Paragraph("  Precio: $" + producto.getPrecio(), normalFont));
                    document.add(new Paragraph("  Categoría: " + producto.getCategoria(), normalFont));
                    document.add(new Paragraph("  Estado: " + producto.getEstado(), normalFont));
                    document.add(new Paragraph("  Fecha de publicación: " + producto.getFechaPublicacion(), normalFont));
                    document.add(new Paragraph("\n"));

                    valorTotalProductos += producto.getPrecio();
                }

                // Estadísticas del mes
                document.add(new Paragraph("Estadísticas del Mes", subtituloFont));
                document.add(new Paragraph("Total de productos: " + productosFiltrados.size(), normalFont));
                document.add(new Paragraph("Valor total de productos: $" + String.format("%.2f", valorTotalProductos), normalFont));
            } else {
                document.add(new Paragraph("No hay productos registrados para este mes", normalFont));
            }

            document.close();

            byte[] pdfBytes = baos.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=reporte_vendedor_" + vendedorId + "mes" + mes + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfBytes.length)
                    .body(resource);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al generar reporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private List<Producto> filtrarProductosPorMes(List<Producto> productos, String mes) {
        if (productos == null) return new ArrayList<>();

        return productos.stream()
                .filter(p -> {
                    LocalDate fecha = LocalDate.parse(p.getFechaPublicacion());
                    return fecha.getMonthValue() == Integer.parseInt(mes);
                })
                .collect(Collectors.toList());
    }

    private String getNombreMes(String mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[Integer.parseInt(mes) - 1];
    }
}