package huffmanImageCompression.Compression

import huffmanImageCompression.Compression.Commands.DCTCommand
import huffmanImageCompression.Compression.Commands.HuffmanCommand
import huffmanImageCompression.Compression.Commands.ICommand
import huffmanImageCompression.Compression.Commands.QuantizationCommand
import huffmanImageCompression.Context
import huffmanImageCompression.MainMenuState
import huffmanImageCompression.ObservableIterator
import huffmanImageCompression.StateManager
import huffmanImageCompression.Utils.UIUtils
import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import java.util.*

class CompressionController {
    @FXML var imageView: ImageView? = null
    @FXML var imgScroll: ScrollPane? = null
    @FXML var undoBtn: Button? = null
    @FXML var nextBtn: Button? = null

    var commands = LinkedList<ICommand>(Arrays.asList(DCTCommand(), QuantizationCommand(), HuffmanCommand()))
    var commandIterator = ObservableIterator(commands.listIterator())

    fun initialize() {
        imageView?.image = Context.image
        UIUtils.setZoomScroll(imageView!!, imgScroll!!)

        commandIterator.hasNextProperty.value = true
        undoBtn!!.disableProperty().bind(Bindings.not(commandIterator.hasPreviousProperty))
        nextBtn!!.disableProperty().bind(Bindings.not(commandIterator.hasNextProperty))
    }

    fun restoreImage() {
        while (commandIterator.hasPrevious()) {
            commandIterator.previous().Undo()
        }
    }

    fun undo() {
        if (commandIterator.hasPrevious()) {
            commandIterator.previous().Undo()
        }
    }

    fun next() {
        if (commandIterator.hasNext()) {
            commandIterator.next().Do()
        }
    }

    fun returnToPreviousState() {
        StateManager.changeState(MainMenuState(), Context.stage!!)
    }
}