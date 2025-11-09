package com.mycompany.rest.api.ipc2.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.WebApplicationException;
import java.time.LocalDate;
import services.anunciantes.report.ReporteAnunciantesService;
import services.anunciantes.report.ReporteAnunciantesCompletoDto;

@Path("reports5")
public class ReporteAnunciantesResource {
    
    @GET
    @Path("anunciantes")
    @Produces(MediaType.TEXT_PLAIN)
    public String generarReporteAnunciantes(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("idAnunciante") Integer idAnunciante) {
        
        try {
            if (fechaInicioStr == null || fechaFinStr == null) {
                return "ERROR: Se requieren los parámetros fechaInicio y fechaFin";
            }
            
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            
            if (fechaInicio.isAfter(fechaFin)) {
                return "ERROR: Fecha inicio no puede ser después de fecha fin";
            }
            
            ReporteAnunciantesService service = new ReporteAnunciantesService();
            ReporteAnunciantesCompletoDto reporteData = service.generarReporteAnunciantes(fechaInicio, fechaFin, idAnunciante);
            
            return "Reporte de anunciantes generado exitosamente. Revisa la consola del servidor para ver los resultados.";
            
        } catch (Exception e) {
            return "Error generando reporte: " + e.getMessage();
        }
    }
    
    @GET
    @Path("anunciantes-pdf")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response descargarReporteAnunciantesPDF(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr,
            @QueryParam("idAnunciante") Integer idAnunciante) {
        
        
        
        try {
            if (fechaInicioStr == null || fechaFinStr == null) {
                throw new WebApplicationException("Se requieren los parámetros fechaInicio y fechaFin", 400);
            }
            
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            
            if (fechaInicio.isAfter(fechaFin)) {
                throw new WebApplicationException("Fecha inicio no puede ser después de fecha fin", 400);
            }
            
            ReporteAnunciantesService service = new ReporteAnunciantesService();
            byte[] pdfBytes = service.generarReportePDF(fechaInicio, fechaFin, idAnunciante);
            
            

            String filename = "reporte_anunciantes_" + fechaInicio + "_" + fechaFin;
            if (idAnunciante != null) {
                filename += "_anunciante_" + idAnunciante;
            }
            filename += ".pdf";
            
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