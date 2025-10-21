package db;

import java.time.LocalDateTime;
import java.util.*;
import models.users.User;
import models.users.Role;

public class UsersDB {

    private static final Map<Integer, User> DB = new HashMap<>();

    static {
        Role adminSistema = new Role(1, "ADMINISTRADOR DE SISTEMA",
                "Usuario encargado de cines, peliculas y reportes...");
        Role adminCine = new Role(2, "ADMINISTRADOR DE CINE",
                "Usuario encargado de un cine, salas, funciones...");
        Role anunciante = new Role(3, "ANUNCIANTE",
                "Usuario con capacidad para crear anuncios...");
        Role comun = new Role(4, "COMUN",
                "Usuario que compra boletos...");

        DB.put(1, new User(1, adminSistema, "admin@mail.com", "12345",
                "Administrador General", "ACTIVO", LocalDateTime.now()));
        DB.put(2, new User(2, adminCine, "cine@mail.com", "cine123",
                "María Maria", "ACTIVO", LocalDateTime.now()));
        DB.put(3, new User(3, anunciante, "anunciante@mail.com", "abcde",
                "Carlos López", "INACTIVO", LocalDateTime.now()));
        DB.put(4, new User(4, comun, "usuario@mail.com", "clave",
                "Juan Pérez", "ACTIVO", LocalDateTime.now()));
    }

    /** Crea un nuevo usuario en la base en memoria
     * @param newUser
     * @return  */
    public User createUser(User newUser) {
        DB.put(newUser.getIdUsuario(), newUser);
        return newUser;
    }

    /** Verifica si existe un usuario por su ID
     * @param idUsuario
     * @return  */
    public boolean existsUser(int idUsuario) {
        return DB.containsKey(idUsuario);
    }

    
    public List<User> getAllUsers() {
        return new ArrayList<>(DB.values());
    }

    
    public Optional<User> getById(int idUsuario) {
        return Optional.ofNullable(DB.get(idUsuario));
    }

    
    public Optional<User> getByEmail(String email) {
        return DB.values()
                .stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    
    public User updateUser(Integer idUsuario, User userToUpdate) {
        return DB.put(idUsuario, userToUpdate);
    }
    
    public User updateUserByEmail(String email, User userToUpdate) {
    for (Map.Entry<Integer, User> entry : DB.entrySet()) {
        User existingUser = entry.getValue();
        if (existingUser.getEmail().equalsIgnoreCase(email)) {
            DB.put(entry.getKey(), userToUpdate);
            return userToUpdate;
        }
    }
    return null; // si no se encontró el usuario con ese correo
}


    
    public boolean deleteUser(int idUsuario) {
        return DB.remove(idUsuario) != null;
    }
}
