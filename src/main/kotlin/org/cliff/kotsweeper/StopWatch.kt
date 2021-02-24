package org.cliff.kotsweeper

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.util.Duration
//import java.time.Duration
import java.time.Instant

/**
 * performs some ActionEvent, at the specified updateInterval. Uses a JavaFX Timeline object to handle the event
 * on a separate javafx UI thread
 * User: Cliff
 * Date: 6/19/2017
 * Time: 2:21 PM
 */
class StopWatch( updateInterval:Double, handler:EventHandler<ActionEvent> ) {

    //holds the instant this StopWatch was instantiated
    val startTime = Instant.now()

    val stopwatch:Timeline = Timeline (
            KeyFrame( Duration.seconds( updateInterval ), handler ))
    init {
        stopwatch.cycleCount = Timeline.INDEFINITE
        stopwatch.play()
    }

    fun stop() {
        stopwatch.stop()
    }
}