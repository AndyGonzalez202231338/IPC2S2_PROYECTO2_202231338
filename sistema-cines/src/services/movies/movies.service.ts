import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Movie } from "../../models/Movies/Movie";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MoviesService {
    private restConstants = new RestConstants();
    constructor(private httpClient: HttpClient) { }

    //operaciones basicas de pelicula
    createMovie(movie: Movie): Observable<Movie> {
        return this.httpClient.post<Movie>(`${this.restConstants.getApiURL()}movies`, movie);
    }

    getAllMovies(): Observable<Movie[]> {
        return this.httpClient.get<Movie[]>(`${this.restConstants.getApiURL()}movies`);
    }

    getMovieById(id: number): Observable<Movie> {
        return this.httpClient.get<Movie>(`${this.restConstants.getApiURL()}movies/${id}`);
    }

    updateMovie(id: number, movieToUpdate: Movie): Observable<Movie> {
        return this.httpClient.put<Movie>(`${this.restConstants.getApiURL()}movies/${id}`, movieToUpdate);
    }

    deleteMovie(id: number): Observable<void> {
        return this.httpClient.delete<void>(`${this.restConstants.getApiURL()}movies/${id}`);
    }

        // Crear enviando informacion de sus multiples categorias
    createMovieWithCategories(formData: FormData): Observable<any> {
        console.log('FormData to be sent:', formData);
        return this.httpClient.post<any>(`${this.restConstants.getApiURL()}movies`, formData);
    }

    // En movies.service.ts solo cambiar el estado
    updateMovieEstado(id: number, nuevoEstado: string): Observable<any> {
        const updateData = { estado: nuevoEstado };
        return this.httpClient.put<any>(`${this.restConstants.getApiURL()}movies/${id}/estado`, updateData);
    }
}
