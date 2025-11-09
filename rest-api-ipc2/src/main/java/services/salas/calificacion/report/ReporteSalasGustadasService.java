package services.salas.calificacion.report;

import db.DBConnectionSingleton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


public class ReporteSalasGustadasService {
    
    public ReporteSalasGustadasDto generarReporteSalasGustadas(LocalDate fechaInicio, LocalDate fechaFin, 
        Integer idSala, Integer idCine) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try {
            List<SalaCalificacionesDto> salas = obtenerSalasGustadas(conn, fechaInicio, fechaFin, idSala, idCine);
            
            for (SalaCalificacionesDto sala : salas) {
                List<CalificacionDto> calificaciones = obtenerCalificacionesPorSala(conn, sala.getNombreSala(), fechaInicio, fechaFin);
                sala.setCalificaciones(calificaciones);
                sala.setTotalCalificaciones(calificaciones.size());
                
                if (!calificaciones.isEmpty()) {
                    Double promedio = calificaciones.stream()
                        .mapToInt(CalificacionDto::getCalificacion)
                        .average()
                        .orElse(0.0);
                    sala.setPromedioCalificacion(Math.round(promedio * 100.0) / 100.0);
                }
            }
            
            ReporteSalasGustadasDto reporteCompleto = new ReporteSalasGustadasDto(salas);
            imprimirReporteConsola(reporteCompleto, fechaInicio, fechaFin, idSala, idCine);
            
            return reporteCompleto;
            
        } catch (SQLException e) {
            System.err.println("Error en base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando reporte de salas gustadas", e);
        }
    }
    
    public byte[] generarReportePDF(LocalDate fechaInicio, LocalDate fechaFin, Integer idSala, Integer idCine) {
        try {
            ReporteSalasGustadasDto reporteData = generarReporteSalasGustadas(fechaInicio, fechaFin, idSala, idCine);
            return generarPDF(reporteData, fechaInicio, fechaFin, idSala, idCine);
            
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF", e);
        }
    }
    
    private byte[] generarPDF(ReporteSalasGustadasDto reporte, LocalDate fechaInicio, LocalDate fechaFin, 
                             Integer idSala, Integer idCine) throws JRException {
        
        InputStream reportStream = getClass().getClassLoader()
            .getResourceAsStream("reports/ReporteSalasGustadas.jasper");
        
        if (reportStream == null) {
            throw new JRException("No se encontró la plantilla ReporteSalasGustadas.jasper");
        }
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaInicio", java.sql.Date.valueOf(fechaInicio));
        parameters.put("fechaFin", java.sql.Date.valueOf(fechaFin));
        parameters.put("totalSalas", reporte.getTotalSalas());
        parameters.put("totalCalificaciones", reporte.getTotalCalificaciones());
        parameters.put("promedioGeneral", reporte.getPromedioGeneral());
        
        String filtro = "";
        if (idSala != null) {
            filtro = "Filtrado por sala: " + idSala;
        } else if (idCine != null) {
            filtro = "Filtrado por cine: " + idCine;
        } else {
            filtro = "Todas las salas";
        }
        parameters.put("filtroAplicado", filtro);
        
        JRDataSource dataSource = new JRBeanCollectionDataSource(reporte.getSalas());
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);
        
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    private List<SalaCalificacionesDto> obtenerSalasGustadas(Connection conn, LocalDate fechaInicio, LocalDate fechaFin, 
                                                            Integer idSala, Integer idCine) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT s.nombre_sala, " +
            "       AVG(cs.calificacion) as promedio, " +
            "       COUNT(cs.calificacion) as total_calificaciones " +
            "FROM sala s " +
            "LEFT JOIN comentario_sala cs ON s.id_sala = cs.id_sala " +
            "WHERE cs.fecha_comentario BETWEEN ? AND ? " +
            "AND cs.estado = 'ACTIVO' " +
            "AND s.estado = 'ACTIVA' "
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
        
        sql.append(
            "GROUP BY s.id_sala, s.nombre_sala " +
            "HAVING COUNT(cs.calificacion) > 0 " +
            "ORDER BY promedio DESC, total_calificaciones DESC " +
            "LIMIT 5"
        );
        
        List<SalaCalificacionesDto> salas = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SalaCalificacionesDto sala = new SalaCalificacionesDto(
                        rs.getString("nombre_sala")
                    );
                    sala.setPromedioCalificacion(rs.getDouble("promedio"));
                    sala.setTotalCalificaciones(rs.getInt("total_calificaciones"));
                    salas.add(sala);
                }
            }
        }
        
        return salas;
    }
    
    private List<CalificacionDto> obtenerCalificacionesPorSala(Connection conn, String nombreSala, 
                                                              LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        String sql = "SELECT u.nombre_completo, cs.calificacion, cs.fecha_comentario " +
                     "FROM comentario_sala cs " +
                     "JOIN sala s ON cs.id_sala = s.id_sala " +
                     "JOIN usuario u ON cs.id_usuario = u.id_usuario " +
                     "WHERE s.nombre_sala = ? " +
                     "AND cs.fecha_comentario BETWEEN ? AND ? " +
                     "AND cs.estado = 'ACTIVO' " +
                     "AND cs.calificacion IS NOT NULL " +
                     "ORDER BY cs.fecha_comentario DESC";
        
        List<CalificacionDto> calificaciones = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreSala);
            stmt.setTimestamp(2, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            stmt.setTimestamp(3, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CalificacionDto calificacion = new CalificacionDto(
                        rs.getString("nombre_completo"),
                        rs.getInt("calificacion"),
                        rs.getTimestamp("fecha_comentario").toLocalDateTime()
                    );
                    calificaciones.add(calificacion);
                }
            }
        }
        
        return calificaciones;
    }
    
    private void imprimirReporteConsola(ReporteSalasGustadasDto reporte, LocalDate fechaInicio, LocalDate fechaFin, 
                                       Integer idSala, Integer idCine) {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("REPORTE DE SALAS MÁS GUSTADAS");
        System.out.println("Período: " + fechaInicio + " hasta " + fechaFin);
        if (idSala != null) {
            System.out.println("Filtrado por sala: " + idSala);
        } else if (idCine != null) {
            System.out.println("Filtrado por cine: " + idCine);
        } else {
            System.out.println("Todas las salas");
        }
        System.out.println("=".repeat(100));
        
        for (SalaCalificacionesDto sala : reporte.getSalas()) {
            System.out.println("\nSALA: " + sala.getNombreSala());
            System.out.println("Promedio: " + String.format("%.2f", sala.getPromedioCalificacion()) + "/5");
            System.out.println("Total calificaciones: " + sala.getTotalCalificaciones());
            System.out.println("-".repeat(80));
            
            if (sala.getCalificaciones() != null && !sala.getCalificaciones().isEmpty()) {
                System.out.println("Calificaciones:");
                System.out.printf("%-30s %-10s %-20s%n", "USUARIO", "CALIFICACIÓN", "FECHA");
                System.out.println("-".repeat(80));
                
                for (CalificacionDto calificacion : sala.getCalificaciones()) {
                    System.out.printf("%-30s %-10d %-20s%n",
                        truncate(calificacion.getNombreUsuario(), 29),
                        calificacion.getCalificacion(),
                        calificacion.getFechaCalificacion().toLocalDate().toString()
                    );
                }
            } else {
                System.out.println("No tiene calificaciones en este período");
            }
            
            System.out.println("-".repeat(80));
        }
        
        System.out.println("\nTOTAL GENERALES:");
        System.out.println("-".repeat(40));
        System.out.printf("%-20s: %d%n", "Total Salas", reporte.getTotalSalas());
        System.out.printf("%-20s: %d%n", "Total Calificaciones", reporte.getTotalCalificaciones());
        System.out.printf("%-20s: %.2f/5%n", "Promedio General", reporte.getPromedioGeneral());
        
        System.out.println("=".repeat(100));
    }
    
    private String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }
}