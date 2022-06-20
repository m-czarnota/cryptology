import hashlib
import random
import secrets

from LamportSign import LamportSign
from MerkleSign import MerkleSign

secret_key_pairs = []
public_key_pairs = []
count_of_pairs = 256


def generate_secret_key():
    for i in range(0, count_of_pairs):
        secret_key_pairs.append([])

        for j in range(0, 2):
            secret_key_pairs[i].append(random.getrandbits(count_of_pairs))


def generate_public_key():
    for i in range(0, count_of_pairs):
        public_key_pairs.append([])

        for j in range(0, 2):
            public_key_pairs[i].append(hashlib.sha256(f"{secret_key_pairs[i][j]}".encode()).hexdigest())


if __name__ == '__main__':
    lamportSign = LamportSign()
    # lamportSign.generate_keys_pairs()
    # signed_message = lamportSign.sign_message('my awesome lamport message')
    # print(lamportSign.verify_sign(signed_message))

    merkle_sign = MerkleSign()
    merkle_sign.generate_keys_pairs()
