import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SendScreen extends JPanel {
    private final KeyStore keyStore;
    private JTextField filePathField;
    private JComboBox<String> aesKeySelector;
    private JComboBox<String> rsaKeySelector;
    private JTextArea outputArea;
    
    public SendScreen(KeyStore keyStore) {
        this.keyStore = keyStore;
        setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        filePathField = new JTextField();
        JButton browseButton = new JButton("Procurar");
        aesKeySelector = new JComboBox<>();
        rsaKeySelector = new JComboBox<>();
        JButton sendButton = new JButton("Preparar Envio");
        outputArea = new JTextArea(10, 60);
        outputArea.setEditable(false);
        
        browseButton.addActionListener(e -> browseFile());
        sendButton.addActionListener(e -> preparePackage());
        
        inputPanel.add(new JLabel("Arquivo:"));
        inputPanel.add(filePathField);
        inputPanel.add(browseButton);
        inputPanel.add(new JLabel("Chave AES:"));
        inputPanel.add(aesKeySelector);
        inputPanel.add(new JLabel("Chave RSA Pública:"));
        inputPanel.add(rsaKeySelector);
        inputPanel.add(sendButton);
        
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
        aesKeySelector.removeAllItems();
        rsaKeySelector.removeAllItems();
        
        for (String keyId : keyStore.getKeyIds()) {
            if (keyId.startsWith("AES")) {
                aesKeySelector.addItem(keyId);
            } else if (keyId.endsWith("_pub")) {
                rsaKeySelector.addItem(keyId);
            }
        }
    }
    
    private void preparePackage() {
        String filePath = filePathField.getText();
        String aesKeyId = (String) aesKeySelector.getSelectedItem();
        String rsaKeyId = (String) rsaKeySelector.getSelectedItem();
        
        if (filePath.isEmpty() || aesKeyId == null || rsaKeyId == null) {
            outputArea.append("Preencha todos os campos corretamente\n");
            return;
        }
        
        try {
            File file = new File(filePath);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, keyStore.getKey(aesKeyId));
            byte[] encryptedContent = aesCipher.doFinal(fileContent);
            
            String encryptedFilePath = filePath + ".enc";
            Files.write(Paths.get(encryptedFilePath), encryptedContent);
            
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, keyStore.getKey(rsaKeyId));
            byte[] encryptedKey = rsaCipher.doFinal(keyStore.getKey(aesKeyId).getEncoded());
            
            String encryptedKeyPath = filePath + ".key";
            Files.write(Paths.get(encryptedKeyPath), encryptedKey);
            
            Signature signature = Signature.getInstance("SHA256withRSA");
            String privKeyId = rsaKeyId.replace("_pub", "_priv");
            signature.initSign((PrivateKey) keyStore.getKey(privKeyId));
            signature.update(fileContent);
            byte[] digitalSignature = signature.sign();
            
            String signaturePath = filePath + ".sig";
            Files.write(Paths.get(signaturePath), digitalSignature);
            
            String packagePath = file.getName() + "_package.zip";
            
            outputArea.append("Pacote de envio preparado com sucesso!\n");
            outputArea.append("Conteúdo do pacote:\n");
            outputArea.append("- Arquivo cifrado: " + encryptedFilePath + "\n");
            outputArea.append("- Chave AES cifrada: " + encryptedKeyPath + "\n");
            outputArea.append("- Assinatura digital: " + signaturePath + "\n");
            outputArea.append("Pacote simulado: " + packagePath + "\n");
            
            showSimulatedScreen("Pacote de Envio Preparado",
                "Arquivo original: " + file.getName() + "\n" +
                "Tamanho original: " + fileContent.length + " bytes\n" +
                "Arquivo cifrado: " + encryptedFilePath + "\n" +
                "Tamanho cifrado: " + encryptedContent.length + " bytes\n" +
                "Chave AES cifrada com RSA: " + encryptedKeyPath + "\n" +
                "Assinatura digital: " + signaturePath + "\n" +
                "Pacote final simulado: " + packagePath);
            
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