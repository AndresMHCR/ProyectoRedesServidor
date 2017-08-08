package epn;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
        
public class ThreadEnvia implements Runnable {
    private final PrincipalChat main; 
    private ObjectOutputStream salida;
    private String mensaje;
    private Socket conexion; 
   
    public ThreadEnvia(Socket conexion, final PrincipalChat main){
        this.conexion = conexion;
        this.main = main;
        String key = "92AE31A79FEEB2A3"; //llave
        String iv = "0123456789ABCDEF"; // vector de inicialización
        //Evento que ocurre al escribir en el areaTexto
        main.campoTexto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mensaje = event.getActionCommand();
                try {
					enviarDatos(epn.StringEncrypt.encrypt(key, iv, mensaje));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //se envia el mensaje
                main.campoTexto.setText(""); //borra el texto del enterfield
            } //Fin metodo actionPerformed
        } 
        );//Fin llamada a addActionListener
    } 
    
   //enviar objeto a cliente 
   private void enviarDatos(String mensaje) throws Exception{
	   String key = "92AE31A79FEEB2A3"; //llave
       String iv = "0123456789ABCDEF"; // vector de inicialización
      try {
         salida.writeObject(mensaje);
         salida.flush(); //flush salida a cliente
         main.mostrarMensaje("Servidor>>> " +  epn.StringEncrypt.decrypt(key, iv, mensaje));
      } //Fin try
      catch (IOException ioException){ 
         main.mostrarMensaje("Error escribiendo Mensaje");
      } //Fin catch  
      
   } //Fin methodo enviarDatos

   //manipula areaPantalla en el hilo despachador de eventos
    public void mostrarMensaje(String mensaje) {
        main.areaTexto.append(mensaje);
    } 
   
    public void run() {
         try {
            salida = new ObjectOutputStream(conexion.getOutputStream());
            salida.flush(); 
        } catch (SocketException ex) {
        } catch (IOException ioException) {
          ioException.printStackTrace();
        } catch (NullPointerException ex) {
        }
    }   
   
} 
