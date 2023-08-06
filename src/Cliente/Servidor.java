package Cliente;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor extends JFrame implements Runnable {
    ArrayList<String> IpsConected= new ArrayList<String>();
    JTextArea mytext;
    public Servidor(){
        setBounds(1200,300,280,350);
        myPanel newPanel= new myPanel();
        setLayout(new BorderLayout());
        add(newPanel, BorderLayout.CENTER);
        Thread myHilo= new Thread(this);
        myHilo.start();
    }

    @Override
    public void run() {
        //esto es desde JTextField
        //DataInputStream flujoEntrada = null;
        ObjectInputStream flujoEntrada;
        try {
            ServerSocket servidor = new ServerSocket(8080);
            while(true){
                Socket miSocket = servidor.accept();

                /*
                flujoEntrada = new DataInputStream(miSocket.getInputStream());
                String mensaje = flujoEntrada.readUTF();
                mytext.append("\n"+mensaje);
                miSocket.close();
                 */

                flujoEntrada= new ObjectInputStream(miSocket.getInputStream());
                MessageUser newMessage = (MessageUser) flujoEntrada.readObject();
                String message= newMessage.getMessage();

                //-----------Verificacion de coneccion--------
                if(message.equals("Online")){
                    //--------- Deteccion de usuario online -----
                    InetAddress localizacion = miSocket.getInetAddress();
                    String IPremote= localizacion.getHostAddress();
                    System.out.println("Usuario ip: "+IPremote+" Conectect.");
                    IpsConected.add(IPremote);
                    newMessage.setIps(IpsConected);

                    for (String ip: IpsConected) {
                        Socket SendMessage= new Socket( ip, 9090);
                        ObjectOutputStream MessageCli = new ObjectOutputStream(SendMessage.getOutputStream());
                        MessageCli.writeObject(newMessage);
                        MessageCli.close();
                        miSocket.close();
                    }
                    //-------------------
                }else{

                    Socket SendMessage= new Socket(newMessage.getIp(), 9090);
                    ObjectOutputStream MessageCli = new ObjectOutputStream(SendMessage.getOutputStream());
                    MessageCli.writeObject(newMessage);
                    MessageCli.close();
                    miSocket.close();
                    mytext.append("Mensaje de: "+newMessage.getName()+"\nMessage: "+newMessage.getMessage()+"\nDireccion Ip: "+"192.168.1.15"+"\n\n");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    class myPanel extends JPanel{
        myPanel(){
            mytext= new JTextArea();
            add(mytext);
        }
    }
}