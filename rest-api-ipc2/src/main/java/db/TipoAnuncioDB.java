package db;

import models.anuncios.TipoAnuncio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author andy
 */
public class TipoAnuncioDB {

    private static final String SELECT_ALL_TIPOS_ANUNCIO = 
        "SELECT id_tipo_anuncio, nombre, descripcion, precio_anuncio FROM tipos_anuncio";
    
    private static final String SELECT_TIPO_ANUNCIO_BY_ID = 
        "SELECT id_tipo_anuncio, nombre, descripcion, precio_anuncio FROM tipos_anuncio WHERE id_tipo_anuncio = ?";

    /** Obtiene todos los tipos de anuncio */
    public List<TipoAnuncio> getAllTiposAnuncio() {
        List<TipoAnuncio> list = new ArrayList<>();
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TIPOS_ANUNCIO)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TipoAnuncio tipoAnuncio = mapToTipoAnuncio(rs);
                list.add(tipoAnuncio);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            // Puedes lanzar una excepción personalizada si lo prefieres
            throw new RuntimeException("Error al obtener los tipos de anuncio", e);
        }
        
        return list;
    }

    /** Obtiene tipo de anuncio por ID */
    public Optional<TipoAnuncio> getTipoAnuncioById(int idTipoAnuncio) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_TIPO_ANUNCIO_BY_ID)) {
            stmt.setInt(1, idTipoAnuncio);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                TipoAnuncio tipoAnuncio = mapToTipoAnuncio(rs);
                return Optional.of(tipoAnuncio);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el tipo de anuncio con ID: " + idTipoAnuncio, e);
        }
        
        return Optional.empty();
    }

    // ======== Helper Methods ========
    
    /** Mapea un ResultSet a un objeto TipoAnuncio */
    private TipoAnuncio mapToTipoAnuncio(ResultSet rs) throws SQLException {
        return new TipoAnuncio(
            rs.getInt("id_tipo_anuncio"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            rs.getBigDecimal("precio_anuncio")
        );
    }
}