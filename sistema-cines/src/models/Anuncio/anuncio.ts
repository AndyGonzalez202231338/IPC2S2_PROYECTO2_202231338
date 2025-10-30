import { EstadoAnuncio } from "./EstadoAnuncio";

export interface Anuncio {
  idAnuncio?: number;
  idUsuario: number;
  idTipoAnuncio: number;
  idPeriodo: number;
  titulo: string;
  contenidoTexto?: string;
  imagenUrl?: string;  //Solo string para datos recibidos
  videoUrl?: string;
  fechaInicio: string;
  fechaFin: string;
  costoTotal: number;
  estado?: EstadoAnuncio;
}