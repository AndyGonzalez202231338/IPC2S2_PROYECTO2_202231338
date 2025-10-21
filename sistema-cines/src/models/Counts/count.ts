import { CountTypeEnum } from "./count-type";  

export interface Count {
    idUsuario: number;
    rol: Role;
    email: string;
    password: string;
    nombreCompleto: string;
    estado: 'ACTIVO' | 'INACTIVO';
    fechaCreacion: string; // LocalDateTime llega como string ISO
}

export interface Role {
    idRol: number;
    nombreRol: string;
    descripcion: string;
}