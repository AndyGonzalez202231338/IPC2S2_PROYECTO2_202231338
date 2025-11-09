package services.salas.report;

import java.util.List;

public class ReporteSalasPopularesDto {
    private List<SalaPopularDto> salas;
    
    public ReporteSalasPopularesDto(List<SalaPopularDto> salas) {
        this.salas = salas;
    }
    
    // Getters
    public List<SalaPopularDto> getSalas() { return salas; }
}
