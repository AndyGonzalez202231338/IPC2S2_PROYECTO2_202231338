import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

// Components
import { Header } from '../../header/header';
import { Footer } from '../../footer/footer';
import { HeaderAdminSistema } from '../../header-admin-sistema/header-admin-sistema';
import { HeaderAdminCine } from "../../header-admin-cine/header-admin-cine";
import { HeaderAnunciante } from '../../header-anunciante/header-anunciante';

// Services & Models
import { HomesService, User } from '../../../services/homes/homes.services';
import { CineAdministradorService } from '../../../services/cines/cine-administrador.service';
import { AnunciosService } from '../../../services/anuncios/anuncios.service';
import { Cine } from '../../../models/Cines/cine';
import { Anuncio, Aside } from '../../aside/aside';
import { AnuncioCompleto } from '../../../models/Anuncio/AnuncioCompleto';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    Header, 
    Footer, 
    HeaderAdminSistema, 
    HeaderAdminCine, 
    HeaderAnunciante,
    Aside
  ],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class Home implements OnInit, OnDestroy {
  protected normalTitle = 'Home';
  currentUser: User | null = null;
  cines: Cine[] = [];
  cineSeleccionado: Cine | null = null;
  mostrarSelectorCine: boolean = false;
  
  // Variables para anuncios
  anunciosConPublicidad: AnuncioCompleto[] = [];
  anunciosIzquierda: Anuncio[] = [];
  anunciosDerecha: Anuncio[] = [];
  cargandoAnuncios: boolean = true;

  constructor(
    private homesService: HomesService, 
    private cineAdminService: CineAdministradorService,
    private anunciosService: AnunciosService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Obtener el usuario actual del servicio
    this.currentUser = this.homesService.getCurrentUser();

    // Verificar si ya tiene un cine seleccionado
    const cineGuardado = this.homesService.getCineSeleccionado();
    if (cineGuardado) {
      this.cineSeleccionado = cineGuardado;
    }

    // Si es administrador de cine y no tiene cine seleccionado, cargar sus cines
    if (this.isAdminCine() && !this.cineSeleccionado) {
      this.cargarCinesAdministrador();
    }
    console.log('cargando los anuncios')
    // Cargar anuncios con publicidad activa
    this.cargarAnunciosConPublicidad();
  }

  ngOnDestroy(): void {
    // No necesitamos limpiar URLs ya que usamos Base64
  }

  private cargarCinesAdministrador(): void {
    if (this.currentUser) {
      this.cineAdminService.getCinesByAdministrador(this.currentUser.idUsuario).subscribe({
        next: (cines) => {
          this.cines = cines;
          
          // Si solo tiene un cine, seleccionarlo automáticamente
          if (this.cines.length === 1) {
            this.seleccionarCine(this.cines[0]);
          } else if (this.cines.length > 1) {
            this.mostrarSelectorCine = true;
          }
        },
        error: (err) => {
          console.error('Error al cargar cines:', err);
        }
      });
    }
  }

  private cargarAnunciosConPublicidad(): void {
    console.log('Cargando anuncios con publicidad activa...');
    this.cargandoAnuncios = true;
    
    this.anunciosService.getAnunciosConPublicidad().subscribe({
      next: (anunciosCompletos: AnuncioCompleto[]) => {
        this.anunciosConPublicidad = anunciosCompletos;
        console.log('Anuncios con publicidad cargados:', this.anunciosConPublicidad);
        this.procesarAnunciosParaAside();
        this.cargandoAnuncios = false;
      },
      error: (err) => {
        console.error('Error al cargar anuncios con publicidad:', err);
        this.cargandoAnuncios = false;
      }
    });
  }

  private procesarAnunciosParaAside(): void {
  
  // Convertir AnuncioCompleto a Anuncio para el aside
  const anunciosConvertidos: Anuncio[] = this.anunciosConPublicidad.map((anuncioCompleto, index) => {

    // Convertir el blob de imagen a Base64 URL si existe
    let imagenUrl: string | null = null;
    
    if (anuncioCompleto.imagenUrl && Array.isArray(anuncioCompleto.imagenUrl)) {
      try {
        
        
        imagenUrl = this.convertirArrayNumerosABase64(anuncioCompleto.imagenUrl);
        
      } catch (error) {
        
        imagenUrl = null;
      }
    } else {
    }
    
    return {
      id_anuncio: anuncioCompleto.idAnuncio,
      titulo: anuncioCompleto.titulo,
      contenido_texto: anuncioCompleto.contenidoTexto,
      imagen_url: imagenUrl,
      video_url: anuncioCompleto.videoUrl
    };
  });

  // Ambos aside reciben la misma lista completa de anuncios
  this.anunciosIzquierda = [...anunciosConvertidos];
  this.anunciosDerecha = [...anunciosConvertidos];
  
  
  // Debug adicional: contar cuántos tienen imagen
  const conImagen = anunciosConvertidos.filter(a => a.imagen_url).length;
}


  private convertirArrayNumerosABase64(arrayNumeros: number[]): string {
  if (!arrayNumeros || arrayNumeros.length === 0) {
    return '';
  }
  
  try {


    
    const uint8Array = new Uint8Array(arrayNumeros.length);
    
    for (let i = 0; i < arrayNumeros.length; i++) {
      // Convertir de signed byte (-128 a 127) a unsigned byte (0 a 255)
      uint8Array[i] = arrayNumeros[i] & 0xFF;
    }

    // Convertir a base64
    let binary = '';
    const len = uint8Array.byteLength;
    
    for (let i = 0; i < len; i++) {
      binary += String.fromCharCode(uint8Array[i]);
    }
    
    const base64String = btoa(binary);
    
    // Determinar el tipo MIME
    const mimeType = this.determinarTipoMIME(uint8Array);
    
    
    
    // Crear data URL
    const dataUrl = `data:${mimeType};base64,${base64String}`;
    
    return dataUrl;
    
  } catch (error) {
    return '';
  }
}


  // Método auxiliar para determinar el tipo MIME del blob
  private determinarTipoMIME(imageData: Uint8Array): string {
  // Verificar si es JPEG (empieza con FF D8 FF)
  if (imageData[0] === 0xFF && imageData[1] === 0xD8 && imageData[2] === 0xFF) {
    return 'image/jpeg';
  }
  
  // Verificar si es PNG (empieza con 89 50 4E 47)
  if (imageData[0] === 0x89 && imageData[1] === 0x50 && imageData[2] === 0x4E && imageData[3] === 0x47) {
    return 'image/png';
  }
  
  // Verificar si es GIF (empieza con 47 49 46 38)
  if (imageData[0] === 0x47 && imageData[1] === 0x49 && imageData[2] === 0x46 && imageData[3] === 0x38) {
    return 'image/gif';
  }
  
  // Por defecto, asumir JPEG
  return 'image/jpeg';
}

  seleccionarCine(cine: Cine): void {
    this.cineSeleccionado = cine;
    this.homesService.setCineSeleccionado(cine);
    this.mostrarSelectorCine = false;
  }

  cambiarCine(): void {
    this.mostrarSelectorCine = true;
  }

  isAdminCine(): boolean {
    return this.currentUser?.rol.nombreRol === 'ADMINISTRADOR DE CINE';
  }

  irADashboardCine(): void {
    if (this.cineSeleccionado) {
      this.router.navigate(['/cine/dashboard', this.cineSeleccionado.idCine]);
    }
  }
}