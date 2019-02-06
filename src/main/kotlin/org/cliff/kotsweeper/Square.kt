package org.cliff.kotsweeper

/**
 * data class representing the current state of a square on the game board
 * User: Cliff
 * Date: 5/30/2017
 * Time: 12:43 PM
 */
data class Square(var type:SquareType = SquareType.EMPTY,
                  var status: SquareStatus = SquareStatus.UNKNOWN,
                  var adjCount:Int = 0 ) {


}