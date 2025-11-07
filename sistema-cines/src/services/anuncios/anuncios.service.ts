import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { AnuncioCompleto } from "../../models/Anuncio/AnuncioCompleto";
import { Observable, switchMap } from "rxjs";
import { Anuncio } from "../../models/Anuncio/anuncio";
import { TipoAnuncio } from "../../models/Anuncio/TipoAnuncio";
import { PeriodoAnuncio } from "../../models/Anuncio/PeriodoAnuncio";

export interface Publicidad {
    idPublicidad: number;
    idAnuncio: number;
    idUsuario: number;
    precioBloqueo: number;
    estado: string;
}

export interface BloqueoPublicidad {
  id_bloqueo_publicidad: number;
  id_cine: number;
  id_publicidad: number;
  fecha_inicio: string;
  fecha_fin: string;
  costo_total: number;
  fecha_pago: string;
}

@Injectable({
  providedIn: 'root'
})
export class AnunciosService {
    private restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    // ========== MÉTODOS PARA ANUNCIOS ==========
    
    public createNewAnuncio(anuncio: any): any {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}anuncios`, anuncio);
    }

    public getAllAnuncios(): Observable<AnuncioCompleto[]> {
        return this.httpClient.get<AnuncioCompleto[]>(`${this.restConstants.getApiURL()}anuncios`);
    }

    public getAnuncioById(id: number): Observable<Anuncio> {
        return this.httpClient.get<Anuncio>(`${this.restConstants.getApiURL()}anuncios/${id}`);
    }

    public updateAnuncio(id: number, anuncioToUpdate: any): Observable<Anuncio> {
        return this.httpClient.put<Anuncio>(`${this.restConstants.getApiURL()}anuncios/${id}`, anuncioToUpdate);
    }

    public deleteAnuncio(id: number): Observable<void> {
        return this.httpClient.delete<void>(`${this.restConstants.getApiURL()}anuncios/${id}`);
    }

    // Métodos específicos para usuario
    public getAnunciosByUsuario(idUsuario: number): Observable<Anuncio[]> {
        return this.httpClient.get<Anuncio[]>(
        `${this.restConstants.getApiURL()}anuncios/usuario/${idUsuario}`
        );
    }

    public getAnunciosCompletosByUsuario(idUsuario: number): Observable<AnuncioCompleto[]> {
        return this.httpClient.get<AnuncioCompleto[]>(
        `${this.restConstants.getApiURL()}anuncios/usuario/${idUsuario}/completos`
        );
    }

    public getAnuncioCompleto(idAnuncio: number): Observable<AnuncioCompleto> {
        return this.httpClient.get<AnuncioCompleto>(
        `${this.restConstants.getApiURL()}anuncios/${idAnuncio}/completo`
        );
    }

    // Métodos para tipos y períodos
    public getTiposAnuncio(): Observable<TipoAnuncio[]> {
        return this.httpClient.get<TipoAnuncio[]>(
        `${this.restConstants.getApiURL()}tipos-anuncio`
        );
    }

    public getPeriodosAnuncio(): Observable<PeriodoAnuncio[]> {
        return this.httpClient.get<PeriodoAnuncio[]>(
        `${this.restConstants.getApiURL()}periodos-anuncio`
        );
    }

    // Método para crear anuncio con imagen
    public crearAnuncioConImagen(formData: FormData): Observable<Anuncio> {
        return this.httpClient.post<Anuncio>(
        `${this.restConstants.getApiURL()}anuncios/con-imagen`, 
        formData
        );
    }

    // Método para actualizar anuncio con imagen
    public actualizarAnuncioConImagen(idAnuncio: number, formData: FormData): Observable<Anuncio> {
        return this.httpClient.put<Anuncio>(
            `${this.restConstants.getApiURL()}anuncios/${idAnuncio}/con-imagen`, 
            formData
        );
    }

    // Métodos para imágenes
    public getImagenAnuncio(idAnuncio: number): Observable<Blob> {
        return this.httpClient.get(
            `${this.restConstants.getApiURL()}anuncios/${idAnuncio}/imagen`,
            { responseType: 'blob' }
        );
    }

    public getImagenUrl(idAnuncio: number): Observable<string> {
        return new Observable(observer => {
            this.getImagenAnuncio(idAnuncio).subscribe({
                next: (blob: Blob) => {
                    const imageUrl = URL.createObjectURL(blob);
                    observer.next(imageUrl);
                    observer.complete();
                },
                error: (error) => {
                    observer.error(error);
                }
            });
        });
    }

    // Método para cambiar estado del anuncio
    cambiarEstadoAnuncio(idAnuncio: number, nuevoEstado: string): Observable<any> {
        const updateRequest = {
            estado: nuevoEstado
        };

        return this.httpClient.put(
            `${this.restConstants.getApiURL()}anuncios/${idAnuncio}`,
            updateRequest
        );
    }

    // ========== MÉTODOS PARA PUBLICIDADES ==========

    crearPublicidad(idAnuncio: number, idUsuario: number, precioBloqueo: number): Observable<Publicidad> {
        const request = {
            idAnuncio,
            idUsuario,
            precioBloqueo
        };

        return this.httpClient.post<Publicidad>(
            `${this.restConstants.getApiURL()}publicidades`,
            request
        );
    }

    actualizarPublicidad(idPublicidad: number, precioBloqueo: number): Observable<Publicidad> {
        const request = { precioBloqueo };
        
        return this.httpClient.put<Publicidad>(
            `${this.restConstants.getApiURL()}publicidades/${idPublicidad}`,
            request
        );
    }

    getAllPublicidades(): Observable<Publicidad[]> {
        return this.httpClient.get<Publicidad[]>(
            `${this.restConstants.getApiURL()}publicidades`
        );
    }

    getPublicidadByAnuncioId(idAnuncio: number): Observable<Publicidad | null> {
        return this.httpClient.get<Publicidad | null>(
            `${this.restConstants.getApiURL()}publicidades/anuncio/${idAnuncio}`
        );
    }

    // ========== MÉTODOS PARA ANUNCIOS CON PUBLICIDAD ACTIVA ==========

    getAnunciosConPublicidad(): Observable<AnuncioCompleto[]> {
        return this.httpClient.get<AnuncioCompleto[]>(
            `${this.restConstants.getApiURL()}anuncios/con-publicidad-activa`
        );
    }

    // ========== MÉTODOS PARA BLOQUEO DE PUBLICIDAD ==========

    verificarAnuncioBloqueado(idAnuncio: number, idCine: number): Observable<BloqueoPublicidad | null> {
        return this.httpClient.get<BloqueoPublicidad | null>(
            `${this.restConstants.getApiURL()}bloqueo-publicidad/anuncio/${idAnuncio}/cine/${idCine}`
        );
    }

    getBloqueosPorCine(idCine: number): Observable<BloqueoPublicidad[]> {
        return this.httpClient.get<BloqueoPublicidad[]>(
            `${this.restConstants.getApiURL()}bloqueo-publicidad/cine/${idCine}`
        );
    }

    bloquearAnuncio(idAnuncio: number, idCine: number, idPublicidad: number, costoTotal: number): Observable<BloqueoPublicidad> {
        const request = {
            idAnuncio,
            idCine,
            idPublicidad,
            costoTotal
        };

        return this.httpClient.post<BloqueoPublicidad>(
            `${this.restConstants.getApiURL()}bloqueo-publicidad`,
            request
        );
    }

    getBloqueosActivosPorAnuncio(idAnuncio: number): Observable<BloqueoPublicidad[]> {
        return this.httpClient.get<BloqueoPublicidad[]>(
            `${this.restConstants.getApiURL()}bloqueo-publicidad/anuncio/${idAnuncio}/activos`
        );
    }


    /**
     * Subir atrchivos a local videos
     */

    // Método para subir video
// Método para subir video (más simple)
uploadVideo(videoFile: File): Observable<string> {
    const formData = new FormData();
    formData.append('video', videoFile);
    
    // URL CORREGIDA - quita "rest-api-ipc2"
    return this.httpClient.post<string>(
        `http://localhost:8080/api-ipc2/v1/anuncios/upload-video`,
        formData
    );
}

