import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, OnDestroy, SimpleChanges, ViewChild, ElementRef } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

export interface Anuncio {
  id_anuncio: number;
  titulo: string;
  contenido_texto?: string;
  imagen_url?: string | null; // Ahora siempre es Base64 string o null
  video_url?: string;
}

@Component({
  selector: 'app-aside',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './aside.html',
  styleUrls: ['./aside.css']
})
export class Aside implements OnChanges, OnDestroy {
  @Input() anuncios: Anuncio[] = [];
  @Input() lado: 'izquierda' | 'derecha' = 'izquierda';
  
  // Referencias a elementos de video
  @ViewChild('videoElement') videoElement!: ElementRef<HTMLVideoElement>;
  
  anuncioActual: Anuncio | null = null;
  private intervalo: any;
  private indiceActual: number = 0;

  constructor(private sanitizer: DomSanitizer) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['anuncios']) {
      this.iniciarRotacion();
    }
  }

  ngOnDestroy(): void {
    this.detenerRotacion();
    // No necesitamos limpiar URLs ya que usamos Base64
  }

  // MÉTODO: Verificar si tiene imagen válida
  tieneImagenValida(anuncio: Anuncio): boolean {
    return !!(anuncio.imagen_url && anuncio.imagen_url.trim() !== '');
  }

  // MÉTODO: Verificar si tiene video válido
  tieneVideoValido(anuncio: Anuncio): boolean {
    return !!(anuncio.video_url && anuncio.video_url.trim() !== '');
  }

  // MÉTODO: Obtener URL de imagen (ahora siempre es Base64 string)
  getImagenUrl(anuncio: Anuncio): string | null {
    return anuncio.imagen_url || null;
  }

  // Resto de métodos permanecen igual...
  onVideoLoad(): void {
    if (this.videoElement?.nativeElement) {
      this.videoElement.nativeElement.muted = true;
      this.videoElement.nativeElement.volume = 0;
    }
  }

  getYouTubeEmbedUrl(url: string): SafeResourceUrl {
    if (!url) return this.sanitizer.bypassSecurityTrustResourceUrl('');
    
    let videoId = '';
    
    try {
      const urlObj = new URL(url);
      
      if (url.includes('youtube.com/watch')) {
        videoId = urlObj.searchParams.get('v') || '';
      } else if (url.includes('youtu.be/')) {
        videoId = urlObj.pathname.split('/')[1] || '';
      } else if (url.includes('youtube.com/embed/')) {
        const existingUrl = new URL(url);
        existingUrl.searchParams.set('mute', '1');
        return this.sanitizer.bypassSecurityTrustResourceUrl(existingUrl.toString());
      }
    } catch (error) {
      console.error('Error procesando URL de YouTube:', error);
    }
    
    if (videoId) {
      const embedUrl = `https://www.youtube.com/embed/${videoId}?rel=0&modestbranding=1&mute=1&controls=0`;
      return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
    }
    
    return this.sanitizer.bypassSecurityTrustResourceUrl('');
  }

  getVimeoEmbedUrl(url: string): SafeResourceUrl {
    if (!url) return this.sanitizer.bypassSecurityTrustResourceUrl('');
    
    let videoId = '';
    
    if (url.includes('vimeo.com/')) {
      const match = url.match(/vimeo\.com\/(\d+)/);
      videoId = match ? match[1] : '';
    }
    
    if (videoId) {
      const embedUrl = `https://player.vimeo.com/video/${videoId}?background=1&muted=1&autoplay=1&loop=1`;
      return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
    }
    
    return this.sanitizer.bypassSecurityTrustResourceUrl('');
  }

  getTipoContenido(url: string | undefined): 'video-directo' | 'youtube' | 'tiktok' | 'vimeo' | 'enlace' | 'ninguno' {
    if (!url || url.trim() === '') return 'ninguno';
    
    const urlLower = url.toLowerCase();
    
    if (urlLower.includes('youtube.com/watch') || 
        urlLower.includes('youtu.be/') || 
        urlLower.includes('youtube.com/embed')) {
      return 'youtube';
    }
    
    if (urlLower.includes('tiktok.com') || urlLower.includes('vm.tiktok.com')) {
      return 'tiktok';
    }
    
    if (urlLower.includes('vimeo.com')) {
      return 'vimeo';
    }
    
    if (urlLower.match(/\.(mp4|webm|ogg|mov|avi|m3u8)$/i)) {
      return 'video-directo';
    }
    
    return 'enlace';
  }

  getTikTokEmbedUrl(url: string): SafeResourceUrl {
    if (!url) return this.sanitizer.bypassSecurityTrustResourceUrl('');
    
    if (url.includes('tiktok.com/') && !url.includes('vm.tiktok.com')) {
      const embedUrl = `https://www.tiktok.com/embed/v2/?url=${encodeURIComponent(url)}`;
      return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
    }
    
    return this.sanitizer.bypassSecurityTrustResourceUrl('');
  }

  getYouTubeThumbnail(url: string): string {
    const videoId = this.extractYouTubeId(url);
    if (videoId) {
      return `https://img.youtube.com/vi/${videoId}/hqdefault.jpg`;
    }
    return '';
  }

  private extractYouTubeId(url: string): string {
    if (!url) return '';
    
    const regex = /(?:youtube\.com\/(?:[^\/]+\/.+\/|(?:v|e(?:mbed)?)\/|.*[?&]v=)|youtu\.be\/)([^"&?\/\s]{11})/;
    const match = url.match(regex);
    return match ? match[1] : '';
  }

  tieneContenidoMultimedia(anuncio: Anuncio): boolean {
    return this.tieneImagenValida(anuncio) || this.tieneVideoValido(anuncio);
  }

  private iniciarRotacion(): void {
    this.detenerRotacion();
    
    if (this.anuncios.length > 0) {
      this.seleccionarAnuncioAleatorio();
      
      this.intervalo = setInterval(() => {
        this.seleccionarAnuncioAleatorio();
      }, 25000);
    } else {
      this.anuncioActual = null;
    }
  }

  private seleccionarAnuncioAleatorio(): void {
    if (this.anuncios.length === 0) {
      this.anuncioActual = null;
      return;
    }

    if (this.anuncios.length === 1) {
      this.anuncioActual = this.anuncios[0];
      return;
    }

    let nuevoIndice;
    do {
      nuevoIndice = Math.floor(Math.random() * this.anuncios.length);
    } while (nuevoIndice === this.indiceActual && this.anuncios.length > 1);

    this.indiceActual = nuevoIndice;
    this.anuncioActual = this.anuncios[this.indiceActual];
  }

  private detenerRotacion(): void {
    if (this.intervalo) {
      clearInterval(this.intervalo);
      this.intervalo = null;
    }
  }

  siguienteAnuncio(): void {
    this.seleccionarAnuncioAleatorio();
  }

  reiniciarRotacion(): void {
    this.iniciarRotacion();
  }
}