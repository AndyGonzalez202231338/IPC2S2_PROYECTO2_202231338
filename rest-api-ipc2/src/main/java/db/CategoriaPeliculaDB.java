package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.movies.CategoriaPelicula;

/**
 *
 * @author andy
 */
public class CategoriaPeliculaDB {

    private static final String SELECT_ALL_CATEGORIAS = 
        "SELECT id_categoria, nombre_categoria, descripcion FROM categoria_pelicula";
    
    private static final String SELECT_CATEGORIA_BY_ID = 
        "SELECT id_categoria, nombre_categoria, descripcion FROM categoria_pelicula WHERE id_categoria = ?";

    /** Obtiene todas las categorías de películas */
    public List<CategoriaPelicula> getAllCategorias() {
        List<CategoriaPelicula> list = new ArrayList<>();
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_CATEGORIAS)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CategoriaPelicula categoria = mapToCategoriaPelicula(rs);
                list.add(categoria);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener las categorías de películas", e);
        }
        
        return list;
    }

    /** Obtiene categoría por ID */
    public Optional<CategoriaPelicula> getCategoriaById(int idCategoria) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_CATEGORIA_BY_ID)) {
            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                CategoriaPelicula categoria = mapToCategoriaPelicula(rs);
                return Optional.of(categoria);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la categoría con ID: " + idCategoria, e);
        }
        
        return Optional.empty();
    }

    
    /** Mapea un ResultSet a un objeto CategoriaPelicula */
    private CategoriaPelicula mapToCategoriaPelicula(ResultSet rs) throws SQLException {
        return new CategoriaPelicula(
            rs.getInt("id_categoria"),
            rs.getString("nombre_categoria"),
            rs.getString("descripcion")
        );
    }
}