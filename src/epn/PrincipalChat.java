package epn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**Clase que se encarga de correr los threads de enviar y recibir texto
 * y de crear la interfaz grafica.
 * 
 * @author Rafa
 */
public class PrincipalChat extends JFrame{
    public JTextField campoTexto; //Para mostrar mensajes de los usuarios
    public JTextArea areaTexto; //Para ingresar mensaje a enviar
    private static ServerSocket servidor; //
    private static Socket conexion; //Socket para conectarse con el cliente
    private static String ip = "192.168.100.45"; //ip a la cual se conecta
    
    public static PrincipalChat main; 
    
    public PrincipalChat(){
        super("Servidor"); //Establece titulo al Frame
        
        campoTexto = new JTextField(); //crea el campo para texto
        campoTexto.setEditable(false); //No permite que sea editable el campo de texto
        add(campoTexto, BorderLayout.NORTH); //Coloca el campo de texto en la parte superior
        
        areaTexto = new JTextArea(); //Crear displayArea
        areaTexto.setEditable(false);
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        areaTexto.setBackground(Color.orange); //Pone de color cyan al areaTexto
        areaTexto.setForeground(Color.BLACK); //pinta azul la letra en el areaTexto
        campoTexto.setForeground(Color.BLACK); //pinta toja la letra del mensaje a enviar
        
        
        //Crea menu Archivo y submenu Salir, ademas agrega el submenu al menu
        JMenu menuArchivo = new JMenu("Archivo"); 
        JMenuItem salir = new JMenuItem("Salir");
        menuArchivo.add(salir); //Agrega el submenu Salir al menu menuArchivo
        
        JMenuBar barra = new JMenuBar(); //Crea la barra de menus
        setJMenuBar(barra); //Agrega barra de menus a la aplicacion
        barra.add(menuArchivo); //agrega menuArchivo a la barra de menus
        
        //Accion que se realiza cuando se presiona el submenu Salir
        salir.addActionListener(new ActionListener() { //clase interna anonima
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); //Sale de la aplicacion
                }
        });
        
        setSize(300, 320); //Establecer tamano a ventana
        setVisible(true); //Pone visible la ventana
    }
    
    //Para mostrar texto en displayArea
    public void mostrarMensaje(String mensaje) {
        areaTexto.append(mensaje + "\n");
    } 
    public void habilitarTexto(boolean editable) {
        campoTexto.setEditable(editable);
    }
 
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PrincipalChat main = new PrincipalChat(); //Instanciacion de la clase Principalchat
        main.setLocationRelativeTo(null);   //Centrar el JFrame
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //habilita cerrar la ventana
        ExecutorService executor = Executors.newCachedThreadPool(); //Para correr los threads
 
        try {
            //main.mostrarMensaje("No se encuentra Servidor");
            servidor = new ServerSocket(11111, 100); 
            main.mostrarMensaje("Esperando Cliente ...");

            //Bucle infinito para esperar conexiones de los clientes
            while (true){
                try {
                    conexion = servidor.accept(); //Permite al servidor aceptar conexiones        

                    //main.mostrarMensaje("Conexion Establecida");
                    main.mostrarMensaje("Conectado a : " + conexion.getInetAddress().getHostName());

                    main.habilitarTexto(true); //permite escribir texto para enviar

                    //Ejecucion de los threads
                    executor.execute(new ThreadRecibe(conexion, main)); //client
                    executor.execute(new ThreadEnvia(conexion, main));
                } catch (IOException ex) {
                    Logger.getLogger(PrincipalChat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PrincipalChat.class.getName()).log(Level.SEVERE, null, ex);
        } //Fin del catch
        finally {
        }
        executor.shutdown();
    }
}
