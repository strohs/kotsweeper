package org.cliff.kotsweeper

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

/**
 * Controller for a "square". Each square on the board is constructed out of a JavaFX StackPane containing a
 * Rectangle and Text UI elements
 * User: Cliff
 * Date: 6/27/2017
 * Time: 10:25 AM
 */
class SquareCtrl(val kSweeper: KSweeper, val row:Int, val col:Int ) {


    val stackPane:StackPane = build()
    val rect:Rectangle = stackPane.children[0] as Rectangle
    val text:Text = stackPane.children[1] as Text

    /**
     * Construct a StackPane. The squares of the game's grid are constructed
     * out of StackPanes with a Rectangle and a Text control as its children
     */
    private fun build() : StackPane {
        val stack = StackPane()
        stack.id = "sqr${row}c${col}"
        val square = Rectangle( 24.0, 24.0 )
        square.styleClass.add("stack-pane-rectangle")

        val squareText = Text("")
        squareText.styleClass.add("stack-pane-text")

        stack.children.addAll( square, squareText )
        stack.styleClass.add("stack-pane")

        stack.addEventHandler( MouseEvent.MOUSE_CLICKED, squareClickedHandler() )

        return stack
    }

    private fun squareClickedHandler() : EventHandler<MouseEvent> = EventHandler { ev ->
        //when a square on the main grid is clicked, delegate the info to KSweeper and let it take action
        when ( ev.button ) {
        //left mouse button clicked
            MouseButton.PRIMARY -> {
                kSweeper.processGridClick( MouseButton.PRIMARY, this )
            }
        //right mouse button clicked
            MouseButton.SECONDARY -> {
                kSweeper.processGridClick( MouseButton.SECONDARY, this )

            }
            else -> println("${ev.button} click not used in this game")
        }
    }



    /**
     * sets what text a square should display based on the backing Square and (possibly) what text is
     * currently being displayed on the board
     * @param square the square from the backing Board
     */
    fun setSquareText( square:Square ) {
        val status = square.status
        when ( status ) {
            SquareStatus.MARKED -> text.text = KSweeper.BLACK_FLAG
            SquareStatus.QUESTION -> text.text = KSweeper.QUESTION_MARK
            SquareStatus.MISMARKED -> text.text = KSweeper.MISMARKED
            SquareStatus.REVEALED -> {
                //if a square is the Empty Type then set square text to its adjacent mine count, else set text to BOMB
                text.text = if ( square.type == SquareType.EMPTY ) square.adjCount.toString() else KSweeper.BOMB
            }
            SquareStatus.UNKNOWN -> text.text = ""
        }
        text.fill = colorizeText( text.text )
    }

    /**
     * sets the text color of a square based on the number of mines adjacent to it
     */
    fun colorizeText( str:String ) : Color {
        when ( str ) {
            "1"             -> return Color.BLUE
            "2"             -> return Color.GREEN
            "3"             -> return Color.RED
            KSweeper.BOMB   -> return Color.BLACK
            else            -> return Color.BLACK
        }
    }
}