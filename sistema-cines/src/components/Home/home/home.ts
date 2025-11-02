import { HomesService, User } from './../../../services/homes/homes.services';
import { Footer } from './../../footer/footer';
import { Component, OnInit } from '@angular/core';
import { Header } from '../../header/header';
import { CommonModule } from '@angular/common';
import { HeaderAdminSistema } from '../../header-admin-sistema/header-admin-sistema';
import { HeaderAdminCine } from "../../header-admin-cine/header-admin-cine";
import { HeaderAnunciante } from '../../header-anunciante/header-anunciante';
import { CineAdministradorService } from '../../../services/cines/cine-administrador.service';
import { Cine } from '../../../models/Cines/cine';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [Header, Footer, CommonModule, HeaderAdminSistema, HeaderAdminCine, HeaderAnunciante],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  protected normalTitle = 'Home';
  currentUser: User | null = null;
  cines: Cine[] = [];
  cineSeleccionado: Cine | null = null;
  mostrarSelectorCine: boolean = false;

  constructor(
    private homesService: HomesService, 
    private cineAdminService: CineAdministradorService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Obtener el usuario actual del servicio
    this.currentUser = this.homesService.getCurrentUser();

    // Verificar si ya tiene un cine seleccionado
    const cineGuardado = this.homesService.getCineSeleccionado();
    if (cineGuardado) {
      console.log('Ya tiene cine seleccionado:', cineGuardado);
      this.cineSeleccionado = cineGuardado;
    }

    // Si es administrador de cine y no tiene cine seleccionado, cargar sus cines
    if (this.isAdminCine() && !this.cineSeleccionado) {
      console.log('Es un adiministrador de cine, cargando cines...');
      this.cargarCinesAdministrador();
    }
  }

  private cargarCinesAdministrador(): void {
    if (this.currentUser) {
      this.cineAdminService.getCinesByAdministrador(this.currentUser.idUsuario).subscribe({
        next: (cines) => {
          this.cines = cines;
          console.log('Cines asignados al administrador:', this.cines);
          
          // Si solo tiene un cine, seleccionarlo automáticamente
          if (this.cines.length === 1) {
            this.seleccionarCine(this.cines[0]);
          } else if (this.cines.length > 1) {
            this.mostrarSelectorCine = true;
          }
        },
        error: (err) => {
          console.error('Error al cargar cines:', err);
        }
      });
    }
  }

  seleccionarCine(cine: Cine): void {
    this.cineSeleccionado = cine;
    this.homesService.setCineSeleccionado(cine);
    this.mostrarSelectorCine = false;
    console.log('Cine seleccionado:', cine);
  }

  cambiarCine(): void {
    this.mostrarSelectorCine = true;
  }

  //comprobar el rol del usuario
  isAdminCine(): boolean {
    return this.currentUser?.rol.nombreRol === 'ADMINISTRADOR DE CINE';
  }

  // Navegar al dashboard del cine
  irADashboardCine(): void {
    if (this.cineSeleccionado) {
      this.router.navigate(['/cine/dashboard', this.cineSeleccionado.idCine]);
    }
  }
}