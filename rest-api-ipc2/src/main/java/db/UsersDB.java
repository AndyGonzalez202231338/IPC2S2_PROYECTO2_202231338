package db;

import models.users.User;
import models.users.Role;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersDB {

    private static final String CREAR_USUARIO_QUERY = 
        "INSERT INTO usuario (id_rol, email, password, nombre_completo, estado, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String ENCONTRAR_USUARIO_POR_ID_QUERY = 
        "SELECT u.*, r.nombre_rol as nombre_rol, r.descripcion as rol_descripcion " +
        "FROM usuario u INNER JOIN rol r ON u.id_rol = r.id_rol WHERE u.id_usuario = ?";
    
    private static final String ENCONTRAR_USUARIO_POR_EMAIL_QUERY = 
        "SELECT u.*, r.nombre_rol as nombre_rol, r.descripcion as rol_descripcion " +
        "FROM usuario u INNER JOIN rol r ON u.id_rol = r.id_rol WHERE u.email = ?";
    
    private static final String TODOS_LOS_USUARIOS_QUERY = 
        "SELECT u.*, r.nombre_rol as nombre_rol, r.descripcion as rol_descripcion " +
        "FROM usuario u INNER JOIN rol r ON u.id_rol = r.id_rol";
    
    private static final String ACTUALIZAR_USUARIO_POR_ID_QUERY = 
        "UPDATE usuario SET id_rol = ?, email = ?, password = ?, nombre_completo = ?, estado = ? WHERE id_usuario = ?";
    
    private static final String ACTUALIZAR_USUARIO_POR_EMAIL_QUERY = 
        "UPDATE usuario SET id_rol = ?, password = ?, nombre_completo = ?, estado = ? WHERE email = ?";
    
    private static final String ELIMINAR_USUARIO_POR_ID_QUERY = 
        "DELETE FROM usuario WHERE id_usuario = ?";
    
    private static final String EXISTE_USUARIO_POR_ID_QUERY = 
        "SELECT 1 FROM usuario WHERE id_usuario = ?";

    /** Crea un nuevo usuario en la base de datos */
    public User createUser(User newUser) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(CREAR_USUARIO_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            insert.setInt(1, newUser.getRol().getIdRol());
            insert.setString(2, newUser.getEmail());
            insert.setString(3, newUser.getPassword());
            insert.setString(4, newUser.getNombreCompleto());
            insert.setString(5, newUser.getEstado());
            insert.setTimestamp(6, Timestamp.valueOf(newUser.getFechaCreacion()));
            
            int affectedRows = insert.executeUpdate();
            
            if (affectedRows > 0) {
                // Obtener el ID generado
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUser.setIdUsuario(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUser;
    }

    /** Verifica si existe un usuario por su ID */
    public boolean existsUser(int idUsuario) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_USUARIO_POR_ID_QUERY)) {
            query.setInt(1, idUsuario);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Obtiene todos los usuarios */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(TODOS_LOS_USUARIOS_QUERY)) {
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /** Obtiene un usuario por ID */
    public Optional<User> getById(int idUsuario) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(ENCONTRAR_USUARIO_POR_ID_QUERY)) {
            query.setInt(1, idUsuario);
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Obtiene un usuario por email */
    public Optional<User> getByEmail(String email) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(ENCONTRAR_USUARIO_POR_EMAIL_QUERY)) {
            query.setString(1, email);
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Actualiza un usuario por ID */
    public User updateUser(Integer idUsuario, User userToUpdate) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement updateStmt = connection.prepareStatement(ACTUALIZAR_USUARIO_POR_ID_QUERY)) {
            
            updateStmt.setInt(1, userToUpdate.getRol().getIdRol());
            updateStmt.setString(2, userToUpdate.getEmail());
            updateStmt.setString(3, userToUpdate.getPassword());
            updateStmt.setString(4, userToUpdate.getNombreCompleto());
            updateStmt.setString(5, userToUpdate.getEstado());
            updateStmt.setInt(6, idUsuario);
            
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userToUpdate;
    }

    /** Actualiza un usuario por email */
    public User updateUserByEmail(String email, User userToUpdate) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement updateStmt = connection.prepareStatement(ACTUALIZAR_USUARIO_POR_EMAIL_QUERY)) {
            
            updateStmt.setInt(1, userToUpdate.getRol().getIdRol());
            updateStmt.setString(2, userToUpdate.getPassword());
            updateStmt.setString(3, userToUpdate.getNombreCompleto());
            updateStmt.setString(4, userToUpdate.getEstado());
            updateStmt.setString(5, email);
            
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userToUpdate;
    }

    /** Elimina un usuario por ID */
    public boolean deleteUser(int idUsuario) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement deleteStmt = connection.prepareStatement(ELIMINAR_USUARIO_POR_ID_QUERY)) {
            
            deleteStmt.setInt(1, idUsuario);
            int affectedRows = deleteStmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Método auxiliar para mapear un ResultSet a un objeto User */
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        // Crear el rol
        Role role = new Role(
            resultSet.getInt("id_rol"),
            resultSet.getString("nombre_rol"),  // Cambiado de "rol_nombre" a "nombre_rol"
            resultSet.getString("rol_descripcion")
        );
        
        // Crear el usuario
        User user = new User(
            resultSet.getInt("id_usuario"),
            role,
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("nombre_completo"),  // Cambiado de "nombre" a "nombre_completo"
            resultSet.getString("estado"),
            resultSet.getTimestamp("fecha_creacion").toLocalDateTime()
        );
        
        return user;
    }
}