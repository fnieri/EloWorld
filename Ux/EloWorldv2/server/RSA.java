
import javax.crypto.Cipher;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

public class RSA {

    //Cette classe ne vient pas de notre travail, elle a été trouvée a cette adresse:
    //https://gustavopeiretti.com/rsa-encrypt-decrypt-java/

    private PublicKey loadPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        // reading from resource folder
        byte[] publicKeyBytes = Objects.requireNonNull(getClass().getResourceAsStream("/key.pub")).readAllBytes();

        KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return publicKeyFactory.generatePublic(publicKeySpec);
    }

    public String encode(String toEncode) throws Exception {

        PublicKey publicKey = loadPublicKey();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytes = cipher.doFinal(toEncode.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(bytes));
    }

    private PrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        // reading from resource folder
        byte[] privateKeyBytes = Objects.requireNonNull(getClass().getResourceAsStream("/key.priv")).readAllBytes();

        KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return privateKeyFactory.generatePrivate(privateKeySpec);
    }

    public String decode(String toDecode) throws Exception {

        PrivateKey privateKey = loadPrivateKey();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(toDecode));
        return new String(bytes);
    }

    public static void main(String[] args) throws Exception {
        RSA rsa = new RSA();
        RSAKeyGenerator testKeyGenerator = new RSAKeyGenerator();
        testKeyGenerator.generateKeys();
        String toEncrypt = "The machine is Eternal";
        System.out.println(toEncrypt);
        String crypted = rsa.encode(toEncrypt);
        System.out.println(crypted);
        String decoded = rsa.decode(crypted);
        System.out.println(decoded);
    }
}