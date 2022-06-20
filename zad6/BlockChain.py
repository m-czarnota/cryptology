import hashlib

from CoinBlock import CoinBlock


class BlockChain:
    def __init__(self):
        self.difficulty = 4
        self.chain = []
        self.generate_genesis_block()

    def generate_genesis_block(self):
        self.chain.append(CoinBlock("0", ['Genesis Block'], self.new_index()))

    def create_block_from_transaction(self, transaction_list):
        previous_block_hash = self.last_block.block_hash
        self.chain.append(CoinBlock(previous_block_hash, transaction_list, self.new_index()))

    def display_chain(self):
        for i in range(len(self.chain)):
            print(f"Data {i + 1}: {self.chain[i].block_data}")
            print(f"Hash {i + 1}: {self.chain[i].block_hash}\n")

    def proof_of_work(self, prev_proof_of_work=None):
        count = prev_proof_of_work if prev_proof_of_work is not None else 1

        while True:
            new_hash = hashlib.sha256(str(self.last_block.block_hash + str(count)).encode()).hexdigest()

            if new_hash.startswith('0' * self.difficulty):
                return count

            count += 1

    def mine(self, prev_proof_of_work=None):
        proof_of_work = self.proof_of_work(prev_proof_of_work)
        last_block = self.last_block
        new_index = self.new_index()
        new_block = CoinBlock(last_block.block_hash, [str(last_block.index), str(proof_of_work)], new_index)

        if new_block.block_hash in self.chain_hashes:
            self.mine(proof_of_work)
            return

        return new_block

    def add_block(self, block: CoinBlock):
        self.chain.append(block)

    @property
    def last_block(self):
        return self.chain[-1]

    @property
    def chain_size(self):
        return len(self.chain)

    def new_index(self):
        return self.chain_size + 1

    @property
    def chain_hashes(self):
        return [block_hash.block_hash for block_hash in self.chain]
