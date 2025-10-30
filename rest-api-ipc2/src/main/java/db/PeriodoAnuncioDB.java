package db;

import models.anuncios.PeriodoAnuncio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author andy
 */
public class PeriodoAnuncioDB {

    private static final String SELECT_ALL_PERIODOS_ANUNCIO = 
        "SELECT id_periodo, nombre, dias_duracion, precio_duracion FROM periodos_anuncio";
    
    private static final String SELECT_PERIODO_ANUNCIO_BY_ID = 
        "SELECT id_periodo, nombre, dias_duracion, precio_duracion FROM periodos_anuncio WHERE id_periodo = ?";

    /** Obtiene todos los periodos de anuncio */
    public List<PeriodoAnuncio> getAllPeriodoAnuncio() {
        List<PeriodoAnuncio> list = new ArrayList<>();
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_PERIODOS_ANUNCIO)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                PeriodoAnuncio periodoAnuncio = mapToPeriodoAnuncio(rs);
                list.add(periodoAnuncio);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener los periodos de anuncio", e);
        }
        
        return list;
    }

    /** Obtiene periodo de anuncio por ID */
    public Optional<PeriodoAnuncio> getPeriodoAnuncioById(int idPeriodoAnuncio) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_PERIODO_ANUNCIO_BY_ID)) {
            stmt.setInt(1, idPeriodoAnuncio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                PeriodoAnuncio periodoAnuncio = mapToPeriodoAnuncio(rs);
                return Optional.of(periodoAnuncio);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el periodo de anuncio con ID: " + idPeriodoAnuncio, e);
        }
        
        return Optional.empty();
    }

    // ======== Helper Methods ========
    
    /** Mapea un ResultSet a un objeto PeriodoAnuncio */
    private PeriodoAnuncio mapToPeriodoAnuncio(ResultSet rs) throws SQLException {
        return new PeriodoAnuncio(
            rs.getInt("id_periodo"),
            rs.getString("nombre"),
            rs.getInt("dias_duracion"),
            rs.getBigDecimal("precio_duracion")
        );
    }
}