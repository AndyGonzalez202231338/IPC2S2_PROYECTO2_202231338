import { Role } from "./role";

export interface UserToUpdateRequest {
    idUsuario: number;
    rol: Role;
    email: string; // Asegúrate de incluir el email
    password: string;
    nombreCompleto: string;
    estado: 'ACTIVO' | 'INACTIVO';
    fechaCreacion: string; // Mantener como string pero en formato ISO
}