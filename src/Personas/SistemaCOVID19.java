package Personas;

import Exeption.AislamientoException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class SistemaCOVID19 { private List<Persona> personas;
    private List<Persona> persona;
    private int cantidadKitsDisponibles;
    private Map<Integer, Registro> tablaTemperaturas;

    public SistemaCOVID19() {
        this.personas = new ArrayList<>();
        this.cantidadKitsDisponibles = 10;
        this.tablaTemperaturas = new HashMap<>();
    }

    public void registrarPersona(String nombre, String apellido, int edad, String barrio, String dni, String ocupacion) {
        if (existeDNI(dni)) {
            System.out.println("El DNI ya está registrado.");
            return;
        }

        if (cantidadKitsDisponibles > 0) {
            Persona persona = new Persona(nombre, apellido, edad, barrio, dni, ocupacion);
            int numeroKit = generarNumeroKit();
            persona.setNumeroKit(numeroKit);
            personas.add(persona);
            cantidadKitsDisponibles--;
            System.out.println("Registro exitoso. Número de kit asignado: " + numeroKit);
        } else {
            System.out.println("No hay suficientes kits disponibles. Consultando al SSM...");
            boolean tieneMasKits = consultarSSM();
            if (tieneMasKits) {
                cantidadKitsDisponibles++; // Aumentar la cantidad de kits disponibles
                registrarPersona(nombre, apellido, edad, barrio, dni, ocupacion); // Volver a intentar el registro
            } else {
                throw new RuntimeException("No hay más kits disponibles en el SSM.");
            }
        }
    }
    public void testear() {
        Random random = new Random();

        for (Persona persona : personas) {
            double temperatura = 36 + random.nextDouble() * 3; // Generar temperatura aleatoria entre 36 y 39 grados
            Registro registro = new Registro(persona.getDni(), temperatura);
            tablaTemperaturas.put(persona.getNumeroKit(), registro);
        }

        // Mostrar la tabla de temperaturas
        System.out.println("Tabla de Temperaturas:");
        for (Map.Entry<Integer, Registro> entry : tablaTemperaturas.entrySet()) {
            int numeroKit = entry.getKey();
            Registro registro = entry.getValue();
            System.out.println("Kit: " + numeroKit + ", DNI: " + registro.getDni() + ", Temperatura: " + registro.getTemperatura());
        }
    }
    public void aislar() {
        boolean hayPersonasAisladas = false;

        for (Map.Entry<Integer, Registro> entry : tablaTemperaturas.entrySet()) {
            int numeroKit = entry.getKey();
            Registro registro = entry.getValue();

            if (registro.getTemperatura() >= 38) {
                hayPersonasAisladas = true;

                // Lanzar excepción con el número de kit y el barrio
                String barrio = obtenerBarrioPorNumeroKit(numeroKit);
                throw new AislamientoException(numeroKit, barrio);
            }
        }

        if (!hayPersonasAisladas) {
            System.out.println("No hay personas que deban ser aisladas.");
        }
    }

    private String obtenerBarrioPorNumeroKit(int numeroKit) {
        for (Persona persona : personas) {
            if (persona.getNumeroKit() == numeroKit) {
                return persona.getBarrio();
            }
        }
        return null;
    }

    private boolean existeDNI(String dni) {
        for (Persona persona : personas) {
            if (persona.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }
    public void guardarUrgente() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("urgente.dat");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            for (Map.Entry<Integer, Registro> entry : tablaTemperaturas.entrySet()) {
                Registro registro = entry.getValue();
                if (registro.getTemperatura() >= 38) {
                    objectOutputStream.writeObject(registro);
                }
            }

            objectOutputStream.close();
            fileOutputStream.close();
            System.out.println("Datos guardados en urgente.dat.");
        } catch (IOException e) {
            System.out.println("Error al guardar los datos en urgente.dat: " + e.getMessage());
        }
    }

    private int generarNumeroKit() {
        Random random= new Random();
        return random.nextInt(100);
    }

    private boolean consultarSSM() {

        return false;
    }
    public void generarJSON() {
        List<Persona> sanosList = new ArrayList<>();
        List<Registro> aislarList = new ArrayList<>();

        for (Map.Entry<Integer, Registro> entry : tablaTemperaturas.entrySet()) {
            int numeroKit = entry.getKey();
            Registro registro = entry.getValue();

            Persona persona = obtenerPersonaPorNumeroKit(numeroKit);

            if (registro.getTemperatura() >= 38) {
                aislarList.add(registro);
            } else {
                sanosList.add(persona);
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("sanos", gson.toJsonTree(sanosList));
        jsonObject.add("aislar", gson.toJsonTree(aislarList));

        String jsonText = gson.toJson(jsonObject);

        try (FileWriter fileWriter = new FileWriter("datos.json")) {
            fileWriter.write(jsonText);
            System.out.println("JSON guardado correctamente en datos.json.");
        } catch (IOException e) {
            System.out.println("Error al guardar el JSON en datos.json: " + e.getMessage());
        }
    }


    private Persona obtenerPersonaPorNumeroKit(int numeroKit) {
        for (Persona persona : personas) {
            if (persona.getNumeroKit() == numeroKit) {
                return persona;
            }
        }
        return null;
    }
}

