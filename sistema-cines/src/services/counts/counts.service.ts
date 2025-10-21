import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Count, Role } from "../../models/Counts/count";
import { Observable, of } from "rxjs";
import { User } from "../homes/homes.services";
import { CountTypeEnum } from "../../models/Counts/count-type";
import { UserToUpdateRequest } from "../../models/Counts/update-user-request";

@Injectable({
    providedIn: 'root'
})
export class CountsService {
    private selectedUser: Count | null = null;
    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    public createNewCount(count: Count): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}users`, count);
    }

    public getAllUsers(): Observable<Count[]> {
        return this.httpClient.get<Count[]>(`${this.restConstants.getApiURL()}users`);
    }

    public getCountByEmail(email: string): Observable<Count> {
        return this.httpClient.get<Count>(`${this.restConstants.getApiURL()}users/${email}`);
    }

    public updateUser(correo: string, userToUpdate: UserToUpdateRequest): Observable<Count> {
    return this.httpClient.put<Count>(`${this.restConstants.getApiURL()}users/${correo}`, userToUpdate);
    }
    // Métodos para manejar el usuario seleccionado
    setSelectedUser(user: Count): void {
        this.selectedUser = user;
    }

    getSelectedUser(): Count | null {
        return this.selectedUser;
    }

    clearSelectedUser(): void {
        this.selectedUser = null;
    }

    // Usuarios de prueba (temporal)
    private testUsers: User[] = [
  {
    idUsuario: 1,
    role: {
      idRol: 1,
      nombreRol: 'ADMINISTRADOR DE SISTEMA',
      descripcion: 'Usuario encargado de cines, películas y reportes de ganancias del sistema en anuncios. Gestiona las cuentas de los usuarios de la web.'
    },
    email: 'admin@mail.com',
    password: 'admin123',
    nombreCompleto: 'Administrador General',
    estado: 'ACTIVO',
    fechaCreacion: new Date().toISOString()
  },
  {
    idUsuario: 2,
    role: {
      idRol: 2,
      nombreRol: 'ADMINISTRADOR DE CINE',
      descripcion: 'Usuario encargado de un cine, administración de salas, funciones, bloqueo de anuncios y reportes de ganancias generadas por compra de boletos.'
    },
    email: 'cine@mail.com',
    password: 'cine123',
    nombreCompleto: 'María Gonzalez',
    estado: 'ACTIVO',
    fechaCreacion: new Date().toISOString()
  },
  {
    idUsuario: 3,
    role: {
      idRol: 3,
      nombreRol: 'ANUNCIANTE',
      descripcion: 'Usuario con capacidad para crear anuncios y gestionar sus publicaciones en la web.'
    },
    email: 'anunciante@mail.com',
    password: 'anunciante123',
    nombreCompleto: 'Carlos López',
    estado: 'INACTIVO',
    fechaCreacion: new Date().toISOString()
  },
  {
    idUsuario: 4,
    role: {
      idRol: 4,
      nombreRol: 'COMUN',
      descripcion: 'Usuario que compra boletos para funciones de cine dentro del sistema.'
    },
    email: 'usuario@mail.com',
    password: 'usuario123',
    nombreCompleto: 'Juan Pérez',
    estado: 'ACTIVO',
    fechaCreacion: new Date().toISOString()
  }
];


    // Método temporal que simula la obtención de todos los usuarios
    //para un strng desde un backend role: this.mapRole(user.role.nombreRol)

    getAllCounts(): Observable<User[]> {
  const mockUsers: User[] = this.testUsers.map((user, index) => ({
    idUsuario: index + 1,
    nombreCompleto: user.nombreCompleto,
    email: user.email,
    password: user.password,
    estado: user.estado,
    fechaCreacion: user.fechaCreacion,
    role: user.role //ya es un objeto Role, no hace falta mapear
  }));

  return of(mockUsers);
}


private mapRole(roleName: string): Role {
  switch (roleName.toUpperCase()) {
    case 'ADMINISTRADOR DE SISTEMA':
      return {
        idRol: 1,
        nombreRol: 'ADMINISTRADOR DE SISTEMA',
        descripcion: 'Usuario encargado de cines, películas y reportes...'
      };
    case 'ADMINISTRADOR DE CINE':
      return {
        idRol: 2,
        nombreRol: 'ADMINISTRADOR DE CINE',
        descripcion: 'Usuario encargado de un cine, salas, funciones...'
      };
    case 'ANUNCIANTE':
      return {
        idRol: 3,
        nombreRol: 'ANUNCIANTE',
        descripcion: 'Usuario con capacidad para crear anuncios...'
      };
    default:
      return {
        idRol: 4,
        nombreRol: 'COMUN',
        descripcion: 'Usuario que compra boletos...'
      };
  }
}


    // Función auxiliar para mapear roles de string a CountTypeEnum
    private mapRoleToCountType(role: string): CountTypeEnum {
        const roleMap: { [key: string]: CountTypeEnum } = {
            'ADMIN_SISTEMA': CountTypeEnum.ADMIN_SISTEMA,
            'ADMIN_CINE': CountTypeEnum.ADMIN_CINE,
            'ANUNCIANTE': CountTypeEnum.ANUNCIANTE,
            'COMUN': CountTypeEnum.COMUN
        };
        
        return roleMap[role] || CountTypeEnum.COMUN;
    }
}