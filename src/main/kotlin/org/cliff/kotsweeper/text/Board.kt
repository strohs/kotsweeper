package org.cliff.kotsweeper.text

import org.cliff.kotsweeper.Square
import org.cliff.kotsweeper.SquareStatus
import org.cliff.kotsweeper.SquareType
import java.util.*

/**
 * A board holds the current state of the game. It also contains functions to reveal the squares of the game, generate
 *  mines, determine if the game has ended, and generate a new board.
 * User: Cliff
 * Date: 5/30/2017
 * Time: 2:14 PM
 */
class Board( val rows:Int, val cols:Int ) {

    data class Move( val mark:String, val row:Int, val col:Int )

    //total number of mines allowed on a board, it scales according to the board dimensions
    private val TOTAL_MINES: Int = Math.round( rows * cols * 0.15 ).toInt()

    private val board :MutableList<Square> = newBoard()


    companion object {

        val MIN_ROWS = 4
        val MAX_ROWS = 20
        val MIN_COLS = 4
        val MAX_COLS = 20

        /**
         * return a mutable list of empty squares in row major order
         */
        fun emptyBoard(rows:Int = MIN_ROWS, cols: Int = MIN_COLS ) : MutableList<Square> {
            return MutableList( rows * cols, { Square() } )
        }

    }

    /* convert 1d row major index into a 2d row index */
    fun rowIndex(idx: Int ) : Int = idx / cols
    /* convert 1d column index into a 2d column index */
    fun colIndex(idx: Int ) : Int = idx - ( rowIndex(idx) * cols )
    /* convert 2d row/col indices to a 1d index, row major order */
    fun toIdx1d( r:Int, c:Int ) : Int = c + (r * cols)
    /* convert a 1d index into a 2d row/column indices pair */
    fun idxTo2d( idx:Int ) : Pair<Int,Int> = Pair( rowIndex(idx), colIndex(idx) )

    fun getSquare( r:Int, c:Int ) : Square {
        return board[ toIdx1d( r,c ) ]
    }

    fun getSquare( idx:Int ) : Square {
        return board[ idx ]
    }



    private fun newBoard() : MutableList<Square> {
        val list: MutableList<Square> = emptyBoard( rows, cols )
        val randIndices = genRandomMines( list.indices.toList() )
        //set random mine locations
        for ( idx in randIndices ) {
            list[idx].type = SquareType.MINE
            list[idx].adjCount = 0
        }

        //generate adjacency counts for squares that neighbor mines
        list.forEachIndexed { index, square -> square.adjCount = adjMineCount( index, list ) }

        return list
    }


    /**
     * generate random locations for mines and update the board with those locations. The algorithm used in this
     * function builds a temp list of integers corresponding to squares on the board. The temp list is then shuffled
     * and the first 'TOTAL_MINES' amount of indices are used as mine locations.
     * returns: ()
     */
    private fun genRandomMines( list:List<Int> ) : List<Int> {
        //generate a list of indices corresponding to squares on our board, then shuffle them
        Collections.shuffle( list )

        //take 'TOTAL_MINES' amount of indices from the shuffled list and use them as mine locations
        return list.take( TOTAL_MINES )
    }



    /**
     * return a List of indices Pairs that are adjacent to the square at (r,c). Only indices that are within bounds
     * of the board are returned. The square at r,c is NOT returned in the List of pairs.
     * r = row index of the square
     * c = column index of the square
     */
    fun adjIndices( r:Int, c:Int ) : List<Pair<Int,Int>> {
        val rowRanges = (r - 1)..(r + 1)
        val colRanges = (c - 1)..(c + 1)
        val indices = rowRanges.flatMap {
            ri -> colRanges.map { ci -> Pair(ri,ci) }
        }.filterNot { (ri,ci) -> ri < 0 || ri >= rows || ci < 0 || ci >= cols || ( ri == r && ci == c) }
        return indices
    }

    /* helper function that converts 2d adjacent indices into a 1d list */
    fun adjIndices( idx:Int ) : List<Int> {
        val ais = adjIndices( rowIndex( idx ), colIndex( idx ) )
        return ais.map { (ri,ci) -> toIdx1d(ri,ci) }
    }

    /**
     * number of mines adjacent to the square at r,c. The square at r,c is not included in the calculation
     * return: total number of mines adjacent to the square ar r,c
     */
    fun adjMineCount( idx:Int, board:List<Square> ) : Int {
        //filter and return the count of mine squares that are adjacent to our target index (idx)
        return adjIndices( idx ).filter { i -> board[ i ].type == SquareType.MINE }.count()
    }

    /**
     * Check if a square has no neighboring mines
     * returns true if a square is empty AND its adjacent mine counter is zero
     */
    fun bNoNeigboringMines( idx:Int ) : Boolean =
        board[ idx ].type == SquareType.EMPTY && board[ idx ].adjCount == 0

