package db;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.Boleto;

public class BoletosDB {

    private static final String CREAR_BOLETO_QUERY =
        "INSERT INTO boleto (id_funcion, id_usuario, codigo_boleto, fecha_compra, precio_pagado) VALUES (?, ?, ?, ?, ?)";

    private static final String EXISTE_BOLETO_POR_USUARIO_QUERY =
        "SELECT 1 FROM boleto WHERE id_usuario = ?";

    private static final String TODOS_LOS_BOLETOS_QUERY =
        "SELECT * FROM boleto";

    private static final String BOLETOS_POR_USUARIO_QUERY =
        "SELECT * FROM boleto WHERE id_usuario = ?";

    private static final String ELIMINAR_BOLETO_POR_ID_QUERY =
        "DELETE FROM boleto WHERE id_boleto = ?";

    private static final String ENCONTRAR_BOLETO_POR_ID_QUERY =
        "SELECT * FROM boleto WHERE id_boleto = ?";

    /** Crea un nuevo boleto en la base de datos */
    public Boleto createBoleto(Boleto newBoleto) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(CREAR_BOLETO_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            insert.setInt(1, newBoleto.getIdFuncion());
            insert.setInt(2, newBoleto.getIdUsuario());
            insert.setString(3, newBoleto.getCodigoBoleto());
            insert.setTimestamp(4, Timestamp.valueOf(newBoleto.getFechaCompra()));
            insert.setDouble(5, newBoleto.getPrecioPagado());

            int affectedRows = insert.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newBoleto.setIdBoleto(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newBoleto;
    }

    /** Verifica si existe un boleto asociado a un usuario */
    public boolean existsBoleto(int idUsuario) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_BOLETO_POR_USUARIO_QUERY)) {
            query.setInt(1, idUsuario);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Obtiene todos los boletos */
    public List<Boleto> getAllBoletos() {
        List<Boleto> boletos = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(TODOS_LOS_BOLETOS_QUERY)) {
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                Boleto boleto = mapResultSetToBoleto(resultSet);
                boletos.add(boleto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boletos;
    }

    /** Obtiene todos los boletos vendidos a un usuario específico */
    public List<Boleto> getBoletosByUsuario(int idUsuario) {
        List<Boleto> boletos = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(BOLETOS_POR_USUARIO_QUERY)) {
            query.setInt(1, idUsuario);
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                Boleto boleto = mapResultSetToBoleto(resultSet);
                boletos.add(boleto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boletos;
    }

    /** Obtiene un boleto por su ID */
    public Optional<Boleto> getById(int idBoleto) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement query = connection.prepareStatement(ENCONTRAR_BOLETO_POR_ID_QUERY)) {
            query.setInt(1, idBoleto);
            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                Boleto boleto = mapResultSetToBoleto(resultSet);
                return Optional.of(boleto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /** Elimina un boleto por su ID */
    public boolean deleteBoleto(int idBoleto) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement deleteStmt = connection.prepareStatement(ELIMINAR_BOLETO_POR_ID_QUERY)) {

            deleteStmt.setInt(1, idBoleto);
            int affectedRows = deleteStmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Método auxiliar para mapear un ResultSet a un objeto Boleto */
    private Boleto mapResultSetToBoleto(ResultSet resultSet) throws SQLException {
        return new Boleto(
            resultSet.getInt("id_boleto"),
            resultSet.getInt("id_funcion"),
            resultSet.getInt("id_usuario"),
            resultSet.getString("codigo_boleto"),
            resultSet.getTimestamp("fecha_compra").toLocalDateTime(),
            resultSet.getDouble("precio_pagado")
        );
    }
}
