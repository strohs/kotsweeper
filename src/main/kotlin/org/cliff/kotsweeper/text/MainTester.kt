package org.cliff.kotsweeper.text

import org.cliff.kotsweeper.BoardModel

/**
 * Created with IntelliJ IDEA.
 * User: Cliff
 * Date: 5/30/2017
 * Time: 11:00 AM
 */


val MINE_CHAR = '*'
val MARK_CHAR = '!'


fun main(args: Array<String>) {

    tester()
//    val board = Board( 4, 4 )
//
//    board.printBoard()
//    println("-----------------------")
//
//    var input:String? = ""
//
//    while ( input != "exit" ) {
//        println("Enter your move:")
//        input = readLine()
//        if ( input == "exit" || input.isNullOrEmpty() ) break
//        println("=> $input")
//        try {
//            val ( cmd,row,col) = board.parseMove( input!! )
//            when ( cmd ) {
//                "m" -> board.toggleMark( row, col )
//                "r" -> board.toggleMark( row, col )
//                else -> {
//                    val revSquares: List<Int> = board.reveal( row,col )
//                    if ( board.getSquare(row,col).type == SquareType.MINE ) {
//                        println("YOU HIT A MINE!")
//                        input = "exit"
//                    }
//                    if ( board.checkForWin() ) {
//                        println("YOU WIN!!")
//                        input = "exit"
//                    }
//                }
//            }
//            board.printBoard()
//            println("===================")
//        } catch ( e:IllegalArgumentException ) {
//            println(e.message)
//        }
//
//    }
//    println("goodbye")
//    board.printBoard( true )

}

fun tester() {
    var board = BoardModel.newBoard(3, 4)
    println("board rows:${BoardModel.rows(board)}  cols:${BoardModel.cols(board)}")
//    val nb = BoardModel.reveal( 0,1,board )
//    nb.forEach { rowList ->
//        rowList.forEach { println( it ) }
//    }

}


    // fill
//    array.forEachIndexed { i, it ->
//        it.indices.forEach { j ->
//            it[j] = Square( SquareType.EMPTY, SquareStatus.UNKNOWN )
//        }
//    }
    //board.forEach { println(it.asList()) }


