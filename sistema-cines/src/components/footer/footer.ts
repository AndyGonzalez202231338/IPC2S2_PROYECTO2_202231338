import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HomesService } from '../../services/homes/homes.services';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-footer',
  imports: [CommonModule],
  templateUrl: './footer.html',
  styleUrl: './footer.css'
})
export class Footer {
  constructor(
    private homesService: HomesService,
    private router: Router
  ) {}

  // Verificar si hay usuario logueado
  isLoggedIn(): boolean {
    return this.homesService.isAuthenticated();
  }

  // Cerrar sesión
  logout(): void {
    this.homesService.logout();
    this.router.navigate(['/login']);
  }

  // Obtener información del usuario para mostrar en el footer
  getCurrentUser() {
    return this.homesService.getCurrentUser();
  }
}
