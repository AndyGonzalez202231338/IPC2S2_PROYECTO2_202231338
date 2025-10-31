package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.movies.Movies;

public class MovieDB {

    private static final String INSERT_MOVIE
            = "INSERT INTO pelicula (titulo, sinopsis, duracion_minutos, director, reparto, clasificacion, fecha_estreno, poster_url, estado) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_MOVIES
            = "SELECT id_pelicula, titulo, sinopsis, duracion_minutos, director, reparto, clasificacion, fecha_estreno, poster_url, estado FROM pelicula";

    private static final String SELECT_MOVIE_BY_ID
            = "SELECT id_pelicula, titulo, sinopsis, duracion_minutos, director, reparto, clasificacion, fecha_estreno, poster_url, estado FROM pelicula WHERE id_pelicula = ?";

    private static final String UPDATE_MOVIE
            = "UPDATE pelicula SET titulo = ?, sinopsis = ?, duracion_minutos = ?, director = ?, reparto = ?, clasificacion = ?, fecha_estreno = ?, estado = ? WHERE id_pelicula = ?";

    private static final String DELETE_MOVIE
            = "DELETE FROM pelicula WHERE id_pelicula = ?";

    private static final String CHECK_CATEGORIA_EXISTS
            = "SELECT 1 FROM categoria_pelicula WHERE id_categoria = ?";

    private static final String UPDATE_MOVIE_ESTADO
            = "UPDATE pelicula SET estado = ? WHERE id_pelicula = ?";

    public int insertMovie(Movies movie) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(INSERT_MOVIE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, movie.getTitulo());
            stmt.setString(2, movie.getSinopsis());
            stmt.setInt(3, movie.getDuracionMinutos());
            stmt.setString(4, movie.getDirector());
            stmt.setString(5, movie.getReparto());
            stmt.setString(6, movie.getClasificacion());
            stmt.setDate(7, new java.sql.Date(movie.getFechaEstreno().getTime()));
            stmt.setBytes(8, convertToPrimitiveBytes(movie.getPosterUrl()));
            stmt.setString(9, movie.getEstado());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }

            throw new SQLException("No se pudo obtener el ID generado para la película");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar la película", e);
        }
    }

    public List<Movies> getAllMovies() {
        List<Movies> list = new ArrayList<>();
        Connection conn = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_MOVIES)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Movies movie = mapToMovie(rs);
                list.add(movie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener las películas", e);
        }

        return list;
    }

    public Optional<Movies> getMovieById(int id) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_MOVIE_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Movies movie = mapToMovie(rs);
                return Optional.of(movie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la película con ID: " + id, e);
        }

        return Optional.empty();
    }

    public boolean updateMovie(Movies movie) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_MOVIE)) {
            stmt.setString(1, movie.getTitulo());
            stmt.setString(2, movie.getSinopsis());
            stmt.setInt(3, movie.getDuracionMinutos());
            stmt.setString(4, movie.getDirector());
            stmt.setString(5, movie.getReparto());
            stmt.setString(6, movie.getClasificacion());
            stmt.setDate(7, new java.sql.Date(movie.getFechaEstreno().getTime()));
            stmt.setString(8, movie.getEstado());
            stmt.setInt(9, movie.getIdPelicula());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la película", e);
        }
    }

    public boolean updateMovieEstado(int id, String estado) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_MOVIE_ESTADO)) {
            stmt.setString(1, estado);
            stmt.setInt(2, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el estado de la película", e);
        }
    }

    public boolean deleteMovie(int id) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(DELETE_MOVIE)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar la película", e);
        }
    }

    public boolean categoriaExists(int idCategoria) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(CHECK_CATEGORIA_EXISTS)) {
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al verificar categoría", e);
        }
    }

    private Movies mapToMovie(ResultSet rs) throws SQLException {
        return new Movies(
                rs.getInt("id_pelicula"),
                rs.getString("titulo"),
                rs.getString("sinopsis"),
                rs.getInt("duracion_minutos"),
                rs.getString("director"),
                rs.getString("reparto"),
                rs.getString("clasificacion"),
                rs.getDate("fecha_estreno"),
                convertToByteObjects(rs.getBytes("poster_url")),
                rs.getString("estado")
        );
    }

    private byte[] convertToPrimitiveBytes(Byte[] byteObjects) {
        if (byteObjects == null) {
            return null;
        }

        byte[] bytes = new byte[byteObjects.length];
        for (int i = 0; i < byteObjects.length; i++) {
            bytes[i] = byteObjects[i];
        }
        return bytes;
    }

    private Byte[] convertToByteObjects(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        Byte[] byteObjects = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byteObjects[i] = bytes[i];
        }
        return byteObjects;
    }
}
