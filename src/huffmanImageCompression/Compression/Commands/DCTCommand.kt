package huffmanImageCompression.Compression.Commands

/**
 * Created by nilot on 12/05/2016.
 */
class DCTCommand : ICommand {

    override fun execute() {
        println("Do DCT")
    }

    override fun undo() {
        println("Undo DCT")
    }
}