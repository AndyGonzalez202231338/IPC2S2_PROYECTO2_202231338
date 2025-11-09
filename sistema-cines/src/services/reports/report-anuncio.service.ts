// report-anuncios.service.ts
import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";

export interface ReporteAnuncios {
    fechaInicio: string;
    fechaFin: string;
    tipoAnuncio?: number;
    periodo?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReportAnunciosService {
    private restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    // Método para reporte de anuncios (descarga directa)
    public generarReporteAnuncios(reporte: ReporteAnuncios): Observable<Blob> {
        let params: any = {
            fechaInicio: reporte.fechaInicio,
            fechaFin: reporte.fechaFin
        };

        if (reporte.tipoAnuncio) {
            params.tipoAnuncio = reporte.tipoAnuncio;
        }

        if (reporte.periodo) {
            params.periodo = reporte.periodo;
        }
        
        return this.httpClient.get(`${this.restConstants.getApiURL()}reports2/anuncios-pdf`, {
            params: params,
            responseType: 'blob'
        });
    }

    // Método para abrir en nueva pestaña
    public descargarReporteAnunciosPDF(fechaInicio: string, fechaFin: string, tipoAnuncio?: number, periodo?: number): void {
        let url = `${this.restConstants.getApiURL()}reports2/anuncios-pdf?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
        
        if (tipoAnuncio) {
            url += `&tipoAnuncio=${tipoAnuncio}`;
        }
        
        if (periodo) {
            url += `&periodo=${periodo}`;
        }
        window.open(url, '_blank');
    }

    // Método para test (solo texto)
    public testReporteAnuncios(reporte: ReporteAnuncios): Observable<string> {
        let params: any = {
            fechaInicio: reporte.fechaInicio,
            fechaFin: reporte.fechaFin
        };

        if (reporte.tipoAnuncio) {
            params.tipoAnuncio = reporte.tipoAnuncio;
        }

        if (reporte.periodo) {
            params.periodo = reporte.periodo;
        }
        
        return this.httpClient.get(`${this.restConstants.getApiURL()}reports2/anuncios`, {
            params: params,
            responseType: 'text'
        });
    }
}
