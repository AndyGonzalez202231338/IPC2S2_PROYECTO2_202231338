package db;

import models.cines.Cine;
import models.cines.CineAdministrador;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class CinesDB {

    private static final String INSERT_CINE = 
        "INSERT INTO cine (nombre, direccion, fecha_creacion, estado) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_CINES = "SELECT * FROM cine";
    private static final String SELECT_CINE_BY_ID = "SELECT * FROM cine WHERE id_cine = ?";
    private static final String UPDATE_CINE = 
        "UPDATE cine SET nombre = ?, direccion = ?, estado = ? WHERE id_cine = ?";
    private static final String DELETE_CINE = "DELETE FROM cine WHERE id_cine = ?";

    private static final String INSERT_CINE_ADMIN = 
        "INSERT INTO cine_administrador (id_cine, id_usuario, fecha_asignacion, estado) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ADMINS_BY_CINE = 
        "SELECT * FROM cine_administrador WHERE id_cine = ? AND estado = 'ACTIVO'";
    private static final String DELETE_ADMIN_FROM_CINE = 
        "UPDATE cine_administrador SET estado = 'INACTIVO' WHERE id_cine = ? AND id_usuario = ?";
    private static final String DELETE_ALL_ADMINS_FOR_CINE = 
        "DELETE FROM cine_administrador WHERE id_cine = ?";
    private static final String EXISTE_CINE_POR_NOMBRE_QUERY =
            "SELECT 1 FROM cine WHERE nombre = ?";

    /** Crea un cine y asigna administradores
     * @param cine
     * @return  */
    public Cine createCine(Cine cine) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_CINE, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cine.getNombre());
            stmt.setString(2, cine.getDireccion());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(4, "ACTIVO");
            stmt.executeUpdate();

            // Obtener ID generado
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int idCine = keys.getInt(1);
                cine.setIdCine(idCine);

                // Asignar administradores si existen
                if (cine.getAdministradores() != null && !cine.getAdministradores().isEmpty()) {
                    for (Integer idUsuario : cine.getAdministradores()) {
                        addAdministradorToCine(idCine, idUsuario);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cine;
    }

    /** Obtiene todos los cines */
    public List<Cine> getAllCines() {
        List<Cine> list = new ArrayList<>();
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CINES)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cine cine = mapToCine(rs);
                // Opcional: cargar administradores para cada cine
                cine.setAdministradores(getAdministradoresIdsByCine(cine.getIdCine()));
                list.add(cine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Obtiene cine por ID */
    public Optional<Cine> getCineById(int idCine) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_CINE_BY_ID)) {
            stmt.setInt(1, idCine);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cine cine = mapToCine(rs);
                cine.setAdministradores(getAdministradoresIdsByCine(idCine));
                return Optional.of(cine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /** Actualiza datos del cine */
    public boolean updateCine(Cine cine) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_CINE)) {
            stmt.setString(1, cine.getNombre());
            stmt.setString(2, cine.getDireccion());
            stmt.setString(3, cine.getEstado());
            stmt.setInt(4, cine.getIdCine());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Elimina cine y sus administradores */
    public boolean deleteCine(int idCine) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        try {
            try (PreparedStatement deleteAdmins = conn.prepareStatement(DELETE_ALL_ADMINS_FOR_CINE)) {
                deleteAdmins.setInt(1, idCine);
                deleteAdmins.executeUpdate();
            }
            try (PreparedStatement deleteCine = conn.prepareStatement(DELETE_CINE)) {
                deleteCine.setInt(1, idCine);
                return deleteCine.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Agrega un administrador a un cine */
    public void addAdministradorToCine(int idCine, int idUsuario) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_CINE_ADMIN)) {
            stmt.setInt(1, idCine);
            stmt.setInt(2, idUsuario);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(4, "ACTIVO");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Obtiene administradores por cine */
    public List<CineAdministrador> getAdministradoresByCine(int idCine) {
        List<CineAdministrador> list = new ArrayList<>();
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ADMINS_BY_CINE)) {
            stmt.setInt(1, idCine);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapToCineAdmin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Obtiene solo los IDs de administradores por cine */
    public List<Integer> getAdministradoresIdsByCine(int idCine) {
        List<Integer> adminIds = new ArrayList<>();
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ADMINS_BY_CINE)) {
            stmt.setInt(1, idCine);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                adminIds.add(rs.getInt("id_usuario"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminIds;
    }

    /** Elimina un administrador específico */
    public boolean removeAdministradorFromCine(int idCine, int idUsuario) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_ADMIN_FROM_CINE)) {
            stmt.setInt(1, idCine);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======== Helpers ========
    private Cine mapToCine(ResultSet rs) throws SQLException {
    int idCine = rs.getInt("id_cine");

    // Primero mapeamos los datos básicos del cine
    Cine cine = new Cine(
            idCine,
            rs.getString("nombre"),
            rs.getString("direccion"),
            rs.getTimestamp("fecha_creacion").toLocalDateTime(),
            rs.getString("estado"),
            new ArrayList<>() // Inicialmente vacío
    );

    // Ahora obtenemos la lista de administradores desde la tabla intermedia
    List<Integer> administradores = getAdministradoresIdsByCine(idCine);
    cine.setAdministradores(administradores);

    return cine;
}

    private CineAdministrador mapToCineAdmin(ResultSet rs) throws SQLException {
        return new CineAdministrador(
                rs.getInt("id_cine_administrador"),
                rs.getInt("id_cine"),
                rs.getInt("id_usuario"),
                rs.getTimestamp("fecha_asignacion").toLocalDateTime(),
                rs.getString("estado")
        );
    }
    
    /** Verifica si existe un cine por su nombre */
    public boolean existsCine(String nombre) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_CINE_POR_NOMBRE_QUERY)) {
            query.setString(1, nombre);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}