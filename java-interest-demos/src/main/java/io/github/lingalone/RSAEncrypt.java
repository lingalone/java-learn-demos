package io.github.lingalone;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * RSA表单加密
 *
 * @author <a href="https://github.com/lingalone">lingalone</a>
 * @version 1.0.0
 * @link
 * @since 2018/3/12
 */

public class RSAEncrypt {

    public static final String KEY_ALGORITHM = "RSA";

    public static KeyPair genKeyPair() throws NoSuchAlgorithmException {

        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        return null;
    }
}
