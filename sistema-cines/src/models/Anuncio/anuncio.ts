import { EstadoAnuncio } from "./EstadoAnuncio";

export interface Anuncio {
  idAnuncio?: number;
  idUsuario: number;
  idTipoAnuncio: number;
  idPeriodo: number;
  titulo: string;
  contenidoTexto?: string;
  imagenUrl?: Uint8Array | null;
  videoUrl?: string;
  fechaInicio: string;
  fechaFin: string;
  costoTotal: number;
  estado?: EstadoAnuncio;
}