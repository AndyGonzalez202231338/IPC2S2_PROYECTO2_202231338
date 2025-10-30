import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { AnuncioCompleto } from "../../models/Anuncio/AnuncioCompleto";
import { Observable } from "rxjs";
import { Anuncio } from "../../models/Anuncio/anuncio";
import { TipoAnuncio } from "../../models/Anuncio/TipoAnuncio";
import { PeriodoAnuncio } from "../../models/Anuncio/PeriodoAnuncio";

@Injectable({
  providedIn: 'root'
})
export class AnunciosService {
    private restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    // ========== MÉTODOS EXISTENTES (mantener para compatibilidad) ==========
    
    public createNewAnuncio(anuncio: any): any {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}anuncios`, anuncio);
    }

    public getAllAnuncios(): any {
        return this.httpClient.get<any[]>(`${this.restConstants.getApiURL()}anuncios`);
    }

    public getAnuncioById(id: number): any {
        return this.httpClient.get<any>(`${this.restConstants.getApiURL()}anuncios/${id}`);
    }

    public updateAnuncio(id: number, anuncioToUpdate: any): any {
        return this.httpClient.put<any>(`${this.restConstants.getApiURL()}anuncios/${id}`, anuncioToUpdate);
    }

    public deleteAnuncio(id: number): any {
        return this.httpClient.delete<void>(`${this.restConstants.getApiURL()}anuncios/${id}`);
    }

    // NUEVOS MÉTODOS ESPECÍFICOS PARA USUARIO
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

    // NUEVOS MÉTODOS PARA TIPOS Y PERÍODOS
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

    // Método para crear anuncio con validación (mantener para compatibilidad)
    public crearAnuncioCompleto(anuncio: Anuncio): Observable<Anuncio> {
        return this.httpClient.post<Anuncio>(
        `${this.restConstants.getApiURL()}anuncios`, 
        anuncio
        );
    }

    // ========== NUEVOS MÉTODOS PARA MANEJO DE ARCHIVOS ==========

    /**
     * Crea un nuevo anuncio con imagen (usando FormData)
     */
    public crearAnuncioConImagen(formData: FormData): Observable<Anuncio> {
    // Debug: mostrar qué se está enviando
    console.log('Enviando FormData:');
    for (let [key, value] of (formData as any).entries()) {
        console.log(key + ': ', value);
    }
    
    return this.httpClient.post<Anuncio>(
        `${this.restConstants.getApiURL()}anuncios/con-imagen`, 
        formData
    );
}

    /**
     * Actualiza un anuncio con nueva imagen
     */
    public actualizarAnuncioConImagen(idAnuncio: number, formData: FormData): Observable<Anuncio> {
        return this.httpClient.put<Anuncio>(
            `${this.restConstants.getApiURL()}anuncios/${idAnuncio}/con-imagen`, 
            formData
        );
    }

    /**
     * Obtiene la imagen de un anuncio como blob
     */
    public getImagenAnuncio(idAnuncio: number): Observable<Blob> {
        return this.httpClient.get(
            `${this.restConstants.getApiURL()}anuncios/${idAnuncio}/imagen`,
            { responseType: 'blob' }
        );
    }

    /**
     * Obtiene la URL de la imagen para mostrar (crea object URL)
     */
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

    /**
     * Método auxiliar para crear FormData desde un objeto Anuncio y archivo
     */
    public crearFormDataDesdeAnuncio(anuncio: Anuncio, archivoImagen?: File): FormData {
        const formData = new FormData();
        
        // Agregar campos del anuncio
        formData.append('id_usuario', anuncio.idUsuario.toString());
        formData.append('id_tipo_anuncio', anuncio.idTipoAnuncio.toString());
        formData.append('id_periodo', anuncio.idPeriodo.toString());
        formData.append('titulo', anuncio.titulo);
        
        if (anuncio.contenidoTexto) {
            formData.append('contenido_texto', anuncio.contenidoTexto);
        }
        
        if (anuncio.videoUrl) {
            formData.append('video_url', anuncio.videoUrl);
        }
        
        formData.append('costo_total', anuncio.costoTotal.toString());
        formData.append('fecha_inicio', anuncio.fechaInicio);
        formData.append('fecha_fin', anuncio.fechaFin);
        formData.append('estado', anuncio.estado || 'ACTIVO');
        
        // Agregar archivo si existe
        if (archivoImagen) {
            formData.append('imagen', archivoImagen, archivoImagen.name);
        }
        
        return formData;
    }

    //metodo para cambiar el estado del anuncio activo/inactivo
    cambiarEstadoAnuncio(idAnuncio: number, nuevoEstado: string): Observable<any> {
        const updateRequest = {
        estado: nuevoEstado
    };

    return this.httpClient.put(
        `${this.restConstants.getApiURL()}anuncios/${idAnuncio}`,
        updateRequest
    );
    }
}