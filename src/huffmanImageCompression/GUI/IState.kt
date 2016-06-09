package huffmanImageCompression.GUI

import javafx.stage.Stage

interface IState {
    fun enterState(stage: Stage)
    fun leaveState()
}