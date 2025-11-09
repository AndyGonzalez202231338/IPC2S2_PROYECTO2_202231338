import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Count } from "../../models/Counts/count";

export interface ReporteAnunciante {
    fechaInicio: string;
    fechaFin: string;
    idUsuario?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReportAnuncianteService {
    private restConstants = new RestConstants();
    
    constructor(private httpClient: HttpClient) { }

    public generarReporteAnunciante(reporte: ReporteAnunciante): Observable<Blob> {
        let params: any = {
            fechaInicio: reporte.fechaInicio,
            fechaFin: reporte.fechaFin
        };

        if (reporte.idUsuario) {
            params.idUsuario = reporte.idUsuario;
        }

        return this.httpClient.get(`${this.restConstants.getApiURL()}reports5/anunciantes-pdf`, {
            params: params,
            responseType: 'blob'
        });
    }

    public descargarReporteAnunciantePDF(fechaInicio: string, fechaFin: string, idAnunciante?: number): void {
        let url = `${this.restConstants.getApiURL()}reports5/anunciantes-pdf?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;

        if (idAnunciante) {
            url += `&idAnunciante=${idAnunciante}`;
        }
        window.open(url, '_blank');
    }

    public testReporteAnunciante(reporte: ReporteAnunciante): Observable<string> {
        let params: any = {
            fechaInicio: reporte.fechaInicio,
            fechaFin: reporte.fechaFin
        };

        if (reporte.idUsuario) {
            params.idUsuario = reporte.idUsuario;
        }
        
        return this.httpClient.get(`${this.restConstants.getApiURL()}reports5/anuncios`, {
            params: params,
            responseType: 'text'
        });
    }

    public obtenerListaAnunciantes(): Observable<Count[]> {
        return this.httpClient.get<Count[]>(`${this.restConstants.getApiURL()}users/anunciantes`);
    }
}



