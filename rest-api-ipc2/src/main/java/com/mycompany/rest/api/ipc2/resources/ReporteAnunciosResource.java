/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rest.api.ipc2.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import services.anuncios.report.ReporteAnunciosCompletoDto;
import services.anuncios.report.ReporteAnunciosService;

/**
 *
 * @author andy
 */
@Path("reports2")
public class ReporteAnunciosResource {

    @GET
    @Path("anuncios")
    @Produces(MediaType.TEXT_PLAIN)
    public String generarReporteAnuncios(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("tipoAnuncio") Integer tipoAnuncio,
            @QueryParam("periodo") Integer periodo) {

        try {
            // Validar parámetros
            if (fechaInicioStr == null || fechaFinStr == null) {
                return "ERROR: Se requieren los parámetros fechaInicio y fechaFin";
            }

            // Convertir parámetros a LocalDate
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);

            // Validar intervalo
            if (fechaInicio.isAfter(fechaFin)) {
                return "ERROR: Fecha inicio no puede ser después de fecha fin";
            }

            ReporteAnunciosService service = new ReporteAnunciosService();
            ReporteAnunciosCompletoDto reporteData = service.generarReporteAnuncios(
                    fechaInicio, fechaFin, tipoAnuncio, periodo);

            return "Reporte de anuncios generado exitosamente. Revisa la consola del servidor para ver los resultados.";

        } catch (Exception e) {
            return "Error generando reporte: " + e.getMessage();
        }
    }

    @GET
    @Path("anuncios-pdf")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response descargarReporteAnunciosPDF(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("tipoAnuncio") Integer tipoAnuncio,
            @QueryParam("periodo") Integer periodo) {

        System.out.println("🔍 === INICIANDO GENERACIÓN PDF ANUNCIOS ===");

        try {
            // Validar parámetros
            if (fechaInicioStr == null || fechaFinStr == null) {
                throw new WebApplicationException("Se requieren los parámetros fechaInicio y fechaFin", 400);
            }

            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);

            if (fechaInicio.isAfter(fechaFin)) {
                throw new WebApplicationException("Fecha inicio no puede ser después de fecha fin", 400);
            }

            ReporteAnunciosService service = new ReporteAnunciosService();

            // USAR EL NUEVO MÉTODO que genera el PDF
            byte[] pdfBytes = service.generarReportePDF(fechaInicio, fechaFin, tipoAnuncio, periodo);

            System.out.println("✅ PDF generado exitosamente. Tamaño: " + pdfBytes.length + " bytes");

            String filename = "reporte_anuncios_" + fechaInicio + "_" + fechaFin + ".pdf";

            return Response
                    .ok(pdfBytes, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .build();

        } catch (Exception e) {
            System.out.println("💥 ERROR en generación de PDF: " + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException("Error generando PDF: " + e.getMessage(), 500);
        }
    }
}
