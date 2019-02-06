package org.cliff.kotsweeper

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView

/**
 * controller class for the Smiley Face Button on the UI
 * User: Cliff
 * Date: 6/20/2017
 * Time: 10:38 AM
 */
class SmileyBtnCtrl(val ksweeper:KSweeper ) {

    val btn:Button = build()

    /**
     * builds the Smiley Face Button
     */
    fun build() : Button {
        val imageHappy = Image("${KSweeper.IMAGES_PATH}/happySmiley.png")

        //val imageHappy = Image( KSweeper::class.java.getResourceAsStream( "${KSweeper.IMAGES_PATH}/happySmiley.png" ))
        val btn = Button()
        with (btn) {
            tooltip = Tooltip( KSweeper.SMILEY_TT_TEXT )
            graphic = ImageView( imageHappy )
            id = KSweeper.SMILEY_BUTTON_ID
            addEventHandler( ActionEvent.ACTION, smileyClicked() )
        }
        return btn
    }


    /**
     * set the image displayed on this button
     */
    fun setImage( path:String ) {
        val image = Image( path )
        //val image = Image( KSweeper::class.java.getResourceAsStream( path ))
        btn.graphic = ImageView( image )
    }


    private fun smileyClicked() : EventHandler<ActionEvent> = EventHandler { ev ->
        //start a new game when the smiley button is clicked
        ksweeper.resetGame()
    }
}