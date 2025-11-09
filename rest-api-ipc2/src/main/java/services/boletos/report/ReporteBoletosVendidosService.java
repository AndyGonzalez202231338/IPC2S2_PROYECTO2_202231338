/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.boletos.report;

import db.DBConnectionSingleton;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

/**
 *
 * @author andy
 */
public class ReporteBoletosVendidosService {
        public ReporteBoletosVendidosDto generarReporteBoletosVendidos(LocalDate fechaInicio, LocalDate fechaFin, 
        Integer idSala, Integer idCine) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try {
            List<SalaBoletosDto> salas = obtenerSalasConBoletos(conn, fechaInicio, fechaFin, idSala, idCine);
            
            for (SalaBoletosDto sala : salas) {
                List<BoletoUsuarioDto> boletosUsuarios = obtenerBoletosPorSala(conn, sala.getNombreSala(), fechaInicio, fechaFin);
                sala.setBoletosUsuarios(boletosUsuarios);
                
                // Calcular totales para la sala
                Integer totalBoletos = boletosUsuarios.stream()
                    .mapToInt(BoletoUsuarioDto::getCantidadBoletos)
                    .sum();
                Double totalRecaudado = boletosUsuarios.stream()
                    .mapToDouble(b -> b.getPrecioPagado() * b.getCantidadBoletos())
                    .sum();
                
                sala.setTotalBoletos(totalBoletos);
                sala.setTotalRecaudado(totalRecaudado);
            }
            
            ReporteBoletosVendidosDto reporteCompleto = new ReporteBoletosVendidosDto(salas);
            imprimirReporteConsola(reporteCompleto, fechaInicio, fechaFin, idSala, idCine);
            
            return reporteCompleto;
            
        } catch (SQLException e) {
            System.err.println("Error en base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando reporte de boletos vendidos", e);
        }
    }
    
    public byte[] generarReportePDF(LocalDate fechaInicio, LocalDate fechaFin, Integer idSala, Integer idCine) {
        try {
            ReporteBoletosVendidosDto reporteData = generarReporteBoletosVendidos(fechaInicio, fechaFin, idSala, idCine);
            return generarPDF(reporteData, fechaInicio, fechaFin, idSala, idCine);
            
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF", e);
        }
    }
    
    private byte[] generarPDF(ReporteBoletosVendidosDto reporte, LocalDate fechaInicio, LocalDate fechaFin, 
                             Integer idSala, Integer idCine) throws JRException {
        
        InputStream reportStream = getClass().getClassLoader()
            .getResourceAsStream("reports/ReporteBoletosVendidos.jasper");
        
        if (reportStream == null) {
            throw new JRException("No se encontró la plantilla ReporteBoletosVendidos.jasper");
        }
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaInicio", java.sql.Date.valueOf(fechaInicio));
        parameters.put("fechaFin", java.sql.Date.valueOf(fechaFin));
        parameters.put("totalSalas", reporte.getTotalSalas());
        parameters.put("totalBoletosVendidos", reporte.getTotalBoletosVendidos());
        parameters.put("totalGeneral", reporte.getTotalGeneral());
        
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
    
    private List<SalaBoletosDto> obtenerSalasConBoletos(Connection conn, LocalDate fechaInicio, LocalDate fechaFin, 
                                                       Integer idSala, Integer idCine) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT DISTINCT s.nombre_sala " +
            "FROM sala s " +
            "JOIN funcion f ON s.id_sala = f.id_sala " +
            "JOIN boleto b ON f.id_funcion = b.id_funcion " +
            "WHERE b.fecha_compra BETWEEN ? AND ? " +
            "AND s.estado = 'ACTIVA' " +
            "AND f.estado != 'CANCELADA' "
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
        
        sql.append("ORDER BY s.nombre_sala");
        
        List<SalaBoletosDto> salas = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SalaBoletosDto sala = new SalaBoletosDto(
                        rs.getString("nombre_sala")
                    );
                    salas.add(sala);
                }
            }
        }
        
        return salas;
    }
    
    private List<BoletoUsuarioDto> obtenerBoletosPorSala(Connection conn, String nombreSala, 
                                                        LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        String sql = "SELECT u.nombre_completo, " +
                     "       COUNT(b.id_boleto) as cantidad_boletos, " +
                     "       b.precio_pagado, " +
                     "       MAX(b.fecha_compra) as fecha_compra " +
                     "FROM boleto b " +
                     "JOIN funcion f ON b.id_funcion = f.id_funcion " +
                     "JOIN sala s ON f.id_sala = s.id_sala " +
                     "JOIN usuario u ON b.id_usuario = u.id_usuario " +
                     "WHERE s.nombre_sala = ? " +
                     "AND b.fecha_compra BETWEEN ? AND ? " +
                     "AND u.estado = 'ACTIVO' " +
                     "GROUP BY u.nombre_completo, b.precio_pagado " +
                     "ORDER BY cantidad_boletos DESC, u.nombre_completo";
        
        List<BoletoUsuarioDto> boletos = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreSala);
            stmt.setTimestamp(2, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            stmt.setTimestamp(3, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BoletoUsuarioDto boleto = new BoletoUsuarioDto(
                        rs.getString("nombre_completo"),
                        rs.getInt("cantidad_boletos"),
                        rs.getDouble("precio_pagado"),
                        rs.getTimestamp("fecha_compra").toLocalDateTime()
                    );
                    boletos.add(boleto);
                }
            }
        }
        
        return boletos;
    }
    
    private void imprimirReporteConsola(ReporteBoletosVendidosDto reporte, LocalDate fechaInicio, LocalDate fechaFin, 
                                       Integer idSala, Integer idCine) {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("REPORTE DE BOLETOS VENDIDOS");
        System.out.println("Período: " + fechaInicio + " hasta " + fechaFin);
        if (idSala != null) {
            System.out.println("Filtrado por sala: " + idSala);
        } else if (idCine != null) {
            System.out.println("Filtrado por cine: " + idCine);
        } else {
            System.out.println("Todas las salas");
        }
        System.out.println("=".repeat(100));
        
        for (SalaBoletosDto sala : reporte.getSalas()) {
            System.out.println("\nSALA: " + sala.getNombreSala());
            System.out.println("Total Recaudado: Q" + String.format("%.2f", sala.getTotalRecaudado()));
            System.out.println("Total Boletos: " + sala.getTotalBoletos());
            System.out.println("-".repeat(80));
            
            if (sala.getBoletosUsuarios() != null && !sala.getBoletosUsuarios().isEmpty()) {
                System.out.println("👥 Boletos por Usuario:");
                System.out.printf("%-30s %-10s %-12s %-20s%n", "USUARIO", "CANTIDAD", "PRECIO", "FECHA COMPRA");
                System.out.println("-".repeat(80));
                
                for (BoletoUsuarioDto boleto : sala.getBoletosUsuarios()) {
                    System.out.printf("%-30s %-10d Q%-11.2f %-20s%n",
                        truncate(boleto.getNombreUsuario(), 29),
                        boleto.getCantidadBoletos(),
                        boleto.getPrecioPagado(),
                        boleto.getFechaCompra().toLocalDate().toString()
                    );
                }
            } else {
                System.out.println("No tiene boletos vendidos en este período");
            }
            
            System.out.println("-".repeat(80));
        }
        
        System.out.println("\nTOTALES GENERALES:");
        System.out.println("-".repeat(40));
        System.out.printf("%-20s: %d%n", "Total Salas", reporte.getTotalSalas());
        System.out.printf("%-20s: %d%n", "Total Boletos", reporte.getTotalBoletosVendidos());
        System.out.printf("%-20s: Q%.2f%n", "Total General", reporte.getTotalGeneral());
        
        System.out.println("=".repeat(100));
    }
    
    private String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }
}
