class MerkleNode:
    def __init__(self, is_root: bool = False, is_leaf: bool = False, parent: 'MerkleNode' = None, children: list = [], hashed: str = '', height: int = 0):
        self.is_root = is_root
        self.is_leaf = is_leaf
        self.parent = parent
        self.children = children
        self.hashed = hashed
        self.height = height
        self.has_been_used = False
        self.number_in_row = 0
