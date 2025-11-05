package db;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.funcion.Funcion;


public class FuncionDB {

    private static final String CREAR_FUNCION_QUERY =
        "INSERT INTO funcion (id_sala, id_pelicula, fecha_hora_funcion, precio_boleto_adulto, precio_boleto_nino, asientos_disponibles, estado) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String ENCONTRAR_FUNCION_POR_ID_QUERY =
        "SELECT * FROM funcion WHERE id_funcion = ?";

    private static final String TODAS_LAS_FUNCIONES_QUERY =
        "SELECT * FROM funcion";

    private static final String FUNCIONES_POR_SALA_QUERY =
        "SELECT * FROM funcion WHERE id_sala = ?";

    private static final String FUNCIONES_POR_CINE_QUERY =
        "SELECT f.* FROM funcion f " +
        "INNER JOIN sala s ON f.id_sala = s.id_sala " +
        "WHERE s.id_cine = ?";

    private static final String ACTUALIZAR_FUNCION_QUERY =
        "UPDATE funcion SET id_sala = ?, id_pelicula = ?, fecha_hora_funcion = ?, precio_boleto_adulto = ?, precio_boleto_nino = ?, asientos_disponibles = ?, estado = ? " +
        "WHERE id_funcion = ?";

    private static final String ELIMINAR_FUNCION_QUERY =
        "DELETE FROM funcion WHERE id_funcion = ?";

    private static final String EXISTE_FUNCION_POR_ID_QUERY =
        "SELECT 1 FROM funcion WHERE id_funcion = ?";
    
    private static final String EXISTE_FUNCION_EN_SALA_Y_HORARIO_QUERY =
        "SELECT 1 FROM funcion WHERE id_sala = ? AND fecha_hora_funcion = ? AND estado != 'CANCELADA'";

    /** Crea una nueva función en la base de datos */
    public Funcion createFuncion(Funcion newFuncion) {
        
        if (existeFuncionEnSalaYHorario(newFuncion.getIdSala(), newFuncion.getFechaHoraFuncion())) {
            throw new RuntimeException("Ya existe una función programada en esta sala para el horario seleccionado");
        }
        
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(CREAR_FUNCION_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            insert.setInt(1, newFuncion.getIdSala());
            insert.setInt(2, newFuncion.getIdPelicula());
            insert.setTimestamp(3, Timestamp.valueOf(newFuncion.getFechaHoraFuncion()));
            insert.setDouble(4, newFuncion.getPrecioBoletoAdulto());
            insert.setDouble(5, newFuncion.getPrecioBoletoNino());
            insert.setInt(6, newFuncion.getAsientosDisponibles());
            insert.setString(7, newFuncion.getEstado());

            int affectedRows = insert.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newFuncion.setIdFuncion(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newFuncion;
    }

    /** Verifica si existe una función por su ID */
    public boolean existsFuncion(int idFuncion) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_FUNCION_POR_ID_QUERY)) {
            query.setInt(1, idFuncion);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Obtiene todas las funciones */
    public List<Funcion> getAllFunciones() {
        List<Funcion> funciones = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(TODAS_LAS_FUNCIONES_QUERY)) {
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                funciones.add(mapResultSetToFuncion(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funciones;
    }

    /** Obtiene una función por ID */
    public Optional<Funcion> getById(int idFuncion) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(ENCONTRAR_FUNCION_POR_ID_QUERY)) {
            query.setInt(1, idFuncion);
            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToFuncion(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Obtiene todas las funciones de una sala */
    public List<Funcion> getBySala(int idSala) {
        List<Funcion> funciones = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(FUNCIONES_POR_SALA_QUERY)) {
            query.setInt(1, idSala);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                funciones.add(mapResultSetToFuncion(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funciones;
    }

    /** Obtiene todas las funciones de un cine (JOIN con sala) */
    public List<Funcion> getByCine(int idCine) {
        List<Funcion> funciones = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(FUNCIONES_POR_CINE_QUERY)) {
            query.setInt(1, idCine);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                funciones.add(mapResultSetToFuncion(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funciones;
    }

    /** Actualiza una función */
    public Funcion updateFuncion(int idFuncion, Funcion funcionToUpdate) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement updateStmt = connection.prepareStatement(ACTUALIZAR_FUNCION_QUERY)) {

            updateStmt.setInt(1, funcionToUpdate.getIdSala());
            updateStmt.setInt(2, funcionToUpdate.getIdPelicula());
            updateStmt.setTimestamp(3, Timestamp.valueOf(funcionToUpdate.getFechaHoraFuncion()));
            updateStmt.setDouble(4, funcionToUpdate.getPrecioBoletoAdulto());
            updateStmt.setDouble(5, funcionToUpdate.getPrecioBoletoNino());
            updateStmt.setInt(6, funcionToUpdate.getAsientosDisponibles());
            updateStmt.setString(7, funcionToUpdate.getEstado());
            updateStmt.setInt(8, idFuncion);

            updateStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcionToUpdate;
    }

    /** Elimina una función por su ID */
    public boolean deleteFuncion(int idFuncion) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement deleteStmt = connection.prepareStatement(ELIMINAR_FUNCION_QUERY)) {
            deleteStmt.setInt(1, idFuncion);
            int affectedRows = deleteStmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Mapea un registro de ResultSet a un objeto Funcion */
    private Funcion mapResultSetToFuncion(ResultSet rs) throws SQLException {
        Funcion funcion = new Funcion();
        funcion.setIdFuncion(rs.getInt("id_funcion"));
        funcion.setIdSala(rs.getInt("id_sala"));
        funcion.setIdPelicula(rs.getInt("id_pelicula"));
        funcion.setFechaHoraFuncion(rs.getTimestamp("fecha_hora_funcion").toLocalDateTime());
        funcion.setPrecioBoletoAdulto(rs.getDouble("precio_boleto_adulto"));
        funcion.setPrecioBoletoNino(rs.getDouble("precio_boleto_nino"));
        funcion.setAsientosDisponibles(rs.getInt("asientos_disponibles"));
        funcion.setEstado(rs.getString("estado"));
        return funcion;
    }
    
    /** Verifica si ya existe una función en la misma sala y horario */
    public boolean existeFuncionEnSalaYHorario(int idSala, LocalDateTime fechaHoraFuncion) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_FUNCION_EN_SALA_Y_HORARIO_QUERY)) {
            query.setInt(1, idSala);
            query.setTimestamp(2, Timestamp.valueOf(fechaHoraFuncion));
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
