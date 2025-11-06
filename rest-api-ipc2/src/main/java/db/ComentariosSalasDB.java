package db;

import models.comentario.ComentarioSala;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComentariosSalasDB {

    private static final String CREAR_COMENTARIO_SALA_QUERY = 
        "INSERT INTO comentario_sala (id_sala, id_usuario, comentario, calificacion, fecha_comentario, estado) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String ENCONTRAR_COMENTARIO_POR_ID_QUERY = 
        "SELECT * FROM comentario_sala WHERE id_comentario_sala = ?";
    
    private static final String COMENTARIOS_POR_SALA_QUERY = 
        "SELECT * FROM comentario_sala WHERE id_sala = ?";
    
    private static final String COMENTARIOS_POR_USUARIO_Y_SALA_QUERY = 
        "SELECT * FROM comentario_sala WHERE id_usuario = ? AND id_sala = ?";
    
    private static final String TODOS_LOS_COMENTARIOS_QUERY = 
        "SELECT * FROM comentario_sala";
    
    private static final String EXISTE_COMENTARIO_POR_USUARIO_QUERY = 
        "SELECT 1 FROM comentario_sala WHERE id_usuario = ? AND id_sala = ?";

    /** Crea un nuevo comentario de sala en la base de datos */
    public ComentarioSala createComentarioSala(ComentarioSala newComentarioSala) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(CREAR_COMENTARIO_SALA_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            insert.setInt(1, newComentarioSala.getIdSala());
            insert.setInt(2, newComentarioSala.getIdUsuario());
            insert.setString(3, newComentarioSala.getComentario());
            insert.setInt(4, newComentarioSala.getCalificacion());
            insert.setTimestamp(5, Timestamp.valueOf(newComentarioSala.getFechaComentario()));
            insert.setString(6, newComentarioSala.getEstado());
            
            int affectedRows = insert.executeUpdate();
            
            if (affectedRows > 0) {
                // Obtener el ID generado
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newComentarioSala.setIdComentarioSala(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newComentarioSala;
    }

    /** Verifica si existe un comentario para un usuario y sala específicos */
    public boolean existsComentarioSala(int idUsuario, int idSala) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_COMENTARIO_POR_USUARIO_QUERY)) {
            query.setInt(1, idUsuario);
            query.setInt(2, idSala);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Obtiene todos los comentarios de sala */
    public List<ComentarioSala> getAllComentariosSalas() {
        List<ComentarioSala> comentarios = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(TODOS_LOS_COMENTARIOS_QUERY)) {
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                ComentarioSala comentario = mapResultSetToComentarioSala(resultSet);
                comentarios.add(comentario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comentarios;
    }

    /** Obtiene un comentario por ID */
    public Optional<ComentarioSala> getById(int idComentarioSala) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(ENCONTRAR_COMENTARIO_POR_ID_QUERY)) {
            query.setInt(1, idComentarioSala);
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()) {
                ComentarioSala comentario = mapResultSetToComentarioSala(resultSet);
                return Optional.of(comentario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Obtiene todos los comentarios de una sala específica */
    public List<ComentarioSala> getComentariosBySala(int idSala) {
        List<ComentarioSala> comentarios = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(COMENTARIOS_POR_SALA_QUERY)) {
            query.setInt(1, idSala);
            ResultSet resultSet = query.executeQuery();
            
            while (resultSet.next()) {
                ComentarioSala comentario = mapResultSetToComentarioSala(resultSet);
                comentarios.add(comentario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comentarios;
    }

    /** Obtiene comentarios de un usuario específico para una sala específica */
    public List<ComentarioSala> getComentariosByUsuarioAndSala(int idUsuario, int idSala) {
        List<ComentarioSala> comentarios = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(COMENTARIOS_POR_USUARIO_Y_SALA_QUERY)) {
            query.setInt(1, idUsuario);
            query.setInt(2, idSala);
            ResultSet resultSet = query.executeQuery();
            
            while (resultSet.next()) {
                ComentarioSala comentario = mapResultSetToComentarioSala(resultSet);
                comentarios.add(comentario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comentarios;
    }

    /** Método auxiliar para mapear un ResultSet a un objeto ComentarioSala */
    private ComentarioSala mapResultSetToComentarioSala(ResultSet resultSet) throws SQLException {
        return new ComentarioSala(
            resultSet.getInt("id_comentario_sala"),
            resultSet.getInt("id_sala"),
            resultSet.getInt("id_usuario"),
            resultSet.getString("comentario"),
            resultSet.getInt("calificacion"),
            resultSet.getTimestamp("fecha_comentario").toLocalDateTime(),
            resultSet.getString("estado")
        );
    }
}