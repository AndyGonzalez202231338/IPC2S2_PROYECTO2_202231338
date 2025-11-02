import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface CarteraCine {
  idCarteraCine: number;
  idCine: number;
  saldo: number;
  fechaCreacion?: string;
  fechaActualizacion?: string;
}

export interface Transaccion {
  idTransaccion: number;
  idCarteraCine: number;
  tipo: 'DEPOSITO' | 'RETIRO' | 'COMPRA';
  monto: number;
  descripcion: string;
  fechaTransaccion: string;
}

export interface DepositoRequest {
  monto: number;
}

export interface CompraRequest {
  monto: number;
}

export interface CarteraCineResponse {
  idCarteraCine: number;
  idCine: number;
  saldo: number;
}


@Injectable({
  providedIn: 'root'
})
export class CarteraCineService {
  private restConstants = new RestConstants();

  constructor(private http: HttpClient) { }

  // Obtener cartera por ID de usuario
  getCarteraByCine(idCine: number): Observable<CarteraCineResponse> {
    return this.http.get<CarteraCineResponse>(
      `${this.restConstants.getApiURL()}wallet-cine/cine/${idCine}`
    );
  }

  // Realizar depósito
  realizarDeposito(idCine: number, depositoRequest: DepositoRequest): Observable<CarteraCineResponse> {
    return this.http.post<CarteraCineResponse>(
      `${this.restConstants.getApiURL()}wallet-cine/cine/${idCine}/deposit`,
      depositoRequest
    );
  }

  // Realizar compra (para retiros o compras)
  realizarCompra(idCine: number, compraRequest: CompraRequest): Observable<CarteraCineResponse> {
    return this.http.post<CarteraCineResponse>(
      `${this.restConstants.getApiURL()}wallet-cine/cine/${idCine}/purchase`,
      compraRequest
    );
  }

  // Método para obtener transacciones (si tienes este endpoint)
  getTransaccionesByCine(idCine: number): Observable<Transaccion[]> {
    // Si no tienes este endpoint aún, puedes simularlo o omitirlo
    return this.http.get<Transaccion[]>(
      `${this.restConstants.getApiURL()}wallet-cine/cine/${idCine}/transactions`
    );
  }
}