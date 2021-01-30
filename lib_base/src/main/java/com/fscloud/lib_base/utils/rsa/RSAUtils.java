package com.fscloud.lib_base.utils.rsa;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @author MrChen
 * @ProjectName :
 * @date :2020/12/29 15:02
 * @description:
 */
public class RSAUtils {
    public static final String RSA_ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

    public static final int KEY_SIZE_2048 = 2048;
    public static final int KEY_SIZE_1024 = 1024;

    private static final String ALGORITHM = "RSA";


    public static KeyPair generateKeyPair() {
        return generateKeyPair(KEY_SIZE_2048);
    }

    public static KeyPair generateKeyPair(int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(keySize);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Failed to generate key pair!", e);
        }
    }

    public static PublicKey getPublicKey(String base64PublicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Utils.decode(base64PublicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get public key!", e);
        }
    }

    public static PublicKey getPublicKey(BigInteger modulus, BigInteger exponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get public key!", e);
        }
    }

    public static String getBase64PublicKey(PublicKey publicKey) {
        return Base64Utils.encode(publicKey.getEncoded());
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64Utils.decode(base64PrivateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get private key!", e);
        }
    }

    public static PrivateKey getPrivateKey(BigInteger modulus, BigInteger exponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to get private key!", e);
        }
    }

    public static String getBase64PrivateKey(PrivateKey privateKey) {
        return Base64Utils.encode(privateKey.getEncoded());
    }

    public static byte[] encryptAsByteArray(String data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new IllegalArgumentException("Encrypt failed!", e);
        }
    }

    public static byte[] encryptAsByteArray(String data, String base64PublicKey) {
        return encryptAsByteArray(data, getPublicKey(base64PublicKey));
    }

    public static String encryptAsString(String data, PublicKey publicKey) {
        return Base64Utils.encode(encryptAsByteArray(data, publicKey));
    }

    public static String encryptAsString(String data, String base64PublicKey) {
        return Base64Utils.encode(encryptAsByteArray(data, getPublicKey(base64PublicKey)));
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(data));
        } catch (Exception e) {
            throw new IllegalArgumentException("Decrypt failed!", e);
        }
    }

    public static String decrypt(byte[] data, String base64PrivateKey) {
        return decrypt(data, getPrivateKey(base64PrivateKey));
    }

    public static String decrypt(String data, PrivateKey privateKey) {
        return decrypt(Base64Utils.decode(data), privateKey);
    }

    public static String decrypt(String data, String base64PrivateKey) {
        return decrypt(Base64Utils.decode(data), getPrivateKey(base64PrivateKey));
    }

    public static void main(String[] args) {
//        KeyPair keyPair = RSAUtils.generateKeyPair();
//        String privateKey = Base64.encode(keyPair.getPrivate().getEncoded());
//        String publicKey = Base64.encode(keyPair.getPublic().getEncoded());

        String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCR6RBGgRWjf3suQYz2E5exWlmAkWI7/CBXsrJWi0vB2oLz9I3fHjcRWfUlSjnxC8Ta1e7dGP81BMv97uNDpHsYHjQ4YxpCEmil6S1r3DBfApliHmHrg3cUH9rRebNf5YtKcFGumtDtnp7R2WEQSazBKgKxvQCk/I8dWNXEDF8P65iziHb0tktE8i4z0OWaMQrzNGNC9DCCI+tor8aDnGa/L9jpusblApng2jX5LVWUmUValLRjipGFNtpRttQ1MQCwDDqhcLTv2ih20dyTUhY/HGNQ5guGxOTuv40dQn+QDR1H31Iokjz4ASNwjErrm+IrNYa3Kcc1RY705HU1mNg3AgMBAAECggEAV+HJhL2g+93/omem8FvypManW9G1Gi4MiOW/lnLFyVfEQg2NGdLIFjJZfgFMDswo4Cm0egThSdy+xQ6KYaaC4Y7nowLFwP/3ed6NFgcEN2WIXtmv3rtLEJrzH/IuQNcv40DtYb4rksNUI07LsJS/cYTNJKKS7cyUondsIW3WH+67UssJXZsEzH6RKmlwknVSgWBq8aQefaez/ZfNJSwekdX0498rTeTv2zpbEwD5nVdt5CYbBol+PwlfbG5jzVVcTihvPYF1oeXUO87+bbD+Rbi2XodzOYfhjdJiYROCTm+MhZr2sjemEzp77p4FeWVYZPF+b60k+Yi+G6wYDXOesQKBgQDbdEMSqx35hegHA7IkZbJzLaE65pPNbNhLKlv9UP64do4p94OKwziKzinBKLnzdsnPPGoiu4amOIGJCXlExax44go3dlH5uF+B02zRlCEYo5fVxx3SwgO3L79EFUltHhHIce5aWAdzqMw+kuD+IKxhrS9A+IBN4ve/rg0+Ldz73wKBgQCqNX7CzwYpIzYjuzqhFvyFX+CG5hg0A2abknA57zRaM0CL0Mu+dORpLkOW84liZlY740P3AA1Hb1fQWKgGG6w8mkTHB39YF0GknFSgcxIRexI6E1BLY27uAlIIc9vq4L27n16dkYb9kD0xwAznxCcnpxxLZ8x1a1pB78xXNRuuqQKBgQCkv7Na4HuwR4bZPT0Pdglxzl+sCE4Nehkm1LYahiOz98ziVJ68HbLlAcD28cwnqpX2rjQ/vuhnASS97A7OQlgoqAljecAw6OlKXZ7j94ChLq9eHg2Vm3GOpJnCUvi8okZXZLC7wjpV6fLrRvkBzyWabpl6/RPVZP75N4FZnLS5oQKBgQCbbmSGc1UV5fC/qm88wefxx/8aUS6S+PQPZyeS0CzHnfv4ccBM+SPIJRZV16mCsR9Yaw4alfKKZFQVj5BIm6aqZNofwmn4wKwSLSxepRY/3lpjrFVrsq6PLJFK5LpT/81jc4KMc2OJU9l9//oI/6gd3s3I0k30gAcZJWk9zeKsmQKBgBaXzNBUBxzLaCl577x00ar1DXsL9NpmVBj+1rDGEJ35tbrcco8SWPIsOUsiRCQ0nwM/0x3bUAu59bC0T0thy9ZyajP5lMKfeWMqMJAwsUYN0J125x5YEeWSPr3zDSYCIhKR3x62IPK866hdZBts0xYbHbCormDSNpXyY2kHMiNk";
        String publicKey ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkekQRoEVo397LkGM9hOXsVpZgJFiO/wgV7KyVotLwdqC8/SN3x43EVn1JUo58QvE2tXu3Rj/NQTL/e7jQ6R7GB40OGMaQhJopekta9wwXwKZYh5h64N3FB/a0XmzX+WLSnBRrprQ7Z6e0dlhEEmswSoCsb0ApPyPHVjVxAxfD+uYs4h29LZLRPIuM9DlmjEK8zRjQvQwgiPraK/Gg5xmvy/Y6brG5QKZ4No1+S1VlJlFWpS0Y4qRhTbaUbbUNTEAsAw6oXC079oodtHck1IWPxxjUOYLhsTk7r+NHUJ/kA0dR99SKJI8+AEjcIxK65viKzWGtynHNUWO9OR1NZjYNwIDAQAB";
        System.out.println("私钥privateKey:" + privateKey);
        System.out.println("公钥publicKey:" + publicKey);

        String content = "123123";
        System.out.println("原始字符串：" + content);
        //公钥加密
            String encryptAsString = RSAUtils.encryptAsString(content, publicKey);
        System.out.println("rsa加密后字符串：" + encryptAsString);
        //私钥解密
//        String encryptAsString ="FhT9qb+x4ZZUVQ3qiUZR1kaapN+hAPwHl9HsYZApwsnUmNT0e7EzVGs/Am6na52uecK+QPZvYIRwghb6DtZHc9gyaE8brhgYzlssuBQcxNoEefDpdirz2zW4K+YSikVivXsj+rrYUfoSYMzL++OOj0CdrZEMPOVs0NKHWCqwf2IxYy2Hk0589J94b6IbM4w8F1Z5+58I1/5I0901ZmWxq+AtpZga4j9zvHpQTxkEU3sNfMLVIPCsmgAKRedUOSC/iFj14tzN8Wdg0iFm69SfmGph0RdOKGpuVf/IA+wCYR29rBsYxh0aIwyiNbaWZRZAafXIEwvOyoLT5Il1Ri5joA==";
//        String decrypt = RSAUtils.decrypt(encryptAsString, privateKey);
//        System.out.println("rsa解密后字符串：" + decrypt);
    }
}
