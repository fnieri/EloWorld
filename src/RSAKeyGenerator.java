import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

public class RSAKeyGenerator {

    //la fonction generateKeys() est un aggrégat d'idées tirées de ce site:
    //https://gustavopeiretti.com/rsa-keys-java/

    public void generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        PrivateKey aPrivate = kp.getPrivate();
        PublicKey aPublic = kp.getPublic();

        try (FileOutputStream outPrivate = new FileOutputStream("key.priv")) {
            outPrivate.write(aPrivate.getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream outPublic = new FileOutputStream("key.pub")) {
            outPublic.write(aPublic.getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        RSAKeyGenerator testKeyGenerator = new RSAKeyGenerator();
        testKeyGenerator.generateKeys();
    }

}
