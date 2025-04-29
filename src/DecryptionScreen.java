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

public class DecryptionScreen extends JPanel {
    private final KeyStore keyStore;
    private JTextField filePathField;
    private JComboBox<String> keySelector;
    private JTextArea outputArea;
    
    public DecryptionScreen(KeyStore keyStore) {
        this.keyStore = keyStore;
        setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        filePathField = new JTextField();
        JButton browseButton = new JButton("Procurar");
        keySelector = new JComboBox<>();
        JButton decryptButton = new JButton("Descriptografar");
        outputArea = new JTextArea(10, 60);
        outputArea.setEditable(false);
        
        browseButton.addActionListener(e -> browseFile());
        decryptButton.addActionListener(e -> decryptFile());
        
        inputPanel.add(new JLabel("Arquivo Cifrado:"));
        inputPanel.add(filePathField);
        inputPanel.add(browseButton);
        inputPanel.add(new JLabel("Chave:"));
        inputPanel.add(keySelector);
        inputPanel.add(decryptButton);
        
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
    
    private void decryptFile() {
        String filePath = filePathField.getText();
        String keyId = (String) keySelector.getSelectedItem();
        
        if (filePath.isEmpty() || keyId == null) {
            outputArea.append("Selecione um arquivo e uma chave válida\n");
            return;
        }
        
        try {
            File file = new File(filePath);
            byte[] encryptedContent = Files.readAllBytes(file.toPath());
            
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keyStore.getKey(keyId));
            byte[] decryptedContent = cipher.doFinal(encryptedContent);
            
            String outputPath = filePath.replace(".enc", ".dec");
            Files.write(Paths.get(outputPath), decryptedContent);
            
            outputArea.append("Arquivo descriptografado com sucesso!\n");
            outputArea.append("Salvo como: " + outputPath + "\n");
            
            showSimulatedScreen("Descriptografia Completa", 
                "Arquivo cifrado: " + file.getName() + "\n" +
                "Chave: " + keyId + "\n" +
                "Tamanho cifrado: " + encryptedContent.length + " bytes\n" +
                "Tamanho descriptografado: " + decryptedContent.length + " bytes\n" +
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