import Exeption.AislamientoException;
import Personas.Persona;
import Personas.SistemaCOVID19;

public class Main {
    public static void main(String[] args) {
        SistemaCOVID19 sistema = new SistemaCOVID19();

        sistema.registrarPersona("Martin", "Montagner", 26, "parque luro", "40306986", "Empleado");
        sistema.registrarPersona("Fran", "Guidi", 22, "Las toninas", "44375485", "Estudiante");

        sistema.testear();
        boolean casosSospechososEncontrados = false;
        try {

            sistema.aislar();
            casosSospechososEncontrados = true;
        } catch (AislamientoException e) {
            System.out.println("Persona aislada: Número de Kit: " + e.getNumeroKit() + ", Barrio: " + e.getBarrio());
        }
        if (!casosSospechososEncontrados) {
            System.out.println("No se encontraron casos sospechosos.");
        }

        sistema.generarJSON();
    }

}
