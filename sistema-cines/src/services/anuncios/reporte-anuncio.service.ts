import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AnuncioReporte {
  idAnuncio: number;
  titulo: string;
  tipoAnuncio: string;
  periodo: string;
  fechaInicio: string;
  fechaFin: string;
  costoTotal: number;
  estado: string;
  usuario: string;
}

export interface ReporteAnunciosResponse {
  anuncios: AnuncioReporte[];
  totalCosto: number;
  totalAnuncios: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReporteAnunciosService {

  private baseUrl = 'http://localhost:8080/rest-api-ipc2/api/reports';

  constructor(private http: HttpClient) { }

  generarReporteAnuncios(
    fechaInicio: string,
    fechaFin: string,
    tipoAnuncio?: number,
    periodo?: number
  ): Observable<any> {
    let params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);

    if (tipoAnuncio) {
      params = params.set('tipoAnuncio', tipoAnuncio.toString());
    }

    if (periodo) {
      params = params.set('periodo', periodo.toString());
    }

    return this.http.get(`${this.baseUrl}/anuncios`, { 
      params, 
      responseType: 'text' 
    });
  }

  descargarReportePDF(
    fechaInicio: string,
    fechaFin: string,
    tipoAnuncio?: number,
    periodo?: number
  ): Observable<Blob> {
    let params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);

    if (tipoAnuncio) {
      params = params.set('tipoAnuncio', tipoAnuncio.toString());
    }

    if (periodo) {
      params = params.set('periodo', periodo.toString());
    }

    return this.http.get(`${this.baseUrl}/anuncios/pdf`, {
      params,
      responseType: 'blob'
    });
  }
}