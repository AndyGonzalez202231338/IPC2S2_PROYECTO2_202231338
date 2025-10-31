import { Component } from '@angular/core';
import { HomesService, User } from '../../services/homes/homes.services';
import { HeaderAnunciante } from '../../components/header-anunciante/header-anunciante';
import { Footer } from '../../components/footer/footer';
import { HeaderAdminSistema } from '../../components/header-admin-sistema/header-admin-sistema';
import { RouterLink } from '@angular/router';
import { NgIf } from '@angular/common';
import { Header } from '../../components/header/header';
import { CreateMovieComponent } from '../../components/movies-gestion/create-movie-component/create-movie-component';

@Component({
  selector: 'app-create-movies-page',
  imports: [RouterLink, CreateMovieComponent, HeaderAdminSistema, Header, Footer, NgIf],
  templateUrl: './create-movies-page.html',
  styleUrl: './create-movies-page.css'
})
export class CreateMoviesPage {
  currentUser: User | null = null;

  constructor(private homesService: HomesService) {}

  ngOnInit(): void {
    // Obtener el usuario actual del servicio
    this.currentUser = this.homesService.getCurrentUser();
  } 
}
