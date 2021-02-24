package org.cliff.kotsweeper

import javafx.scene.control.Tooltip
import javafx.scene.control.TextField

/**
 * Created with IntelliJ IDEA.
 * User: Cliff
 * Date: 6/21/2017
 * Time: 1:50 PM
 */
class RemainingMinesCtrl( val kSweeper: KSweeper ) {

    val textField: TextField = build()

    fun build() : TextField {
        val textField = TextField( BoardModel.totalMines( kSweeper.model.rows, kSweeper.model.cols).toString())

        with ( textField ) {
            id = KSweeper.REMAINING_MINES_ID
            isEditable = false
            styleClass.add("info-text-field")
            tooltip = Tooltip( KSweeper.REMAINING_TT_TEXT )
        }

        return textField
    }

    /**
     * update the controllers textField with new text
     */
    fun setText( newCount:String ) {
        textField.text = newCount
    }
}