    /**
     * return a list of squares that are connected to are starting square at index 'idx'
     */
    fun connectedEmptySquares( idx:Int ) : List<Int> {
        //contains all empty squares connected to our starting square
        val visited:MutableList<Int> = mutableListOf()

        if ( bNoNeigboringMines( idx ) ) {
            visited.add( idx )
            //get adjacent empty squares
            val adjacents = adjIndices( idx ).filter { bNoNeigboringMines( it ) }.toMutableList()

            while ( adjacents.isNotEmpty() ) {
                //add all adjacent empty cells to the visited list that have not already been visited
                visited.addAll( adjacents.filterNot { visited.contains(it) } )
                //remove the first index Pair from the adjacents list
                val firstIdx = adjacents.removeAt(0)
                //use this pair to get a list of its adjacent,empty squares
                val nextAdjacents = adjIndices( firstIdx ).filter { bNoNeigboringMines( it ) }
                //append the new adjacent squares to our adjacent list, but only if they haven't already been visited
                adjacents.addAll( nextAdjacents.filterNot { visited.contains(it) } )
            }
            //additionally add the squares that are on the perimeter of the squares that have been visited
            val squaresWithCounts: Set<Int> = visited.flatMap { adjIndices( it ) }.toSet()
            visited.addAll( squaresWithCounts )
        }
        return visited
    }


    fun reveal( r:Int, c:Int ) : List<Int> {
        val idx = toIdx1d( r, c )

        board[ idx ].status = SquareStatus.REVEALED
        //board.set( idx, board[idx].copy( status = SquareStatus.REVEALED ))
        //check if empty square with adjCount of 0, if so then reveal all connected empty squares
        val connectedEmpties: List<Int> = connectedEmptySquares( idx )
        connectedEmpties.forEach { index -> board[index].status = SquareStatus.REVEALED }

        val revIndexes = mutableListOf<Int>()
        revIndexes.add( idx )
        revIndexes.addAll( connectedEmpties )
        return revIndexes.toList()
    }

    /**
     * marks a square on the board as a possible mine location, but does not reveal the square. A player can only
     * toggleMark squares that have not already been revealed. If a square has already been marked, then trying to toggleMark it
     * again will remove the toggleMark.
     */
    fun toggleMark(r:Int, c:Int ) : Square {
        val index = toIdx1d( r, c )
        if ( board[ index ].status != SquareStatus.REVEALED) {
            when ( board[ index ].status ) {
                SquareStatus.UNKNOWN -> board[ index ].status = SquareStatus.MARKED
                SquareStatus.MARKED -> board[ index ].status = SquareStatus.UNKNOWN
                else -> System.err.println("DEBUG: unkown board status: ${board[ index ].status} while toggling a mark")
            }
        }
        return board[ index ]
    }

    /**
     * return all indices that were marked as a mine but were actually empty squares
     */
    fun mismarkedIndices() : List<Int> {
        val indices = mutableListOf<Int>()
        board.forEachIndexed { index, sq ->
            if ( sq.status == SquareStatus.MARKED && sq.type != SquareType.MINE)
                indices.add( index )
        }
        return indices
    }

    fun markedIndices() : List<Int> {
        return board.mapIndexed { index, square ->
            if (square.status == SquareStatus.MARKED)
                index
            else -1 }.filter { it > -1 }
    }


    /**
     * check if the player has won. A player wins if all mined squares have been marked and all
     * non-mined squares have been revealed.
     */
    fun checkForWin() : Boolean {
        //get a count of all revealed squares
        //val revealedSquares = board.filter { s -> s.status == SquareStatus.REVEALED }.count()
        val minesMarked = board.filter { s -> s.status == SquareStatus.MARKED && s.type == SquareType.MINE }.count()
        //return true if the number of non-mined squares == revealed squares and mined squares are marked
        return minesMarked == TOTAL_MINES
    }

    /**
     * returns a list of indices that contain a mine
     */
    fun getMinedIndices() : List<Int>  {
        val indices = mutableListOf<Int>()
        board.forEachIndexed { index, (type) -> if (type == SquareType.MINE) indices.add(index) }
        return indices
    }


    //MOVE PARSER Begin
    fun parseMove( move:String ) : Move {
        fun bValidIndices( row:Int, col:Int ) : Boolean = (row in 0 until rows) && (col in 0 until cols)
        val regx = Regex("""([mr])?(\d+),(\d+)""")
        val match = regx.matchEntire( move )
        if (match != null) {
            val cmd = match.groupValues[1]
            val row = match.groupValues[2].toInt()
            val col = match.groupValues[3].toInt()
            if (!bValidIndices( row, col ))
                throw IllegalArgumentException("$row,$col is out of the board bounds")
            return Move( cmd, row, col )
        } else {
            throw IllegalArgumentException("$move  is not a valid move")
        }
    }

    


}