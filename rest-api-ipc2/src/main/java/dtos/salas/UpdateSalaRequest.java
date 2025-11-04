package dtos.salas;

public class UpdateSalaRequest {
    private String permiteComentario;
    private String estado;

    public UpdateSalaRequest() {}

    public UpdateSalaRequest(String permiteComentario, String estado) {
        this.permiteComentario = permiteComentario;
        this.estado = estado;
    }

    public String getPermiteComentario() {
        return permiteComentario;
    }

    public void setPermiteComentario(String permiteComentario) {
        this.permiteComentario = permiteComentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
}