export interface Cine {
    id_cine: number;
    id_usuario_admin: number;
    nombre: string;
    direccion: string;
    fecha_creacion: Date;
    //estado activo/inactivo
    estado: boolean;
}