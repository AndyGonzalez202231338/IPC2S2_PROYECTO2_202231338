package db;

import models.cines.Cine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CineAdministradorDB {

    private static final String SELECT_CINES_BY_USUARIO
            = "SELECT c.id_cine, c.nombre, c.direccion, c.fecha_creacion, c.estado "
            + "FROM cine c "
            + "INNER JOIN cine_administrador ca ON c.id_cine = ca.id_cine "
            + "WHERE ca.id_usuario = ? AND ca.estado = 'ACTIVO' AND c.estado = 'ACTIVO'";

    public List<Cine> getCinesByUsuarioId(int idUsuario) {
        List<Cine> cines = new ArrayList<>();
        Connection conn = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_CINES_BY_USUARIO)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cine cine = mapToCine(rs);
                cines.add(cine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener cines del administrador con ID: " + idUsuario, e);
        }

        return cines;
    }

    private Cine mapToCine(ResultSet rs) throws SQLException {
        return new Cine(
                rs.getInt("id_cine"),
                rs.getString("nombre"),
                rs.getString("direccion"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                rs.getString("estado")
        );
    }
}
