package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.salas.Sala;

public class SalasDB {

    private static final String INSERT_SALA_QUERY =
        "INSERT INTO sala (id_cine, nombre_sala, filas, columnas, permite_comentarios, estado) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SALAS_QUERY =
        "SELECT * FROM sala";

    private static final String SELECT_SALA_BY_ID_QUERY =
        "SELECT * FROM sala WHERE id_sala = ?";

    private static final String SELECT_SALAS_BY_CINE_QUERY =
        "SELECT * FROM sala WHERE id_cine = ?";

    private static final String EXISTS_SALA_BY_NOMBRE_AND_CINE_QUERY =
        "SELECT 1 FROM sala WHERE nombre_sala = ? AND id_cine = ?";

    private static final String UPDATE_SALA_QUERY =
        "UPDATE sala SET permite_comentarios = ?, estado = ? WHERE id_sala = ?";

    /** Crea una nueva sala en la base de datos */
    public Sala createSala(Sala nuevaSala) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_SALA_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, nuevaSala.getIdCine());
            stmt.setString(2, nuevaSala.getNombreSala());
            stmt.setInt(3, nuevaSala.getFilas());
            stmt.setInt(4, nuevaSala.getColumnas());
            stmt.setString(5, nuevaSala.getPermiteComentario()); // "SI" o "NO"
            stmt.setString(6, nuevaSala.getEstado()); // "ACTIVA" o "BLOQUEADA"

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nuevaSala.setIdSala(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nuevaSala;
    }

    /** Actualiza una sala existente - solo permite_comentarios y estado */
    public Sala update(Sala sala) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SALA_QUERY)) {
            stmt.setString(1, sala.getPermiteComentario()); // "SI" o "NO"
            stmt.setString(2, sala.getEstado()); // "ACTIVA" o "BLOQUEADA"
            stmt.setInt(3, sala.getIdSala());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Actualización fallida, ninguna fila afectada");
            }

            return sala;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la sala: " + e.getMessage());
        }
    }

    /** Devuelve todas las salas */
    public List<Sala> findAll() {
        List<Sala> salas = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_SALAS_QUERY)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                salas.add(mapResultSetToSala(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salas;
    }

    /** Busca una sala por su ID */
    public Optional<Sala> findById(int idSala) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_SALA_BY_ID_QUERY)) {
            stmt.setInt(1, idSala);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToSala(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Busca todas las salas asociadas a un cine específico */
    public List<Sala> findByCineId(int idCine) {
        List<Sala> salas = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_SALAS_BY_CINE_QUERY)) {
            stmt.setInt(1, idCine);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                salas.add(mapResultSetToSala(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salas;
    }

    /** Verifica si existe una sala con un nombre específico dentro de un cine */
    public boolean existsSalaByNombreAndCine(String nombreSala, int idCine) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(EXISTS_SALA_BY_NOMBRE_AND_CINE_QUERY)) {
            stmt.setString(1, nombreSala);
            stmt.setInt(2, idCine);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Mapea un ResultSet a un objeto Sala */
    private Sala mapResultSetToSala(ResultSet rs) throws SQLException {
        return new Sala(
            rs.getInt("id_sala"),
            rs.getInt("id_cine"),
            rs.getString("nombre_sala"),
            rs.getInt("filas"),
            rs.getInt("columnas"),
            rs.getString("permite_comentarios"), // "SI" o "NO"
            rs.getString("estado") // "ACTIVA" o "BLOQUEADA"
        );
    }
}