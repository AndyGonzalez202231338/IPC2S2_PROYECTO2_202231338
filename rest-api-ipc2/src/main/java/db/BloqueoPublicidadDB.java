package db;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.bloqueo.BloqueoPublicidad;

public class BloqueoPublicidadDB {

    private static final String INSERT_BLOQUEO
            = "INSERT INTO bloqueo_publicidad (id_cine, id_publicidad, fecha_inicio, fecha_fin, costo_total, fecha_pago) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL
            = "SELECT * FROM bloqueo_publicidad";

    private static final String SELECT_BY_ID
            = "SELECT * FROM bloqueo_publicidad WHERE id_bloqueo_publicidad = ?";

    private static final String SELECT_BY_CINE
            = "SELECT * FROM bloqueo_publicidad WHERE id_cine = ?";

    private static final String SELECT_BY_PUBLICIDAD
            = "SELECT * FROM bloqueo_publicidad WHERE id_publicidad = ?";

    private static final String SELECT_BY_ANUNCIO_AND_CINE
            = "SELECT * FROM bloqueo_publicidad WHERE id_publicidad = ? AND id_cine = ?";

    private static final String SELECT_BLOQUEOS_ACTIVOS_BY_ANUNCIO
            = "SELECT * FROM bloqueo_publicidad "
            + "WHERE id_publicidad = ? AND ? BETWEEN fecha_inicio AND fecha_fin";

    private static final String SELECT_BLOQUEOS_ACTIVOS_BY_CINE
            = "SELECT * FROM bloqueo_publicidad "
            + "WHERE id_cine = ? AND ? BETWEEN fecha_inicio AND fecha_fin";

    private static final String DELETE_BY_ID
            = "DELETE FROM bloqueo_publicidad WHERE id_bloqueo_publicidad = ?";

    private static final String UPDATE_BY_ID
            = "UPDATE bloqueo_publicidad SET id_cine = ?, id_publicidad = ?, fecha_inicio = ?, fecha_fin = ?, costo_total = ?, fecha_pago = ? "
            + "WHERE id_bloqueo_publicidad = ?";

    private static final String EXISTS_BLOQUEO_EN_RANGO_FECHAS
            = "SELECT COUNT(*) > 0 FROM bloqueo_publicidad "
            + "WHERE id_publicidad = ? AND "
            + "((fecha_inicio BETWEEN ? AND ?) OR "
            + "(fecha_fin BETWEEN ? AND ?) OR "
            + "(? BETWEEN fecha_inicio AND fecha_fin))";

    private static final String EXISTS_BLOQUEO_BY_CINE_AND_ANUNCIO
            = "SELECT COUNT(*) > 0 FROM bloqueo_publicidad bp "
            + "INNER JOIN publicidad p ON bp.id_publicidad = p.id_publicidad "
            + "WHERE bp.id_cine = ? AND p.id_anuncio = ? AND ? BETWEEN bp.fecha_inicio AND bp.fecha_fin";

    private static final String SELECT_BY_ANUNCIO_ID
            = "SELECT bp.* FROM bloqueo_publicidad bp "
            + "INNER JOIN publicidad p ON bp.id_publicidad = p.id_publicidad "
            + "WHERE p.id_anuncio = ?";

    private static final String SELECT_BLOQUEOS_ACTIVOS_BY_PUBLICIDAD
            = "SELECT * FROM bloqueo_publicidad "
            + "WHERE id_publicidad = ? AND ? BETWEEN fecha_inicio AND fecha_fin";

    private static final String COUNT_BLOQUEOS_ACTIVOS_BY_ANUNCIO
            = "SELECT COUNT(*) FROM bloqueo_publicidad bp "
            + "INNER JOIN publicidad p ON bp.id_publicidad = p.id_publicidad "
            + "WHERE p.id_anuncio = ? AND ? BETWEEN bp.fecha_inicio AND bp.fecha_fin";

