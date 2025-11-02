package db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.publicidad.Publicidad;

/**
 *
 * @author andy
 */
public class PublicidadDB {
    
    private static final String CREAR_PUBLICIDAD_QUERY = 
        "INSERT INTO publicidad (id_anuncio, id_usuario, precio_bloqueo, estado) VALUES (?, ?, ?, 'ACTIVO')";
    
    private static final String OBTENER_TODAS_PUBLICIDADES_QUERY = 
        "SELECT * FROM publicidad";
    
    private static final String OBTENER_PUBLICIDAD_POR_ANUNCIO_QUERY = 
        "SELECT * FROM publicidad WHERE id_anuncio = ?";
    
    private static final String EXISTE_PUBLICIDAD_POR_ANUNCIO_QUERY = 
        "SELECT 1 FROM publicidad WHERE id_anuncio = ?";
    
    
    private static final String UPDATE_ESTADO =
        "UPDATE publicidad SET estado = ? WHERE id_publicidad = ?";
    
    
    /** Crea una nueva publicidad */
    public Publicidad crearPublicidad(int idAnuncio, int idUsuario, BigDecimal precioBloqueo) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(CREAR_PUBLICIDAD_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            insert.setInt(1, idAnuncio);
            insert.setInt(2, idUsuario);
            insert.setBigDecimal(3, precioBloqueo);
            
            int affectedRows = insert.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return new Publicidad(
                            generatedKeys.getInt(1),
                            idAnuncio,
                            idUsuario,
                            precioBloqueo,
                            "ACTIVO"
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /** Obtiene publicidad por ID de anuncio */
    public Optional<Publicidad> getPublicidadByAnuncioId(int idAnuncio) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(OBTENER_PUBLICIDAD_POR_ANUNCIO_QUERY)) {
            query.setInt(1, idAnuncio);
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()) {
                Publicidad publicidad = mapResultSetToPublicidad(resultSet);
                return Optional.of(publicidad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Obtiene todas las publicidades */
    public List<Publicidad> getAllPublicidades() {
        List<Publicidad> publicidades = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        
        try (PreparedStatement query = connection.prepareStatement(OBTENER_TODAS_PUBLICIDADES_QUERY)) {
            ResultSet resultSet = query.executeQuery();
            
            while (resultSet.next()) {
                Publicidad publicidad = mapResultSetToPublicidad(resultSet);
                publicidades.add(publicidad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicidades;
    }

    /** Verifica si ya existe publicidad para un anuncio */
    public boolean existePublicidadPorAnuncio(int idAnuncio) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_PUBLICIDAD_POR_ANUNCIO_QUERY)) {
            query.setInt(1, idAnuncio);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Publicidad mapResultSetToPublicidad(ResultSet resultSet) throws SQLException {
        return new Publicidad(
            resultSet.getInt("id_publicidad"),
            resultSet.getInt("id_anuncio"),
            resultSet.getInt("id_usuario"),
            resultSet.getBigDecimal("precio_bloqueo"),
            resultSet.getString("estado")
        );
    }
    
     /** Actualiza el estado de una publicidad */
    public boolean actualizarEstado(int idPublicidad, String nuevoEstado) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_ESTADO)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idPublicidad);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
}
