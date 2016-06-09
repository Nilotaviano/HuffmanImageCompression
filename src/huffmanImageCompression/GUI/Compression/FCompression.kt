package huffmanImageCompression.GUI.Compression

import huffmanImageCompression.GUI.IState
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.stage.Stage

class FCompression : Application(), IState {

    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("Compression.fxml"))
        primaryStage.title = "OpenCV - Compressão Huffman"
        primaryStage.scene.root = root
        primaryStage.show()
    }

    override fun enterState(stage: Stage) {
        start(stage)
    }

    override fun leaveState() {

    }
}