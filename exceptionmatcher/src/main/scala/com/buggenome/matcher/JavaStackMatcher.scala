package com.buggenome.matcher

import com.buggenome.stacktrace.StackTrace
import collection.mutable.ListBuffer
import scala.Array

/**
 * Responsible for matching a stack trace against a set of stacks.
 * @param dependencies must contain an instantiated array with JavaStackSimilarityCalculators to be used by the matcher.<br/>
 *          The order of the calculators determine the order of the matching results. The first N matches will be<br/>
 *          based on the first calculator, the second M matches based on the second and so on ...
 * User: pasemes
 * Date: 07/05/11
 * Time: 20:04
 */
class JavaStackMatcher(val dependencies : { val similarityCalculators : Array[JavaStackSimilarityCalculator] } ) {

    def computeSimilarStacks(stackTrace : StackTrace, stackTraces : List[StackTrace]) : List[StackTrace] = {
        val orderedSimilarStacks = new ListBuffer[StackTrace]()
        dependencies.similarityCalculators.foreach{ similarityCalculator =>
            val mappedSimilarity = stackTraces.map{ //maps to a tuple (stackTrace, similarityValue)
                    similarStack => (similarStack, similarityCalculator.computeSimilarity(stackTrace, similarStack))}
            val orderedMappedSimilarity = mappedSimilarity.sortWith{ //sorts the stacks based on the similarityValue
                    (stackSimilarityTuple1, stackSimilarityTuple2) => stackSimilarityTuple1._2 < stackSimilarityTuple2._2}
            orderedSimilarStacks ++= orderedMappedSimilarity.filter(_._2 != 0).map(_._1) //removes the stacks which don't have any degree of similarity and maps it back to a list of stacks
        }
        orderedSimilarStacks.toList
    }
}

/**
  * Configures a JavaStackMatcher with similarity calculators sorted by result relevance, i.e., the first calculator used by the matcher
  *      use the most precise and restrictive algorithm for similarity calculation and the last is the least precise/restrictive.
  */
object JavaStackMatcherPrecisionConfig {
    lazy val similarityCalculators : Array[JavaStackSimilarityCalculator] = {
        val calculators = new Array[JavaStackSimilarityCalculator](4)
        calculators(0) = new JavaStackCompleteMatchSimilarityCalculator
        calculators(1) = new JavaStackChainedSimilarityCalculator
        calculators(2) = new JavaStackTopDownSimilarityCalculator
        calculators(3) = new JavaStackBottomUpSimilarityCalculator
        calculators
    }
    lazy val javaStackMatcher : JavaStackMatcher = new JavaStackMatcher(this)
}