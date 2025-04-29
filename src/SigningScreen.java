import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SigningScreen extends JPanel {
    private final KeyStore keyStore;
    private JTextField filePathField;
    private JComboBox<String> keySelector;
    private JTextArea outputArea;
    
    public SigningScreen(KeyStore keyStore) {
        this.keyStore = keyStore;
        setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        filePathField = new JTextField();
        JButton browseButton = new JButton("Procurar");
        keySelector = new JComboBox<>();
        JButton signButton = new JButton("Assinar");
        outputArea = new JTextArea(10, 60);
        outputArea.setEditable(false);
        
        browseButton.addActionListener(e -> browseFile());
        signButton.addActionListener(e -> signFile());
        
        inputPanel.add(new JLabel("Arquivo:"));
        inputPanel.add(filePathField);
        inputPanel.add(browseButton);
        inputPanel.add(new JLabel("Chave Privada:"));
        inputPanel.add(keySelector);
        inputPanel.add(signButton);
        
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        
        refreshKeyList();
    }
    
    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }
    
    private void refreshKeyList() {
        keySelector.removeAllItems();
        for (String keyId : keyStore.getKeyIds()) {
            if (keyId.endsWith("_priv")) {
                keySelector.addItem(keyId);
            }
        }
    }
    
    private void signFile() {
        String filePath = filePathField.getText();
        String keyId = (String) keySelector.getSelectedItem();
        
        if (filePath.isEmpty() || keyId == null) {
            outputArea.append("Selecione um arquivo e uma chave válida\n");
            return;
        }
        
        try {
            File file = new File(filePath);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign((PrivateKey) keyStore.getKey(keyId));
            signature.update(fileContent);
            byte[] digitalSignature = signature.sign();
            
            String signaturePath = filePath + ".sig";
            Files.write(Paths.get(signaturePath), digitalSignature);
            
            outputArea.append("Arquivo assinado com sucesso!\n");
            outputArea.append("Assinatura salva como: " + signaturePath + "\n");
            
            showSimulatedScreen("Assinatura Digital", 
                "Arquivo: " + file.getName() + "\n" +
                "Chave privada: " + keyId + "\n" +
                "Tamanho da assinatura: " + digitalSignature.length + " bytes\n" +
                "Assinatura (Base64):\n" + 
                Base64.getEncoder().encodeToString(digitalSignature));
            
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