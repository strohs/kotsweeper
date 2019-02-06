package org.cliff.kotsweeper

import java.util.*

/**
 * Pure functions used to compute the next state of the game board. Most of these functions take the current boardState
 *  as input and produce a new BoardState as output.
 * User: Cliff
 * Date: 6/14/2017
 * Time: 10:52 AM
 */
object BoardModel {

    /* returns the number of rows in a 2d list */
    fun <T> rows(list: List<List<T>>) = list.count()

    /* returns the number of columns in a 2d list */
    fun <T> cols(list: List<List<T>>) = list[0].count()

    /* convert 2d row/col indices to a 1d index, row major order */
    fun <T> toIdx1d(r: Int, c: Int, list: List<List<T>>): Int = (rows(list) * r) + c

    /* convert a 1d index into a 2d row/column indices pair */
    fun idxTo2d(idx: Int, rowSize: Int, colSize: Int): Pair<Int, Int> {
        if (rowSize == 0 || colSize == 0)
            throw IllegalArgumentException("empty list was passed converting idx=$idx to 2d row/col")
        return Pair(idx / colSize, idx - ((idx / colSize) * colSize))
    }

    /**
     * return a mutable list of empty squares in row major order
     */
    fun emptyBoard( rows: Int = Model.MIN_ROWS, cols: Int = Model.MIN_COLS ): MutableList<MutableList<Square>> {
        val board: MutableList<MutableList<Square>> = MutableList(rows, { _ ->
            MutableList(cols, { _ -> Square() })
        })
        return board
    }

    /**
     * returns a brand new board with the specified dimensions
     */
    fun newBoard(rows: Int, cols: Int): List<List<Square>> {
        val board = emptyBoard(rows, cols)
        val mineIndices = genMineLocations(board)

        //set random mine locations
        for ((row, col) in mineIndices) {
            board[row][col].type = SquareType.MINE
            board[row][col].adjCount = 0
        }

        //generate adjacency counts for squares that neighbor mines
        board.forEachIndexed { ri, rowList ->
            rowList.forEachIndexed { ci, square -> square.adjCount = adjMineCount(ri, ci, board) }
        }

        return board
    }

    /**
     * Check if a square has no neighboring mines
     * returns true if a square is empty AND its adjacent mine counter is zero
     */
    fun bNoNeigboringMines(row: Int, col: Int, board: List<List<Square>>): Boolean =
            board[row][col].type == SquareType.EMPTY && board[row][col].adjCount == 0

    /**
     * return a list of squares that are connected to the square at 'row' 'col'
     */
    fun connectedEmptySquares(row: Int, col: Int, board: List<List<Square>>): List<Pair<Int, Int>> {
        //contains all empty squares connected to our starting square
        val visited: MutableList<Pair<Int, Int>> = mutableListOf()

        if (bNoNeigboringMines(row, col, board)) {
            visited.add(Pair(row, col))
            //get adjacent empty squares
            val adjacents = adjIndices(row, col, board).filter { (ri, ci) -> bNoNeigboringMines(ri, ci, board) }.toMutableList()

            while (adjacents.isNotEmpty()) {
                //add all adjacent empty cells to the visited list that have not already been visited
                visited.addAll(adjacents.filterNot { visited.contains(it) })
                //remove the first index Pair from the adjacents list
                val (frIdx, fcIdx) = adjacents.removeAt(0)
                //use this pair to get a list of its adjacent,empty squares
                val nextAdjacents = adjIndices(frIdx, fcIdx, board)
                        .filter { (ri, ci) -> bNoNeigboringMines(ri, ci, board) }
                //append the new adjacent squares to our adjacent list, but only if they haven't already been visited
                adjacents.addAll(nextAdjacents.filterNot { visited.contains(it) })
            }
            //additionally add the squares that are on the perimeter of the squares that have been visited
            val squaresWithCounts: List<Pair<Int, Int>> = visited.flatMap { (r, c) -> adjIndices(r, c, board) }
            visited.addAll(squaresWithCounts)
        }
        return visited.distinct()
    }


    /**
     * returns the total amount of mines that a game board should contain.
     */
    fun totalMines(rows: Int, cols: Int): Int = Math.round(rows * cols * 0.15).toInt()




    /**
     * reveal a square on the board. This function will cause connected squares to be revealed as well.
     * @param row the row index of the board square to reveal
     * @param col the column index of the board square to reveal
     * @param board the game board
     * @return a new copy of the board with one or more squares revealed
     */
    fun reveal(row: Int, col: Int, board: List<List<Square>>): List<List<Square>> {
        //create a copy of board
        val newBoard = copyBoard(board)
        newBoard[row][col].status = SquareStatus.REVEALED

        //no need to compute connected Squares if the revealed square was a mine
        if (newBoard[row][col].type != SquareType.MINE) {
            //get a list of squares "connected" to the revealed square and mark them as revealed
            val connectedEmpties: List<Pair<Int, Int>> = connectedEmptySquares(row, col, newBoard)
            connectedEmpties.forEach { (r, c) -> newBoard[r][c].status = SquareStatus.REVEALED }
        }

        return newBoard.toList()
    }

    fun revealBoard(board: List<List<Square>>): List<List<Square>> {
        //create a copy of board
        val newBoard = copyBoard(board)
        newBoard.forEach { rowList ->
            rowList.forEach { square ->
                //only reveal squares that are not marked
                if (square.status == SquareStatus.MARKED && square.type == SquareType.EMPTY) {
                    square.status = SquareStatus.MISMARKED
                } else if (square.type == SquareType.MINE)
                    square.status = SquareStatus.REVEALED

            }
        }
        return newBoard.toList()
    }

