
package src;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class RSAKeyGenerator {

    //la fonction generateKeys() est un aggrégat d'idées tirées de ce site:
    //https://gustavopeiretti.com/rsa-keys-java/

    public List<String> generateKeys() throws NoSuchAlgorithmException {
        List<String> keys = new ArrayList<>();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(512);
        KeyPair kp = kpg.generateKeyPair();
        PrivateKey aPrivate = kp.getPrivate();
        PublicKey aPublic = kp.getPublic();
        keys.add(String.valueOf(aPrivate));
        keys.add(String.valueOf(aPublic));
        System.out.println(keys.get(0));
        System.out.println(keys.get(1));
        System.out.println("--------------------");
        System.out.println(getModulus(String.valueOf(aPrivate)));
        System.out.println("--------------------");
        System.out.println(getPrivateExponent(String.valueOf(aPrivate)));
        System.out.println("--------------------");
        System.out.println(getPublicExponent(String.valueOf(aPublic)));
        return keys;
    }

    public static String getPrivateExponent(String privateKey){
        String privateExponent ="";
        int start = privateKey.indexOf("private exponent:") + "private exponent:".length();
        int end = privateKey.length();

        privateExponent = privateKey.substring(start, end);

        return privateExponent;
    }

    public static String getPublicExponent(String publicKey){
        String publicExponent ="";
        int start = publicKey.indexOf("public exponent:") + "public exponent:".length();
        int end = publicKey.length();

        publicExponent = publicKey.substring(start, end);

        return publicExponent;
    }

    public static String getModulus(String key){
        String modulus = "";
        int start = key.indexOf("modulus:") +  "modulus:".length();
        int end = key.indexOf("modulus:") +  "modulus:".length() + 155;

        modulus = key.substring(start, end);

        return modulus;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        RSAKeyGenerator testKeyGenerator = new RSAKeyGenerator();
        testKeyGenerator.generateKeys();
    }

}
