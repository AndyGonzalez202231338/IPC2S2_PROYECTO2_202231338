package com.mycompany.rest.api.ipc2.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.WebApplicationException;
import java.time.LocalDate;
import services.salas.report.ReporteSalasPopularesService;
import services.salas.report.ReporteSalasPopularesDto;

@Path("reports3")
public class ReporteSalasPopularesResource {
    
    @GET
    @Path("salas-populares")
    @Produces(MediaType.TEXT_PLAIN)
    public String generarReporteSalasPopulares(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr) {
        
        try {
            if (fechaInicioStr == null || fechaFinStr == null) {
                return "ERROR: Se requieren los parámetros fechaInicio y fechaFin";
            }
            
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            
            if (fechaInicio.isAfter(fechaFin)) {
                return "ERROR: Fecha inicio no puede ser después de fecha fin";
            }
            
            ReporteSalasPopularesService service = new ReporteSalasPopularesService();
            ReporteSalasPopularesDto reporteData = service.generarReporteSalasPopulares(fechaInicio, fechaFin);
            
            return "Reporte de salas populares generado exitosamente. Revisa la consola del servidor para ver los resultados.";
            
        } catch (Exception e) {
            return "Error generando reporte: " + e.getMessage();
        }
    }
    
    @GET
    @Path("salas-populares-pdf")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response descargarReporteSalasPopularesPDF(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr) {
        
        System.out.println("🔍 === INICIANDO GENERACIÓN PDF SALAS POPULARES ===");
        
        try {
            if (fechaInicioStr == null || fechaFinStr == null) {
                throw new WebApplicationException("Se requieren los parámetros fechaInicio y fechaFin", 400);
            }
            
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            
            if (fechaInicio.isAfter(fechaFin)) {
                throw new WebApplicationException("Fecha inicio no puede ser después de fecha fin", 400);
            }
            
            ReporteSalasPopularesService service = new ReporteSalasPopularesService();
            byte[] pdfBytes = service.generarReportePDF(fechaInicio, fechaFin);
            
            System.out.println("PDF generado exitosamente. Tamaño: " + pdfBytes.length + " bytes");

            String filename = "reporte_salas_populares_" + fechaInicio + "_" + fechaFin + ".pdf";       
            
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