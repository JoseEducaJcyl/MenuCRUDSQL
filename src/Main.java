// importaciones necesarias par la conexion SQL y Scanner
import java.sql.*;
import java.util.Scanner;

// Clase Main para ejecutar el codigo
public class Main {
    public static void main(String[] args) {
        // Creacion de un objeto de la clase Scaneer
        Scanner sc = new Scanner(System.in);
        // Creacion de las variables para la conexion
        String url = "jdbc:oracle:thin:@//localhost:1521/xe"; // Cambia según tu BD
        String usuario = "JAVA";
        String contraseña = "12345";

        // Variable boolean para controlar el bucle del menu
        boolean terminado = false;

        // Bucle do-while para el menu
        do {
            System.out.println("----MENU----");
            System.out.println("1---CREAR TABLA");
            System.out.println("2---MOSTRAR DATOS");
            System.out.println("3---ACTUALIZAR DATOS");
            System.out.println("4---ELIMINAR DATOS");
            System.out.println("5---SALIR");
            System.out.println("-------------");
            // Se elige una opcion
            System.out.println("Ingrese la opcion: ");
            int opcion = sc.nextInt();
            sc.nextLine(); // Limpiar el buffer del Scanner

            // Switch para ir por las opciones
            switch (opcion) {
                case 1:
                    crearTabla(url,usuario,contraseña); // Metodo para crear la tabla
                    break;
                case 2:
                    mostrarDatos(url,usuario,contraseña); // Metodo para mostrar la tabla
                    break;
                case 3:
                    System.out.println("Actualizar o insertar datos? (1,2)"); // Sub menu para insertar o actualizar datos
                    int opcion2 = sc.nextInt();
                    sc.nextLine();
                    if (opcion2 == 1) {
                        actualizarDatos(url,usuario,contraseña); // Metodo para actualizar datos
                        break;
                    }else if (opcion2 == 2) {
                        insertarDatos(url,usuario,contraseña); // Metodo para insertar datos
                        break;
                    }
                case 4:
                    elminarDatos(url,usuario,contraseña); // Metodo para eliminar datos
                    break;
                case 5:
                    System.out.println("Saliendo...");  // Opcion para salir
                    terminado = true;
                    break;
                default:
                    System.out.println("Error: Opcion incorrecta"); // En default vuelve al inicio si el usuario se equivoca
                    break;
            }
        }while (!terminado);
        sc.close();

    }


    // Metodo crearTabal, al cual se le pasa la url, usuario y contraseña
    public static void crearTabla(String url, String usuario, String contraseña){
        // Con un try-catch se intenta una conexion
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña))
        {
            // Se crea un objeto statement
            Statement stmt = conn.createStatement();
            // Se crea una variable 'query' con un 'create table' para hacer la tabla empleado
            String query = "CREATE TABLE empleado (" +
                    "id NUMBER PRIMARY KEY," +
                    "nombre VARCHAR2(100)," +
                    "salario NUMBER(10, 2))";

            // Con stmt ejecutamos la query
            stmt.execute(query);
            // Mensaje si ha salido bien
            System.out.println("Tabla empleado creada");
        } catch (SQLException e) {
            // Si falla se muestra un mensaje de error y se captura la excepcion
            System.out.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Metodo para actualizarDatos, al cual se le pasa la url, usuario y contraseña
    public static void actualizarDatos(String url, String usuario, String contraseña) {
        // Con un try-catch se intenta una conexion
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña);
             // Se crea un objeto statement
             Statement stmt = conn.createStatement()){

            // Se crea una variable 'query' con un 'update' para actualizar la tabla empleado
            String sql ="UPDATE empleado SET salario = ? WHERE nombre = ?";
            // Se prepara la query con prepareStatement
            PreparedStatement ps = conn.prepareStatement(sql);
            // Se pasan en base a su colunma y orden en base a la query (En este caso, los datos estan puestos por defecto para
            // probar)
            ps.setDouble(1, 40000.0);
            ps.setString(2, "Ana");
            // Se ejecuta
            ps.executeUpdate();

            // Mensaje si ha salido bien
            System.out.println("Todo bien actualizado");
        } catch (SQLException e) {
            // Si falla se muestra un mensaje de error y se captura la excepcion
            System.out.println("Error al actualizar en tabla: " + e.getMessage());
        }

    }

    // Metodo para mostrarDatos, al cual se le pasa la url, usuario y contraseña
    public static void mostrarDatos(String url, String usuario, String contraseña){
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña);
             // Se crea un objeto statement
             Statement stmt = conn.createStatement()){

            // Se crea una variable 'query' con un 'select' para mostrar los datos la tabla empleado
            String sql = "SELECT id, nombre, salario FROM empleado";

            // Con ResulSet le para la query
            ResultSet rs = stmt.executeQuery(sql);

            // Mientras que de resultados, los datos que devuelve guardan en variables que luego usamos para mostrar
            // por consola
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double salario = rs.getDouble("salario");
                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Salario: " +
                        salario);
            }
        } catch (SQLException e) {
            // Si falla se muestra un mensaje de error y se captura la excepcion
            System.out.println("Error al mostar la tabla: " + e.getMessage());
        }
    }


    // Metodo para insertarDatos, al cual se le pasa la url, usuario y contraseña
    public static void insertarDatos(String url, String usuario, String contraseña){
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña);
             // Se crea un objeto statement
             Statement stmt = conn.createStatement()){

            // Se crea una variable 'query' con un 'insert' para insertar datos en  la tabla empleado (En este caso
            // como ejemplo, ya estan dados en el codigo)
            String sql ="INSERT INTO empleado (id, nombre, salario) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,6);
            ps.setString(2, "Ana");
            ps.setDouble(3, 35000);
            // Se ejecuta
            ps.executeUpdate();

            // Mensaje si ha salido bien
            System.out.println("Todo bien insertado");
        } catch (SQLException e) {
            // Si falla se muestra un mensaje de error y se captura la excepcion
            System.out.println("Error al insertar en tabla: " + e.getMessage());
        }
    }

    public static void elminarDatos(String url, String usuario, String contraseña) {
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña);
             // Se crea un objeto statement
             Statement stmt = conn.createStatement()){

            // Se crea una variable 'query' con un 'delete' para borrar datos en la tabla empleado (En este caso
            // como ejemplo, ya estan dados en el codigo)
            String sql ="DELETE FROM empleado WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,6);
            ps.executeUpdate();

            // Mensaje si ha salido bien
            System.out.println("Todo bien eliminado");
        } catch (SQLException e) {
            // Si falla se muestra un mensaje de error y se captura la excepcion
            System.out.println("Error al elimnar en tabla: " + e.getMessage());
        }
    }
}