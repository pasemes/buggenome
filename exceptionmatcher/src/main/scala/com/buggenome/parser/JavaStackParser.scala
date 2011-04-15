package com.buggenome.parser

import com.weiglewilczek.slf4s.Logging
import com.buggenome.stacktrace.frame.{StackTraceTopLine, StackTraceMethodInvocationLine, StackTraceFramesInCommon}
import com.buggenome.stacktrace.StackTrace

/**
 * A stack trace parser for the java language compiler.
 * @author Paulo Sérgio
 * @version 1.0, 23/01/11
 */
class JavaStackParser extends Logging {

    /**
     * Parses a stack trace.
     * @param receives an string array where each element is a line from the stack.
     * @return a StackTrace with all parsed lines in it
     */
    def parse(rawStack : Array[String]) : StackTrace = {
        return parse(rawStack.clone, null)
    }

    /**
     * Recursive function. It calls itself when it finds an Caused by top line in order to parse the chained stack.
     */
    private def parse(stackToProcess : Array[String], chainedStack : StackTrace) : StackTrace = {

        logger debug "Beginning to process a new stack trace ####################################"
        val stackTrace = new StackTrace

        stackToProcess.foreach( stackLine => {

            var slimmedStackLine = stackLine
            //removing some possible noise from the line
            slimmedStackLine.trim() // first removing trailing spaces
            slimmedStackLine = slimmedStackLine.filter(c => c != '\t')  // and then removing tab characters

            logger debug "Beginning to parse a stack trace line -----------------------------------"
            logger debug "Parsing line: " + slimmedStackLine

            slimmedStackLine match {
                //we ordered the case statements according to the specificity of each
                //for instance, the first case is the FramesInCommon which always begins with three dots '...'
                case StackTraceFramesInCommon(numberFramesInCommon) =>
                    logger debug "Identified a frames in common line: " + slimmedStackLine
                    stackTrace.addFrame(new StackTraceFramesInCommon(numberFramesInCommon.toInt, chainedStack))

                case StackTraceMethodInvocationLine(clazz, method, file, language, line) =>
                    logger debug "Identified a stack method invocation line: " + slimmedStackLine
                    stackTrace.addFrame(new StackTraceMethodInvocationLine(clazz, method, file, language, line))

                case StackTraceTopLine(chained, exception, message) =>
                    logger debug "Identified a stack top line: " + slimmedStackLine
                    val lineIndex = stackToProcess.indexOf(stackLine)
                    if(lineIndex == 0) {//if index is 0, then we are beginning to process the stack, and so we add the line to the current stack
                        stackTrace.addFrame(new StackTraceTopLine(chained, exception, message))
                        if(chained) {//as we are beginning to process a chained stack (index = 0 and chained = true), we have to link it with the previous stack (chainedStack)
                            stackTrace.causedBy(chainedStack)
                            chainedStack.resultedIn(stackTrace)
                        }
                    } else if(chained) { //if the index is not zero, then we have to process a new stack trace which is the exception source
                        parse(stackToProcess.drop(lineIndex), stackTrace) //the drop method eliminates the already processed lines from the stack
                        return stackTrace
                    } else {//we have a problem if the index is not 0 and the line is not chained, see the exception message for details
                        val e = new InvalidStackTopLineException(stackLine)
                        logger error e.getMessage()
                        throw e
                    }

                case _ =>
                    val e = new StackLineFormatException(stackLine)
                    logger error e.getMessage()
                    throw e
            }
        })
        return stackTrace
    }
}

class StackLineFormatException(invalidStackLine : String) extends Exception(invalidStackLine) {
    override def getMessage() : String = {
        "Incapable of parsing the following stack trace line: " + super.getMessage() +
                ". Check for invalid characters or other 'noise' that could have caused this."
    }
}

class InvalidStackTopLineException(invalidStackLine : String) extends Exception(invalidStackLine) {
    override def getMessage() : String = {
        "The stack parser identified an stack top line which is not the first line of an stack and is not chained. " +
                "The invalid line is: " + super.getMessage()
    }
}

