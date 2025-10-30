import { Anuncio } from "./anuncio";
import { EstadoAnuncio } from "./EstadoAnuncio";
import { PeriodoAnuncio } from "./PeriodoAnuncio";
import { TipoAnuncio } from "./TipoAnuncio";

export interface AnuncioCompleto {
  idAnuncio: number;
  idUsuario: number;
  titulo: string;
  contenidoTexto: string;
  imagenUrl: String; // o string si conviertes a base64
  videoUrl: string;
  fechaInicio: string;
  fechaFin: string;
  costoTotal: number;
  estado: string;
  tipoAnuncio: TipoAnuncio;
  periodoAnuncio: PeriodoAnuncio;
}
