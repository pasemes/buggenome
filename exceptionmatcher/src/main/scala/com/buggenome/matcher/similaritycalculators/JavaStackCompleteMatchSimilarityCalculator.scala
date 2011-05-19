package com.buggenome.matcher.similaritycalculators

import com.buggenome.stacktrace.StackTrace
import com.buggenome.stacktrace.frame.{StackTraceTopLine, StackTraceMethodInvocationLine}

/**
 * Implements a similarity calculator which checks all lines for a "complete match". Since it is either a complete match
 *      or no match it returns 0 or 1 in each of its comparison methods.
 * User: pasemes
 * Date: 14/05/11
 * Time: 15:06
 */
//sempre retorna 1 ou 0
class JavaStackCompleteMatchSimilarityCalculator extends JavaStackSimilarityCalculator {

    protected def fullSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        if (stackTrace == otherStackTrace) 1
        else 0
    }

    protected def ignoringLineSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        val stackLines = stackTrace.getAllLines
        val otherStackLines = otherStackTrace.getAllLines

        if(stackLines.length != otherStackLines.length) return 0 //if the size is different then it will not be possible to have a complete match (return 0)

        for(i <- 0 to stackLines.length-1) {
            (stackLines(i), otherStackLines(i)) match {
                case (stackMethodLine: StackTraceMethodInvocationLine, otherStackMethodLine: StackTraceMethodInvocationLine) =>
                        if (!stackMethodLine.equalIgnoringLineNumber(otherStackMethodLine)) return 0
                case (stackTopLine: StackTraceTopLine, otherStackTopLine: StackTraceTopLine) =>
                        if (stackTopLine != otherStackTopLine) return 0
                case _ => return 0 //in case the tuple elements have different types, means that they are different, so we don't have a complete match (return 0)
            }
        }
        return 1 //since all lines are equal (ignoring the line number for the stack trace method lines) we return 1
    }

    protected def wildcardSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        0.0  //TODO implement
    }
}