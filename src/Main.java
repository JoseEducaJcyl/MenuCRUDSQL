import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String url = "jdbc:oracle:thin:@//localhost:1521/xe"; // Cambia según tu BD
        String usuario = "JAVA";
        String contraseña = "12345";

        boolean terminado = false;

        do {
            System.out.println("----MENU----");
            System.out.println("1---CREAR TABLA");
            System.out.println("2---MOSTRAR DATOS");
            System.out.println("3---ACTUALIZAR DATOS");
            System.out.println("4---ELIMINAR DATOS");
            System.out.println("5---SALIR");
            System.out.println("-------------");
            System.out.println("Ingrese la opcion: ");
            int opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    crearTabla(url,usuario,contraseña);
                    break;
                case 2:
                    mostrarDatos(url,usuario,contraseña);
                    break;
                case 3:
                    System.out.println("Actualizar o insertar datos? (1,2)");
                    int opcion2 = sc.nextInt();
                    sc.nextLine();
                    if (opcion2 == 1) {
                        actualizarDatos(url,usuario,contraseña);
                        break;
                    }else if (opcion2 == 2) {
                        insertarDatos(url,usuario,contraseña);
                        break;
                    }
                case 4:
                    elminarDatos(url,usuario,contraseña);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    terminado = true;
                    break;
                default:
                    System.out.println("Error: Opcion incorrecta");
                    break;
            }
        }while (!terminado);
        sc.close();

    }


    public static void crearTabla(String url, String usuario, String contraseña){
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña))
        {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE empleado (" +
                    "id NUMBER PRIMARY KEY," +
                    "nombre VARCHAR2(100)," +
                    "salario NUMBER(10, 2))";
            stmt.execute(query);
            System.out.println("Tabla empleado creada");
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void actualizarDatos(String url, String usuario, String contraseña) {
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña);
             Statement stmt = conn.createStatement()){

            String sql ="UPDATE empleado SET salario = ? WHERE nombre = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, 40000.0);
            ps.setString(2, "Ana");
            ps.executeUpdate();


            System.out.println("Todo bien actualizado");
        } catch (SQLException e) {
            System.out.println("Error al actualizar en tabla: " + e.getMessage());
        }

    }

    public static void mostrarDatos(String url, String usuario, String contraseña){
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña);
             Statement stmt = conn.createStatement()){

            String sql = "SELECT id, nombre, salario FROM empleado";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double salario = rs.getDouble("salario");
                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Salario: " +
                        salario);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostar la tabla: " + e.getMessage());
        }
    }


    public static void insertarDatos(String url, String usuario, String contraseña){
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña);
             Statement stmt = conn.createStatement()){

            String sql ="INSERT INTO empleado (id, nombre, salario) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,6);
            ps.setString(2, "Ana");
            ps.setDouble(3, 35000);
            ps.executeUpdate();


            System.out.println("Todo bien insertado");
        } catch (SQLException e) {
            System.out.println("Error al insertar en tabla: " + e.getMessage());
        }
    }

    public static void elminarDatos(String url, String usuario, String contraseña) {
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña);
             Statement stmt = conn.createStatement()){

            String sql ="DELETE FROM empleado WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,6);
            ps.executeUpdate();

            System.out.println("Todo bien eliminado");
        } catch (SQLException e) {
            System.out.println("Error al elimnar en tabla: " + e.getMessage());
        }
    }
}