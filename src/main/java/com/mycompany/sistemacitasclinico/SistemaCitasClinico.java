/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemacitasclinico;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SistemaCitasClinico {
    private static final String RUTA_CITAS_CSV = "C:\\Users\\jarma\\Documents\\Sistema de citas\\Citas.csv";
    private static Map<String, Administrador> administradores = new HashMap<>();
    private static Map<String, Doctor> doctores = new HashMap<>();
    private static Map<String, Paciente> pacientes = new HashMap<>();
    private static List<Cita> citas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            cargarUsuariosPrecargados();
            cargarCitasDesdeCSV();
            mostrarMenuPrincipal();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void cargarUsuariosPrecargados() {
        administradores.put("admin1", new Administrador("Admin Uno", "admin1", "admin123"));
        doctores.put("doc1", new Doctor("Dr. Lopez", "doc1", "doc123", "Cardiologia"));
        pacientes.put("pac1", new Paciente("Juan Perez", "pac1", "pac123", "5512345678"));
    }

    private static void cargarCitasDesdeCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_CITAS_CSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String idCita = datos[0];
                String doctorId = datos[1];
                String pacienteId = datos[2];
                LocalDate fecha = LocalDate.parse(datos[3]);
                LocalTime hora = LocalTime.parse(datos[4]);
                citas.add(new Cita(idCita, doctorId, pacienteId, fecha, hora));
            }
        } catch (FileNotFoundException e) {
            System.out.println("El archivo de citas no se encontró. Se creará uno nuevo cuando se guarden citas.");
        } catch (IOException e) {
            System.out.println("No se pudo cargar las citas desde el archivo CSV: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al cargar las citas: " + e.getMessage());
        }
    }

    private static void guardarCitasEnCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_CITAS_CSV))) {
            for (Cita cita : citas) {
                bw.write(cita.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar las citas en el archivo CSV: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al guardar las citas: " + e.getMessage());
        }
    }

    private static void mostrarMenuPrincipal() {
        while (true) {
            try {
                System.out.println("\n--- Sistema de Citas Clinico ---");
                System.out.println("1. Iniciar Sesion como Administrador");
                System.out.println("2. Iniciar Sesion como Doctor");
                System.out.println("3. Iniciar Sesion como Paciente");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opcion: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (opcion) {
                    case 1 -> iniciarSesionAdministrador();
                    case 2 -> iniciarSesionDoctor();
                    case 3 -> iniciarSesionPaciente();
                    case 4 -> {
                        guardarCitasEnCSV();
                        System.out.println("Saliendo del sistema...");
                        return;
                    }
                    default -> System.out.println("Opcion invalida, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
                scanner.nextLine(); // Limpiar el buffer
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error: " + e.getMessage());
            }
        }
    }

    private static void iniciarSesionAdministrador() {
        try {
            System.out.print("Ingrese ID de administrador: ");
            String id = scanner.nextLine();
            System.out.print("Ingrese contrasena: ");
            String contrasena = scanner.nextLine();

            Administrador admin = administradores.get(id);
            if (admin != null && admin.getContrasena().equals(contrasena)) {
                System.out.println("Bienvenido " + admin.getNombre());
                menuAdministrador(admin);
            } else {
                System.out.println("ID o contrasena incorrecta.");
            }
        } catch (Exception e) {
            System.out.println("Error al iniciar sesion: " + e.getMessage());
        }
    }

    private static void iniciarSesionDoctor() {
        try {
            System.out.print("Ingrese ID del doctor: ");
            String id = scanner.nextLine();
            System.out.print("Ingrese contrasena: ");
            String contrasena = scanner.nextLine();

            Doctor doctor = doctores.get(id);
            if (doctor != null && doctor.getContrasena().equals(contrasena)) {
                System.out.println("Bienvenido " + doctor.getNombre());
                menuDoctor(doctor);
            } else {
                System.out.println("ID o contrasena incorrecta.");
            }
        } catch (Exception e) {
            System.out.println("Error al iniciar sesion: " + e.getMessage());
        }
    }

    private static void iniciarSesionPaciente() {
        try {
            System.out.print("Ingrese ID del paciente: ");
            String id = scanner.nextLine();
            System.out.print("Ingrese contrasena: ");
            String contrasena = scanner.nextLine();

            Paciente paciente = pacientes.get(id);
            if (paciente != null && paciente.getContrasena().equals(contrasena)) {
                System.out.println("Bienvenido " + paciente.getNombre());
                menuPaciente(paciente);
            } else {
                System.out.println("ID o contrasena incorrecta.");
            }
        } catch (Exception e) {
            System.out.println("Error al iniciar sesion: " + e.getMessage());
        }
    }

    private static void menuAdministrador(Administrador admin) {
        while (true) {
            try {
                System.out.println("\n--- Menu Administrador ---");
                System.out.println("1. Dar de alta doctor");
                System.out.println("2. Dar de alta paciente");
                System.out.println("3. Crear una cita");
                System.out.println("4. Ver todas las citas");
                System.out.println("5. Editar una cita");
                System.out.println("6. Cerrar sesion");
                System.out.print("Seleccione una opcion: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> darAltaDoctor();
                    case 2 -> darAltaPaciente();
                    case 3 -> crearCita();
                    case 4 -> verTodasLasCitas();
                    case 5 -> editarCita();
                    case 6 -> {
                        System.out.println("Cerrando sesion de administrador...");
                        return;
                    }
                    default -> System.out.println("Opcion invalida, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
                scanner.nextLine(); // Limpiar el buffer
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error: " + e.getMessage());
            }
        }
    }

    private static void crearCita() {
        try {
            System.out.print("Ingrese el ID de la cita: ");
            String idCita = scanner.nextLine();

            System.out.print("Ingrese el ID del doctor: ");
            String doctorId = scanner.nextLine();
            if (!doctores.containsKey(doctorId)) {
                System.out.println("Doctor no encontrado.");
                return;
            }

            System.out.print("Ingrese el ID del paciente: ");
            String pacienteId = scanner.nextLine();
            if (!pacientes.containsKey(pacienteId)) {
                System.out.println("Paciente no encontrado.");
                return;
            }

            System.out.print("Ingrese la fecha de la cita (yyyy-MM-dd): ");
            LocalDate fecha = LocalDate.parse(scanner.nextLine());
            System.out.print("Ingrese la hora de la cita (HH:mm): ");
            LocalTime hora = LocalTime.parse(scanner.nextLine());

            Cita nuevaCita = new Cita(idCita, doctorId, pacienteId, fecha, hora);
            citas.add(nuevaCita);

            System.out.println("Cita creada exitosamente.");
            guardarCitasEnCSV();
        } catch (Exception e) {
            System.out.println("Error al crear la cita: " + e.getMessage());
        }
    }

    private static void editarCita() {
        try {
            System.out.print("Ingrese el ID de la cita a editar: ");
            String idCita = scanner.nextLine();
            Cita cita = null;

            for (Cita c : citas) {
                if (c.getIdCita().equals(idCita)) {
                    cita = c;
                    break;
                }
            }

            if (cita == null) {
                System.out.println("Cita no encontrada.");
                return;
            }

            System.out.print("Ingrese la nueva fecha (yyyy-MM-dd): ");
            LocalDate nuevaFecha = LocalDate.parse(scanner.nextLine());
            System.out.print("Ingrese la nueva hora (HH:mm): ");
            LocalTime nuevaHora = LocalTime.parse(scanner.nextLine());

            cita.setFecha(nuevaFecha);
            cita.setHora(nuevaHora);

            System.out.println("Cita editada exitosamente.");
            guardarCitasEnCSV();
        } catch (Exception e) {
            System.out.println("Error al editar la cita: " + e.getMessage());
        }
    }

    private static void menuDoctor(Doctor doctor) {
        while (true) {
            try {
                System.out.println("\n--- Menu Doctor ---");
                System.out.println("1. Ver mis citas");
                System.out.println("2. Crear una cita");
                System.out.println("3. Editar una cita");
                System.out.println("4. Cerrar sesion");
                System.out.print("Seleccione una opcion: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> verCitasDoctor(doctor);
                    case 2 -> crearCita();
                    case 3 -> editarCita();
                    case 4 -> {
                        System.out.println("Cerrando sesion de doctor...");
                        return;
                    }
                    default -> System.out.println("Opcion invalida, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
                scanner.nextLine(); // Limpiar el buffer
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error: " + e.getMessage());
            }
        }
    }

    private static void verCitasDoctor(Doctor doctor) {
        try {
            System.out.println("\n--- Citas del Doctor " + doctor.getNombre() + " ---");
            for (Cita cita : citas) {
                if (cita.getDoctorId().equals(doctor.id)) {
                    System.out.println(cita);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al mostrar las citas: " + e.getMessage());
        }
    }

    private static void menuPaciente(Paciente paciente) {
        while (true) {
            try {
                System.out.println("\n--- Menu Paciente ---");
                System.out.println("1. Ver mis citas");
                System.out.println("2. Crear una cita");
                System.out.println("3. Editar una cita");
                System.out.println("4. Cerrar sesion");
                System.out.print("Seleccione una opcion: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> verCitasPaciente(paciente);
                    case 2 -> crearCita();
                    case 3 -> editarCita();
                    case 4 -> {
                        System.out.println("Cerrando sesion de paciente...");
                        return;
                    }
                    default -> System.out.println("Opcion invalida, intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
                scanner.nextLine(); // Limpiar el buffer
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error: " + e.getMessage());
            }
        }
    }

    private static void verCitasPaciente(Paciente paciente) {
        try {
            System.out.println("\n--- Citas del Paciente " + paciente.getNombre() + " ---");
            for (Cita cita : citas) {
                if (cita.getPacienteId().equals(paciente.id)) {
                    System.out.println(cita);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al mostrar las citas: " + e.getMessage());
        }
    }

    private static void darAltaDoctor() {
        try {
            System.out.print("Ingrese nombre del doctor: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese ID del doctor: ");
            String id = scanner.nextLine();
            System.out.print("Ingrese contrasena: ");
            String contrasena = scanner.nextLine();
            System.out.print("Ingrese especialidad: ");
            String especialidad = scanner.nextLine();

            doctores.put(id, new Doctor(nombre, id, contrasena, especialidad));
            System.out.println("Doctor dado de alta exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al dar de alta al doctor: " + e.getMessage());
        }
    }

    private static void darAltaPaciente() {
        try {
            System.out.print("Ingrese nombre del paciente: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese ID del paciente: ");
            String id = scanner.nextLine();
            System.out.print("Ingrese contrasena: ");
            String contrasena = scanner.nextLine();
            System.out.print("Ingrese telefono celular: ");
            String telefono = scanner.nextLine();

            pacientes.put(id, new Paciente(nombre, id, contrasena, telefono));
            System.out.println("Paciente dado de alta exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al dar de alta al paciente: " + e.getMessage());
        }
    }

    private static void verTodasLasCitas() {
        try {
            System.out.println("\n--- Lista de Citas ---");
            for (Cita cita : citas) {
                System.out.println(cita);
            }
        } catch (Exception e) {
            System.out.println("Error al mostrar las citas: " + e.getMessage());
        }
    }
}

// Clases auxiliares
class Usuario {
    protected String nombre;
    protected String id;
    protected String contrasena;

    public Usuario(String nombre, String id, String contrasena) {
        this.nombre = nombre;
        this.id = id;
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }
}

class Administrador extends Usuario {
    public Administrador(String nombre, String id, String contrasena) {
        super(nombre, id, contrasena);
    }
}

class Doctor extends Usuario {
    private String especialidad;

    public Doctor(String nombre, String id, String contrasena, String especialidad) {
        super(nombre, id, contrasena);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}

class Paciente extends Usuario {
    private String telefonoCelular;

    public Paciente(String nombre, String id, String contrasena, String telefonoCelular) {
        super(nombre, id, contrasena);
        this.telefonoCelular = telefonoCelular;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }
}

class Cita {
    private String idCita;
    private String doctorId;
    private String pacienteId;
    private LocalDate fecha;
    private LocalTime hora;

    public Cita(String idCita, String doctorId, String pacienteId, LocalDate fecha, LocalTime hora) {
        this.idCita = idCita;
        this.doctorId = doctorId;
        this.pacienteId = pacienteId;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getIdCita() {
        return idCita;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getPacienteId() {
        return pacienteId;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String toCSV() {
        return idCita + "," + doctorId + "," + pacienteId + "," + fecha + "," + hora;
    }

    @Override
    public String toString() {
        return "Cita ID: " + idCita + ", Doctor ID: " + doctorId + ", Paciente ID: " + pacienteId + ", Fecha: " + fecha + ", Hora: " + hora;
    }
}