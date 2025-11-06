// services/ticket/ticket-info.service.ts
import { Injectable } from '@angular/core';
import { Observable, forkJoin, map, switchMap } from 'rxjs';
import { BoletoService } from './boleto.service';
import { Funcion, FunctionsService } from '../function/function.service';
import { TicketInfo } from '../../models/ticket/TicketInfo';
import { SalasService } from '../salas/salas.service';
import { MoviesService } from '../movies/movies.service';


export interface Boleto {
        idBoleto?: number;
        idFuncion: number;
        idUsuario: number;
        codigoBoleto: string;
        fechaCompra: string;
        precioPagado: number;
    }

@Injectable({
  providedIn: 'root'
})
export class TicketInfoService {
  constructor(
    private boletoService: BoletoService,
    private funcionService: FunctionsService,
    private salaService: SalasService,
    private movieService: MoviesService
  ) {}

  getTicketInfoByUserId(idUsuario: number): Observable<TicketInfo[]> {
    return this.boletoService.getUniqueTicketsByUserId(idUsuario).pipe(
      switchMap(boletos => {
        const ticketInfoRequests = boletos.map(boleto => 
          this.getTicketInfoForBoleto(boleto)
        );
        return forkJoin(ticketInfoRequests);
      })
    );
  }

  private getTicketInfoForBoleto(boleto: Boleto): Observable<TicketInfo> {
    return this.funcionService.getFunctionById(boleto.idFuncion).pipe(
      switchMap(funcion => {
        const salaRequest = this.salaService.getSalaById(funcion.idSala);
        const peliculaRequest = this.movieService.getMovieById(funcion.idPelicula);
        
        return forkJoin([salaRequest, peliculaRequest]).pipe(
          map(([sala, pelicula]) => ({
            boleto: boleto,
            funcion: funcion,
            sala: sala,
            pelicula: pelicula,
            puedeCalificarSala: sala.permiteComentario === 'SI',
            puedeCalificarPelicula: true // Siempre se puede calificar películas
          }))
        );
      })
    );
  }
}