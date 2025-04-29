import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class KeyListScreen extends JPanel {
    private final KeyStore keyStore;
    private JTextArea keyListArea;
    
    public KeyListScreen(KeyStore keyStore) {
        this.keyStore = keyStore;
        setLayout(new BorderLayout());
        
        keyListArea = new JTextArea(15, 60);
        keyListArea.setEditable(false);
        
        JButton refreshButton = new JButton("Atualizar Lista");
        refreshButton.addActionListener(e -> refreshKeyList());
        
        add(refreshButton, BorderLayout.NORTH);
        add(new JScrollPane(keyListArea), BorderLayout.CENTER);
        
        refreshKeyList();
    }
    
    private void refreshKeyList() {
        StringBuilder sb = new StringBuilder("=== Chaves Armazenadas ===\n\n");
        for (String keyId : keyStore.getKeyIds()) {
            sb.append("ID: ").append(keyId).append("\n");
            sb.append("Tipo: ").append(getKeyType(keyId)).append("\n\n");
        }
        keyListArea.setText(sb.toString());
        showSimulatedScreen("Listagem de Chaves", sb.toString());
    }
    
    private String getKeyType(String keyId) {
        if (keyId.startsWith("AES")) return "AES (256 bits)";
        if (keyId.endsWith("_pub")) return "RSA Pública (2048 bits)";
        if (keyId.endsWith("_priv")) return "RSA Privada (2048 bits)";
        return "Desconhecido";
    }
    
    private void showSimulatedScreen(String title, String content) {
        System.out.println("=== " + title + " ===");
        System.out.println(content);
        System.out.println("====================");
    }
}