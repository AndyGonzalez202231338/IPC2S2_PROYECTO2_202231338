export interface Cine {
    idCine?: number;
    nombre: string;
    direccion: string;
    fechaCreacion?: string | Date;
    estado?: 'ACTIVO' | 'INACTIVO';
    administradores?: number[]; // Array de IDs de usuarios administradores
}