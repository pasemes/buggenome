package com.buggenome.matcher

import com.buggenome.stacktrace.StackTrace
import collection.mutable.ListBuffer
import util.Sorting

/**
 * Created by IntelliJ IDEA.
 * User: pasemes
 * Date: 07/05/11
 * Time: 20:04
 */
class JavaStackMatcher {

    val similarityCalculators = JavaSimilarityCalculatorFactory.getSimilarityCalculatorsSortedByRelevance

    def computeSimilarStacks(stackTrace : StackTrace, stackTraces : List[StackTrace]) : List[StackTrace] = {
        val orderedSimilarStacks = new ListBuffer[StackTrace]()
        similarityCalculators.foreach{ similarityCalculator =>
            val mappedSimilarity = stackTraces.map{ //maps to a tuple (stackTrace, similarityValue)
                    similarStack => (similarStack, similarityCalculator.computeSimilarity(stackTrace, similarStack))}
            val orderedMappedSimilarity = mappedSimilarity.sortWith{ //sorts the stacks based on the similarityValue
                    (stackSimilarityTuple1, stackSimilarityTuple2) => stackSimilarityTuple1._2 < stackSimilarityTuple2._2}
            orderedSimilarStacks ++= orderedMappedSimilarity.filter(_._2 != 0).map(_._1) //removes the stacks which don't have any degree of similarity and maps it back to a list of stacks
        }
        return orderedSimilarStacks.toList
    }
}