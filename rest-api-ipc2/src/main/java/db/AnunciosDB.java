package db;

import models.anuncios.Anuncio;
import models.anuncios.TipoAnuncio;
import models.anuncios.PeriodoAnuncio;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class AnunciosDB {

    private static final String CREAR_ANUNCIO_QUERY = 
        "INSERT INTO anuncio (id_usuario, id_tipo_anuncio, id_periodo, titulo, contenido_texto, " +
        "imagen_url, video_url, fecha_inicio, fecha_fin, costo_total, estado) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    // NUEVA QUERY PARA CREAR ANUNCIO CON IMAGEN BLOB
    private static final String CREAR_ANUNCIO_CON_IMAGEN_QUERY = 
        "INSERT INTO anuncio (id_usuario, id_tipo_anuncio, id_periodo, titulo, contenido_texto, " +
        "imagen_url, video_url, fecha_inicio, fecha_fin, costo_total, estado) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String ENCONTRAR_ANUNCIO_POR_ID_QUERY = 
        "SELECT * FROM anuncio WHERE id_anuncio = ?";
    
    private static final String ENCONTRAR_ANUNCIO_COMPLETO_POR_ID_QUERY = 
        "SELECT a.*, ta.nombre as tipo_nombre, ta.descripcion as tipo_descripcion, ta.precio_anuncio, " +
        "p.nombre as periodo_nombre, p.dias_duracion, p.precio_duracion " +
        "FROM anuncio a " +
        "INNER JOIN tipos_anuncio ta ON a.id_tipo_anuncio = ta.id_tipo_anuncio " +
        "INNER JOIN periodos_anuncio p ON a.id_periodo = p.id_periodo " +
        "WHERE a.id_anuncio = ?";
    
    private static final String TODOS_LOS_ANUNCIOS_QUERY = 
        "SELECT * FROM anuncio";
    
    private static final String ANUNCIOS_POR_USUARIO_QUERY = 
        "SELECT * FROM anuncio WHERE id_usuario = ?";
    
    private static final String ANUNCIOS_COMPLETOS_POR_USUARIO_QUERY = 
        "SELECT a.*, ta.nombre as tipo_nombre, ta.descripcion as tipo_descripcion, ta.precio_anuncio, " +
        "p.nombre as periodo_nombre, p.dias_duracion, p.precio_duracion " +
        "FROM anuncio a " +
        "INNER JOIN tipos_anuncio ta ON a.id_tipo_anuncio = ta.id_tipo_anuncio " +
        "INNER JOIN periodos_anuncio p ON a.id_periodo = p.id_periodo " +
        "WHERE a.id_usuario = ?";
    
    private static final String ACTUALIZAR_ANUNCIO_QUERY = 
        "UPDATE anuncio SET titulo = ?, contenido_texto = ?, imagen_url = ?, video_url = ?, estado = ? " +
        "WHERE id_anuncio = ?";
    
    // NUEVA QUERY PARA ACTUALIZAR CON IMAGEN BLOB
    private static final String ACTUALIZAR_ANUNCIO_CON_IMAGEN_QUERY = 
        "UPDATE anuncio SET titulo = ?, contenido_texto = ?, imagen_url = ?, video_url = ?, estado = ? " +
        "WHERE id_anuncio = ?";
    
    // NUEVA QUERY PARA ACTUALIZAR SOLO LA IMAGEN
    private static final String ACTUALIZAR_IMAGEN_ANUNCIO_QUERY = 
        "UPDATE anuncio SET imagen_url = ? WHERE id_anuncio = ?";
    
    // NUEVA QUERY PARA OBTENER SOLO LA IMAGEN
    private static final String OBTENER_IMAGEN_ANUNCIO_QUERY = 
        "SELECT imagen_url FROM anuncio WHERE id_anuncio = ?";
    
    private static final String ELIMINAR_ANUNCIO_QUERY = 
        "DELETE FROM anuncio WHERE id_anuncio = ?";
    
    private static final String EXISTE_ANUNCIO_POR_TITULO_USUARIO_QUERY = 
        "SELECT 1 FROM anuncio WHERE titulo = ? AND id_usuario = ?";

    /** Crea un nuevo anuncio en la base de datos */
    public Anuncio createAnuncio(Anuncio newAnuncio) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(CREAR_ANUNCIO_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            insert.setInt(1, newAnuncio.getIdUsuario());
            insert.setInt(2, newAnuncio.getIdTipoAnuncio());
            insert.setInt(3, newAnuncio.getIdPeriodo());
            insert.setString(4, newAnuncio.getTitulo());
            insert.setString(5, newAnuncio.getContenidoTexto());
            
            // Manejar imagen_url como String (puede ser null)
            if (newAnuncio.getImagenUrl() != null) {
                insert.setBytes(6, newAnuncio.getImagenUrl());
            } else {
                insert.setNull(6, Types.VARCHAR);
            }
            
            insert.setString(7, newAnuncio.getVideoUrl());
            insert.setTimestamp(8, Timestamp.valueOf(newAnuncio.getFechaInicio()));
            insert.setTimestamp(9, Timestamp.valueOf(newAnuncio.getFechaFin()));
            insert.setBigDecimal(10, newAnuncio.getCostoTotal());
            insert.setString(11, newAnuncio.getEstado());
            
            int affectedRows = insert.executeUpdate();
            
            if (affectedRows > 0) {
                // Obtener el ID generado
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newAnuncio.setIdAnuncio(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el anuncio", e);
        }
        return newAnuncio;
    }

    /** NUEVO: Crea un anuncio con imagen BLOB */
    public Anuncio createAnuncioConImagen(Anuncio newAnuncio, InputStream imagenInputStream) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(CREAR_ANUNCIO_CON_IMAGEN_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            
            insert.setInt(1, newAnuncio.getIdUsuario());
            insert.setInt(2, newAnuncio.getIdTipoAnuncio());
            insert.setInt(3, newAnuncio.getIdPeriodo());
            insert.setString(4, newAnuncio.getTitulo());
            insert.setString(5, newAnuncio.getContenidoTexto());
            
            // Manejar imagen como BLOB
            if (imagenInputStream != null) {
                insert.setBlob(6, imagenInputStream);
            } else {
                insert.setNull(6, Types.BLOB);
            }
            
            insert.setString(7, newAnuncio.getVideoUrl());
            insert.setTimestamp(8, Timestamp.valueOf(newAnuncio.getFechaInicio()));
            insert.setTimestamp(9, Timestamp.valueOf(newAnuncio.getFechaFin()));
            insert.setBigDecimal(10, newAnuncio.getCostoTotal());
            insert.setString(11, newAnuncio.getEstado());
            
            int affectedRows = insert.executeUpdate();
            
            if (affectedRows > 0) {
                // Obtener el ID generado
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newAnuncio.setIdAnuncio(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el anuncio con imagen", e);
        }
        return newAnuncio;
    }

    /** Verifica si existe un anuncio por título y usuario */
    public boolean existsAnuncio(String titulo, int idUsuario) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(EXISTE_ANUNCIO_POR_TITULO_USUARIO_QUERY)) {
            query.setString(1, titulo);
            query.setInt(2, idUsuario);
            ResultSet result = query.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Obtiene todos los anuncios */
    public List<Anuncio> getAllAnuncios() {
        List<Anuncio> anuncios = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(TODOS_LOS_ANUNCIOS_QUERY)) {
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                Anuncio anuncio = mapResultSetToAnuncio(resultSet);
                anuncios.add(anuncio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener todos los anuncios", e);
        }
        return anuncios;
    }

    /** Obtiene un anuncio por ID */
    public Optional<Anuncio> getAnuncioById(int idAnuncio) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(ENCONTRAR_ANUNCIO_POR_ID_QUERY)) {
            query.setInt(1, idAnuncio);
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()) {
                Anuncio anuncio = mapResultSetToAnuncio(resultSet);
                return Optional.of(anuncio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el anuncio con ID: " + idAnuncio, e);
        }
        return Optional.empty();
    }

    /** Obtiene un anuncio completo por ID (con joins) */
    public Optional<Anuncio> getAnuncioCompletoById(int idAnuncio) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(ENCONTRAR_ANUNCIO_COMPLETO_POR_ID_QUERY)) {
            query.setInt(1, idAnuncio);
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()) {
                Anuncio anuncio = mapResultSetToAnuncioCompleto(resultSet);
                return Optional.of(anuncio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el anuncio completo con ID: " + idAnuncio, e);
        }
        return Optional.empty();
    }

    /** Obtiene anuncios por usuario */
    public List<Anuncio> getAnunciosByUsuario(int idUsuario) {
        List<Anuncio> anuncios = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(ANUNCIOS_POR_USUARIO_QUERY)) {
            query.setInt(1, idUsuario);
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                Anuncio anuncio = mapResultSetToAnuncioCompleto(resultSet);
                anuncios.add(anuncio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener anuncios del usuario: " + idUsuario, e);
        }
        return anuncios;
    }

    /** Obtiene anuncios completos por usuario (con joins) */
    public List<Anuncio> getAnunciosCompletosByUsuario(int idUsuario) {
        List<Anuncio> anuncios = new ArrayList<>();
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(ANUNCIOS_COMPLETOS_POR_USUARIO_QUERY)) {
            query.setInt(1, idUsuario);
            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                Anuncio anuncio = mapResultSetToAnuncioCompleto(resultSet);
                anuncios.add(anuncio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener anuncios completos del usuario: " + idUsuario, e);
        }
        return anuncios;
    }

    /** Actualiza un anuncio */
    public boolean updateAnuncio(Anuncio anuncioToUpdate) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement updateStmt = connection.prepareStatement(ACTUALIZAR_ANUNCIO_QUERY)) {
            
            updateStmt.setString(1, anuncioToUpdate.getTitulo());
            updateStmt.setString(2, anuncioToUpdate.getContenidoTexto());
            
            // Manejar imagen_url como String
            if (anuncioToUpdate.getImagenUrl() != null) {
                updateStmt.setBytes(3, anuncioToUpdate.getImagenUrl());
            } else {
                updateStmt.setNull(3, Types.VARCHAR);
            }
            
            updateStmt.setString(4, anuncioToUpdate.getVideoUrl());
            updateStmt.setString(5, anuncioToUpdate.getEstado());
            updateStmt.setInt(6, anuncioToUpdate.getIdAnuncio());
            
            int affectedRows = updateStmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el anuncio", e);
        }
    }

    /** NUEVO: Actualiza un anuncio con imagen BLOB */
    public boolean updateAnuncioConImagen(Anuncio anuncioToUpdate, InputStream imagenInputStream) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement updateStmt = connection.prepareStatement(ACTUALIZAR_ANUNCIO_CON_IMAGEN_QUERY)) {
            
            updateStmt.setString(1, anuncioToUpdate.getTitulo());
            updateStmt.setString(2, anuncioToUpdate.getContenidoTexto());
            
            // Manejar imagen como BLOB
            if (imagenInputStream != null) {
                updateStmt.setBlob(3, imagenInputStream);
            } else {
                updateStmt.setNull(3, Types.BLOB);
            }
            
            updateStmt.setString(4, anuncioToUpdate.getVideoUrl());
            updateStmt.setString(5, anuncioToUpdate.getEstado());
            updateStmt.setInt(6, anuncioToUpdate.getIdAnuncio());
            
            int affectedRows = updateStmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el anuncio con imagen", e);
        }
    }

    /** NUEVO: Actualiza solo la imagen de un anuncio */
    public boolean actualizarImagenAnuncio(int idAnuncio, InputStream imagenInputStream) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement updateStmt = connection.prepareStatement(ACTUALIZAR_IMAGEN_ANUNCIO_QUERY)) {
            
            // Manejar imagen como BLOB
            if (imagenInputStream != null) {
                updateStmt.setBlob(1, imagenInputStream);
            } else {
                updateStmt.setNull(1, Types.BLOB);
            }
            
            updateStmt.setInt(2, idAnuncio);
            
            int affectedRows = updateStmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la imagen del anuncio", e);
        }
    }

    /** NUEVO: Obtiene la imagen de un anuncio como byte array */
    public byte[] getImagenAnuncio(int idAnuncio) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement query = connection.prepareStatement(OBTENER_IMAGEN_ANUNCIO_QUERY)) {
            query.setInt(1, idAnuncio);
            ResultSet resultSet = query.executeQuery();
            
            if (resultSet.next()) {
                Blob imagenBlob = resultSet.getBlob("imagen_url");
                if (imagenBlob != null) {
                    // Convertir Blob a byte array
                    return imagenBlob.getBytes(1, (int) imagenBlob.length());
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la imagen del anuncio con ID: " + idAnuncio, e);
        }
    }

    /** Elimina un anuncio por ID */
    public boolean deleteAnuncio(int idAnuncio) {
        Connection connection = DBConnectionSingleton.getInstance().getConnection();
        try (PreparedStatement deleteStmt = connection.prepareStatement(ELIMINAR_ANUNCIO_QUERY)) {
            
            deleteStmt.setInt(1, idAnuncio);
            int affectedRows = deleteStmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el anuncio", e);
        }
    }

    /** Método auxiliar para mapear un ResultSet a un objeto Anuncio básico */
    private Anuncio mapResultSetToAnuncio(ResultSet resultSet) throws SQLException {
        Anuncio anuncio = new Anuncio();
        anuncio.setIdAnuncio(resultSet.getInt("id_anuncio"));
        anuncio.setIdUsuario(resultSet.getInt("id_usuario"));
        anuncio.setIdTipoAnuncio(resultSet.getInt("id_tipo_anuncio"));
        anuncio.setIdPeriodo(resultSet.getInt("id_periodo"));
        anuncio.setTitulo(resultSet.getString("titulo"));
        anuncio.setContenidoTexto(resultSet.getString("contenido_texto"));
        
        // Manejar imagen_url que ahora puede ser BLOB
        Blob imagenBlob = resultSet.getBlob("imagen_url");
        if (imagenBlob != null) {
            // Si quieres almacenar la imagen como base64 en el objeto, puedes hacerlo aquí
            // Por ahora lo dejamos como null ya que se manejará por separado
            anuncio.setImagenUrl(null);
        } else {
            // Si era un string URL, mantenerlo
            anuncio.setImagenUrl(resultSet.getBytes("imagen_url"));
        }
        
        anuncio.setVideoUrl(resultSet.getString("video_url"));
        anuncio.setFechaInicio(resultSet.getTimestamp("fecha_inicio").toLocalDateTime());
        anuncio.setFechaFin(resultSet.getTimestamp("fecha_fin").toLocalDateTime());
        anuncio.setCostoTotal(resultSet.getBigDecimal("costo_total"));
        anuncio.setEstado(resultSet.getString("estado"));
        
        return anuncio;
    }

    /** Método auxiliar para mapear un ResultSet a un objeto Anuncio completo (con joins) */
    private Anuncio mapResultSetToAnuncioCompleto(ResultSet resultSet) throws SQLException {
    Anuncio anuncio = mapResultSetToAnuncio(resultSet);
    
    try {
        // Crear y asignar TipoAnuncio
        TipoAnuncio tipoAnuncio = new TipoAnuncio();
        tipoAnuncio.setIdTipoAnuncio(resultSet.getInt("id_tipo_anuncio"));
        tipoAnuncio.setNombre(resultSet.getString("tipo_nombre"));
        tipoAnuncio.setDescripcion(resultSet.getString("tipo_descripcion"));
        tipoAnuncio.setPrecioAnuncio(resultSet.getBigDecimal("precio_anuncio"));
        
        // Crear y asignar PeriodoAnuncio
        PeriodoAnuncio periodoAnuncio = new PeriodoAnuncio();
        periodoAnuncio.setIdPeriodo(resultSet.getInt("id_periodo"));
        periodoAnuncio.setNombre(resultSet.getString("periodo_nombre"));
        periodoAnuncio.setDiasDuracion(resultSet.getInt("dias_duracion"));
        periodoAnuncio.setPrecioDuracion(resultSet.getBigDecimal("precio_duracion"));
        
        // Asignar los objetos relacionados al anuncio
        anuncio.setTipoAnuncio(tipoAnuncio);
        anuncio.setPeriodoAnuncio(periodoAnuncio);
        
        // Log para depuración
        System.out.println("Mapeando anuncio completo - ID: " + anuncio.getIdAnuncio());
        System.out.println("TipoAnuncio asignado: " + (tipoAnuncio != null ? tipoAnuncio.getNombre() : "NULL"));
        System.out.println("PeriodoAnuncio asignado: " + (periodoAnuncio != null ? periodoAnuncio.getNombre() : "NULL"));
        
    } catch (SQLException e) {
        System.err.println("Error mapeando objetos relacionados: " + e.getMessage());
        e.printStackTrace();
    }
    
    return anuncio;
}
}       