import hashlib
import random

from MerkleNode import MerkleNode


class MerkleSign:
    def __init__(self):
        self.secret_key_pairs = []
        self.count_of_pairs = 256
        self.root = None
        self.height = 0

    def generate_secret_key(self):
        for i in range(0, self.count_of_pairs):
            self.secret_key_pairs.append([])

            for j in range(0, 2):
                self.secret_key_pairs[i].append(random.getrandbits(self.count_of_pairs))

    def generate_public_key(self):
        public_key_pairs = []
        nodes = []

        number_in_tree = 0
        for i in range(0, self.count_of_pairs):
            public_key_pairs.append([])

            for j in range(0, 2):
                public_key_pairs[i].append(hashlib.sha256(f"{self.secret_key_pairs[i][j]}".encode()).hexdigest())

                node = MerkleNode(hashed=public_key_pairs[i][j], height=self.height, is_leaf=True)
                node.number_in_row = number_in_tree
                nodes.append(node)

                number_in_tree += 1

        new_nodes = nodes.copy()
        while len(new_nodes) > 1:
            nodes_copy = new_nodes.copy()
            new_nodes = []
            self.height += 1
            number_in_tree = 0

            for i in range(0, len(nodes_copy), 2):
                node = MerkleNode()
                node.height = self.height
                node.children = nodes_copy[i:i + 2]
                node.hashed = hashlib.sha256((node.children[0].hashed + node.children[1].hashed).encode()).hexdigest()
                node.number_in_row = number_in_tree

                number_in_tree += 1
                new_nodes.append(node)
                self.root = node

    def generate_keys_pairs(self):
        self.generate_secret_key()
        self.generate_public_key()

    def sign_message(self, message: str) -> str:
        pass

    def get_leaf_from_height(self, height: int, number_in_row, node: 'MerkleNode' = None):
        if node.height != height:
            node = self.get_leaf_from_height(height, number_in_row)
