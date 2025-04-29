import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.crypto.Cipher;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EncryptionScreen extends JPanel {
    private final KeyStore keyStore;
    private JTextField filePathField;
    private JComboBox<String> keySelector;
    private JTextArea outputArea;
    
    public EncryptionScreen(KeyStore keyStore) {
        this.keyStore = keyStore;
        setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        filePathField = new JTextField();
        JButton browseButton = new JButton("Procurar");
        keySelector = new JComboBox<>();
        JButton encryptButton = new JButton("Criptografar");
        outputArea = new JTextArea(10, 60);
        outputArea.setEditable(false);
        
        browseButton.addActionListener(e -> browseFile());
        encryptButton.addActionListener(e -> encryptFile());
        
        inputPanel.add(new JLabel("Arquivo:"));
        inputPanel.add(filePathField);
        inputPanel.add(browseButton);
        inputPanel.add(new JLabel("Chave:"));
        inputPanel.add(keySelector);
        inputPanel.add(encryptButton);
        
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
            if (keyId.startsWith("AES")) {
                keySelector.addItem(keyId);
            }
        }
    }
    
    private void encryptFile() {
        String filePath = filePathField.getText();
        String keyId = (String) keySelector.getSelectedItem();
        
        if (filePath.isEmpty() || keyId == null) {
            outputArea.append("Selecione um arquivo e uma chave válida\n");
            return;
        }
        
        try {
            File file = new File(filePath);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyStore.getKey(keyId));
            byte[] encryptedContent = cipher.doFinal(fileContent);
            
            String outputPath = filePath + ".enc";
            Files.write(Paths.get(outputPath), encryptedContent);
            
            outputArea.append("Arquivo criptografado com sucesso!\n");
            outputArea.append("Salvo como: " + outputPath + "\n");
            
            showSimulatedScreen("Criptografia Completa", 
                "Arquivo: " + file.getName() + "\n" +
                "Chave: " + keyId + "\n" +
                "Tamanho original: " + fileContent.length + " bytes\n" +
                "Tamanho cifrado: " + encryptedContent.length + " bytes\n" +
                "Salvo em: " + outputPath);
            
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