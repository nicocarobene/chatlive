package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Cliente extends JFrame {
    JTextArea myMessage;
    JComboBox IP;
    String name;
    public Cliente(String name, String ip){
        JLabel texto= new JLabel("--Cliente--");
        JLabel nombre= new JLabel(name);
        IP= new JComboBox();
        this.name= name;
        setBounds(600,300,280,350);
        myPanel newPanel= new myPanel();
        add(newPanel, BorderLayout.SOUTH);
        add(new JPanel().add(myMessage), BorderLayout.CENTER);
        JPanel infoUser= new JPanel();
        infoUser.add(nombre);
        infoUser.add(texto);
        infoUser.add(IP);
        add(infoUser, BorderLayout.NORTH);
// ------- envio de coneccion a servidor al iniciar -------
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
                try{
                    Socket miSocket= new Socket("192.168.1.15", 8080);
                    MessageUser init= new MessageUser(name, "Online", miSocket.getInetAddress().getHostAddress());
                    ObjectOutputStream mess= new ObjectOutputStream(miSocket.getOutputStream());
                    mess.writeObject(init);
                    mess.close();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }
//----------------------------------------------------------
    class myPanel extends JPanel implements Runnable{
        JTextField campoTexto;
        JButton myButton;
        public myPanel(){
            Thread myListen= new Thread(this);
            myListen.start();
            myMessage= new JTextArea();
            campoTexto= new JTextField(15);
            campoTexto.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    myButton.doClick();
                }
            });
            myButton= new JButton("Send");
            myButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Socket myNetwork= new Socket( "192.168.1.15", 8080);

                        //esto seria desde el JField mandar directamente el texto pero sin contexto sin nombre e ip:
                        /*
                        DataOutputStream flujoSalida= new DataOutputStream(myNetwork.getOutputStream());
                        flujoSalida.writeUTF(campoTexto.getText());
                        flujoSalida.close();
                        campoTexto.setText("");
                         */

                        //y esto enviando un objeto
                        MessageUser newMessage= new MessageUser(name,campoTexto.getText(),IP.getSelectedItem().toString());
                        ObjectOutputStream Message= new ObjectOutputStream(myNetwork.getOutputStream());
                        Message.writeObject(newMessage);
                        campoTexto.setText("");
                    } catch (IOException ex) {
                        System.out.println("Error al intentar conectar a la red web");
                        throw new RuntimeException(ex);
                    }
                }
            });
            add(campoTexto);
            add(myButton);
        }

        @Override
        public void run() {
            try {
                ServerSocket ListendMessage= new ServerSocket(9090);
                while(true){
                    Socket mySocket= ListendMessage.accept();
                    ObjectInputStream listendMessage= new ObjectInputStream(mySocket.getInputStream());
                    MessageUser newMessage =(MessageUser)listendMessage.readObject();
                    listendMessage.close();
                    mySocket.close();
                    String mess =newMessage.getMessage();
                    if(mess.equals("Online")){
                        ArrayList<String> ips =newMessage.getIps();
                        IP.removeAllItems();
                        for (String ip: ips) {
                            IP.addItem(ip);
                        }
                    }else{
                        myMessage.append(newMessage.getName()+": "+newMessage.getMessage()+"\n");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

