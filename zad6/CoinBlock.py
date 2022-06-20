import hashlib
import random
import time


class CoinBlock:
    def __init__(self, previous_block_hash: str, transaction_list: list, index: int):
        self.previous_block_hash = previous_block_hash
        self.transaction_list = transaction_list
        self.time = str(time.time())
        self.nonce = random.random()
        self.index = index

        self.block_data = f"{' - '.join(transaction_list)} - {previous_block_hash} - {self.time} - {self.nonce} - {index}"
        self.block_hash = hashlib.sha256(self.block_data.encode()).hexdigest()
