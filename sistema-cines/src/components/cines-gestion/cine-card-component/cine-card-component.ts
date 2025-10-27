import { Component, Input } from '@angular/core';
import { Cine } from '../../../models/Cines/cine';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Count } from '../../../models/Counts/count';

@Component({
  selector: 'app-cine-card-component',
  imports: [DatePipe, CurrencyPipe, RouterLink],
  templateUrl: './cine-card-component.html',
  styleUrl: './cine-card-component.css'
})
export class CineCardComponent {
  @Input({ required: true })
  selectedCine!: Cine;

  @Input()
  administradoresData: Count[] = [];
  
  // Método para obtener los datos del administrador por ID
  getAdministradorData(idUsuario: number): Count | undefined {
    return this.administradoresData.find(admin => admin.idUsuario === idUsuario);
  }

  // Método para formatear la fecha que puede ser string, Date o number[]
  formatFecha(fecha: string | Date | undefined): string {
    if (!fecha) return 'Fecha no disponible';
    
    try {
      let fechaObj: Date;

      if (Array.isArray(fecha)) {
        // Si es un array [2025,10,26,17,58,49]
        const [year, month, day, hour, minute, second] = fecha;
        fechaObj = new Date(year, month - 1, day, hour, minute, second);
      } else if (typeof fecha === 'string') {
        // Si es un string ISO
        fechaObj = new Date(fecha);
      } else {
        // Si ya es un objeto Date
        fechaObj = fecha;
      }

      // Verificar si la fecha es válida
      if (isNaN(fechaObj.getTime())) {
        return 'Fecha inválida';
      }

      return fechaObj.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (error) {
      console.error('Error formateando fecha:', error, fecha);
      return 'Fecha inválida';
    }
  }

  // Método auxiliar para verificar si es array (para debugging)
  getTipoFecha(): string {
    const fecha = this.selectedCine.fechaCreacion;
    if (Array.isArray(fecha)) return 'array';
    if (typeof fecha === 'string') return 'string';
    if (fecha instanceof Date) return 'Date';
    return 'undefined';
  }
}