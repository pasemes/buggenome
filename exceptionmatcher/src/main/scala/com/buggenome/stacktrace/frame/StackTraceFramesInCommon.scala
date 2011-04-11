/*
 * Created by IntelliJ IDEA.
 * User: Paulo Sérgio
 * Date: 27/02/11
 * Time: 23:05
 */
package com.buggenome.stacktrace.frame

import util.matching.Regex
import com.buggenome.stacktrace.StackTrace


/**
 * Represents a set of frames in common. Requires that the number of frames in common be higher than the inCommonStack sizeAllLines.
 * @param inCommonStack an instance of StackTrace which has the frames in common.
 * @param stackFrame an instance of StackTraceFramesInCommon giving the number of the frames in common.
 */
class StackTraceFramesInCommon(numberFramesInCommon : Int, inCommonStack : StackTrace) extends StackTraceFrame {

    require(numberFramesInCommon != null, "The number of frames in common cannot be null")

    require(inCommonStack != null, "The in common stack cannot be null")

    require(numberFramesInCommon <= inCommonStack.sizeAllLines - 1,
            "The frames in common number must be lower than the inCommonStack size, considering that the first line (StackTopLine) cannot be common")

    /**
     * Returns the frames in common
     * @return a Seq of StackTraceLines
     */
    def getFramesInCommon() : Seq[StackTraceLine] = {
        inCommonStack.getLinesNotChained.takeRight(numberFramesInCommon)
    }

    def getNumberOfFramesInCommon : Int = numberFramesInCommon

    /**
     *  Returns a string representation of this stack trace element.
     */
    override def toString() : String = {
        return "... " + numberFramesInCommon + " more"
    }
}

/**
 * Regex for the frames in common line (which occurs in chained exceptions)
 * It has the format: ( [...] [FRAMES] [more] )
 */
object StackTraceFramesInCommon extends Regex(regex = """\.\.\.\s([\d]+)\smore""")