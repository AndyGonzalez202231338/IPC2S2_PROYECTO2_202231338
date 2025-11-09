package com.mycompany.rest.api.ipc2.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.WebApplicationException;
import java.time.LocalDate;
import services.boletos.report.ReporteBoletosVendidosService;
import services.boletos.report.ReporteBoletosVendidosDto;

@Path("reports9")
public class ReporteBoletosVendidosResource {
    
    @GET
    @Path("boletos-vendidos")
    @Produces(MediaType.TEXT_PLAIN)
    public String generarReporteBoletosVendidos(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("idSala") Integer idSala,
            @QueryParam("idCine") Integer idCine) {
        
        try {
            if (fechaInicioStr == null || fechaFinStr == null) {
                return "ERROR: Se requieren los parámetros fechaInicio y fechaFin";
            }
            
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            
            if (fechaInicio.isAfter(fechaFin)) {
                return "ERROR: Fecha inicio no puede ser después de fecha fin";
            }
            
            ReporteBoletosVendidosService service = new ReporteBoletosVendidosService();
            ReporteBoletosVendidosDto reporteData = service.generarReporteBoletosVendidos(fechaInicio, fechaFin, idSala, idCine);
            
            return "Reporte de boletos vendidos generado exitosamente. Revisa la consola del servidor para ver los resultados.";
            
        } catch (Exception e) {
            return "Error generando reporte: " + e.getMessage();
        }
    }
    
    @GET
    @Path("boletos-vendidos-pdf")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response descargarReporteBoletosVendidosPDF(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("idSala") Integer idSala,
            @QueryParam("idCine") Integer idCine) {
        
        System.out.println("=== INICIANDO GENERACIÓN PDF BOLETOS VENDIDOS ===");
        
        try {
            if (fechaInicioStr == null || fechaFinStr == null) {
                throw new WebApplicationException("Se requieren los parámetros fechaInicio y fechaFin", 400);
            }
            
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            
            if (fechaInicio.isAfter(fechaFin)) {
                throw new WebApplicationException("Fecha inicio no puede ser después de fecha fin", 400);
            }
            
            ReporteBoletosVendidosService service = new ReporteBoletosVendidosService();
            byte[] pdfBytes = service.generarReportePDF(fechaInicio, fechaFin, idSala, idCine);
            
            System.out.println("PDF generado exitosamente. Tamaño: " + pdfBytes.length + " bytes");

            String filename = "reporte_boletos_vendidos_" + fechaInicio + "_" + fechaFin;
            if (idSala != null) {
                filename += "_sala_" + idSala;
            } else if (idCine != null) {
                filename += "_cine_" + idCine;
            }
            filename += ".pdf";
            
            return Response
                .ok(pdfBytes, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .build();
                
        } catch (Exception e) {
            System.out.println("ERROR en generación de PDF: " + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException("Error generando PDF: " + e.getMessage(), 500);
        }
    }
}