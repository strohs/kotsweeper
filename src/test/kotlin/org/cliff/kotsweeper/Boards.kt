package org.cliff.kotsweeper

/**
 * Created with IntelliJ IDEA.
 * User: Cliff
 * Date: 6/22/2017
 * Time: 1:24 PM
 */
object Boards {

    val mine : Square = Square(SquareType.MINE)

    //generate an empty and unknown Square Type with the specified adjacency count
    fun eadj( n:Int ) = Square(SquareType.EMPTY, SquareStatus.UNKNOWN, n)

    //unknown square types with adjacency counts from 1 to 8
    val uadj1 : Square = Square(SquareType.EMPTY, SquareStatus.UNKNOWN, 1)
    val uadj2 : Square = Square(SquareType.EMPTY, SquareStatus.UNKNOWN, 2)
    val uadj3 : Square = Square(SquareType.EMPTY, SquareStatus.UNKNOWN, 3)
    val uadj4 : Square = Square(SquareType.EMPTY, SquareStatus.UNKNOWN, 4)
    val uadj5 : Square = Square(SquareType.EMPTY, SquareStatus.UNKNOWN, 5)
    val uadj6 : Square = Square(SquareType.EMPTY, SquareStatus.UNKNOWN, 6)
    val uadj7 : Square = Square(SquareType.EMPTY, SquareStatus.UNKNOWN, 7)
    val uadj8 : Square = Square(SquareType.EMPTY, SquareStatus.UNKNOWN, 8)


    val emptyList = listOf<List<Square>>()

    val empty2x2 = listOf(
            listOf(Square(), Square()),
            listOf(Square(), Square()))

    val empty3x3 = listOf(
            listOf(Square(), Square(), Square()),
            listOf(Square(), Square(), Square()),
            listOf(Square(), Square(), Square()))

    val mined_1_1_3x3 = listOf(
            listOf(uadj1, uadj1, uadj1),
            listOf(uadj1, mine, uadj1),
            listOf(uadj1, uadj1, uadj1))

    val mined_1_2_3x3 = listOf(
            listOf(eadj(0), eadj(1), eadj(1)),
            listOf(eadj(0), eadj(1), mine),
            listOf(eadj(0), eadj(1), eadj(1)))




}