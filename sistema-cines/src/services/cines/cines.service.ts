import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Cine } from "../../models/Cines/cine";

@Injectable({
  providedIn: 'root'
})
export class CinesService {
    private restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    public createNewCine(cine: Cine): Observable<void> {
    return this.httpClient.post<void>(`${this.restConstants.getApiURL()}cines`, cine);
  }

  public getAllCines(): Observable<Cine[]> {
    return this.httpClient.get<Cine[]>(`${this.restConstants.getApiURL()}cines`);
  }

  public getCineById(id: number): Observable<Cine> {
    return this.httpClient.get<Cine>(`${this.restConstants.getApiURL()}cines/${id}`);
  }

  public updateCine(id: number, cineToUpdate: Cine): Observable<Cine> {
    return this.httpClient.put<Cine>(`${this.restConstants.getApiURL()}cines/${id}`, cineToUpdate);
  }

  public deleteCine(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.restConstants.getApiURL()}cines/${id}`);
  }

  
}
