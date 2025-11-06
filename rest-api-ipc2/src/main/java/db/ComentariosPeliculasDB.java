package db;

import models.comentario.ComentarioPelicula;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComentariosPeliculasDB {

    private static final String CREAR_COMENTARIO_PELICULA_QUERY = 
        "INSERT INTO comentario_pelicula (id_pelicula, id_usuario, comentario, calificacion, fecha_comentario, estado) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String ENCONTRAR_COMENTARIO_POR_ID_QUERY = 
        "SELECT * FROM comentario_pelicula WHERE id_comentario_pelicula = ?";
    
    private static final String COMENTARIOS_POR_PELICULA_QUERY = 
        "SELECT * FROM comentario_pelicula WHERE id_pelicula = ?";
    
    private static final String COMENTARIOS_POR_USUARIO_Y_PELICULA_QUERY = 
        "SELECT * FROM comentario_pelicula WHERE id_usuario = ? AND id_pelicula = ?";
    
    private static final String TODOS_LOS_COMENTARIOS_QUERY = 
        "SELECT * FROM comentario_pelicula";
    
    private static final String EXISTE_COMENTARIO_POR_USUARIO_QUERY = 
        "SELECT 1 FROM comentario_pelicula WHERE id_usuario = ? AND id_pelicula = ?";

    /** Crea un nuevo comentario de película en la base de datos */
    public ComentarioPelicula createComentarioPelicula(ComentarioPelicula newComentarioPelicula) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(CREAR_COMENTARIO_PELICULA_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            insert.setInt(1, newComentarioPelicula.getIdPelicula());
            insert.setInt(2, newComentarioPelicula.getIdUsuario());
            insert.setString(3, newComentarioPelicula.getComentario());
            insert.setInt(4, newComentarioPelicula.getCalificacion());
            insert.setTimestamp(5, Timestamp.valueOf(newComentarioPelicula.getFechaComentario()));
            insert.setString(6, newComentarioPelicula.getEstado());
            
            int affectedRows = insert.executeUpdate();
            
            if (affectedRows > 0) {
                // Obtener el ID generado
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newComentarioPelicula.setIdComentarioPelicula(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newComentarioPelicula;
    }

    /** Verifica si existe un comentario para un usuario y película específicos */
    public boolean existsComentarioPelicula(int idUsuario, int idPelicula) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_COMENTARIO_POR_USUARIO_QUERY)) {
            query.setInt(1, idUsuario);
            query.setInt(2, idPelicula);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Obtiene todos los comentarios de películas */
    public List<ComentarioPelicula> getAllComentariosPeliculas() {
        List<ComentarioPelicula> comentarios = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(TODOS_LOS_COMENTARIOS_QUERY)) {
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ComentarioPelicula comentario = mapResultSetToComentarioPelicula(resultSet);
                comentarios.add(comentario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comentarios;
    }

    /** Obtiene un comentario por ID */
    public Optional<ComentarioPelicula> getById(int idComentarioPelicula) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(ENCONTRAR_COMENTARIO_POR_ID_QUERY)) {
            query.setInt(1, idComentarioPelicula);
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()) {
                ComentarioPelicula comentario = mapResultSetToComentarioPelicula(resultSet);
                return Optional.of(comentario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Obtiene todos los comentarios de una película específica */
    public List<ComentarioPelicula> getComentariosByPelicula(int idPelicula) {
        List<ComentarioPelicula> comentarios = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(COMENTARIOS_POR_PELICULA_QUERY)) {
            query.setInt(1, idPelicula);
            ResultSet resultSet = query.executeQuery();
            
            while (resultSet.next()) {
                ComentarioPelicula comentario = mapResultSetToComentarioPelicula(resultSet);
                comentarios.add(comentario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comentarios;
    }

    /** Obtiene comentarios de un usuario específico para una película específica */
    public List<ComentarioPelicula> getComentariosByUsuarioAndPelicula(int idUsuario, int idPelicula) {
        List<ComentarioPelicula> comentarios = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(COMENTARIOS_POR_USUARIO_Y_PELICULA_QUERY)) {
            query.setInt(1, idUsuario);
            query.setInt(2, idPelicula);
            ResultSet resultSet = query.executeQuery();
            
            while (resultSet.next()) {
                ComentarioPelicula comentario = mapResultSetToComentarioPelicula(resultSet);
                comentarios.add(comentario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comentarios;
    }

    /** Método auxiliar para mapear un ResultSet a un objeto ComentarioPelicula */
    private ComentarioPelicula mapResultSetToComentarioPelicula(ResultSet resultSet) throws SQLException {
        return new ComentarioPelicula(
            resultSet.getInt("id_comentario_pelicula"),
            resultSet.getInt("id_pelicula"),
            resultSet.getInt("id_usuario"),
            resultSet.getString("comentario"),
            resultSet.getInt("calificacion"),
            resultSet.getTimestamp("fecha_comentario").toLocalDateTime(),
            resultSet.getString("estado")
        );
    }
}