package huffmanImageCompression.Compression.Commands

/**
 * Created by nilot on 12/05/2016.
 */
class DCTCommand : ICommand {

    override fun Do() {
        println("Do DCT")
    }

    override fun Undo() {
        println("Undo DCT")
    }
}