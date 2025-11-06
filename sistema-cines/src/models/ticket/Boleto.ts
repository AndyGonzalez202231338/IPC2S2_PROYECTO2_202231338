export interface Boleto {
    idBoleto?: number,
    idFuncion: number,
    idUsuario: number;
    codigoBoleto: string;
    fechaCompra: string;
    precioPagado: number;
}