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
import models.cines.CarteraCine;

/**
 *
 * @author andy
 */
public class CarteraCineDB {
    
    private static final String CREAR_CARTERA_CINE_QUERY = 
        "INSERT INTO cartera_cine (id_cine, saldo) VALUES (?, ?)";
    
    private static final String OBTENER_CARTERA_POR_CINE_QUERY = 
        "SELECT * FROM cartera_cine WHERE id_cine = ?";
    
    private static final String OBTENER_CARTERA_POR_ID_QUERY = 
        "SELECT * FROM cartera_cine WHERE id_cartera_cine = ?";
    
    private static final String ACTUALIZAR_SALDO_CINE_QUERY = 
        "UPDATE cartera_cine SET saldo = ? WHERE id_cine = ?";
    
    private static final String ELIMINAR_CARTERA_CINE_QUERY = 
        "DELETE FROM cartera_cine WHERE id_cine = ?";
    
    private static final String EXISTE_CARTERA_POR_CINE_QUERY = 
        "SELECT 1 FROM cartera_cine WHERE id_cine = ?";

    /** Crea una nueva cartera para un cine */
    public CarteraCine crearCarteraCine(int idCine) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(CREAR_CARTERA_CINE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            insert.setInt(1, idCine);
            insert.setBigDecimal(2, BigDecimal.ZERO); // Saldo inicial 0
            
            int affectedRows = insert.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return new CarteraCine(
                            generatedKeys.getInt(1),
                            idCine,
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

    /** Obtiene la cartera de un cine */
    public Optional<CarteraCine> obtenerCarteraPorCine(int idCine) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(OBTENER_CARTERA_POR_CINE_QUERY)) {
            query.setInt(1, idCine);
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()) {
                CarteraCine cartera = mapResultSetToCarteraCine(resultSet);
                return Optional.of(cartera);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Actualiza el saldo de una cartera de cine */
    public boolean actualizarSaldoCine(int idCine, BigDecimal nuevoSaldo) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(ACTUALIZAR_SALDO_CINE_QUERY)) {
            
            update.setBigDecimal(1, nuevoSaldo);
            update.setInt(2, idCine);
            
            int affectedRows = update.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Verifica si un cine ya tiene cartera */
    public boolean existeCarteraPorCine(int idCine) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_CARTERA_POR_CINE_QUERY)) {
            query.setInt(1, idCine);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Elimina la cartera de un cine */
    public boolean eliminarCarteraCine(int idCine) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement delete = connection.prepareStatement(ELIMINAR_CARTERA_CINE_QUERY)) {
            delete.setInt(1, idCine);
            int affectedRows = delete.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private CarteraCine mapResultSetToCarteraCine(ResultSet resultSet) throws SQLException {
        return new CarteraCine(
            resultSet.getInt("id_cartera_cine"),
            resultSet.getInt("id_cine"),
            resultSet.getBigDecimal("saldo")
        );
    }
}
