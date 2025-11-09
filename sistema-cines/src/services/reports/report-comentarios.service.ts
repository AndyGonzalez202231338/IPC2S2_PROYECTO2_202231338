import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { RestConstants } from "../../shared/restapi/rest-constants";

export interface ReporteComentarios {
    fechaInicio: string;
    fechaFin: string;
}

@Injectable({
  providedIn: 'root'
})
export class ReportComentariosService {
    private restConstants = new RestConstants();
    
    constructor(private httpClient: HttpClient) { }
    
    public generarReporteComentarios(reporte: ReporteComentarios): Observable<Blob> {
        const params = {
            fechaInicio: reporte.fechaInicio,
            fechaFin: reporte.fechaFin
        };
        
        return this.httpClient.get(`${this.restConstants.getApiURL()}reports4/comentarios-pdf`, {
            params: params,
            responseType: 'blob'
        });
    }

    public descargarReporteComentariosPDF(fechaInicio: string, fechaFin: string): void {
        const url = `${this.restConstants.getApiURL()}reports4/comentarios-pdf?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
        window.open(url, '_blank');
    }

    public testReporteComentarios(reporte: ReporteComentarios): Observable<string> {
        const params = {
            fechaInicio: reporte.fechaInicio,
            fechaFin: reporte.fechaFin
        };
        
        return this.httpClient.get(`${this.restConstants.getApiURL()}reports4/comentarios`, {
            params: params,
            responseType: 'text'
        });
    }
}