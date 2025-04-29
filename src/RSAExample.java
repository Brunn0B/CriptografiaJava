import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSAExample {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        String mensagem = "Confidencial";
        byte[] textoCifrado = cipher.doFinal(mensagem.getBytes());
        String textoCifradoBase64 = Base64.getEncoder().encodeToString(textoCifrado);
        System.out.println("🔐 Cifrado: " + textoCifradoBase64);

        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] textoDecifrado = cipher.doFinal(Base64.getDecoder().decode(textoCifradoBase64));
        System.out.println("🔓 Original: " + new String(textoDecifrado));
    }
}