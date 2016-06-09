package huffmanImageCompression.Compression.Commands

import org.opencv.core.Mat

/**
 * Created by nilot on 12/05/2016.
 */
interface ICommand {
    fun execute(mat: Mat): Mat
    fun undo(mat: Mat): Mat
}