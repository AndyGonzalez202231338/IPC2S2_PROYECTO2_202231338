package services.salas.peliculas.report;

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

public class ReportePeliculasProyectadasService {
    
    public ReportePeliculasProyectadasDto generarReportePeliculasProyectadas(LocalDate fechaInicio, LocalDate fechaFin, 
                                                                            Integer idSala, Integer idCine) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try {
            List<SalaPeliculasDto> salas = obtenerSalasConPeliculas(conn, fechaInicio, fechaFin, idSala, idCine);
            String filtroAplicado = generarTextoFiltro(idSala, idCine);
            
            ReportePeliculasProyectadasDto reporte = new ReportePeliculasProyectadasDto(salas, filtroAplicado);
            imprimirReporteConsola(reporte, fechaInicio, fechaFin);
            
            return reporte;
            
        } catch (SQLException e) {
            System.err.println("Error en base de datos: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando reporte de películas proyectadas", e);
        }
    }
    
    public byte[] generarReportePDF(LocalDate fechaInicio, LocalDate fechaFin, Integer idSala, Integer idCine) {
        try {
            ReportePeliculasProyectadasDto reporteData = generarReportePeliculasProyectadas(fechaInicio, fechaFin, idSala, idCine);
            return generarPDF(reporteData, fechaInicio, fechaFin);
            
        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando PDF", e);
        }
    }
    
    private byte[] generarPDF(ReportePeliculasProyectadasDto reporte, LocalDate fechaInicio, LocalDate fechaFin) 
        throws JRException {
        
        InputStream reportStream = getClass().getClassLoader()
            .getResourceAsStream("reports/ReportePeliculasProyectadas.jasper");
        
        if (reportStream == null) {
            throw new JRException("No se encontró la plantilla ReportePeliculasProyectadas.jasper");
        }
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaInicio", java.sql.Date.valueOf(fechaInicio));
        parameters.put("fechaFin", java.sql.Date.valueOf(fechaFin));
        parameters.put("totalSalas", reporte.getTotalSalas());
        parameters.put("totalPeliculas", reporte.getTotalPeliculas());
        parameters.put("filtroAplicado", reporte.getFiltroAplicado());
        
        JRDataSource dataSource = new JRBeanCollectionDataSource(reporte.getSalas());
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);
        
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    private List<SalaPeliculasDto> obtenerSalasConPeliculas(Connection conn, LocalDate fechaInicio, LocalDate fechaFin, 
                                                           Integer idSala, Integer idCine) throws SQLException {
        // Primero obtener las salas según el filtro
        List<SalaPeliculasDto> salas = obtenerSalas(conn, idSala, idCine);
        
        // Para cada sala, obtener sus películas proyectadas
        for (SalaPeliculasDto sala : salas) {
            List<PeliculaProyectadaDto> peliculas = obtenerPeliculasPorSala(conn, sala.getNombreSala(), fechaInicio, fechaFin);
            sala.setPeliculas(peliculas);
        }
        
        return salas;
    }
    
    private List<SalaPeliculasDto> obtenerSalas(Connection conn, Integer idSala, Integer idCine) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT DISTINCT s.id_sala, s.nombre_sala " +
            "FROM sala s " +
            "WHERE s.estado = 'ACTIVA' "
        );
        
        List<Object> parametros = new ArrayList<>();
        
        if (idSala != null) {
            sql.append("AND s.id_sala = ? ");
            parametros.add(idSala);
        } else if (idCine != null) {
            sql.append("AND s.id_cine = ? ");
            parametros.add(idCine);
        }
        
        sql.append("ORDER BY s.nombre_sala");
        
        List<SalaPeliculasDto> salas = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SalaPeliculasDto sala = new SalaPeliculasDto(
                        rs.getString("nombre_sala")
                    );
                    salas.add(sala);
                }
            }
        }
        
        return salas;
    }
    
    private List<PeliculaProyectadaDto> obtenerPeliculasPorSala(Connection conn, String nombreSala, 
                                                               LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        String sql = "SELECT p.titulo, f.fecha_hora_funcion " +
                     "FROM funcion f " +
                     "JOIN pelicula p ON f.id_pelicula = p.id_pelicula " +
                     "JOIN sala s ON f.id_sala = s.id_sala " +
                     "WHERE s.nombre_sala = ? " +
                     "AND f.fecha_hora_funcion BETWEEN ? AND ? " +
                     "AND f.estado = 'PROGRAMADA' " +
                     "AND p.estado = 'ACTIVA' " +
                     "ORDER BY f.fecha_hora_funcion ASC";
        
        List<PeliculaProyectadaDto> peliculas = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreSala);
            stmt.setTimestamp(2, Timestamp.valueOf(fechaInicio.atStartOfDay()));
            stmt.setTimestamp(3, Timestamp.valueOf(fechaFin.atTime(23, 59, 59)));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PeliculaProyectadaDto pelicula = new PeliculaProyectadaDto(
                        rs.getString("titulo"),
                        rs.getTimestamp("fecha_hora_funcion").toLocalDateTime()
                    );
                    peliculas.add(pelicula);
                }
            }
        }
        
        return peliculas;
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
    
    private void imprimirReporteConsola(ReportePeliculasProyectadasDto reporte, LocalDate fechaInicio, LocalDate fechaFin) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("REPORTE DE PELÍCULAS PROYECTADAS EN SALAS");
        System.out.println("Período: " + fechaInicio + " hasta " + fechaFin);
        System.out.println("Filtro: " + reporte.getFiltroAplicado());
        System.out.println("=".repeat(120));
        
        if (reporte.getSalas() != null && !reporte.getSalas().isEmpty()) {
            for (SalaPeliculasDto sala : reporte.getSalas()) {
                System.out.println("\nSALA: " + sala.getNombreSala());
                System.out.println("-".repeat(80));
                
                if (sala.getPeliculas() != null && !sala.getPeliculas().isEmpty()) {
                    System.out.printf("%-40s %-20s%n", "PELÍCULA", "FECHA Y HORA");
                    System.out.println("-".repeat(80));
                    
                    for (PeliculaProyectadaDto pelicula : sala.getPeliculas()) {
                        System.out.printf("%-40s %-20s%n",
                            truncate(pelicula.getTituloPelicula(), 39),
                            pelicula.getFechaHoraFuncion().toString()
                        );
                    }
                } else {
                    System.out.println("No tiene películas programadas en este período");
                }
                
                System.out.println("-".repeat(80));
            }
        } else {
            System.out.println("No hay salas con películas programadas en este período");
        }
        
        System.out.println("\nTOTALES:");
        System.out.println("-".repeat(40));
        System.out.printf("%-20s: %d%n", "Total Salas", reporte.getTotalSalas());
        System.out.printf("%-20s: %d%n", "Total Películas", reporte.getTotalPeliculas());
        System.out.println("=".repeat(120));
    }
    
    private String truncate(String text, int length) {
        if (text == null) return "";
        return text.length() > length ? text.substring(0, length - 3) + "..." : text;
    }
}