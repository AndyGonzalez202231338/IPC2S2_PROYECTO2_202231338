import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { Footer } from '../../components/footer/footer';
import { Header } from '../../components/header/header';
import { HeaderAdminSistema } from '../../components/header-admin-sistema/header-admin-sistema';
import { HomesService, User } from '../../services/homes/homes.services';
import { RouterLink } from '@angular/router';
import { MovieCardComponent } from '../../components/movies-gestion/movie-card-component/movie-card-component';
import { Movie } from '../../models/Movies/Movie';
import { MoviesService } from '../../services/movies/movies.service';

@Component({
  selector: 'app-movies-page',
  imports: [Footer, HeaderAdminSistema, Header, NgIf, RouterLink, MovieCardComponent],
  templateUrl: './movies-page.html',
  styleUrl: './movies-page.css'
})
export class MoviesPage {
  protected movies: Movie[] = [];
  currentUser: User | null = null;

  constructor(private homeService : HomesService, private moviesService: MoviesService) {}

  ngOnInit(): void {
    this.currentUser = this.homeService.getCurrentUser();
    this.loadMovies();
  }

  private loadMovies(): void {
    console.log('Loading movies from DB...');
    this.moviesService.getAllMovies().subscribe({
      next: (movies) => {
        this.movies = movies;
        console.log('Películas cargadas de DB:', this.movies);
      },
      error: (err) => {
        console.error('Error loading movies:', err);
      }
    });
  }

}
