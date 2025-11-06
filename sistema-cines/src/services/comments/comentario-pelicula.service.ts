// services/comentarios/comentario-pelicula.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RestConstants } from '../../shared/restapi/rest-constants';

export interface ComentarioPelicula {
  idComentarioPelicula?: number;
  idPelicula: number;
  idUsuario: number;
  comentario: string;
  calificacion: number;
  fechaComentario?: string;
  estado?: 'ACTIVO' | 'OCULTO';
}

@Injectable({
  providedIn: 'root'
})
export class ComentarioPeliculaService {
  private restConstants = new RestConstants();

  constructor(private http: HttpClient) {}

  crearComentario(comentario: ComentarioPelicula): Observable<ComentarioPelicula> {
    return this.http.post<ComentarioPelicula>(
      `${this.restConstants.getApiURL()}comentarios-pelicula`, 
      comentario
    );
  }

  getComentariosByPelicula(idPelicula: number): Observable<ComentarioPelicula[]> {
    return this.http.get<ComentarioPelicula[]>(
      `${this.restConstants.getApiURL()}comentarios-pelicula/pelicula/${idPelicula}`
    );
  }

  getComentarioByUsuarioAndPelicula(idUsuario: number, idPelicula: number): Observable<ComentarioPelicula[]> {
    return this.http.get<ComentarioPelicula[]>(
      `${this.restConstants.getApiURL()}comentarios-pelicula/usuario/${idUsuario}/pelicula/${idPelicula}`
    );
  }
}