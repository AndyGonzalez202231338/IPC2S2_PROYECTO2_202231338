package services.anuncios.report;

import db.DBConnectionSingleton;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReporteAnunciosService {
    
    // Método existente para consola
    public ReporteAnunciosCompletoDto generarReporteAnuncios(LocalDate fechaInicio, LocalDate fechaFin, 
                                                           Integer idTipoAnuncio, Integer idPeriodo) {
        
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try {
            List<AnuncioReporteDto> anuncios = obtenerAnuncios(conn, fechaInicio, fechaFin, idTipoAnuncio, idPeriodo);
            ReporteAnunciosCompletoDto reporteCompleto = new ReporteAnunciosCompletoDto(anuncios);
            
            // Imprimir en consola
            imprimirReporteConsola(reporteCompleto, fechaInicio, fechaFin, idTipoAnuncio, idPeriodo);
            
            return reporteCompleto;
            
        } catch (SQLException e) {
            System.err.println("Error en base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando reporte de anuncios", e);
        }
    }
    
    // NUEVO MÉTODO para generar PDF
    public byte[] generarReportePDF(LocalDate fechaInicio, LocalDate fechaFin, 
                                  Integer idTipoAnuncio, Integer idPeriodo) {
        
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try {
            // 1. Obtener datos para el reporte
            List<AnuncioReporteDto> anuncios = obtenerAnuncios(conn, fechaInicio, fechaFin, idTipoAnuncio, idPeriodo);
            ReporteAnunciosCompletoDto reporteCompleto = new ReporteAnunciosCompletoDto(anuncios);
            
            // 2. Imprimir en consola (para debug)
            imprimirReporteConsola(reporteCompleto, fechaInicio, fechaFin, idTipoAnuncio, idPeriodo);
            
            // 3. Generar PDF con JasperReports
            return generarPDF(reporteCompleto, fechaInicio, fechaFin, idTipoAnuncio, idPeriodo);
            
        } catch (SQLException e) {
            System.err.println("Error en base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando reporte de anuncios", e);
        } catch (JRException e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF", e);
        }
    }
    
    private byte[] generarPDF(ReporteAnunciosCompletoDto reporte, LocalDate fechaInicio, LocalDate fechaFin,
                            Integer idTipoAnuncio, Integer idPeriodo) throws JRException {
        
        InputStream reportStream = getClass().getClassLoader()
                .getResourceAsStream("reports/ReporteAnuncios.jasper");
        
        if (reportStream == null) {
            throw new JRException("No se encontró la plantilla ReporteAnuncios.jasper");
        }
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaInicio", java.sql.Date.valueOf(fechaInicio));
        parameters.put("fechaFin", java.sql.Date.valueOf(fechaFin));
        parameters.put("tipoAnuncioFiltro", idTipoAnuncio != null ? "Tipo " + idTipoAnuncio : "Todos");
        parameters.put("periodoFiltro", idPeriodo != null ? "Período " + idPeriodo : "Todos");
        parameters.put("totalAnuncios", reporte.getTotalAnuncios());
        parameters.put("totalCosto", reporte.getTotalCosto());
        
        JRDataSource dataSource = new JRBeanCollectionDataSource(reporte.getAnuncios());
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);
        
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    // Los demás métodos permanecen igual...
    private List<AnuncioReporteDto> obtenerAnuncios(Connection conn, LocalDate fechaInicio, LocalDate fechaFin,
                                                  Integer idTipoAnuncio, Integer idPeriodo) throws SQLException {
        
        StringBuilder sql = new StringBuilder(
            "SELECT a.id_anuncio, a.titulo, ta.nombre as tipo_anuncio, " +
            "p.nombre as periodo, a.fecha_inicio, a.fecha_fin, " +
            "a.costo_total, a.estado, a.id_usuario " +
            "FROM anuncio a " +
            "JOIN tipos_anuncio ta ON a.id_tipo_anuncio = ta.id_tipo_anuncio " +
            "JOIN periodos_anuncio p ON a.id_periodo = p.id_periodo " +
            "WHERE (a.fecha_inicio BETWEEN ? AND ? " +
            "OR a.fecha_fin BETWEEN ? AND ? " +
            "OR (a.fecha_inicio <= ? AND a.fecha_fin >= ?)) "
        );
        
        List<Object> parametros = new ArrayList<>();
        parametros.add(Timestamp.valueOf(fechaInicio.atStartOfDay()));
        parametros.add(Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
        parametros.add(Timestamp.valueOf(fechaInicio.atStartOfDay()));
        parametros.add(Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
        parametros.add(Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
        parametros.add(Timestamp.valueOf(fechaInicio.atStartOfDay()));
        
        if (idTipoAnuncio != null) {
            sql.append(" AND a.id_tipo_anuncio = ?");
            parametros.add(idTipoAnuncio);
        }
        
        if (idPeriodo != null) {
            sql.append(" AND a.id_periodo = ?");
            parametros.add(idPeriodo);
        }
        
        sql.append(" ORDER BY a.fecha_inicio DESC");
        
        List<AnuncioReporteDto> anuncios = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AnuncioReporteDto anuncio = new AnuncioReporteDto(
                        rs.getInt("id_anuncio"),
                        rs.getString("titulo"),
                        rs.getString("tipo_anuncio"),
                        rs.getString("periodo"),
                        rs.getTimestamp("fecha_inicio").toLocalDateTime(),
                        rs.getTimestamp("fecha_fin").toLocalDateTime(),
                        rs.getDouble("costo_total"),
                        rs.getString("estado"),
                        "Usuario " + rs.getInt("id_usuario")
                    );
                    anuncios.add(anuncio);
                }
            }
        }
        
        return anuncios;
    }
    
    private void imprimirReporteConsola(ReporteAnunciosCompletoDto reporte, LocalDate fechaInicio, 
                                      LocalDate fechaFin, Integer idTipoAnuncio, Integer idPeriodo) {
        
        System.out.println("\n" + "=".repeat(130));
        System.out.println("REPORTE DE ANUNCIOS COMPRADOS");
        System.out.println("Período: " + fechaInicio + " hasta " + fechaFin);
        
        if (idTipoAnuncio != null) {
            System.out.println("Filtrado por tipo de anuncio: " + idTipoAnuncio);
        }
        if (idPeriodo != null) {
            System.out.println("Filtrado por período: " + idPeriodo);
        }
        
        System.out.println("=".repeat(130));
        
        System.out.printf("%-8s %-25s %-18s %-12s %-15s %-15s %-12s %-10s %-12s%n", 
            "ID", "TÍTULO", "TIPO", "PERÍODO", "FECHA INICIO", "FECHA FIN", 
            "COSTO", "ESTADO", "USUARIO");
        System.out.println("-".repeat(130));
        
        for (AnuncioReporteDto anuncio : reporte.getAnuncios()) {
            System.out.printf("%-8d %-25s %-18s %-12s %-15s %-15s $%-11.2f %-10s %-12s%n",
                anuncio.getIdAnuncio(),
                truncate(anuncio.getTitulo(), 24),
                truncate(anuncio.getTipoAnuncio(), 17),
                anuncio.getPeriodo(),
                anuncio.getFechaInicio().toLocalDate().toString(),
                anuncio.getFechaFin().toLocalDate().toString(),
                anuncio.getCostoTotal(),
                anuncio.getEstado(),
                truncate(anuncio.getUsuario(), 11));
        }
        
        System.out.println("-".repeat(130));
        
        System.out.printf("%n%-70s %-12s: %d%n", "", "Total Anuncios", reporte.getTotalAnuncios());
        System.out.printf("%-70s %-12s: $%,.2f%n", "", "Total Costo", reporte.getTotalCosto());
        
        System.out.println("=".repeat(130));
    }
    
    private String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }
}