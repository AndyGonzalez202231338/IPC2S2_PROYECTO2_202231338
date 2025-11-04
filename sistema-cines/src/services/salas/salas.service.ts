import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";

export interface Sala {
    idSala: number;
    idCine: number;
    nombreSala: string;
    filas: number;
    columnas: number;
    permiteComentario: String;
    estado: String;
}

// Interface para el request de actualización
export interface UpdateSalaRequest {
    permiteComentario: String;
    estado: String;
}

@Injectable({
  providedIn: 'root'
})
export class SalasService {
    estConstants = new RestConstants();

    constructor(private HttpClient: HttpClient) {}

    createNewSala(sala: Sala) {
        return this.HttpClient.post<Sala>(`${this.estConstants.getApiURL()}salas`, sala);
    }

    getSalasByCine(idCine: number) {
        return this.HttpClient.get<Sala[]>(`${this.estConstants.getApiURL()}salas/cine/${idCine}`);
    } 

    updateSala(idSala: number, updateData: UpdateSalaRequest) {
        return this.HttpClient.put<Sala>(`${this.estConstants.getApiURL()}salas/${idSala}`, updateData);
    }

    deleteSala(idSala: number) {
        return this.HttpClient.delete<void>(`${this.estConstants.getApiURL()}salas/${idSala}`);
    }

    getSalaById(idSala: number) {
        return this.HttpClient.get<Sala>(`${this.estConstants.getApiURL()}salas/${idSala}`);
    }

    getAllSalas() {
        return this.HttpClient.get<Sala[]>(`${this.estConstants.getApiURL()}salas`);
    }   
}

