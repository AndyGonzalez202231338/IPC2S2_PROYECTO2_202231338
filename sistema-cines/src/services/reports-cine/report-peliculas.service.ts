import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Sala } from "../salas/salas.service";

export interface ReportePeliculasSalas{
    fechaInicio: string;
    fechaFin: string;
    idSala?: number;
    idCine?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReportPeliculasSalasService {
  private restConstants = new RestConstants();

  constructor(private httpClient: HttpClient) { }

    public descargarReportePeliculasSalasPDF(
    fechaInicio: string, 
    fechaFin: string, 
    idSala?: number, 
    idCine?: number
  ): void {
    let url = `${this.restConstants.getApiURL()}reports7/peliculas-proyectadas-pdf?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;

    if (idSala) {
      url += `&idSala=${idSala}`;
    } else if (idCine) {
      url += `&idCine=${idCine}`;
    }

    console.log('URL de descarga directa:', url);
    window.open(url, '_blank');
  }

    public testReporteComentarios(reporte: ReportePeliculasSalas): Observable<string> {
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