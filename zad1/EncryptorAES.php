<?php



namespace App\Cryptology\Zad1;

class EncryptorAES
{
    const BLOCK_SIZE = 16;

    protected string $iv;
    protected string $cipher;
    protected string $encryptionKey;
    protected string $encryptedData;

    public function __construct()
    {
        $this->cipher = "aes-256-cbc";
//        $this->iv = openssl_random_pseudo_bytes(openssl_cipher_iv_length($this->cipher));
        $this->iv = "iv_to_aes_is_16_";
    }

    public function encrypt(string $data): string
    {
        $this->encryptedData = openssl_encrypt($data, $this->cipher, $this->encryptionKey, OPENSSL_RAW_DATA, $this->iv);
        return $this->encryptedData;
    }

    public function decrypt(?string $encrypted = null): ?string
    {
        $data = openssl_decrypt($encrypted ?? $this->encryptedData, $this->cipher, $this->encryptionKey, OPENSSL_RAW_DATA);
        if ($data === false) {
            throw new \Exception('Invalid padding');
        }

        return $data;
    }

    /**
     * @return string
     */
    public function getIv(): string
    {
        return $this->iv;
    }

    /**
     * @param string $iv
     */
    public function setIv(string $iv): void
    {
        $this->iv = $iv;
    }

    /**
     * @return string
     */
    public function getCipher(): string
    {
        return $this->cipher;
    }

    /**
     * @param string $cipher
     */
    public function setCipher(string $cipher): void
    {
        $this->cipher = $cipher;
    }

    /**
     * @return string
     */
    public function getEncryptionKey(): string
    {
        return $this->encryptionKey;
    }

    /**
     * @param string $encryptionKey
     */
    public function setEncryptionKey(string $encryptionKey): void
    {
        $this->encryptionKey = $encryptionKey;
    }

    /**
     * @return string
     */
    public function getEncryptedData(): string
    {
        return $this->encryptedData;
    }
}