import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Cine } from "../../models/Cines/cine";
import { Count } from "../../models/Counts/count";
import { CineAdministrador } from "../../models/Cines/cine-administrador";

@Injectable({
  providedIn: 'root'
})
export class CinesService {
    private restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    // Operaciones básicas de Cine
    public createNewCine(cine: Cine): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}cines`, cine);
    }

    public getAllCines(): Observable<Cine[]> {
        return this.httpClient.get<Cine[]>(`${this.restConstants.getApiURL()}cines`);
    }

    public getCineById(id: number): Observable<Cine> {
        return this.httpClient.get<Cine>(`${this.restConstants.getApiURL()}cines/${id}`);
    }

    public updateCine(id: number, cineToUpdate: Cine): Observable<Cine> {
        return this.httpClient.put<Cine>(`${this.restConstants.getApiURL()}cines/${id}`, cineToUpdate);
    }

    public deleteCine(id: number): Observable<void> {
        return this.httpClient.delete<void>(`${this.restConstants.getApiURL()}cines/${id}`);
    }

    // Nuevos métodos para administradores de cine
    public getAdministradoresByCine(idCine: number): Observable<CineAdministrador[]> {
        return this.httpClient.get<CineAdministrador[]>(`${this.restConstants.getApiURL()}cines/${idCine}/administradores`);
    }

    public addAdministradorToCine(idCine: number, idUsuario: number): Observable<CineAdministrador> {
        return this.httpClient.post<CineAdministrador>(`${this.restConstants.getApiURL()}cines/${idCine}/administradores`, {
            id_usuario: idUsuario
        });
    }

    public removeAdministradorFromCine(idCine: number, idUsuario: number): Observable<void> {
        return this.httpClient.delete<void>(`${this.restConstants.getApiURL()}cines/${idCine}/administradores/${idUsuario}`);
    }

    public getUsuariosAdministradores(): Observable<Count[]> {
        return this.httpClient.get<Count[]>(`${this.restConstants.getApiURL()}users/role/ADMIN_CINE`);
    }
}