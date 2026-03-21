package org.sifenboot.integration.util.crypto;

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public final class HashUtils {

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static String sha256Hex(String input) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(
                    input.getBytes(StandardCharsets.UTF_8)
            );
            return bytesToHex(digest);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "Algoritmo SHA-256 no disponible", e
            );
        }
    }
}
