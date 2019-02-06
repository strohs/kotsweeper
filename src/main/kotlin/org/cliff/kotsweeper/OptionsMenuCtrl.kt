package org.cliff.kotsweeper

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.input.MouseEvent

/**
 * Builds the games "Options" menu and all its submenus and custom sliders
 * User: Cliff
 * Date: 6/21/2017
 * Time: 2:08 PM
 */
class OptionsMenuCtrl( val kSweeper: KSweeper ) {

    companion object Constants {
        //these are the defaults beings used for the row/column sliders on the options menu
        val MAJOR_TICK_UNITS    = 5.0
        val MINOR_TICK_COUNT    = 1
        val BLOCK_INCREMENT     = 1.0
    }

    val optionMenu: Menu = build()

    fun build() : Menu {
        //build the new game menu item
        val newGameMenu = buildNewGameMenuItem()

        val optionMenu:Menu = Menu("Options")
        val rowSizeMenu = buildRowSizeMenu()
        val colSizeMenu = buildColSizeMenu()
        optionMenu.items.add( newGameMenu )
        optionMenu.items.add( SeparatorMenuItem() )
        optionMenu.items.add( rowSizeMenu )
        optionMenu.items.add( colSizeMenu )
        return optionMenu
    }

    private fun buildNewGameMenuItem() : MenuItem {
        val newGameMenu = MenuItem( "New Game" )
        newGameMenu.onAction = EventHandler { event: ActionEvent ->
            kSweeper.resetGame()
        }
        return newGameMenu
    }

    /**
     * build a generic slider that can be used to create both a row size slider or a column size slider
     */
    private fun buildSlider( id:String, minVal:Int, curVal:Int, maxVal:Int ) : Slider {
        val slider: Slider = Slider()
        val tooltip: Tooltip = Tooltip()
        with (slider) {
            slider.id = id
            min = minVal.toDouble()
            max = maxVal.toDouble()
            value = curVal.toDouble()
            isShowTickLabels = true
            isShowTickMarks = true
            majorTickUnit = MAJOR_TICK_UNITS
            minorTickCount = MINOR_TICK_COUNT
            blockIncrement = BLOCK_INCREMENT
        }
        slider.onMouseDragged = EventHandler { event: MouseEvent ->
            tooltip.text = slider.value.toInt().toString()
        }
        slider.tooltip = tooltip
        return slider
    }


    /**
     * builds the menu that lets the user set the game's row size
     */
    private fun buildRowSizeMenu() : Menu {
        val rowSlider = buildSlider(KSweeper.ROW_SLIDER_ID, Model.MIN_ROWS, kSweeper.model.rows, Model.MAX_ROWS )
        rowSlider.onMouseReleased = EventHandler { event: MouseEvent ->
            val rowSize = rowSlider.value.toInt()
            kSweeper.model.rows = rowSize
        }
        //add the slider grid to a CustomMenuItem
        val slidersMenu: CustomMenuItem = CustomMenuItem( rowSlider )
        slidersMenu.isHideOnClick = false

        val menu:Menu = Menu("Row Size")
        menu.items.add( slidersMenu )

        return menu
    }

    /**
     * builds the menu that lets the user set the game's column size
     */
    private fun buildColSizeMenu() : Menu {
        val colSlider = buildSlider(KSweeper.COL_SLIDER_ID, Model.MIN_COLS, kSweeper.model.cols,Model.MAX_COLS )
        colSlider.onMouseReleased = EventHandler { event: MouseEvent ->
            val colSize = colSlider.value.toInt()
            kSweeper.model.cols = colSize
        }

        //add the slider grid to a CustomMenuItem
        val slidersMenu: CustomMenuItem = CustomMenuItem( colSlider )
        slidersMenu.isHideOnClick = false

        val menu:Menu = Menu("Col Size")
        menu.items.add( slidersMenu )

        return menu
    }
}