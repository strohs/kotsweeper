package org.cliff.kotsweeper

import org.junit.jupiter.api.Assertions.*
import kotlin.IllegalArgumentException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test


/**
 * Created with IntelliJ IDEA.
 * User: Cliff
 * Date: 6/22/2017
 * Time: 1:12 PM
 */
internal class BoardModelTest {
    @Test
    fun rows() {
        assertEquals( 3, BoardModel.rows(Boards.empty3x3), "3 rows" )
        assertEquals( 2, BoardModel.rows(Boards.empty2x2), "2 rows" )
        assertEquals( 0, BoardModel.rows(Boards.emptyList), "0 rows")
    }

    @Test
    fun cols() {
        assertEquals( 3, BoardModel.rows(Boards.empty3x3), "3 cols" )
        assertEquals( 2, BoardModel.rows(Boards.empty2x2), "2 cols" )
        assertEquals( 0, BoardModel.rows(Boards.emptyList), "0 cols")
    }


    @Test
    fun idxTo2d() {

        assertEquals( Pair(0,0), BoardModel.idxTo2d(0, 2, 3), "index 0 to 0,0" )
        assertEquals( Pair(0,1), BoardModel.idxTo2d(1, 2, 3), "index 1 to 0,1" )
        assertEquals( Pair(0,2), BoardModel.idxTo2d(2, 2, 3), "index 2 to 0,2" )
        assertEquals( Pair(1,0), BoardModel.idxTo2d(3, 2, 3), "index 3 to 1,0" )
        assertEquals( Pair(1,1), BoardModel.idxTo2d(4, 2, 3), "index 4 to 1,1" )
        assertEquals( Pair(1,2), BoardModel.idxTo2d(5, 2, 3), "index 5 to 1,2" )
        assertThrows( IllegalArgumentException::class.java) { BoardModel.idxTo2d(2, 0, 1) }
        assertThrows( IllegalArgumentException::class.java) { BoardModel.idxTo2d(2, 2, 0) }
    }

    @Test
    fun emptyBoard() {
        assertEquals(Boards.empty3x3, BoardModel.emptyBoard(3, 3))
    }

    @Test
    fun newBoard() {
        val nb = BoardModel.newBoard(3, 3)
    }

    @Test
    fun bNoNeigboringMines() {
        assertTrue(BoardModel.bNoNeigboringMines(1, 0, Boards.mined_1_2_3x3))
        assertFalse(BoardModel.bNoNeigboringMines(1, 1, Boards.mined_1_2_3x3))
    }

    @Test
    fun connectedEmptySquares() {
        val connected = listOf( Pair(0,0), Pair(1,0), Pair(2,0), Pair(0,1), Pair(1,1), Pair(2,1) )
        assertEquals( connected, BoardModel.connectedEmptySquares(0, 0, Boards.mined_1_2_3x3))
        val noConnections = emptyList<Square>()
        assertEquals( noConnections, BoardModel.connectedEmptySquares(0, 1, Boards.mined_1_2_3x3))
    }

    @Test
    fun totalMines() {
        assertEquals( 0, BoardModel.totalMines(1, 1), "one square board" )
        assertEquals( 1, BoardModel.totalMines(2, 2), "2x2 board should have 1 mine" )
        assertEquals( 1, BoardModel.totalMines(2, 3), "2x3 board should have 1 mine" )
        assertEquals( 1, BoardModel.totalMines(2, 4), "2x4 board should have 1 mine" )
        assertEquals( 1, BoardModel.totalMines(3, 3), "3x3 board should have 1 mine" )
        assertEquals( 94, BoardModel.totalMines(25, 25), "25x25 board should have 94 mine" )
    }

    @Test
    fun reveal() {
    }

    @Test
    fun revealBoard() {
    }

    @Test
    fun toggleMark() {
    }

    @Test
    fun correctlyMarkedIndices() {
    }

    @Test
    fun checkForWin() {
    }

    @Test
    fun getMinedIndices() {
    }

    @Test
    fun unmarkedMineCount() {
    }

}