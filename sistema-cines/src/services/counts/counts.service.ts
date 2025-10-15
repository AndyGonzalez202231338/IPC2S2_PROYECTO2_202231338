import { Injectable } from "@angular/core";
import { RestConstants } from "../../shared/restapi/rest-constants";
import { HttpClient } from "@angular/common/http";
import { Count } from "../../models/Counts/count";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class CountsService {
    restConstants = new RestConstants();

    constructor(private httpClient: HttpClient) { }

    public createNewCount(count: Count): Observable<void> {
        return this.httpClient.post<void>(`${this.restConstants.getApiURL()}counts`, count);

    }
}