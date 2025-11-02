import { HttpClient } from "@angular/common/http";
import { Cine } from "../../models/Cines/cine";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class CineAdministradorService {
    private restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    //enviar el id de un usuario administrador de cine para verificar a cuantos cines esta asignado
    public getCinesByAdministrador(idUsuario: number): Observable<Cine[]> {
        return this.httpClient.get<Cine[]>(`${this.restConstants.getApiURL()}cine-admin/${idUsuario}/cines`);
    }
}