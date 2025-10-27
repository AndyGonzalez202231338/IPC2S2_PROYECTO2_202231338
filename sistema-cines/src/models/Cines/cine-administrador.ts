export interface CineAdministrador {
    id_cine_administrador?: number;
    id_cine: number;
    id_usuario: number;
    fecha_asignacion?: string | Date;
    estado?: 'ACTIVO' | 'INACTIVO';
}