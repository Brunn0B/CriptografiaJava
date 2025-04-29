import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class KeyGenerationScreen extends JPanel {
    private final KeyStore keyStore;
    private JTextArea outputArea;
    
    public KeyGenerationScreen(KeyStore keyStore) {
        this.keyStore = keyStore;
        setLayout(new BorderLayout());
        
        JPanel buttonPanel = new JPanel();
        JButton aesButton = new JButton("Gerar Chave AES");
        JButton rsaButton = new JButton("Gerar Par RSA");
        
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        
        aesButton.addActionListener(e -> generateAESKey());
        rsaButton.addActionListener(e -> generateRSAKey());
        
        buttonPanel.add(aesButton);
        buttonPanel.add(rsaButton);
        
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }
    
    private void generateAESKey() {
        try {
            String keyId = keyStore.generateAESKey();
            outputArea.append("Chave AES gerada: " + keyId + "\n");
            showSimulatedScreen("Geração de Chave AES", "Chave AES " + keyId + " gerada com sucesso!");
        } catch (Exception ex) {
            outputArea.append("Erro: " + ex.getMessage() + "\n");
        }
    }
    
    private void generateRSAKey() {
        try {
            String keyId = keyStore.generateRSAKeyPair();
            outputArea.append("Par RSA gerado: " + keyId + "\n");
            showSimulatedScreen("Geração de Par RSA", "Par RSA " + keyId + " gerado com sucesso!\nInclui chave pública e privada.");
        } catch (Exception ex) {
            outputArea.append("Erro: " + ex.getMessage() + "\n");
        }
    }
    
    private void showSimulatedScreen(String title, String content) {
        System.out.println("=== " + title + " ===");
        System.out.println(content);
        System.out.println("====================");
    }
}