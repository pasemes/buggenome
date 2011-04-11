package com.buggenome.stacktrace

import frame.{StackTraceTopLine, StackTraceFramesInCommon, StackTraceLine}
import collection.mutable.{ArrayBuffer, LinkedList, Buffer, ListBuffer}
import collection.mutable.ArrayBuffer._

/*
* Represents an java stack trace with its elements (lines).
* User: Paulo Sérgio
* Date: 13/02/11
* Time: 13:40
*/
class StackTrace {

    /**
     * Keeps all lines of this StackTrace. The first element (index 0) is the StackTraceTopLine.
     */
    private val stackLines = new ArrayBuffer[StackTraceLine]()
    private var framesInCommon : StackTraceFramesInCommon = null //we keep the instance in order to know if this StackTrace has frames in common with another

    //the two fields bellow corresponds to the "previous" stack (causedBy) and to the "next" stack (resultedInStack)
    //forming a dobly-linked list
    private var causedByStack : StackTrace = null
    private var resultedInStack : StackTrace = null


    /**
     * Adds the frames in common to the end of this StackTrace.
     * @param stackFrame an instance of StackTraceFramesInCommon which has the frames in common
     */
    def addFrame(stackFrame : StackTraceFramesInCommon) {
        if(stackLines.isEmpty && !stackFrame.isInstanceOf[StackTraceTopLine])
            throw new IllegalArgumentException("The stack line cannot be null")
        stackLines ++= stackFrame.getFramesInCommon
        framesInCommon = stackFrame
    }

    /**
     * Adds a stack frame to the end of this StackTrace
     */
    def addFrame(stackLine : StackTraceLine) {
        if(stackLines.isEmpty && !stackLine.isInstanceOf[StackTraceTopLine])
            throw new IllegalArgumentException("The stack line cannot be null")
        if(stackLines.isEmpty && !stackLine.isInstanceOf[StackTraceTopLine])
            throw new IllegalArgumentException("The first line must be of type StackTraceTopLine")
        if(!stackLines.isEmpty && stackLine.isInstanceOf[StackTraceTopLine])
            throw new IllegalArgumentException("Only the first line can be of type StackTraceTopLine, if you have a chained StackTraceTopLine use causedBy method instead")
        stackLines += stackLine
    }

    /**
     * Get a stack frame from this StackTrace. The zero index is the top line.
     * @return a StackTraceFrame at the index position
     * @throws IndexOutOfBoundsException when the
     */
    def getLine(index : Int) : StackTraceLine = {
        if(index > this.sizeAllLines - 1) {
            throw new IndexOutOfBoundsException()
        }
        getAllLines(index)
    }

    /**
     * Retrieves the StackTraceTopLine from this stack.
     */
    def getTopLine :StackTraceTopLine = getLine(0).asInstanceOf[StackTraceTopLine]

    /**
     * Retrivies the stack lines from this stack only excluding the lines which are from an eventual chained stack.
     */
    def getLinesNotChained : Seq[StackTraceLine] = stackLines

    /**
     * Retrieves all lines from this StackTrace, including the chained stacks.
     */
    def getAllLines : Seq[StackTraceLine] = {
        getResultingStack match {
            case Some(stackTrace) => stackLines ++ stackTrace.getAllLines
            case None => stackLines
        }
    }

    /**
     * Removes the last n lines of this StackTrace
     */
    def removeLastLines(n : Int) { stackLines.trimEnd(n) }

    /**
     * Returns the full StackTrace size, including the chained stacks linked to this StackTrace.
     */
    def sizeAllLines : Int = getAllLines.size

    /**
    * Retrivies the stack lines from this stack only excluding the lines which are from an eventual chained stack.
    */
    def sizeNotChained : Int = stackLines.size

    /**
     * Defines a StackTrace which made this StackTrace occur.
     * @param stackTrace the source of this StackTrace
     */
    def causedBy(stackTrace : StackTrace) {
        causedByStack = stackTrace
    }

    /**
     * Defines the StackTrace resulted from this StackTrace
     */
    def resultedIn(stackTrace : StackTrace) {
        resultedInStack = stackTrace
    }

    /**
     * Get the cause of this StackTrace.
     * @return the cause stack or None if this StackTrace is not chained or is the first StackTrace in the chained stacks
     */
    def getCauseStack : Option[StackTrace] = if(this.wasCausedBy) new Some(causedByStack) else None

    /**
     * Get the stack resulted (caused) from this StackTrace.
     * @return the resulted stack or None if this StackTrace is not chained or is the last StackTrace in the chained stacks
     */
    def getResultingStack : Option[StackTrace] = if(this.hasResultedIn) new Some(resultedInStack) else None

    /**
     * Verifies if this stack was caused by another.
     */
    def wasCausedBy : Boolean = causedByStack != null

    /**
     * Verifies if this stack caused another.
     */
    def hasResultedIn : Boolean = resultedInStack != null

    /**
     * Verifies if this StackTrace has stack frames in common with another StackTrace.
     * @return true if this StackTrace has frames in common with another or false otherwise.
     */
    def hasFramesInCommon : Boolean = framesInCommon != null

    /**
     * Return the number of common frames of this stack with the previous (caused by) stack
     */
    def getNumberOfCommonFrames : Int = if(hasFramesInCommon) framesInCommon.getNumberOfFramesInCommon else 0

    /**
     * Returns a string representation of this stack trace.
     */
    override def toString : String = {
        //TODO implementar essa bagaca
        ""
    }

    /**
     * Two StackTraces are equals when they have the same size and its lines are equals.
     */
    override def equals(obj : Any) : Boolean = {  //TODO testar equals
        if(this eq obj.asInstanceOf[AnyRef]) return true

        if(!obj.isInstanceOf[StackTrace]) return false

        val stackTrace = obj.asInstanceOf[StackTrace]

        if(this.sizeAllLines != stackTrace.sizeAllLines) return false

        for(i <- 0 to this.sizeAllLines) {
            if(this.getLine(i) != (stackTrace.getLine(i))) return false
        }
        return true
    }
}