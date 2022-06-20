import hashlib
import random

from typing import Tuple


class LamportSign:
    def __init__(self):
        self.secret_key_pairs = []
        self.public_key_pairs = []
        self.count_of_pairs = 256

    def generate_secret_key(self):
        for i in range(0, self.count_of_pairs):
            self.secret_key_pairs.append([])

            for j in range(0, 2):
                self.secret_key_pairs[i].append(random.getrandbits(self.count_of_pairs))

    def generate_public_key(self):
        for i in range(0, self.count_of_pairs):
            self.public_key_pairs.append([])

            for j in range(0, 2):
                self.public_key_pairs[i].append(hashlib.sha256(f"{self.secret_key_pairs[i][j]}".encode()).hexdigest())

    def generate_keys_pairs(self):
        self.generate_secret_key()
        self.generate_public_key()

    def sign_message(self, message: str) -> str:
        hash_message, hash_message_binary = self.get_hashed_binary_message(message)

        sign = ''
        for index, byte in enumerate(hash_message_binary):
            sign += str(self.secret_key_pairs[index][int(byte)]) + '_'

        signed_message = message + '__' + sign

        return signed_message

    def verify_sign(self, signed_message: str):
        message = signed_message[0:signed_message.find('__')]
        hash_message, hash_message_binary = self.get_hashed_binary_message(message)

        sign = signed_message[len(message) + 2:]
        hashes = [self.public_key_pairs[i][int(byte)] for i, byte in enumerate(hash_message_binary)]

        numbers = []
        k = 0
        while k != len(sign):
            pos = sign.find('_', k)
            numbers.append(sign[k:pos])
            k = pos + 1

        numbers_hash = [hashlib.sha256(number.encode()).hexdigest() for number in numbers]

        is_correct = [True if numbers_hash[i] == hashes[i] else False for i in range(0, len(hashes))]
        return all(is_correct)

    def get_hashed_binary_message(self, message: str) -> Tuple[str, str]:
        hash_message = hashlib.sha256(message.encode()).hexdigest()
        hash_message_binary = bin(int(hash_message, 16))[2:]
        return hash_message, hash_message_binary
