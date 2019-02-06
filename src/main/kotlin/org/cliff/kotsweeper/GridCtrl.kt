package org.cliff.kotsweeper

import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

/**
 * Controller for the GridPane that contains the mined squares
 * User: Cliff
 * Date: 6/21/2017
 * Time: 2:33 PM
 */
class GridCtrl( val kSweeper: KSweeper, var rows:Int, var cols:Int ) {

    // holds the 2d list of SquareControllers that make up the game's mines grid
    private lateinit var squareControllers:MutableList<MutableList<SquareCtrl>>

    val grid: GridPane = build( rows,cols )

    fun build( rows:Int, cols:Int ): GridPane {
        val grid: GridPane = GridPane()
        grid.id = KSweeper.MAIN_GRID_ID
        squareControllers = emptyList( kSweeper, rows, cols )

        for ( r in 0 until rows ) {
            for ( c in 0 until cols ) {
                grid.add( squareControllers[r][c].stackPane, c, r )
            }
        }
        this.rows = rows
        this.cols = cols
        return grid
    }

    /**
     * update this GridPane so that it displays the data contained within 'boardState'
     */
    fun refreshGrid( boardState:List<List<Square>> ) {
        boardState.forEachIndexed { row, rowList ->
            rowList.forEachIndexed { col, square ->
                squareControllers[row][col].setSquareText( square )
            }
        }
    }

    /**
     * disable all event handlers on the main grid by adding a filter that consumes mouse events
     */
    fun disableMouseClickEvents() {
        grid.children.forEach { it.addEventFilter( MouseEvent.MOUSE_CLICKED, { ev ->
            ev.consume()
        })}
    }

    /**
     * sets the color of the grid square at row,col to the specified color
     */
    fun colorizeSquares( indices:List<Pair<Int,Int>>, color:Color ) {
        indices.forEach { (row,col) ->
            squareControllers[row][col].rect.fill = color
        }
    }


    /**
     * creates an empty 2d list of SquareControllers
     */
    private fun emptyList( kSweeper: KSweeper, rows:Int, cols:Int ) : MutableList<MutableList<SquareCtrl>> {
        val list: MutableList<MutableList<SquareCtrl>> = MutableList( rows, { ridx ->
            MutableList( cols, { cidx -> SquareCtrl( kSweeper,ridx,cidx ) })
        })
        return list
    }


}