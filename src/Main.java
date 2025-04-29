import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class Main {
    private static KeyStore keyStore = new KeyStore();
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistema de Criptografia");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            
            JTabbedPane tabbedPane = new JTabbedPane();
            
            tabbedPane.addTab("Gerar Chave", new KeyGenerationScreen(keyStore));
            tabbedPane.addTab("Listar Chaves", new KeyListScreen(keyStore));
            tabbedPane.addTab("Criptografar", new EncryptionScreen(keyStore));
            tabbedPane.addTab("Descriptografar", new DecryptionScreen(keyStore));
            tabbedPane.addTab("Assinar", new SigningScreen(keyStore));
            tabbedPane.addTab("Enviar", new SendScreen(keyStore));
            
            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}