    /**
     * marks a square on the board as a possible mine location, but does not reveal the square. A player can only
     * toggleMark squares that have not already been revealed. If a square has already been marked, then trying to toggleMark it
     * again will remove the toggleMark.
     */
    fun toggleMark(r: Int, c: Int, board: List<List<Square>>): List<List<Square>> {
        val nb = copyBoard(board)
        if (nb[r][c].status != SquareStatus.REVEALED) {
            when (nb[r][c].status) {
                SquareStatus.UNKNOWN -> nb[r][c].status = SquareStatus.QUESTION
                SquareStatus.QUESTION -> nb[r][c].status = SquareStatus.MARKED
                SquareStatus.MARKED -> nb[r][c].status = SquareStatus.UNKNOWN
                else -> System.err.println("DEBUG: unkown board status: ${nb[r][c].status} while toggling a mark")
            }
        }
        return nb
    }


    /**
     * return a list of row/col Pairs indicating squares that are correctly marked
     */
    fun correctlyMarkedIndices( board:List<List<Square>> ) : List<Pair<Int,Int>> {
        val indices = mutableListOf<Pair<Int, Int>>()
        board.forEachIndexed { ridx, rowList ->
            rowList.forEachIndexed { cidx, sq ->
                if (sq.status == SquareStatus.MARKED && sq.type == SquareType.MINE)
                    indices.add(Pair(ridx, cidx))
            }
        }
        return indices
    }

    /**
     * check if the player has won. A player wins if all mined squares have been correctly marked
     */
    fun checkForWin(board: List<List<Square>>): Boolean {

        val minesMarked = board.fold(0) { total, rowList ->
            total + rowList.filter { s -> s.status == SquareStatus.MARKED && s.type == SquareType.MINE }.count()
        }

        //return true if the number of non-mined squares == revealed squares and mined squares are marked
        return minesMarked == totalMines(rows(board), cols(board))
    }

    /**
     * returns a list of indices pairs, each pair is a row/col index of a mine
     */
    fun getMinedIndices(board: List<List<Square>>): List<Pair<Int, Int>> {
        val indices = mutableListOf<Pair<Int, Int>>()
        board.forEachIndexed { row, rowList ->
            rowList.forEachIndexed { col, square ->
                if (square.type == SquareType.MINE) indices.add(Pair(row, col))
            }
        }

        return indices
    }

    /**
     * returns the difference between the total number of mines on the board - total number of marked squares
     */
    fun unmarkedMineCount( board:List<List<Square>>) : Int {
        fun markedSquareCount( list:List<Square> ) : Int = list.filter { square -> square.status == SquareStatus.MARKED }.count()
        val currentlyMarked = board.fold( 0, { totalCount, rowList -> totalCount + markedSquareCount( rowList )})
        return totalMines( rows(board), cols(board) ) - currentlyMarked
    }


    /**
     * generate random indices to place mines at.
     * returns: a list of Pair<Int,Int> containing the row/col indices to place mines at
     */
    private fun <T> genMineLocations(list: List<List<T>>): List<Pair<Int, Int>> {

        val rows = rows(list)
        val cols = cols(list)
        //generate a list of indices and then shuffle them
        val indices: List<Int> = (0 until (rows * cols)).map { it }
        Collections.shuffle(indices)

        val mineIndices: List<Pair<Int, Int>> =
                indices.take(totalMines(rows(list), cols(list)))
                        .map { idx -> idxTo2d(idx, rows, cols) }

        //take 'TOTAL_MINES' amount of indices from the shuffled list and use them as mine locations
        return mineIndices
    }

    /**
     * return a List of indices Pairs that are adjacent to the square at (r,c). Only indices that are within bounds
     * of the board are returned. The square at r,c is NOT returned in the List of pairs.
     * @param r row index of the square
     * @param c column index of the square
     * @param board 2d model of the board
     */
    private fun <T> adjIndices(r: Int, c: Int, board: List<List<T>>): List<Pair<Int, Int>> {
        val rowRanges = (r - 1)..(r + 1)
        val colRanges = (c - 1)..(c + 1)
        val indices = rowRanges.flatMap {
            ri ->
            colRanges.map { ci -> Pair(ri, ci) }
        }.filterNot { (ri, ci) -> ri < 0 || ri >= rows(board) || ci < 0 || ci >= cols(board) || (ri == r && ci == c) }
        return indices
    }

    /**
     * number of mines adjacent to the square at r,c. The square at r,c is not included in the calculation
     * return: total number of mines adjacent to the square ar r,c
     */
    private fun adjMineCount(row: Int, col: Int, board: List<List<Square>>): Int {
        //filter and return the count of mine squares that are adjacent to our target index (idx)
        return adjIndices(row, col, board)
                .filter { (ridx, cidx) -> board[ridx][cidx].type == SquareType.MINE }
                .count()
    }
    
    /**
     * returns a deep copy of board. Brute Force copy, not very efficient.
     * @param board the board to copy
     */
    private fun copyBoard(board: List<List<Square>>): MutableList<MutableList<Square>> {
        val newBoard: MutableList<MutableList<Square>> = emptyBoard(rows(board), cols(board))

        board.forEachIndexed { ri, rowList ->
            rowList.forEachIndexed { ci, _ ->
                newBoard[ri][ci] = board[ri][ci].copy()
            }
        }
        return newBoard
    }

}