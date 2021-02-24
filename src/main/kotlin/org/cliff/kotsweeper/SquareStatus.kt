package org.cliff.kotsweeper

/**
 * a list of the possible states of each square on the game board, as seen by the player
 * User: Cliff
 * Date: 5/30/2017
 * Time: 11:59 AM
 */
enum class SquareStatus {
    //a square that has not been visited by the user
    UNKNOWN,

    //a square that has been visited by the user and did not contain a mine
    REVEALED,

    //a square that has been marked by the user as a possible mine location, but not yet visited
    MARKED,

    //a square that might be a possible mine location, will show as a question mark '?' on the board
    QUESTION,

    //a square that was marked as a mine but was really empty
    MISMARKED
}