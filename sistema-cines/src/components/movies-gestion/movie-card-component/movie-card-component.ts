import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { Movie } from '../../../models/Movies/Movie';
import { MoviesService } from '../../../services/movies/movies.service';

@Component({
  selector: 'app-movie-card-component',
  templateUrl: './movie-card-component.html',
  styleUrl: './movie-card-component.css'
})
export class MovieCardComponent implements OnInit {
  @Input() movie!: Movie;
  posterUrl: string = '';

  constructor(private moviesService: MoviesService) {}

  ngOnInit(): void {
    this.generatePosterUrl();
  }

  private generatePosterUrl(): void {
    if (!this.movie.posterUrl || this.movie.posterUrl.length === 0) {
      this.posterUrl = '';
      return;
    }
    
    try {
      // Convertir Byte[] a Uint8Array
      const uint8Array = new Uint8Array(this.movie.posterUrl as any);
      
      // Convertir a base64
      const base64String = btoa(String.fromCharCode(...uint8Array));
      
      // Crear data URL
      this.posterUrl = `data:image/jpeg;base64,${base64String}`;
      
    } catch (error) {
      console.error('Error al convertir poster:', error);
      this.posterUrl = '';
    }
  }

  // Formatear fecha
  formatFecha(fecha: any): string {
    if (!fecha) return 'No disponible';
    
    try {
      const date = new Date(fecha);
      return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    } catch (error) {
      return 'Fecha inválida';
    }
  }

  // Previsualización del reparto (primeros 50 caracteres)
  getRepartoPreview(): string {
    if (!this.movie.reparto) return 'No disponible';
    
    if (this.movie.reparto.length > 50) {
      return this.movie.reparto.substring(0, 50) + '...';
    }
    
    return this.movie.reparto;
  }

  // Previsualización de la sinopsis (primeros 100 caracteres)
  getSinopsisPreview(): string {
    if (!this.movie.sinopsis) return 'No disponible';
    
    if (this.movie.sinopsis.length > 100) {
      return this.movie.sinopsis.substring(0, 100) + '...';
    }
    
    return this.movie.sinopsis;
  }

  // Cambiar estado entre ACTIVA/INACTIVA
  toggleEstado(): void {
    const nuevoEstado = this.movie.estado === 'ACTIVA' ? 'INACTIVA' : 'ACTIVA';
    
    console.log(`Cambiando estado de película ${this.movie.idPelicula} a: ${nuevoEstado}`);
    
    // Actualizar localmente (temporalmente)
    this.movie.estado = nuevoEstado;
    this.moviesService.updateMovieEstado(this.movie.idPelicula, nuevoEstado).subscribe({
      next: () => {
        console.log('Estado actualizado correctamente');
     },
      error: (err: any) => {
         console.error('Error al actualizar estado:', err);
         // Revertir cambio si falla
         this.movie.estado = this.movie.estado === 'ACTIVA' ? 'INACTIVA' : 'ACTIVA';
       }
     });
  }

  // Método para debug
  debugPoster(): void {
    console.log('Movie poster data:', {
      hasPoster: !!this.movie.posterUrl,
      posterLength: this.movie.posterUrl?.length,
      posterUrl: this.posterUrl?.substring(0, 100) + '...'
    });
  }
}