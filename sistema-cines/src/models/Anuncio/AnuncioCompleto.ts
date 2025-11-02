import { Anuncio } from "./anuncio";
import { EstadoAnuncio } from "./EstadoAnuncio";
import { PeriodoAnuncio } from "./PeriodoAnuncio";
import { TipoAnuncio } from "./TipoAnuncio";

export interface AnuncioCompleto {
  idAnuncio: number;
  idUsuario: number;
  titulo: string;
  contenidoTexto: string;
  imagenUrl: String;
  videoUrl: string;
  fechaInicio: string;
  fechaFin: string;
  costoTotal: number;
  estado: string;
  tipoAnuncio: TipoAnuncio;
  periodoAnuncio: PeriodoAnuncio;
  idTipoAnuncio?: number;
  idPeriodo?: number;
  
  // Mantener como objeto literal pero verificar nombres
  publicidad?: {
    idPublicidad: number;
    idAnuncio: number;
    idUsuario: number;
    precioBloqueo: number;
    estado: string; // Cambiar a string
  };
}