package com.buggenome.matcher.similaritycalculators

import org.specs.Specification
import org.specs.mock.Mockito
import com.buggenome.stacktrace.StackTrace
import org.mockito.Matchers._

class JavaStackSimilarityCalculatorSpec extends Specification with Mockito {

    "A java stack similarity calculator" should {

        "Compute the similarity between stacks" in {
            val similarityCalculator = new JavaStackSimilarityCalculator {
                protected def fullSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = 0.3
                protected def ignoringLineSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = 0.9
                protected def wildcardSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = 0.6
            }
            similarityCalculator.computeSimilarity(null, null) must beEqualTo(0.6)
        }
    }
}