import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RestConstants } from '../../shared/restapi/rest-constants';

export interface ComentarioSala {
  idComentarioSala?: number;
  idSala: number;
  idUsuario: number;
  comentario: string;
  calificacion: number;
  fechaComentario?: string;
  estado?: 'ACTIVO' | 'OCULTO';
}

@Injectable({
  providedIn: 'root'
})
export class ComentarioSalaService {
  private restConstants = new RestConstants();

  constructor(private http: HttpClient) {}

  crearComentario(comentario: ComentarioSala): Observable<ComentarioSala> {
    return this.http.post<ComentarioSala>(
      `${this.restConstants.getApiURL()}comentarios-sala`, 
      comentario
    );
  }

  getComentariosBySala(idSala: number): Observable<ComentarioSala[]> {
    return this.http.get<ComentarioSala[]>(
      `${this.restConstants.getApiURL()}comentarios-sala/sala/${idSala}`
    );
  }

  getComentarioByUsuarioAndSala(idUsuario: number, idSala: number): Observable<ComentarioSala[]> {
    return this.http.get<ComentarioSala[]>(
      `${this.restConstants.getApiURL()}comentarios-sala/usuario/${idUsuario}/sala/${idSala}`
    );
  }
}