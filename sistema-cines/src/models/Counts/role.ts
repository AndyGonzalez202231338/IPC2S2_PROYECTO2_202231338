export interface Role {
  idRol: number;
  nombreRol: string;
  descripcion: string;
}

//getters 
export function getIdRol(role: Role): number {
  return role.idRol;
}

export function getNombreRol(role: Role): string {
  return role.nombreRol;
}

export function getDescripcion(role: Role): string {
  return role.descripcion;
}

//setters
export function setIdRol(role: Role, idRol: number): void {
  role.idRol = idRol;
}

export function setNombreRol(role: Role, nombreRol: string): void {
  role.nombreRol = nombreRol;
}

export function setDescripcion(role: Role, descripcion: string): void {
  role.descripcion = descripcion;
}

//constructor
export function createRole(idRol: number, nombreRol: string, descripcion: string): Role {
  return {
    idRol,
    nombreRol,
    descripcion
  };
}
