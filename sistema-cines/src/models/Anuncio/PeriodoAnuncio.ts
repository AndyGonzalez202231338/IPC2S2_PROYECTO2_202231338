import { NombrePeriodo } from "./NombrePeriodo";

export interface PeriodoAnuncio {
  idPeriodo?: number;
  nombre: NombrePeriodo;
  diasDuracion: number; 
  precioDuracion: number;
}