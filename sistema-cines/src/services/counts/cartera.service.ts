import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface Cartera {
  idCartera: number;
  idUsuario: number;
  saldo: number;
  fechaCreacion?: string;
  fechaActualizacion?: string;
}

export interface Transaccion {
  idTransaccion: number;
  idCartera: number;
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

export interface CarteraResponse {
  idCartera: number;
  idUsuario: number;
  saldo: number;
}


@Injectable({
  providedIn: 'root'
})
export class CarteraService {
  private restConstants = new RestConstants();

  constructor(private http: HttpClient) { }

  // Obtener cartera por ID de usuario
  getCarteraByUsuario(idUsuario: number): Observable<CarteraResponse> {
    return this.http.get<CarteraResponse>(
      `${this.restConstants.getApiURL()}wallet/user/${idUsuario}`
    );
  }

  // Realizar depósito
  realizarDeposito(idUsuario: number, depositoRequest: DepositoRequest): Observable<CarteraResponse> {
    return this.http.post<CarteraResponse>(
      `${this.restConstants.getApiURL()}wallet/user/${idUsuario}/deposit`,
      depositoRequest
    );
  }

  // Realizar compra (para retiros o compras)
  realizarCompra(idUsuario: number, compraRequest: CompraRequest): Observable<CarteraResponse> {
    return this.http.post<CarteraResponse>(
      `${this.restConstants.getApiURL()}wallet/user/${idUsuario}/purchase`,
      compraRequest
    );
  }

  // Método para obtener transacciones (si tienes este endpoint)
  getTransaccionesByUsuario(idUsuario: number): Observable<Transaccion[]> {
    // Si no tienes este endpoint aún, puedes simularlo o omitirlo
    return this.http.get<Transaccion[]>(
      `${this.restConstants.getApiURL()}wallet/user/${idUsuario}/transactions`
    );
  }
}