package com.buggenome.matcher.similaritycalculators

import org.specs._
import com.buggenome.parser.JavaStackParser

/**
 * User: pasemes
 * Date: 14/05/11
 * Time: 23:15
 */
class JavaStackCompleteMatchSimilarityCalculatorSpec extends Specification {

    "A complete match similarity calculator" should {

        "Match when two stacks are completely equal" in {

            val stackLines = new Array[String](12)
            stackLines(0)  = "NewEx01: Thrown from meth02"
            stackLines(1)  = "at Class01.meth02(StackTr01.java:92)"
            stackLines(2)  = "at Class01.meth01(Unknown Source)"
            stackLines(3)  = "at StackTr01.main(Native Method)"
            stackLines(4)  = "Caused by: NewEx02: Thrown from meth03"
            stackLines(5)  = "at Class01.meth03(StackTr01.java:102)"
            stackLines(6)  = "at Class01.meth02(StackTr01.java:85)"
            stackLines(7)  = "... 2 more"
            stackLines(8)  = "Caused by: NewEx03: Thrown from meth04"
            stackLines(9)  = "at Class01.meth04(StackTr01.java:112)"
            stackLines(10) = "at Class01.meth03(StackTr01.java:100)"
            stackLines(11) = "... 3 more"

            val stackTrace1 = new JavaStackParser().parse(stackLines)
            val stackTrace2 = new JavaStackParser().parse(stackLines)

            val similarityCalculator = new JavaStackCompleteMatchSimilarityCalculator

            println("#######" + similarityCalculator.computeSimilarity(stackTrace1, stackTrace2))


        }
    }
}
