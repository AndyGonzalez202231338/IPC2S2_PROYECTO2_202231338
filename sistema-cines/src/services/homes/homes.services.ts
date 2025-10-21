import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { Role } from "../../models/Counts/role";

// Interface para el usuario
export interface User {
  idUsuario: number;
  role: Role;
  email: string;
  password: string;
  nombreCompleto: string;
  estado: 'ACTIVO' | 'INACTIVO';
  fechaCreacion: string;
}

// Interface para la respuesta del login
export interface LoginResponse {
  success: boolean;
  user?: User;
  message?: string;
}

@Injectable({
  providedIn: 'root'
})
export class HomesService {
  restConstants = new RestConstants();
  private currentUser: User | null = null;

  constructor(private httpClient: HttpClient) { }

  // Método para login que consume la API JAX-RS de NetBeans
  login(email: string, password: string): Observable<LoginResponse> {
    const loginData = {
      email: email,
      password: password
    };

    return this.httpClient.post<LoginResponse>(
      `${this.restConstants.getApiURL()}auth/login`, 
      loginData
    ).pipe(
      tap(response => {
        if (response.success && response.user) {
          this.currentUser = response.user;
        } else {
          this.currentUser = null;
        }
      })
    );
  }

  // Obtener el usuario actual
  getCurrentUser(): User | null {
    return this.currentUser;
  }

  // Cerrar sesión
  logout(): void {
    this.currentUser = null;
  }

  // Método para verificar si el usuario está autenticado
  isAuthenticated(): boolean {
    return this.currentUser !== null;
  }
}