// Método para crear anuncio con video
crearAnuncioConVideo(anuncioData: any, videoFile: File): Observable<Anuncio> {
    return this.uploadVideo(videoFile).pipe(
        switchMap((videoUrl: string) => {
            // Usar la URL del video en el anuncio
            anuncioData.video_url = videoUrl;
            
            // Crear FormData para el anuncio
            const formData = this.crearFormDataDesdeAnuncio(anuncioData);
            
            return this.crearAnuncioConImagen(formData);
        })
    );
}

// Método auxiliar para crear FormData
private crearFormDataDesdeAnuncio(anuncio: any): FormData {
    const formData = new FormData();
    
    formData.append('id_usuario', anuncio.id_usuario.toString());
    formData.append('id_tipo_anuncio', anuncio.id_tipo_anuncio.toString());
    formData.append('id_periodo', anuncio.id_periodo.toString());
    formData.append('titulo', anuncio.titulo);
    formData.append('contenido_texto', anuncio.contenido_texto || '');
    formData.append('video_url', anuncio.video_url || '');
    formData.append('costo_total', anuncio.costo_total.toString());
    formData.append('fecha_inicio', anuncio.fecha_inicio);
    formData.append('fecha_fin', anuncio.fecha_fin);
    formData.append('estado', anuncio.estado || 'ACTIVO');
    
    return formData;
}
}