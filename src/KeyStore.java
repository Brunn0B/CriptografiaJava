import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyStore {
    private Map<String, Key> keys = new HashMap<>();
    
    public void addKey(String id, Key key) {
        keys.put(id, key);
    }
    
    public Key getKey(String id) {
        return keys.get(id);
    }
    
    public Set<String> getKeyIds() {
        return keys.keySet();
    }
    
    public String generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey key = keyGen.generateKey();
        String keyId = "AES_" + UUID.randomUUID().toString().substring(0, 8);
        addKey(keyId, key);
        return keyId;
    }
    
    public String generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        String keyId = "RSA_" + UUID.randomUUID().toString().substring(0, 8);
        addKey(keyId + "_pub", pair.getPublic());
        addKey(keyId + "_priv", pair.getPrivate());
        return keyId;
    }
}