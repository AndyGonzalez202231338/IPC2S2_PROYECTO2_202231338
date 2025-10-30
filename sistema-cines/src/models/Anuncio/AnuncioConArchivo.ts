import { Anuncio } from "./anuncio";

// Interface separada para cuando necesitas enviar un archivo
export interface AnuncioConArchivo extends Omit<Anuncio, 'imagen_url'> {
  imagen_file?: File;  //Propiedad separada para el archivo
}