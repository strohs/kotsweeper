package org.cliff.kotsweeper

import org.cliff.kotsweeper.BoardModel.cols
import org.cliff.kotsweeper.BoardModel.rows


/**
 * Utilities to print a Board to standard output
 * User: Cliff
 * Time: 2:53 PM
 */
class BoardPrinter {


    companion object {

        private fun Int.format(digits: Int) = java.lang.String.format("%${digits}d", this)

        private fun debugSquare( square:Square ) : String {
            when ( square.type ) {
                SquareType.MINE -> return "*"
                else            -> return square.adjCount.toString()
            }
        }

        private fun printSquare( square:Square ) : String {
            when (square.status) {
                SquareStatus.UNKNOWN    -> return "."
                SquareStatus.REVEALED   -> if (square.type == SquareType.MINE)
                                            return "*"
                                            else return square.adjCount.toString()
                SquareStatus.MARKED     -> return "!"
                SquareStatus.QUESTION   -> return "?"
                SquareStatus.MISMARKED  -> return "X"
            }
        }


        fun print( board: List<List<Square>>, debug:Boolean = false ) {
            //print column headers
            print("  ")
            for (i in 0 until cols(board) ) print(" ${i.format(2)}")
            println()
            print( "-".repeat( cols(board) * 3) )
            println()
            for ( r in 0 until rows(board) ) {
                print("${r.format(2)}| ")
                for ( c in 0 until cols(board) ) {
                    if (debug)
                        print( debugSquare( board[r][c] ) + "  ")
                    else
                        print( printSquare( board[r][c] ) + "  ")
                }
                println()
            }
        }

    }
}