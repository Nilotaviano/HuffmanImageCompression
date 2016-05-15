package huffmanImageCompression.Compression.Commands

/**
 * Created by nilot on 12/05/2016.
 */
class QuantizationCommand : ICommand {

    override fun execute() {
        println("Do Quantization")
    }

    override fun undo() {
        println("Undo Quantization")
    }
}