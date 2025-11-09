package com.mycompany.rest.api.ipc2.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.WebApplicationException;
import java.time.LocalDate;
import services.comentarios.report.ReporteComentariosService;
import services.comentarios.report.ReporteComentariosCompletoDto;

@Path("reports4") 
public class ReporteComentariosResource {
    
    @GET
    @Path("comentarios")
    @Produces(MediaType.TEXT_PLAIN)
    public String generarReporteComentarios(
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
            
            ReporteComentariosService service = new ReporteComentariosService();
            ReporteComentariosCompletoDto reporteData = service.generarReporteComentarios(fechaInicio, fechaFin);
            
            return "Reporte de comentarios generado exitosamente. Revisa la consola del servidor para ver los resultados.";
            
        } catch (Exception e) {
            return "Error generando reporte: " + e.getMessage();
        }
    }
    
    @GET
    @Path("comentarios-pdf")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response descargarReporteComentariosPDF(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr) {
        
        try {
            if (fechaInicioStr == null || fechaFinStr == null) {
                throw new WebApplicationException("Se requieren los parámetros fechaInicio y fechaFin", 400);
            }
            
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            
            if (fechaInicio.isAfter(fechaFin)) {
                throw new WebApplicationException("Fecha inicio no puede ser después de fecha fin", 400);
            }
            
            ReporteComentariosService service = new ReporteComentariosService();
            byte[] pdfBytes = service.generarReportePDF(fechaInicio, fechaFin);
            
            

            String filename = "reporte_comentarios_" + fechaInicio + "_" + fechaFin + ".pdf";
            
            return Response
                .ok(pdfBytes, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .build();
                
        } catch (Exception e) {
                
            e.printStackTrace();
            throw new WebApplicationException("Error generando PDF: " + e.getMessage(), 500);
        }
    }
}