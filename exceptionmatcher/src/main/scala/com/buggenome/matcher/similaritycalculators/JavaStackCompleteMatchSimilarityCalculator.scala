package com.buggenome.matcher.similaritycalculators

import com.buggenome.stacktrace.StackTrace

/**
 * Created by IntelliJ IDEA.
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
        val stackLines = stackTrace.getAllLines.toArray
        val otherStackLines = otherStackTrace.getAllLines

        if(stackLines.length != otherStackLines.length) return 0 //if the size is zero then it will not be possible to have a complete match

        for(i <- 0 to stackLines.length-1) {

            stackLines(i).equalIgnoringLine()




        }

        0.0
    }

    protected def wildcardSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        println("JavaStackSimilarityCalculator->wildcardSimilarityComparison")
        0.0  //TODO implement
    }

    private def blah  {
//        if(this eq obj.asInstanceOf[AnyRef]) return true
//
//        if(!obj.isInstanceOf[StackTrace]) return false
//
//        val stackTrace = obj.asInstanceOf[StackTrace]
//
//        if(this.sizeAllLines != stackTrace.sizeAllLines) return false
//
//        for(i <- 0 to this.sizeAllLines - 1) {
//            if(this.getLine(i) != (stackTrace.getLine(i))) return false
//        }
//        return true
    }

}