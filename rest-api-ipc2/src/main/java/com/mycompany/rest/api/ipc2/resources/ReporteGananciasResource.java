package com.mycompany.rest.api.ipc2.resources;

import services.cines.report.ReporteGananciasService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;

@Path("reports")
public class ReporteGananciasResource {
    
    @GET
    @Path("ganancias-pdf")
    @Produces("application/pdf")
    public Response generarReportePDF(
            @QueryParam("fechaInicio") String fechaInicioStr,
            @QueryParam("fechaFin") String fechaFinStr) {
        
        try {
            // Validar parámetros
            if (fechaInicioStr == null || fechaFinStr == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Se requieren los parámetros fechaInicio y fechaFin")
                    .build();
            }
            
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr);
            
            if (fechaInicio.isAfter(fechaFin)) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Fecha inicio no puede ser después de fecha fin")
                    .build();
            }
            
            ReporteGananciasService service = new ReporteGananciasService();
            byte[] pdfBytes = service.generarReportePDF(fechaInicio, fechaFin);
            
            // Crear respuesta con el PDF
            return Response.ok(pdfBytes)
                .header("Content-Disposition", "attachment; filename=\"reporte_ganancias.pdf\"")
                .build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error generando reporte: " + e.getMessage())
                .build();
        }
    }
}