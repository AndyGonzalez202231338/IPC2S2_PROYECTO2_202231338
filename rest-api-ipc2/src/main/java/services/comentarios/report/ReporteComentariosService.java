package services.comentarios.report;

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

public class ReporteComentariosService {
    
    public ReporteComentariosCompletoDto generarReporteComentarios(LocalDate fechaInicio, LocalDate fechaFin) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try {
            List<ComentarioSalaDto> comentarios = obtenerComentarios(conn, fechaInicio, fechaFin);
            ReporteComentariosCompletoDto reporteCompleto = new ReporteComentariosCompletoDto(comentarios);
            
            imprimirReporteConsola(reporteCompleto, fechaInicio, fechaFin);
            
            return reporteCompleto;
            
        } catch (SQLException e) {
            System.err.println("Error en base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando reporte de comentarios", e);
        }
    }
    
    public byte[] generarReportePDF(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            ReporteComentariosCompletoDto reporteData = generarReporteComentarios(fechaInicio, fechaFin);
            return generarPDF(reporteData, fechaInicio, fechaFin);
            
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF", e);
        }
    }
    
    private byte[] generarPDF(ReporteComentariosCompletoDto reporte, LocalDate fechaInicio, LocalDate fechaFin) 
        throws JRException {
        
        InputStream reportStream = getClass().getClassLoader()
            .getResourceAsStream("reports/ReporteComentarios.jasper");
        
        if (reportStream == null) {
            throw new JRException("No se encontró la plantilla ReporteComentarios.jasper");
        }
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaInicio", java.sql.Date.valueOf(fechaInicio));
        parameters.put("fechaFin", java.sql.Date.valueOf(fechaFin));
        parameters.put("totalComentarios", reporte.getTotalComentarios());
        parameters.put("promedioCalificacion", reporte.getPromedioCalificacion());
        
        JRDataSource dataSource = new JRBeanCollectionDataSource(reporte.getComentarios());
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);
        
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    private List<ComentarioSalaDto> obtenerComentarios(Connection conn, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        String sql = "SELECT s.nombre_sala, u.nombre_completo as nombre_usuario, " +
                     "cs.comentario, cs.calificacion, cs.fecha_comentario, cs.estado " +
                     "FROM comentario_sala cs " +
                     "JOIN sala s ON cs.id_sala = s.id_sala " +
                     "JOIN usuario u ON cs.id_usuario = u.id_usuario " +
                     "WHERE cs.fecha_comentario BETWEEN ? AND ? " +
                     "AND cs.estado = 'ACTIVO' " +
                     "AND s.estado = 'ACTIVA' " +
                     "ORDER BY cs.fecha_comentario DESC";
        
        List<ComentarioSalaDto> comentarios = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ComentarioSalaDto comentario = new ComentarioSalaDto(
                        rs.getString("nombre_sala"),
                        rs.getString("nombre_usuario"),
                        rs.getString("comentario"),
                        rs.getInt("calificacion"),
                        rs.getTimestamp("fecha_comentario").toLocalDateTime(),
                        rs.getString("estado")
                    );
                    comentarios.add(comentario);
                }
            }
        }
        
        return comentarios;
    }
    
    private void imprimirReporteConsola(ReporteComentariosCompletoDto reporte, LocalDate fechaInicio, LocalDate fechaFin) {
        System.out.println("\n" + "=".repeat(150));
        System.out.println("REPORTE DE COMENTARIOS DE SALAS");
        System.out.println("Período: " + fechaInicio + " hasta " + fechaFin);
        System.out.println("=".repeat(150));
        
        System.out.printf("%-20s %-25s %-40s %-12s %-20s %-10s%n", 
            "SALA", "USUARIO", "COMENTARIO", "CALIFICACIÓN", "FECHA", "ESTADO");
        System.out.println("-".repeat(150));
        
        for (ComentarioSalaDto comentario : reporte.getComentarios()) {
            System.out.printf("%-20s %-25s %-40s %-12d %-20s %-10s%n",
                truncate(comentario.getNombreSala(), 19),
                truncate(comentario.getNombreUsuario(), 24),
                truncate(comentario.getComentario(), 39),
                comentario.getCalificacion(),
                comentario.getFechaComentario().toLocalDate().toString(),
                comentario.getEstado()
            );
        }
        
        System.out.println("-".repeat(150));
        
        System.out.println("\nTOTALES:");
        System.out.println("-".repeat(50));
        System.out.printf("%-30s: %d%n", "Total Comentarios", reporte.getTotalComentarios());
        System.out.printf("%-30s: %.2f/5%n", "Calificación Promedio", reporte.getPromedioCalificacion());
        
        System.out.println("=".repeat(150));
    }
    
    private String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }
}