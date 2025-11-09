import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Sala } from "../function/function.service";
import { Observable } from "rxjs";

export interface ReporteSalasPopulares{
    fechaInicio: string;
    fechaFin: string;
    idSala?: number;
    idCine?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReporteSalasPopularesService{ 
  private restConstants = new RestConstants();

  constructor(private httpClient: HttpClient) { }

    public descargarReporteSalasPopularesPDF(
    fechaInicio: string, 
    fechaFin: string, 
    idSala?: number, 
    idCine?: number
  ): void {
    let url = `${this.restConstants.getApiURL()}reports8/salas-gustadas-pdf?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;

    if (idSala) {
      url += `&idSala=${idSala}`;
    } else if (idCine) {
      url += `&idCine=${idCine}`;
    }

    console.log('URL de descarga directa:', url);
    window.open(url, '_blank');
  }

   public testReporteComentarios(reporte: ReporteSalasPopulares): Observable<string> {
    let params = new HttpParams()
      .set('fechaInicio', reporte.fechaInicio)
      .set('fechaFin', reporte.fechaFin);

    if (reporte.idSala) {
      params = params.set('idSala', reporte.idSala.toString());
    } else if (reporte.idCine) {
      params = params.set('idCine', reporte.idCine.toString());
    }

    return this.httpClient.get(`${this.restConstants.getApiURL()}reports8/salas-gustadas-pdf?`, {
      params: params,
      responseType: 'text'
    });
  }

  public obtenerListaSalasCine(): Observable<Sala[]> {
    return this.httpClient.get<Sala[]>(`${this.restConstants.getApiURL()}salas`);
  }
}