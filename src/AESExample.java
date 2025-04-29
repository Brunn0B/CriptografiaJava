import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AESExample {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String mensagem = "Segredo!";
        byte[] textoCifrado = cipher.doFinal(mensagem.getBytes());
        String textoCifradoBase64 = Base64.getEncoder().encodeToString(textoCifrado);
        System.out.println("🔒 Cifrado: " + textoCifradoBase64);

        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] textoDecifrado = cipher.doFinal(Base64.getDecoder().decode(textoCifradoBase64));
        System.out.println("🔓 Original: " + new String(textoDecifrado));
    }
}