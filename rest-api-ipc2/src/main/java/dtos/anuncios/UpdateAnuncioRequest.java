package dtos.anuncios;

public class UpdateAnuncioRequest {
    private String titulo;
    private String contenidoTexto;
    private Byte[] imagenUrl;
    private String videoUrl;
    private String estado;
    
    // Constructores
    public UpdateAnuncioRequest() {}
    
    public UpdateAnuncioRequest(String titulo, String contenidoTexto, Byte[] imagenUrl, 
                               String videoUrl, String estado) {
        this.titulo = titulo;
        this.contenidoTexto = contenidoTexto;
        this.imagenUrl = imagenUrl;
        this.videoUrl = videoUrl;
        this.estado = estado;
    }
    
    // Getters y setters
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getContenidoTexto() {
        return contenidoTexto;
    }
    
    public void setContenidoTexto(String contenidoTexto) {
        this.contenidoTexto = contenidoTexto;
    }

    public Byte[] getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(Byte[] imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    
    public String getVideoUrl() {
        return videoUrl;
    }
    
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
}