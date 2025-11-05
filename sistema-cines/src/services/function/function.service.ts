import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RestConstants } from '../../shared/restapi/rest-constants';

export interface Sala {
    idSala: number;
    idCine: number;
    nombreSala: string;
    filas: number;
    columnas: number;
    permiteComentario: String;
    estado: String;
}

export interface Funcion {
  idFuncion?: number;
  idSala: number;
  idPelicula: number;
  fechaHoraFuncion: string;
  precioBoletoAdulto: number;
  precioBoletoNino: number;
  asientosDisponibles: number;
  estado?: 'PROGRAMADA' | 'CANCELADA' | 'COMPLETADA';
}

@Injectable({
  providedIn: 'root'
})
export class FunctionsService {
  estConstants = new RestConstants();

  constructor(private HttpClient: HttpClient) {}

  //crea una sala en la base de datos
  createFunction(funcion: Funcion): Observable<Funcion> {
    console.log('Creando función con datos:', funcion);
    return this.HttpClient.post<Funcion>(`${this.estConstants.getApiURL()}funciones`, funcion);
  }

  //trae todas las funciones de una sala
  getFunctionsBySala(idSala: number): Observable<Funcion[]> {
    return this.HttpClient.get<Funcion[]>(`${this.estConstants.getApiURL()}funciones/sala/${idSala}`);
  }

  //busca una funcion por su id
  getFunctionById(idFuncion: number): Observable<Funcion> {
    return this.HttpClient.get<Funcion>(`${this.estConstants.getApiURL()}funciones/${idFuncion}`);
  }

  //trae todas las funciones
  getAllFunctions(): Observable<Funcion[]> {
    return this.HttpClient.get<Funcion[]>(`${this.estConstants.getApiURL()}funciones`);
  }

  //traer todas las funciones de un cine, la tabla sala contiene id_Cine par realizar un Join
  getFunctionsByCine(idCine: number): Observable<Funcion[]> {
    return this.HttpClient.get<Funcion[]>(`${this.estConstants.getApiURL()}funciones/cine/${idCine}`);
  }
  
}