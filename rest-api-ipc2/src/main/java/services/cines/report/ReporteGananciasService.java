package services.cines.report;

import db.DBConnectionSingleton;
import dtos.cines.report.ReporteGananciasCineDto;
import dtos.cines.report.ReporteGananciasCompletoDto;
import java.io.InputStream;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteGananciasService {
    
    public byte[] generarReportePDF(LocalDate fechaInicio, LocalDate fechaFin) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try {
            // 1. Obtener datos para el reporte
            List<ReporteGananciasCineDto> cines = obtenerCinesActivos(conn);
            calcularCostosDiarios(conn, cines, fechaInicio, fechaFin);
            calcularPagosBloqueoAnuncios(conn, cines, fechaInicio, fechaFin);
            Double totalPorAnuncios = calcularTotalPorAnuncios(conn, fechaInicio, fechaFin);
            
            ReporteGananciasCompletoDto reporteCompleto = new ReporteGananciasCompletoDto(cines, totalPorAnuncios);
            
            // 2. Imprimir en consola (para debug)
            imprimirReporteConsola(reporteCompleto, fechaInicio, fechaFin);
            
            // 3. Generar PDF con JasperReports
            return generarPDF(reporteCompleto, fechaInicio, fechaFin);
            
        } catch (SQLException e) {
            System.err.println("Error en base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando reporte", e);
        } catch (JRException e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF", e);
        }
    }
    
    private byte[] generarPDF(ReporteGananciasCompletoDto reporte, LocalDate fechaInicio, LocalDate fechaFin) 
        throws JRException {
    
    InputStream reportStream = getClass().getClassLoader()
            .getResourceAsStream("reports/ReporteGanancias.jasper");
    
    // Convertir LocalDate a java.sql.Date
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("fechaInicio", java.sql.Date.valueOf(fechaInicio));
    parameters.put("fechaFin", java.sql.Date.valueOf(fechaFin));
    parameters.put("totalCostosTodosCines", reporte.getTotalCostosTodosCines());
    parameters.put("totalIngresosAnuncios", reporte.getTotalIngresosAnuncios());
    parameters.put("totalPorAnuncios", reporte.getTotalPorAnuncios());
    parameters.put("totalGanancia", reporte.getTotalGanancia());
    
    JRDataSource dataSource = new JRBeanCollectionDataSource(reporte.getCines());
    JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);
    
    return JasperExportManager.exportReportToPdf(jasperPrint);
}
    
    // Los demás métodos (obtenerCinesActivos, calcularCostosDiarios, etc.) 
    // permanecen igual que en la versión anterior...
    private List<ReporteGananciasCineDto> obtenerCinesActivos(Connection conn) throws SQLException {
        List<ReporteGananciasCineDto> cines = new ArrayList<>();
        String sql = "SELECT id_cine, nombre FROM cine WHERE estado = 'ACTIVO'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                cines.add(new ReporteGananciasCineDto(
                    rs.getString("nombre"),
                    rs.getInt("id_cine")
                ));
            }
        }
        
        return cines;
    }
    
    private void calcularCostosDiarios(Connection conn, List<ReporteGananciasCineDto> cines, 
                                     LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        
        String sql = "SELECT c.id_cine, c.nombre, " +
                     "COALESCE(SUM(cc.costo_diario * " +
                     "   GREATEST( " +
                     "       DATEDIFF( " +
                     "           LEAST(IFNULL(cc.fecha_fin, CURDATE()), ?), " +
                     "           GREATEST(cc.fecha_inicio, ?) " +
                     "       ) + 1, 0" +
                     "   )" +
                     "), 0) as costo_total " +
                     "FROM cine c " +
                     "LEFT JOIN costos_cine cc ON c.id_cine = cc.id_cine " +
                     "AND cc.fecha_inicio <= ? " +
                     "AND (cc.fecha_fin IS NULL OR cc.fecha_fin >= ?) " +
                     "WHERE c.estado = 'ACTIVO' " +
                     "GROUP BY c.id_cine, c.nombre";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fechaFin));
            stmt.setDate(2, Date.valueOf(fechaInicio));
            stmt.setDate(3, Date.valueOf(fechaFin));
            stmt.setDate(4, Date.valueOf(fechaInicio));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idCine = rs.getInt("id_cine");
                    Double costoTotal = rs.getDouble("costo_total");
                    
                    for (ReporteGananciasCineDto cine : cines) {
                        if (cine.getIdCine() == idCine) {
                            cine.setCostoDiarioTotal(costoTotal);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private void calcularPagosBloqueoAnuncios(Connection conn, List<ReporteGananciasCineDto> cines,
                                            LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        
        String sql = "SELECT c.id_cine, c.nombre, " +
                     "COALESCE(SUM(bp.costo_total), 0) as total_pagos " +
                     "FROM cine c " +
                     "LEFT JOIN bloqueo_publicidad bp ON c.id_cine = bp.id_cine " +
                     "AND DATE(bp.fecha_pago) BETWEEN ? AND ? " +
                     "WHERE c.estado = 'ACTIVO' " +
                     "GROUP BY c.id_cine, c.nombre";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fechaInicio));
            stmt.setDate(2, Date.valueOf(fechaFin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idCine = rs.getInt("id_cine");
                    Double totalPagos = rs.getDouble("total_pagos");
                    
                    for (ReporteGananciasCineDto cine : cines) {
                        if (cine.getIdCine() == idCine) {
                            cine.setPagoBloqueoAnuncios(totalPagos);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private Double calcularTotalPorAnuncios(Connection conn, LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        String sql = "SELECT COALESCE(SUM(costo_total), 0) as total_anuncios " +
                     "FROM anuncio " +
                     "WHERE ((fecha_inicio BETWEEN ? AND ?) " +
                     "OR (fecha_fin BETWEEN ? AND ?) " +
                     "OR (fecha_inicio <= ? AND fecha_fin >= ?)) " +
                     "AND estado = 'ACTIVO'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            stmt.setTimestamp(3, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            stmt.setTimestamp(4, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            stmt.setTimestamp(5, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            stmt.setTimestamp(6, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_anuncios");
                }
            }
        }
        
        return 0.0;
    }
    
    private void imprimirReporteConsola(ReporteGananciasCompletoDto reporte, LocalDate fechaInicio, LocalDate fechaFin) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("REPORTE DE GANANCIAS - " + fechaInicio + " hasta " + fechaFin);
        System.out.println("=".repeat(120));
        
        System.out.printf("%-25s %-18s %-28s %-20s%n", 
            "NOMBRE CINE", "COSTO DIARIO", "PAGO BLOQUEO ANUNCIOS", "TOTAL COSTO CINE");
        System.out.println("-".repeat(95));
        
        for (ReporteGananciasCineDto cine : reporte.getCines()) {
            System.out.printf("%-25s Q%-17.2f Q%-27.2f Q%-19.2f%n",
                cine.getNombreCine(),
                cine.getCostoDiarioTotal(),
                cine.getPagoBloqueoAnuncios(),
                cine.getTotalCostoCine());
        }
        
        System.out.println("-".repeat(95));
        
        System.out.println("\nTOTALES GENERALES:");
        System.out.println("-".repeat(60));
        System.out.printf("%-35s: Q%,.2f%n", "Total Costos Todos Cines", reporte.getTotalCostosTodosCines());
        System.out.printf("%-35s: Q%,.2f%n", "Total Ingresos por Anuncios", reporte.getTotalIngresosAnuncios());
        System.out.printf("%-35s: Q%,.2f%n", "Total por Anuncios", reporte.getTotalPorAnuncios());
        System.out.printf("%-35s: Q%,.2f%n", "TOTAL GANANCIA", reporte.getTotalGanancia());
        
        System.out.println("=".repeat(120));
    }
}