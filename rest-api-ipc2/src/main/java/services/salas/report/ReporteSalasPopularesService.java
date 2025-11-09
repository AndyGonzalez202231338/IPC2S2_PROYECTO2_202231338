package services.salas.report;

import db.DBConnectionSingleton;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReporteSalasPopularesService {
    
        public ReporteSalasPopularesDto generarReporteSalasPopulares(LocalDate fechaInicio, LocalDate fechaFin) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try {
            List<SalaPopularDto> salas = obtenerSalasPopulares(conn, fechaInicio, fechaFin);
            
            for (SalaPopularDto sala : salas) {
                List<UsuarioBoletosDto> usuarios = obtenerUsuariosYBoletosPorSala(conn, sala.getNombreSala(), fechaInicio, fechaFin);
                sala.setUsuarios(usuarios);
                
                Integer totalBoletos = usuarios.stream()
                    .mapToInt(UsuarioBoletosDto::getCantidadBoletos)
                    .sum();
                sala.setTotalBoletosVendidos(totalBoletos);
            }
            
            ReporteSalasPopularesDto reporteCompleto = new ReporteSalasPopularesDto(salas);
            imprimirReporteConsola(reporteCompleto, fechaInicio, fechaFin);
            
            return reporteCompleto;
            
        } catch (SQLException e) {
            System.err.println("Error en base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando reporte de salas populares", e);
        }
    }
    
    public byte[] generarReportePDF(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            ReporteSalasPopularesDto reporteData = generarReporteSalasPopulares(fechaInicio, fechaFin);
            return generarPDF(reporteData, fechaInicio, fechaFin);
            
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF", e);
        }
    }
    
    private byte[] generarPDF(ReporteSalasPopularesDto reporte, LocalDate fechaInicio, LocalDate fechaFin) 
        throws JRException {
        
        InputStream reportStream = getClass().getClassLoader()
            .getResourceAsStream("reports/ReporteSalasPopulares.jasper");
        
        if (reportStream == null) {
            throw new JRException("No se encontró la plantilla ReporteSalasPopulares.jasper");
        }
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaInicio", java.sql.Date.valueOf(fechaInicio));
        parameters.put("fechaFin", java.sql.Date.valueOf(fechaFin));
        parameters.put("totalSalas", reporte.getSalas().size());
        
        JRDataSource dataSource = new JRBeanCollectionDataSource(reporte.getSalas());
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);
        
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    private List<SalaPopularDto> obtenerSalasPopulares(Connection conn, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        String sql = "SELECT s.nombre_sala, " +
                     "ROUND(AVG(cs.calificacion), 2) as promedio_calificacion " +
                     "FROM sala s " +
                     "JOIN comentario_sala cs ON s.id_sala = cs.id_sala " +
                     "WHERE cs.fecha_comentario BETWEEN ? AND ? " +
                     "AND cs.estado = 'ACTIVO' " +
                     "AND s.estado = 'ACTIVA' " +
                     "GROUP BY s.id_sala, s.nombre_sala " +
                     "HAVING COUNT(cs.id_comentario_sala) >= 1 " +
                     "ORDER BY promedio_calificacion DESC " +
                     "LIMIT 5";
        
        List<SalaPopularDto> salas = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SalaPopularDto sala = new SalaPopularDto(rs.getString("nombre_sala"));
                    sala.setPromedioCalificacion(rs.getDouble("promedio_calificacion"));
                    salas.add(sala);
                }
            }
        }
        
        return salas;
    }
    
    private List<UsuarioBoletosDto> obtenerUsuariosYBoletosPorSala(Connection conn, String nombreSala, 
                                                                  LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        String sql = "SELECT u.nombre_completo, COUNT(b.id_boleto) as cantidad_boletos " +
                     "FROM usuario u " +
                     "JOIN boleto b ON u.id_usuario = b.id_usuario " +
                     "JOIN funcion f ON b.id_funcion = f.id_funcion " +
                     "JOIN sala s ON f.id_sala = s.id_sala " +
                     "WHERE s.nombre_sala = ? " +
                     "AND b.fecha_compra BETWEEN ? AND ? " +
                     "AND u.estado = 'ACTIVO' " +
                     "GROUP BY u.id_usuario, u.nombre_completo " +
                     "ORDER BY cantidad_boletos DESC";
        
        List<UsuarioBoletosDto> usuarios = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreSala);
            stmt.setTimestamp(2, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            stmt.setTimestamp(3, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UsuarioBoletosDto usuario = new UsuarioBoletosDto(
                        rs.getString("nombre_completo"),
                        rs.getInt("cantidad_boletos")
                    );
                    usuarios.add(usuario);
                }
            }
        }
        
        return usuarios;
    }
    
    private void imprimirReporteConsola(ReporteSalasPopularesDto reporte, LocalDate fechaInicio, LocalDate fechaFin) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("REPORTE DE LAS 5 SALAS MÁS POPULARES POR CALIFICACIÓN");
        System.out.println("Período: " + fechaInicio + " hasta " + fechaFin);
        System.out.println("=".repeat(120));
        
        int ranking = 1;
        for (SalaPopularDto sala : reporte.getSalas()) {
            System.out.println("\nRANKING #" + ranking);
            System.out.println("-".repeat(80));
            System.out.printf("Sala: %s%n", sala.getNombreSala());
            System.out.printf(" Calificación Promedio: %.2f/5%n", sala.getPromedioCalificacion());
            System.out.printf("Total Boletos Vendidos: %d%n", sala.getTotalBoletosVendidos());
            
            if (sala.getUsuarios() != null && !sala.getUsuarios().isEmpty()) {
                System.out.println("\nUsuarios que compraron boletos:");
                System.out.printf("%-40s %-15s%n", "NOMBRE USUARIO", "BOLETOS COMPRADOS");
                System.out.println("-".repeat(60));
                
                for (UsuarioBoletosDto usuario : sala.getUsuarios()) {
                    System.out.printf("%-40s %-15d%n", 
                        truncate(usuario.getNombreUsuario(), 39), 
                        usuario.getCantidadBoletos());
                }
            } else {
                System.out.println("\n No se registraron compras de boletos en este período");
            }
            
            System.out.println("-".repeat(80));
            ranking++;
        }
        
        System.out.println("=".repeat(120));
    }
    
    private String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }
    
}
