package org.cliff.kotsweeper

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.TextField
import org.cliff.kotsweeper.KSweeper.Companion.TIMER_ID
import java.time.Duration
import java.time.Instant

/**
 * Controller class for the elapsed time TextField
 * User: Cliff
 * Date: 6/21/2017
 * Time: 12:59 PM
 */
class TimerCtrl( val kSweeper: KSweeper ) {

    //how often the text field will update itself with the new elapsed time
    val UPDATE_INTERVAL_SECONDS = 1.0
    
    val timer: TextField = build()

    lateinit private var stopwatch: StopWatch

    /**
     * builds the Timer TextField
     */
    fun build() : TextField {
        val timer = TextField()
        with ( timer ) {
            timer.id = TIMER_ID
            timer.isEditable = false
            timer.styleClass.add("info-text-field")
        }
        stopwatch = buildStopWatch()

        return timer
    }

    private fun buildStopWatch() : StopWatch {
        return StopWatch(UPDATE_INTERVAL_SECONDS, EventHandler { event: ActionEvent ->
            val elapsed = Duration.between( stopwatch.startTime, Instant.now() ).seconds
            timer.text = elapsed.toString()
        })
    }

    /**
     * resets the elapsed time on the timer
     */
    fun resetTimer() {
        stopwatch.stop()
        stopwatch = buildStopWatch()
    }

    fun stopTimer() {
        stopwatch.stop()
    }
}