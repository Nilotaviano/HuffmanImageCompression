package huffmanImageCompression.Compression

import huffmanImageCompression.*
import huffmanImageCompression.Compression.Commands.DCTCommand
import huffmanImageCompression.Compression.Commands.HuffmanCommand
import huffmanImageCompression.Compression.Commands.ICommand
import huffmanImageCompression.Compression.Commands.QuantizationCommand
import huffmanImageCompression.Utils.ImageUtils
import huffmanImageCompression.Utils.UIUtils
import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import java.util.*

class CompressionController {
    @FXML var imageView: ImageView? = null
    @FXML var imgScroll: ScrollPane? = null
    @FXML var saveBtn: Button? = null
    @FXML var undoBtn: Button? = null
    @FXML var nextBtn: Button? = null
    @FXML var labelDCT: Label? = null
    @FXML var labelQuantization: Label? = null
    @FXML var labelHuffman: Label? = null

    var commands = LinkedList<ICommand>(Arrays.asList(DCTCommand(), QuantizationCommand(), HuffmanCommand()))
    var commandIterator = ObservableIterator(commands.listIterator())
    var commandLabels = LinkedList<Label>()

    fun initialize() {
        commandLabels.addAll(Arrays.asList(labelDCT!!, labelQuantization!!, labelHuffman!!))

        imageView?.image = Context.image
        UIUtils.setZoomScroll(imageView!!, imgScroll!!)

        commandIterator.hasNextProperty.value = true
        saveBtn!!.disableProperty().bind(commandIterator.hasNextProperty)
        undoBtn!!.disableProperty().bind(Bindings.not(commandIterator.hasPreviousProperty))
        nextBtn!!.disableProperty().bind(Bindings.not(commandIterator.hasNextProperty))
    }

    fun restoreImage() {
        while (commandIterator.hasPrevious()) {
            commandIterator.previous().undo()
        }
    }

    fun undo() {
        println("undo")
        println("Matriz antes de desfazer ${commandLabels[commandIterator.previousIndex()].text}")
        println(BlockIterator(Context.mat).next().dump())

        if (commandIterator.hasPrevious()) {
            if (commandIterator.hasNext()) {
                commandLabels[commandIterator.nextIndex()].font = Font.font("Verdana", FontWeight.NORMAL, 12.0)
            }

            commandIterator.previous().undo()
            commandLabels[commandIterator.nextIndex()].font = Font.font("Verdana", FontWeight.BOLD, 12.0)

            println("Matriz após desfazer ${commandLabels[commandIterator.nextIndex()].text}")
            println(BlockIterator(Context.mat).next().dump())

        }
        imageView!!.image = ImageUtils.mat2Image(Context.mat)
    }

    fun next() {
        println("next")
        println("Matriz antes de aplicar ${commandLabels[commandIterator.nextIndex()].text}")
        println(BlockIterator(Context.mat).next().dump())

        if (commandIterator.hasNext()) {
            commandLabels[commandIterator.nextIndex()].font = Font.font("Verdana", FontWeight.NORMAL, 12.0)
            commandIterator.next().execute()

            println("Matriz após aplicar ${commandLabels[commandIterator.previousIndex()].text}")
            println(BlockIterator(Context.mat).next().dump())

            if (commandIterator.hasNext()) {
                commandLabels[commandIterator.nextIndex()].font = Font.font("Verdana", FontWeight.BOLD, 12.0)
            }
        }
        imageView!!.image = ImageUtils.mat2Image(Context.mat)
    }

    fun returnToPreviousState() {
        StateManager.changeState(MainMenuState(), Context.stage!!)
    }

    fun saveImage() {

    }
}