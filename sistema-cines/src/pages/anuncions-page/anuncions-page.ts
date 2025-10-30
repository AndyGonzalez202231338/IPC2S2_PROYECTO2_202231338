import { AnuncioCompleto } from './../../models/Anuncio/AnuncioCompleto';
import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { Anuncio } from '../../models/Anuncio/anuncio';
import { AnunciosService } from '../../services/anuncios/anuncios.service';
import { AnuncioCardComponent } from '../../components/anuncios-gestion/anuncio-card-component/anuncio-card-component';
import { HomesService, User } from '../../services/homes/homes.services';
import { HeaderAdminSistema } from '../../components/header-admin-sistema/header-admin-sistema';
import { HeaderAdminCine } from '../../components/header-admin-cine/header-admin-cine';
import { Footer } from '../../components/footer/footer';
import { HeaderAnunciante } from '../../components/header-anunciante/header-anunciante';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-anuncions-page',
  imports: [NgIf, AnuncioCardComponent, Footer, HeaderAdminSistema, HeaderAdminCine, HeaderAnunciante, RouterLink],
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
    // verificar el rol del usuario antes de cargar los anuncios, si es anunciante use getAnunciosCompletosByUsuario pero si es administrador de sistema use getAllAnuncios
    if (this.currentUser?.rol?.nombreRol === 'ANUNCIANTE') {
      this.anunciosService.getAnunciosCompletosByUsuario(this.currentUser?.idUsuario || 0).subscribe(
        (anuncios) => {
          this.anuncios = anuncios;
          console.log('Anuncios cargados:', this.anuncios);
        }
      );
    } else if (this.currentUser?.rol?.nombreRol === 'ADMINISTRADOR DE SISTEMA') {
      this.anunciosService.getAllAnuncios().subscribe(
        (anuncios: AnuncioCompleto[]) => {
          this.anuncios = anuncios;
          console.log('Anuncios cargados:', this.anuncios);
        }
      );
    }else {
      console.warn('Usuario no autorizado para ver anuncios');
    }
  }

  recargarAnuncios(): void {
    this.cargarAnuncios();
  }
}
