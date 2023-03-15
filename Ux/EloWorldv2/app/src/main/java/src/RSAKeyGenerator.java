package src;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class RSAKeyGenerator {

    //la fonction generateKeys() est un aggrégat d'idées tirées de ce site:
    //https://gustavopeiretti.com/rsa-keys-java/

    public List<String> generateKeys() throws NoSuchAlgorithmException {
        List<String> keys = new ArrayList<>();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        PrivateKey aPrivate = kp.getPrivate();
        PublicKey aPublic = kp.getPublic();
        keys.add(String.valueOf(aPrivate));
        keys.add(String.valueOf(aPublic));
        return keys;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        RSAKeyGenerator testKeyGenerator = new RSAKeyGenerator();
        testKeyGenerator.generateKeys();
    }

}