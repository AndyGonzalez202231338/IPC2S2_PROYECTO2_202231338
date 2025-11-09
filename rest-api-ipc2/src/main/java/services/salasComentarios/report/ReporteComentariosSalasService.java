/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.salasComentarios.report;

import db.DBConnectionSingleton;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author andy
 */
public class ReporteComentariosSalasService {
    
    public ReporteComentariosSalasDto generarReporteComentarios(LocalDate fechaInicio, LocalDate fechaFin, 
                                                              Integer idSala, Integer idCine) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try {
            List<ComentarioSalaDto> comentarios = obtenerComentariosSalas(conn, fechaInicio, fechaFin, idSala, idCine);
            String filtroAplicado = generarTextoFiltro(idSala, idCine);
            
            ReporteComentariosSalasDto reporte = new ReporteComentariosSalasDto(comentarios, filtroAplicado);
            imprimirReporteConsola(reporte, fechaInicio, fechaFin);
            
            return reporte;
            
        } catch (SQLException e) {
            System.err.println("Error en base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando reporte de comentarios de salas", e);
        }
    }
    
    public byte[] generarReportePDF(LocalDate fechaInicio, LocalDate fechaFin, Integer idSala, Integer idCine) {
        try {
            ReporteComentariosSalasDto reporteData = generarReporteComentarios(fechaInicio, fechaFin, idSala, idCine);
            return generarPDF(reporteData, fechaInicio, fechaFin);
            
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF", e);
        }
    }
    
    private byte[] generarPDF(ReporteComentariosSalasDto reporte, LocalDate fechaInicio, LocalDate fechaFin) 
        throws JRException {
        
        InputStream reportStream = getClass().getClassLoader()
            .getResourceAsStream("reports/ReporteComentariosSalas.jasper");
        
        if (reportStream == null) {
            throw new JRException("No se encontró la plantilla ReporteComentariosSalas.jasper");
        }
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaInicio", java.sql.Date.valueOf(fechaInicio));
        parameters.put("fechaFin", java.sql.Date.valueOf(fechaFin));
        parameters.put("totalComentarios", reporte.getTotalComentarios());
        parameters.put("filtroAplicado", reporte.getFiltroAplicado());
        
        JRDataSource dataSource = new JRBeanCollectionDataSource(reporte.getComentarios());
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);
        
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    private List<ComentarioSalaDto> obtenerComentariosSalas(Connection conn, LocalDate fechaInicio, LocalDate fechaFin, 
                                                          Integer idSala, Integer idCine) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT s.nombre_sala, u.nombre_completo, cs.comentario, cs.fecha_comentario, cs.calificacion " +
            "FROM comentario_sala cs " +
            "JOIN sala s ON cs.id_sala = s.id_sala " +
            "JOIN usuario u ON cs.id_usuario = u.id_usuario " +
            "WHERE cs.fecha_comentario BETWEEN ? AND ? " +
            "AND cs.estado = 'ACTIVO' " +
            "AND s.estado = 'ACTIVA' " +
            "AND u.estado = 'ACTIVO' "
        );
        
        List<Object> parametros = new ArrayList<>();
        parametros.add(Timestamp.valueOf(fechaInicio.atStartOfDay()));
        parametros.add(Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
        
        if (idSala != null) {
            sql.append("AND s.id_sala = ? ");
            parametros.add(idSala);
        } else if (idCine != null) {
            sql.append("AND s.id_cine = ? ");
            parametros.add(idCine);
        }
        
        sql.append("ORDER BY s.nombre_sala, cs.fecha_comentario DESC");
        
        List<ComentarioSalaDto> comentarios = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ComentarioSalaDto comentario = new ComentarioSalaDto(
                        rs.getString("nombre_sala"),
                        rs.getString("nombre_completo"),
                        rs.getString("comentario"),
                        rs.getTimestamp("fecha_comentario").toLocalDateTime(),
                        rs.getInt("calificacion")
                    );
                    comentarios.add(comentario);
                }
            }
        }
        
        return comentarios;
    }
    
    private String generarTextoFiltro(Integer idSala, Integer idCine) {
        if (idSala != null) {
            return "Filtrado por sala: " + idSala;
        } else if (idCine != null) {
            return "Filtrado por cine: " + idCine;
        } else {
            return "Todas las salas";
        }
    }
    
    private void imprimirReporteConsola(ReporteComentariosSalasDto reporte, LocalDate fechaInicio, LocalDate fechaFin) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("REPORTE DE COMENTARIOS DE SALAS");
        System.out.println("Período: " + fechaInicio + " hasta " + fechaFin);
        System.out.println("Filtro: " + reporte.getFiltroAplicado());
        System.out.println("=".repeat(120));
        
        if (reporte.getComentarios() != null && !reporte.getComentarios().isEmpty()) {
            String salaActual = "";
            
            for (ComentarioSalaDto comentario : reporte.getComentarios()) {
                if (!salaActual.equals(comentario.getNombreSala())) {
                    salaActual = comentario.getNombreSala();
                    System.out.println("\n🎬 SALA: " + salaActual);
                    System.out.println("-".repeat(80));
                }
                
                System.out.println("Usuario: " + comentario.getNombreUsuario());
                System.out.println("Calificación: " + (comentario.getCalificacion() != null ? 
                    "*".repeat(comentario.getCalificacion()) + " (" + comentario.getCalificacion() + "/5)" : "Sin calificación"));
                System.out.println("Comentario: " + comentario.getComentario());
                System.out.println("Fecha: " + comentario.getFechaComentario());
                System.out.println("-".repeat(80));
            }
        } else {
            System.out.println("No hay comentarios en este período");
        }
        
        System.out.println("\nTOTALES:");
        System.out.println("-".repeat(40));
        System.out.printf("%-20s: %d%n", "Total Comentarios", reporte.getTotalComentarios());
        System.out.println("=".repeat(120));
    }
}