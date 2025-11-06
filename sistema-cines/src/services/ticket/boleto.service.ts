    import { Injectable } from "@angular/core";
    import { RestConstants } from "../../shared/restapi/rest-constants";
    import { HttpClient } from "@angular/common/http";
    import { map, Observable } from "rxjs";

    export interface Boleto {
        idBoleto?: number;
        idFuncion: number;
        idUsuario: number;
        codigoBoleto: string;
        fechaCompra: string;
        precioPagado: number;
    }

    @Injectable({
    providedIn: 'root'
    })
    export class BoletoService {
        private restConstants = new RestConstants();

        constructor(private httpClient: HttpClient) { }

        // Crear un solo boleto
        public createTicket(boleto: Boleto): Observable<Boleto> {
            return this.httpClient.post<Boleto>(`${this.restConstants.getApiURL()}boletos`, boleto);
        }

        // Crear múltiples boletos
        public createMultipleTickets(boletos: Boleto[]): Observable<Boleto[]> {
            return this.httpClient.post<Boleto[]>(`${this.restConstants.getApiURL()}boletos/multiple`, boletos);
        }

        // Buscar boletos por id de usuario
        public getTicketsByUserId(idUsuario: number): Observable<Boleto[]> {
            return this.httpClient.get<Boleto[]>(`${this.restConstants.getApiURL()}boletos/usuario/${idUsuario}`);
        }

        // Nuevo método para obtener boletos únicos por función
        public getUniqueTicketsByUserId(idUsuario: number): Observable<Boleto[]> {
            return this.httpClient.get<Boleto[]>(`${this.restConstants.getApiURL()}boletos/usuario/${idUsuario}/unicos`).pipe(
            map(boletos => this.filterUniqueTickets(boletos))
            );
        }

        private filterUniqueTickets(boletos: Boleto[]): Boleto[] {
            const uniqueMap = new Map<number, Boleto>();
            
            boletos.forEach(boleto => {
            if (!uniqueMap.has(boleto.idFuncion)) {
                uniqueMap.set(boleto.idFuncion, boleto);
            }
            });
            
            return Array.from(uniqueMap.values());
        }

        // Generar código único para boleto
        public generarCodigoBoleto(): string {
            const timestamp = Date.now().toString(36);
            const random = Math.random().toString(36).substring(2, 8).toUpperCase();
            return `B-${timestamp}-${random}`;
        }
    }