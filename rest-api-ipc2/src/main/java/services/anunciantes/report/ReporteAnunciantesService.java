package services.anunciantes.report;

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

public class ReporteAnunciantesService {
    
    public ReporteAnunciantesCompletoDto generarReporteAnunciantes(LocalDate fechaInicio, LocalDate fechaFin, Integer idAnunciante) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try {
            List<AnuncianteGananciasDto> anunciantes = obtenerAnunciantes(conn, fechaInicio, fechaFin, idAnunciante);
            
            for (AnuncianteGananciasDto anunciante : anunciantes) {
                List<AnuncioDto> anuncios = obtenerAnunciosPorAnunciante(conn, anunciante.getNombreAnunciante(), fechaInicio, fechaFin);
                anunciante.setAnuncios(anuncios);
                
                Double totalGastado = anuncios.stream()
                    .mapToDouble(AnuncioDto::getCostoTotal)
                    .sum();
                anunciante.setTotalGastado(totalGastado);
            }
            
            ReporteAnunciantesCompletoDto reporteCompleto = new ReporteAnunciantesCompletoDto(anunciantes);
            imprimirReporteConsola(reporteCompleto, fechaInicio, fechaFin, idAnunciante);
            
            return reporteCompleto;
            
        } catch (SQLException e) {
            System.err.println("Error en base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando reporte de anunciantes", e);
        }
    }
    
    public byte[] generarReportePDF(LocalDate fechaInicio, LocalDate fechaFin, Integer idAnunciante) {
        try {
            ReporteAnunciantesCompletoDto reporteData = generarReporteAnunciantes(fechaInicio, fechaFin, idAnunciante);
            return generarPDF(reporteData, fechaInicio, fechaFin, idAnunciante);
            
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF", e);
        }
    }
    
    private byte[] generarPDF(ReporteAnunciantesCompletoDto reporte, LocalDate fechaInicio, LocalDate fechaFin, Integer idAnunciante) 
        throws JRException {
        
        InputStream reportStream = getClass().getClassLoader()
            .getResourceAsStream("reports/ReporteAnunciantes.jasper");
        
        if (reportStream == null) {
            throw new JRException("No se encontró la plantilla ReporteAnunciantes.jasper");
        }
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaInicio", java.sql.Date.valueOf(fechaInicio));
        parameters.put("fechaFin", java.sql.Date.valueOf(fechaFin));
        parameters.put("totalAnunciantes", reporte.getTotalAnunciantes());
        parameters.put("totalGeneral", reporte.getTotalGeneral());
        parameters.put("filtroAnunciante", idAnunciante != null ? "Filtrado por anunciante: " + idAnunciante : "Todos los anunciantes");
        
        JRDataSource dataSource = new JRBeanCollectionDataSource(reporte.getAnunciantes());
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);
        
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    private List<AnuncianteGananciasDto> obtenerAnunciantes(Connection conn, LocalDate fechaInicio, LocalDate fechaFin, Integer idAnunciante) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT DISTINCT u.id_usuario, u.nombre_completo " +
            "FROM usuario u " +
            "JOIN anuncio a ON u.id_usuario = a.id_usuario " +
            "WHERE a.fecha_inicio BETWEEN ? AND ? " +
            "AND u.estado = 'ACTIVO' "
        );
        
        List<Object> parametros = new ArrayList<>();
        parametros.add(Timestamp.valueOf(fechaInicio.atStartOfDay()));
        parametros.add(Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
        
        if (idAnunciante != null) {
            sql.append("AND u.id_usuario = ? ");
            parametros.add(idAnunciante);
        }
        
        sql.append("ORDER BY u.nombre_completo");
        
        List<AnuncianteGananciasDto> anunciantes = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AnuncianteGananciasDto anunciante = new AnuncianteGananciasDto(
                        rs.getString("nombre_completo")
                    );
                    anunciantes.add(anunciante);
                }
            }
        }
        
        return anunciantes;
    }
    
    private List<AnuncioDto> obtenerAnunciosPorAnunciante(Connection conn, String nombreAnunciante, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        String sql = "SELECT a.titulo, a.fecha_inicio, a.fecha_fin, a.costo_total, a.estado " +
                     "FROM anuncio a " +
                     "JOIN usuario u ON a.id_usuario = u.id_usuario " +
                     "WHERE u.nombre_completo = ? " +
                     "AND a.fecha_inicio BETWEEN ? AND ? " +
                     "ORDER BY a.fecha_inicio DESC";
        
        List<AnuncioDto> anuncios = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreAnunciante);
            stmt.setTimestamp(2, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            stmt.setTimestamp(3, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AnuncioDto anuncio = new AnuncioDto(
                        rs.getString("titulo"),
                        rs.getTimestamp("fecha_inicio").toLocalDateTime(),
                        rs.getTimestamp("fecha_fin").toLocalDateTime(),
                        rs.getDouble("costo_total"),
                        rs.getString("estado")
                    );
                    anuncios.add(anuncio);
                }
            }
        }
        
        return anuncios;
    }
    
    private void imprimirReporteConsola(ReporteAnunciantesCompletoDto reporte, LocalDate fechaInicio, LocalDate fechaFin, Integer idAnunciante) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("REPORTE DE GANANCIAS POR ANUNCIANTE");
        System.out.println("Período: " + fechaInicio + " hasta " + fechaFin);
        if (idAnunciante != null) {
            System.out.println("Filtrado por anunciante: " + idAnunciante);
        }
        System.out.println("=".repeat(120));
        
        for (AnuncianteGananciasDto anunciante : reporte.getAnunciantes()) {
            System.out.println("\nANUNCIANTE: " + anunciante.getNombreAnunciante());
            System.out.println("Total Gastado: Q" + String.format("%.2f", anunciante.getTotalGastado()));
            System.out.println("-".repeat(80));
            
            if (anunciante.getAnuncios() != null && !anunciante.getAnuncios().isEmpty()) {
                System.out.println("Anuncios creados:");
                System.out.printf("%-30s %-15s %-15s %-12s %-10s%n", 
                    "TÍTULO", "FECHA INICIO", "FECHA FIN", "COSTO", "ESTADO");
                System.out.println("-".repeat(80));
                
                for (AnuncioDto anuncio : anunciante.getAnuncios()) {
                    System.out.printf("%-30s %-15s %-15s Q%-11.2f %-10s%n",
                        truncate(anuncio.getTitulo(), 29),
                        anuncio.getFechaInicio().toLocalDate().toString(),
                        anuncio.getFechaFin().toLocalDate().toString(),
                        anuncio.getCostoTotal(),
                        anuncio.getEstado()
                    );
                }
            } else {
                System.out.println("No tiene anuncios en este período");
            }
            
            System.out.println("-".repeat(80));
        }
        
        System.out.println("\nTOTALES GENERALES:");
        System.out.println("-".repeat(40));
        System.out.printf("%-20s: %d%n", "Total Anunciantes", reporte.getTotalAnunciantes());
        System.out.printf("%-20s: Q%.2f%n", "Total General", reporte.getTotalGeneral());
        
        System.out.println("=".repeat(120));
    }
    
    private String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }
}