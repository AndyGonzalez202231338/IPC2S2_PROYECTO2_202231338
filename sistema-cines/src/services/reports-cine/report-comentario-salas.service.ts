import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Sala } from "../salas/salas.service";

export interface ReporteComentarioSalas {
    fechaInicio: string;
    fechaFin: string;
    idSala?: number;
    idCine?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReportComentarioSalasService {
  private restConstants = new RestConstants();

  constructor(private httpClient: HttpClient) { }

  public generarReporteComentariosSala(reporte: ReporteComentarioSalas): Observable<Blob> {
    let params = new HttpParams()
      .set('fechaInicio', reporte.fechaInicio)
      .set('fechaFin', reporte.fechaFin);

    if (reporte.idSala) {
      params = params.set('idSala', reporte.idSala.toString());
    } else if (reporte.idCine) {
      params = params.set('idCine', reporte.idCine.toString());
    }

    console.log('Enviando parámetros al backend:', params.toString());

    // CORREGIDO: Cambiar de reports5 a reports6
    return this.httpClient.get(`${this.restConstants.getApiURL()}reports6/comentarios-salas-pdf`, {
      params: params,
      responseType: 'blob'
    });
  }

  public descargarReporteComentarioSalasPDF(
    fechaInicio: string, 
    fechaFin: string, 
    idSala?: number, 
    idCine?: number
  ): void {
    let url = `${this.restConstants.getApiURL()}reports6/comentarios-salas-pdf?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;

    if (idSala) {
      url += `&idSala=${idSala}`;
    } else if (idCine) {
      url += `&idCine=${idCine}`;
    }

    console.log('URL de descarga directa:', url);
    window.open(url, '_blank');
  }

  public testReporteComentarios(reporte: ReporteComentarioSalas): Observable<string> {
    let params = new HttpParams()
      .set('fechaInicio', reporte.fechaInicio)
      .set('fechaFin', reporte.fechaFin);

    if (reporte.idSala) {
      params = params.set('idSala', reporte.idSala.toString());
    } else if (reporte.idCine) {
      params = params.set('idCine', reporte.idCine.toString());
    }

    return this.httpClient.get(`${this.restConstants.getApiURL()}reports6/comentarios-salas`, {
      params: params,
      responseType: 'text'
    });
  }

  public obtenerListaSalasCine(): Observable<Sala[]> {
    return this.httpClient.get<Sala[]>(`${this.restConstants.getApiURL()}salas`);
  }
}