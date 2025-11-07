import { AnuncioCompleto } from './../../models/Anuncio/AnuncioCompleto';
import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { Anuncio } from '../../models/Anuncio/anuncio';
import { AnunciosService } from '../../services/anuncios/anuncios.service';

import { HomesService, User } from '../../services/homes/homes.services';
import { HeaderAdminSistema } from '../../components/header-admin-sistema/header-admin-sistema';
import { HeaderAdminCine } from '../../components/header-admin-cine/header-admin-cine';
import { Footer } from '../../components/footer/footer';
import { HeaderAnunciante } from '../../components/header-anunciante/header-anunciante';
import { RouterLink } from '@angular/router';
import { AnuncioCardComponent } from '../../components/anuncios-gestion/anuncio-card-component/anuncio-card-component';
import { AnuncioCineCardComponent } from '../../components/anuncios-gestion/anuncio-cine-card-component/anuncio-cine-card-component';

@Component({
  selector: 'app-anuncions-page',
  imports: [NgIf, AnuncioCardComponent, AnuncioCineCardComponent, Footer, HeaderAdminSistema, HeaderAdminCine, HeaderAnunciante, RouterLink],
  templateUrl: './anuncions-page.html',
  styleUrl: './anuncions-page.css'
})
export class AnuncionsPage {
  protected anuncios: AnuncioCompleto[] = [];
  currentUser: User | null = null;
  
  constructor(private anunciosService: AnunciosService, private homesService: HomesService) {}

  ngOnInit(): void {
    this.currentUser = this.homesService.getCurrentUser()
    this.cargarAnuncios();
  }

  private cargarAnuncios(): void {
    if (this.currentUser?.rol?.nombreRol === 'ANUNCIANTE') {
      this.anunciosService.getAnunciosCompletosByUsuario(this.currentUser?.idUsuario || 0).subscribe(
        (anuncios) => {
          this.anuncios = anuncios;
          console.log('Anuncios cargados:', this.anuncios);
        }
      );
    } else if (this.currentUser?.rol?.nombreRol === 'ADMINISTRADOR DE SISTEMA') {
      this.anunciosService.getAllAnuncios().subscribe(
        (anuncios) => {
          this.anuncios = anuncios;
          console.log('Anuncios cargados:', this.anuncios);
        }
      );
    } else if (this.currentUser?.rol?.nombreRol === 'ADMINISTRADOR DE CINE') {
      // Para cine, cargar solo anuncios que tienen publicidad
      this.anunciosService.getAnunciosConPublicidad().subscribe(
        (anuncios: AnuncioCompleto[]) => {
          this.anuncios = anuncios;
          console.log('Publicidad disponible para cine:', this.anuncios);
        }
      );
    } else {
      console.warn('Usuario no autorizado para ver anuncios');
    }
  }

  onPublicidadAutorizada(): void {
    console.log('Publicidad autorizada, recargando anuncios...');
    this.cargarAnuncios();
  }

  onAnuncioBloqueado(): void {
    console.log('Anuncio bloqueado, recargando anuncios...');
    this.cargarAnuncios();
  }

  recargarAnuncios(): void {
    this.cargarAnuncios();
  }
}
