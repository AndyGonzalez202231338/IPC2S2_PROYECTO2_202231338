/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import models.users.CarteraDigital;

/**
 *
 * @author andy
 */
public class CarteraDigitalDB {
    
    private static final String CREAR_CARTERA_QUERY = 
        "INSERT INTO cartera_digital (id_usuario, saldo) VALUES (?, ?)";
    
    private static final String OBTENER_CARTERA_POR_USUARIO_QUERY = 
        "SELECT * FROM cartera_digital WHERE id_usuario = ?";
    
    private static final String OBTENER_CARTERA_POR_ID_QUERY = 
        "SELECT * FROM cartera_digital WHERE id_cartera = ?";
    
    private static final String ACTUALIZAR_SALDO_QUERY = 
        "UPDATE cartera_digital SET saldo = ? WHERE id_usuario = ?";
    
    private static final String ELIMINAR_CARTERA_QUERY = 
        "DELETE FROM cartera_digital WHERE id_usuario = ?";
    
    private static final String EXISTE_CARTERA_POR_USUARIO_QUERY = 
        "SELECT 1 FROM cartera_digital WHERE id_usuario = ?";

    /** Crea una nueva cartera digital para un usuario */
    public CarteraDigital crearCartera(int idUsuario) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(CREAR_CARTERA_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            insert.setInt(1, idUsuario);
            insert.setBigDecimal(2, BigDecimal.ZERO); // Saldo inicial 0
            
            int affectedRows = insert.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return new CarteraDigital(
                            generatedKeys.getInt(1),
                            idUsuario,
                            BigDecimal.ZERO
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Obtiene la cartera de un usuario */
    public Optional<CarteraDigital> obtenerCarteraPorUsuario(int idUsuario) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(OBTENER_CARTERA_POR_USUARIO_QUERY)) {
            query.setInt(1, idUsuario);
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()) {
                CarteraDigital cartera = mapResultSetToCartera(resultSet);
                return Optional.of(cartera);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Actualiza el saldo de una cartera */
    public boolean actualizarSaldo(int idUsuario, BigDecimal nuevoSaldo) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(ACTUALIZAR_SALDO_QUERY)) {
            
            update.setBigDecimal(1, nuevoSaldo);
            update.setInt(2, idUsuario);
            
            int affectedRows = update.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Verifica si un usuario ya tiene cartera */
    public boolean existeCarteraPorUsuario(int idUsuario) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_CARTERA_POR_USUARIO_QUERY)) {
            query.setInt(1, idUsuario);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Elimina la cartera de un usuario */
    public boolean eliminarCartera(int idUsuario) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement delete = connection.prepareStatement(ELIMINAR_CARTERA_QUERY)) {
            delete.setInt(1, idUsuario);
            int affectedRows = delete.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private CarteraDigital mapResultSetToCartera(ResultSet resultSet) throws SQLException {
        return new CarteraDigital(
            resultSet.getInt("id_cartera"),
            resultSet.getInt("id_usuario"),
            resultSet.getBigDecimal("saldo")
        );
    }
}