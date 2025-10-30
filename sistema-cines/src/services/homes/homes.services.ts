import { Injectable, Inject, PLATFORM_ID } from "@angular/core";
import { isPlatformBrowser } from "@angular/common";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { Role } from "../../models/Counts/role";

export interface User {
  idUsuario: number;
  rol: Role;
  email: string;
  password: string;
  nombreCompleto: string;
  estado: 'ACTIVO' | 'INACTIVO';
  fechaCreacion: string;
}

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
  private isBrowser: boolean;

  constructor(
    private httpClient: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);

    //Solo accede a localStorage si está en navegador
    if (this.isBrowser) {
      const userData = localStorage.getItem('currentUser');
      if (userData) {
        this.currentUser = JSON.parse(userData);
      }
    }
  }

  login(email: string, password: string): Observable<LoginResponse> {
    const loginData = { email, password };

    return this.httpClient.post<LoginResponse>(
      `${this.restConstants.getApiURL()}auth/login`,
      loginData
    ).pipe(
      tap(response => {
        if (response.success && response.user) {
          this.currentUser = response.user;
          if (this.isBrowser) {
            localStorage.setItem('currentUser', JSON.stringify(response.user));
          }
        } else {
          this.currentUser = null;
          if (this.isBrowser) {
            localStorage.removeItem('currentUser');
          }
        }
      })
    );
  }

  getCurrentUser(): User | null {
    return this.currentUser;
  }

  isAuthenticated(): boolean {
    return this.currentUser !== null;
  }

  logout(): void {
    this.currentUser = null;
    if (this.isBrowser) {
      localStorage.removeItem('currentUser');
    }
  }
}
