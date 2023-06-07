package Personas;

public class Registro {
    private String dni;
    private double temperatura;

    public Registro(String dni, double temperatura) {
        this.dni = dni;
        this.temperatura = temperatura;
    }

    public String getDni() {
        return dni;
    }

    public double getTemperatura() {
        return temperatura;
    }
}
