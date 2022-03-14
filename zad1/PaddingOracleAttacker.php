<?php

namespace App\Cryptology\Zad1;

class PaddingOracleAttacker
{
    protected EncryptorAES $aes;
    protected ?float $startTime;
    protected ?float $executionTime;

    public function __construct()
    {
        $this->aes = new EncryptorAES();
        $this->startTime = null;
        $this->executionTime = null;
    }

    public function attack(): void
    {
//        $plainText = "Kryptologia semestr zimowy 2021/2022, znowy stacjonarnie w 119 WI2 :) :)";
        $plainText = readline("Enter your text: ");
        echo "Plain text: $plainText\n";

        $this->aes->setEncryptionKey("moj_key");
        $this->aes->encrypt($plainText);

        echo "\nDecrypted text:\n{$this->decryptByPaddingOracleAttack()}\n\n";
        echo "time of operation: {$this->getExecutionTime()} micro seconds\n";
    }

    protected function decryptByPaddingOracleAttack(?string $text = null): string
    {
        $text = $text ?? $this->aes->getEncryptedData();
        $textBlocks = str_split($text, EncryptorAES::BLOCK_SIZE);
        $textBlocksCount = count($textBlocks);
        echo "Count of blocks: $textBlocksCount\n";

        $decryptedText = '';
        $this->startTimer();

        foreach ($textBlocks as $textBlockNumber => $textBlock) {
            if ($textBlockNumber === 0) {
                continue;
            }

            $blockText = 'moj block text';
            $lastXor = [];

            for ($byte = 1; $byte <= EncryptorAES::BLOCK_SIZE; $byte++) {
                for ($i = 1; $i < $byte; $i++) {
                    $blockText[EncryptorAES::BLOCK_SIZE - $i] = chr($byte ^ $lastXor[$i - 1]);
                }

                for ($i = 0; $i < 2**8; $i++) {
                    $executionByte = EncryptorAES::BLOCK_SIZE - $byte;
                    $blockText[$executionByte] = chr($i);

                    try {
                        $decryptedData = $this->aes->decrypt($blockText . $textBlocks[$textBlocksCount - $textBlockNumber]);
                    } catch (\Throwable $exception) {
                        $message = $exception->getMessage();
                        if ($message === "Invalid padding") {
                            continue;
                        }
                    }

                    $paddingXor = $byte ^ $i;
                    $lastXor[] = $paddingXor;
                    $decryptedLetter = ord($textBlocks[$textBlocksCount - ($textBlockNumber + 1)][$executionByte]) ^ $paddingXor;
                    $decryptedText = chr($decryptedLetter) . $decryptedText;
                    break;
                }
            }
            $decryptedText = "\n$decryptedText";
        }
        $this->endTimer();

        return trim($decryptedText);
    }

    protected function startTimer(): void
    {
        $this->startTime = microtime(true);
    }

    protected function endTimer(): ?float
    {
        if ($this->startTime === null) {
            return null;
        }

        $endTime = microtime(true);
        $this->startTime = null;

        $this->executionTime = $endTime - $this->startTime;
        return $this->executionTime;
    }

    /**
     * @param bool $convertToSeconds
     * @return float|null
     */
    public function getExecutionTime(bool $convertToSeconds = false): ?float
    {
        if (!$convertToSeconds) {
            return $this->executionTime;
        }
        return $convertToSeconds / 1000 / 1000;
    }
}