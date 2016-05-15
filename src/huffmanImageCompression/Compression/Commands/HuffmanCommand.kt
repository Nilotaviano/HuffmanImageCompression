package huffmanImageCompression.Compression.Commands

/**
 * Created by nilot on 12/05/2016.
 */
class HuffmanCommand : ICommand {

    override fun Do() {
        println("Do Huffman")
    }

    override fun Undo() {
        println("Undo Huffman")
    }
}