import { HttpClient } from "@angular/common/http";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { Observable } from "rxjs";
import { CategoriaPelicula } from "../../models/Movies/CategoriaPelicula";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class CategoriaPeliculaService {
    private restConstants = new RestConstants();
    constructor(private httpClient: HttpClient) { }

    //operaciones basicas de categoria pelicula
    createCategoriaPelicula(categoria: CategoriaPelicula): Observable<CategoriaPelicula> {
        return this.httpClient.post<CategoriaPelicula>(`${this.restConstants.getApiURL()}categoria-peliculas`, categoria);
    }

    getAllCategoriaPeliculas(): Observable<CategoriaPelicula[]> {
        return this.httpClient.get<CategoriaPelicula[]>(`${this.restConstants.getApiURL()}categoria-peliculas`);
    }

    getCategoriaPeliculaById(id: number): Observable<CategoriaPelicula> {
        return this.httpClient.get<CategoriaPelicula>(`${this.restConstants.getApiURL()}categoria-peliculas/${id}`);
    }

    updateCategoriaPelicula(id: number, categoriaToUpdate: CategoriaPelicula): Observable<CategoriaPelicula> {
        return this.httpClient.put<CategoriaPelicula>(`${this.restConstants.getApiURL()}categoria-peliculas/${id}`, categoriaToUpdate);
    }
    deleteCategoriaPelicula(id: number): Observable<void> {
        return this.httpClient.delete<void>(`${this.restConstants.getApiURL()}categoria-peliculas/${id}`);
    }

}