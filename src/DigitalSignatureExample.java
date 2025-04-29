import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.util.Base64;

public class DigitalSignatureExample {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        String mensagem = "Documento legal";
        signature.update(mensagem.getBytes());
        byte[] assinatura = signature.sign();
        String assinaturaBase64 = Base64.getEncoder().encodeToString(assinatura);
        System.out.println("🖋️ Assinatura: " + assinaturaBase64);

        signature.initVerify(keyPair.getPublic());
        signature.update(mensagem.getBytes());
        boolean valida = signature.verify(Base64.getDecoder().decode(assinaturaBase64));
        System.out.println("✅ Válida? " + valida);
    }
}