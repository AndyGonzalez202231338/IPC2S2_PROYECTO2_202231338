export interface Movie {
  idPelicula: number;
  titulo: string;
  sinopsis: string;
  duracionMinutos: number;
  director: string;
  reparto: string;
  clasificacion: string;
  fechaEstreno: Date;
  posterUrl: Uint8Array | null;
  estado: string;
}
