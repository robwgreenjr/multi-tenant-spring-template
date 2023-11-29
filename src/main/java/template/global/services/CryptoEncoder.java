package template.global.services;

import template.global.constants.GlobalVariable;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;
import java.util.Optional;

@Service("CryptoEncoder")
public class CryptoEncoder implements StringEncoder {
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private static final String UNICODE_FORMAT = "UTF8";

    private final Environment env;

    public CryptoEncoder(Environment env) {
        this.env = env;
    }

    @Override
    public String encode(String string) {
        String encryptedString = null;

        try {
            Cipher cipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
            Optional<SecretKey> secretKey = this.getSecretKey();

            if (secretKey.isPresent()) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey.get());
                byte[] plainText = string.getBytes(UNICODE_FORMAT);
                byte[] encryptedText = cipher.doFinal(plainText);
                encryptedString =
                        new String(Base64.encodeBase64(encryptedText));
            }
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }

        return encryptedString;
    }

    @Override
    public String decode(String string) {
        String decryptedText = null;

        try {
            Cipher cipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
            Optional<SecretKey> secretKey = this.getSecretKey();

            if (secretKey.isPresent()) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey.get());
                byte[] encryptedText = Base64.decodeBase64(string);
                byte[] plainText = cipher.doFinal(encryptedText);
                decryptedText = new String(plainText);
            }
        } catch (Exception ignored) {
        }

        return decryptedText;
    }

    @Override
    public Boolean verify(String string, String encodedString) {
        return null;
    }

    private Optional<SecretKey> getSecretKey() {
        Optional<SecretKey> secretKey = Optional.empty();

        try {
            String encryptionKey =
                    env.getProperty(GlobalVariable.ENCRYPTION_KEY);
            if (encryptionKey == null) {
                throw new RuntimeException(
                        "Server '" + GlobalVariable.ENCRYPTION_KEY +
                                "' isn't set.");
            }

            byte[] arrayBytes = encryptionKey.getBytes(UNICODE_FORMAT);
            KeySpec keySpec = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory secretKeyFactory =
                    SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
            secretKey = Optional.of(secretKeyFactory.generateSecret(keySpec));
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }

        return secretKey;
    }
}
