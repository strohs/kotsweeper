package org.cliff.kotsweeper


/**
 * a data class that holds the state of the game board.
 * User: Cliff
 * Date: 6/20/2017
 * Time: 12:47 PM
 */
data class Model( var rows:Int, var cols:Int, var boardState:List<List<Square>> ) {

    companion object Constants {
        // MINimum, MAXimum, and DEFault values for rows/cols used in the game
        const val MIN_ROWS = 5
        const val DEF_ROWS = 8
        const val MAX_ROWS = 25
        const val MIN_COLS = 5
        const val DEF_COLS = 8
        const val MAX_COLS = 25
    }
}