    /**
     * Inserta un nuevo bloqueo de publicidad
     */
    public BloqueoPublicidad create(BloqueoPublicidad bloqueo) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_BLOQUEO, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bloqueo.getIdCine());
            stmt.setInt(2, bloqueo.getIdPublicidad());

            // Fecha inicio - puede ser NULL
            if (bloqueo.getFechaInicio() != null) {
                stmt.setDate(3, Date.valueOf(bloqueo.getFechaInicio()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            // Fecha fin - puede ser NULL
            if (bloqueo.getFechaFin() != null) {
                stmt.setDate(4, Date.valueOf(bloqueo.getFechaFin()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setBigDecimal(5, bloqueo.getCostoTotal());

            // PARÁMETRO 6: fecha_pago - establecer como NULL para que MySQL use DEFAULT CURRENT_TIMESTAMP
            stmt.setNull(6, Types.TIMESTAMP);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    bloqueo.setIdBloqueoPublicidad(idGenerado);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear bloqueo: " + e.getMessage());
        }
        return bloqueo;
    }

    /**
     * Obtiene todos los bloqueos
     */
    public List<BloqueoPublicidad> findAll() {
        List<BloqueoPublicidad> bloqueos = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bloqueos.add(mapResultSetToBloqueo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bloqueos;
    }

    /**
     * Busca por ID
     */
    public Optional<BloqueoPublicidad> findById(int id) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBloqueo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Busca por cine
     */
    public List<BloqueoPublicidad> findByCineId(int idCine) {
        List<BloqueoPublicidad> bloqueos = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_CINE)) {
            stmt.setInt(1, idCine);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bloqueos.add(mapResultSetToBloqueo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bloqueos;
    }

    /**
     * Busca por publicidad
     */
    public List<BloqueoPublicidad> findByPublicidadId(int idPublicidad) {
        List<BloqueoPublicidad> bloqueos = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_PUBLICIDAD)) {
            stmt.setInt(1, idPublicidad);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bloqueos.add(mapResultSetToBloqueo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bloqueos;
    }

    /**
     * Busca por anuncio y cine
     */
    public Optional<BloqueoPublicidad> findByAnuncioAndCine(int idPublicidad, int idCine) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ANUNCIO_AND_CINE)) {
            stmt.setInt(1, idPublicidad);
            stmt.setInt(2, idCine);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBloqueo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Bloqueos activos por anuncio - maneja fechas null
     */
    public List<BloqueoPublicidad> findBloqueosActivosByAnuncio(int idPublicidad, LocalDate fechaActual) {
        List<BloqueoPublicidad> bloqueos = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BLOQUEOS_ACTIVOS_BY_ANUNCIO)) {
            stmt.setInt(1, idPublicidad);
            stmt.setDate(2, Date.valueOf(fechaActual));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BloqueoPublicidad bloqueo = mapResultSetToBloqueo(rs);
                // Solo agregar si tiene fechas válidas
                if (bloqueo.getFechaInicio() != null && bloqueo.getFechaFin() != null) {
                    bloqueos.add(bloqueo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bloqueos;
    }

    /**
     * Bloqueos activos por cine
     */
    public List<BloqueoPublicidad> findBloqueosActivosByCine(int idCine, LocalDate fechaActual) {
        List<BloqueoPublicidad> bloqueos = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BLOQUEOS_ACTIVOS_BY_CINE)) {
            stmt.setInt(1, idCine);
            stmt.setDate(2, Date.valueOf(fechaActual));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bloqueos.add(mapResultSetToBloqueo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bloqueos;
    }

    /**
     * Actualiza un bloqueo
     */
    public BloqueoPublicidad update(BloqueoPublicidad bloqueo) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_BY_ID)) {
            stmt.setInt(1, bloqueo.getIdCine());
            stmt.setInt(2, bloqueo.getIdPublicidad());
            stmt.setDate(3, Date.valueOf(bloqueo.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(bloqueo.getFechaFin()));
            stmt.setBigDecimal(5, bloqueo.getCostoTotal());
            if (bloqueo.getFechaPago() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(bloqueo.getFechaPago()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }
            stmt.setInt(7, bloqueo.getIdBloqueoPublicidad());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bloqueo;
    }

    /**
     * Elimina un bloqueo
     */
    public boolean delete(int idBloqueo) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_BY_ID)) {
            stmt.setInt(1, idBloqueo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mapeo de ResultSet BloqueoPublicidad - maneja fechas null
     */
    private BloqueoPublicidad mapResultSetToBloqueo(ResultSet rs) throws SQLException {
        BloqueoPublicidad bloqueo = new BloqueoPublicidad(
                rs.getInt("id_cine"),
                rs.getInt("id_publicidad"),
                rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toLocalDate() : null,
                rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null,
                rs.getBigDecimal("costo_total")
        );

        bloqueo.setIdBloqueoPublicidad(rs.getInt("id_bloqueo_publicidad"));

        if (rs.getTimestamp("fecha_pago") != null) {
            bloqueo.setFechaPago(rs.getTimestamp("fecha_pago").toLocalDateTime());
        }

        return bloqueo;
    }

    /**
     * Verifica si existe un bloqueo en un rango de fechas para una publicidad
     */
    public boolean existsBloqueoEnRangoFechas(int idPublicidad, LocalDate fechaInicio, LocalDate fechaFin) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(EXISTS_BLOQUEO_EN_RANGO_FECHAS)) {
            stmt.setInt(1, idPublicidad);
            stmt.setDate(2, Date.valueOf(fechaInicio));
            stmt.setDate(3, Date.valueOf(fechaFin));
            stmt.setDate(4, Date.valueOf(fechaInicio));
            stmt.setDate(5, Date.valueOf(fechaFin));
            stmt.setDate(6, Date.valueOf(fechaInicio));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifica si un cine ya tiene un bloqueo activo para un anuncio
     */
    public boolean existsBloqueoByCineAndAnuncio(int idCine, int idAnuncio) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(EXISTS_BLOQUEO_BY_CINE_AND_ANUNCIO)) {
            stmt.setInt(1, idCine);
            stmt.setInt(2, idAnuncio);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Busca bloqueos por ID de anuncio
     */
    public List<BloqueoPublicidad> findByAnuncioId(int idAnuncio) {
        List<BloqueoPublicidad> bloqueos = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ANUNCIO_ID)) {
            stmt.setInt(1, idAnuncio);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bloqueos.add(mapResultSetToBloqueo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bloqueos;
    }

    /**
     * Bloqueos activos por publicidad
     */
    public List<BloqueoPublicidad> findBloqueosActivosByPublicidad(int idPublicidad, LocalDate fechaActual) {
        List<BloqueoPublicidad> bloqueos = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BLOQUEOS_ACTIVOS_BY_PUBLICIDAD)) {
            stmt.setInt(1, idPublicidad);
            stmt.setDate(2, Date.valueOf(fechaActual));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bloqueos.add(mapResultSetToBloqueo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bloqueos;
    }

    /**
     * Cuenta bloqueos activos por anuncio
     */
    public int countBloqueosActivosByAnuncio(int idAnuncio, LocalDate fechaActual) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(COUNT_BLOQUEOS_ACTIVOS_BY_ANUNCIO)) {
            stmt.setInt(1, idAnuncio);
            stmt.setDate(2, Date.valueOf(fechaActual));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Verifica disponibilidad de anuncio (sin bloqueos activos)
     */
    public boolean isAnuncioDisponible(int idAnuncio) {
        return countBloqueosActivosByAnuncio(idAnuncio, LocalDate.now()) == 0;
    }
}
