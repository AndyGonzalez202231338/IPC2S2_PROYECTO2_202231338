package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.movies.CategoriaPelicula;

public class PeliculaCategoriasDB {
    
    private static final String INSERT_PELICULA_CATEGORIA = 
        "INSERT INTO pelicula_categorias (id_pelicula, id_categoria) VALUES (?, ?)";
    
    private static final String SELECT_CATEGORIAS_BY_PELICULA = 
        "SELECT cp.id_categoria, cp.nombre_categoria, cp.descripcion " +
        "FROM categoria_pelicula cp " +
        "INNER JOIN pelicula_categorias pc ON cp.id_categoria = pc.id_categoria " +
        "WHERE pc.id_pelicula = ?";
    
    private static final String DELETE_CATEGORIAS_BY_PELICULA = 
        "DELETE FROM pelicula_categorias WHERE id_pelicula = ?";
    
    public void insertPeliculaCategoria(int idPelicula, int idCategoria) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_PELICULA_CATEGORIA)) {
            stmt.setInt(1, idPelicula);
            stmt.setInt(2, idCategoria);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar relación película-categoría", e);
        }
    }
    
    public List<CategoriaPelicula> getCategoriasByPeliculaId(int idPelicula) {
        List<CategoriaPelicula> list = new ArrayList<>();
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_CATEGORIAS_BY_PELICULA)) {
            stmt.setInt(1, idPelicula);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CategoriaPelicula categoria = new CategoriaPelicula(
                    rs.getInt("id_categoria"),
                    rs.getString("nombre_categoria"),
                    rs.getString("descripcion")
                );
                list.add(categoria);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener categorías de la película", e);
        }
        
        return list;
    }
    
    public void deleteCategoriasByPeliculaId(int idPelicula) {
        Connection conn = DBConnectionSingleton.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_CATEGORIAS_BY_PELICULA)) {
            stmt.setInt(1, idPelicula);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar categorías de la película", e);
        }
    }
}
