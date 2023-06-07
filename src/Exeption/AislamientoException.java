package Exeption;

public class AislamientoException extends RuntimeException {
    private int numeroKit;
    private String barrio;

    public AislamientoException(int numeroKit, String barrio) {
        this.numeroKit = numeroKit;
        this.barrio = barrio;
    }

    public int getNumeroKit() {
        return numeroKit;
    }

    public String getBarrio() {
        return barrio;
    }
}