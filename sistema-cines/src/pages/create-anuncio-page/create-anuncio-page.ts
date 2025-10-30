import { NgIf } from '@angular/common';
import { HomesService, User } from './../../services/homes/homes.services';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CreateAnuncioComponent } from '../../components/anuncios-gestion/create-anuncio-component/create-anuncio-component';
import { HeaderAdminSistema } from '../../components/header-admin-sistema/header-admin-sistema';
import { HeaderAnunciante } from '../../components/header-anunciante/header-anunciante';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-create-anuncio-page',
  imports: [RouterLink, CreateAnuncioComponent, HeaderAdminSistema, HeaderAnunciante, Footer, NgIf],
  templateUrl: './create-anuncio-page.html',
  styleUrl: './create-anuncio-page.css'
})
export class CreateAnuncioPage {
  currentUser: User | null = null;

  constructor(private homesService: HomesService) {}

  ngOnInit(): void {
    // Obtener el usuario actual del servicio
    this.currentUser = this.homesService.getCurrentUser();
  }
}
