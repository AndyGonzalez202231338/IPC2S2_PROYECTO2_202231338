// report-sistema.service.ts
import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";

export interface ReporteSistema {
    fechaInicio: string;
    fechaFin: string;
}

@Injectable({
  providedIn: 'root'
})
export class ReportSistemaService {
    private restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    // Método para reporte de ganancias
    public generarReporteSistema(reporte: ReporteSistema): Observable<Blob> {
        const params = {
            fechaInicio: reporte.fechaInicio,
            fechaFin: reporte.fechaFin
        };
        
        return this.httpClient.get(`${this.restConstants.getApiURL()}reports/ganancias-pdf`, {
            params: params,
            responseType: 'blob'
        });
    }

    // Método para abrir en nueva pestaña
    public descargarReporteGananciasPDF(fechaInicio: string, fechaFin: string): void {
        const url = `${this.restConstants.getApiURL()}reports/ganancias-pdf?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
        window.open(url, '_blank');
    }
}