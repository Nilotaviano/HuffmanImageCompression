package huffmanImageCompression.Compression.Commands

/**
 * Created by nilot on 12/05/2016.
 */
class QuantizationCommand : ICommand {

    override fun Do() {
        println("Do Quantization")
    }

    override fun Undo() {
        println("Undo Quantization")
    }
}