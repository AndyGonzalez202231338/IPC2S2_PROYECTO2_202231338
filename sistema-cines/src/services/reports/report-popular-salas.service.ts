// report-popular-salas.service.ts
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { RestConstants } from "../../shared/restapi/rest-constants";

export interface ReporteSalasPopulares {
    fechaInicio: string;
    fechaFin: string;
}

@Injectable({
  providedIn: 'root'
})
export class ReportPopularSalasService {
    private restConstants = new RestConstants();
    
    constructor(private httpClient: HttpClient) { }
    
    public generarReporteSalasPopulares(reporte: ReporteSalasPopulares): Observable<Blob> {
        const params = {
            fechaInicio: reporte.fechaInicio,
            fechaFin: reporte.fechaFin
        };
        
        return this.httpClient.get(`${this.restConstants.getApiURL()}reports3/salas-populares-pdf`, {
            params: params,
            responseType: 'blob'
        });
    }

    public descargarReportSalasPopularPDF(fechaInicio: string, fechaFin: string): void {
        const url = `${this.restConstants.getApiURL()}reports3/salas-populares-pdf?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
        window.open(url, '_blank');
    }
}