import { Funcion, Sala } from "../../services/function/function.service";
import { Movie } from "../Movies/Movie";
import { Boleto } from "../../services/ticket/boleto.service";

export interface TicketInfo {
  boleto: Boleto;
  funcion: Funcion;
  sala: Sala;
  pelicula: Movie;
  puedeCalificarSala: boolean;
  puedeCalificarPelicula: boolean;
}