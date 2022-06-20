from BlockChain import BlockChain

if __name__ == '__main__':
    blockChain = BlockChain()

    for i in range(0, 4):
        blockChain.add_block(blockChain.mine())

    blockChain.display_chain()
