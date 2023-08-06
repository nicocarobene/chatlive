import Cliente.Cliente;
import Cliente.Servidor;

import javax.swing.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Cliente firstClient= new Cliente("Nicolas", "192.168.1.15");
        firstClient.setVisible(true);
        firstClient.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Servidor firstServidor= new Servidor();
        firstServidor.setVisible(true);
        firstServidor.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    }
}