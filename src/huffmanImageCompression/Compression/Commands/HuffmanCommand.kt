package huffmanImageCompression.Compression.Commands

/**
 * Created by nilot on 12/05/2016.
 */
class HuffmanCommand : ICommand {

    override fun execute() {
        println("Do Huffman")
    }

    override fun undo() {
        println("Undo Huffman")
    }
}