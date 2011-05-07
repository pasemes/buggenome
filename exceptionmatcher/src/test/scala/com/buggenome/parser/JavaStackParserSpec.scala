package com.buggenome.parser

import org.specs._
import com.buggenome.stacktrace.StackTrace

/**
 *
 * @author Paulo Sérgio
 * @version 1.0, 23/01/11
 */

class JavaStackParserSpec extends Specification {

    "A java stack trace parser" should {

        "Be able to parse simple (not chained) stack traces" in {

            //example taken from
            //http://www.developer.com/java/data/article.php/1464821/Processing-Stack-Trace-Data-in-Java.htm
            val stackLines = new Array[String](4)
            stackLines(0) = "NewEx01: Thrown from meth02"
            stackLines(1) = "at Class01.meth02(StackTr01.java:92)"
            stackLines(2) = "at Class01.meth01(StackTr01.java:60)"
            stackLines(3) = "at StackTr01.main(StackTr01.java:52)"

            val stackTrace : StackTrace = new JavaStackParser().parse(stackLines)

            stackTrace.sizeAllLines must beEqualTo(4)

            for(i <- 0 to 3)
                stackLines(i) must beEqualTo(stackTrace.getLine(i).toString)
        }

        "Be able to parse chained stack traces" in {

            //example taken from
            //http://www.developer.com/java/data/article.php/1464821/Processing-Stack-Trace-Data-in-Java.htm
            val stackLines = new Array[String](12)

            stackLines(0)  = "NewEx01: Thrown from meth02"
            stackLines(1)  = "at Class01.meth02(StackTr01.java:92)"
            stackLines(2)  = "at Class01.meth01(StackTr01.java:60)"
            stackLines(3)  = "at StackTr01.main(StackTr01.java:52)"
            stackLines(4)  = "Caused by: NewEx02: Thrown from meth03"
            stackLines(5)  = "at Class01.meth03(StackTr01.java:102)"
            stackLines(6)  = "at Class01.meth02(StackTr01.java:85)"
            stackLines(7)  = "... 2 more"
            stackLines(8)  = "Caused by: NewEx03: Thrown from meth04"
            stackLines(9)  = "at Class01.meth04(StackTr01.java:112)"
            stackLines(10) = "at Class01.meth03(StackTr01.java:100)"
            stackLines(11) = "... 3 more"

            val stackTrace : StackTrace = new JavaStackParser().parse(stackLines)

            stackTrace.sizeAllLines must beEqualTo(15)

            for(i <- 0 to 6)
                stackLines(i) must beEqualTo(stackTrace.getLine(i).toString)

            stackLines(2) must beEqualTo(stackTrace.getLine(7).toString)
            stackLines(3) must beEqualTo(stackTrace.getLine(8).toString)

            stackLines(8) must beEqualTo(stackTrace.getLine(9).toString)
            stackLines(9) must beEqualTo(stackTrace.getLine(10).toString)
            stackLines(10) must beEqualTo(stackTrace.getLine(11).toString)

            stackLines(6) must beEqualTo(stackTrace.getLine(12).toString)
            stackLines(2) must beEqualTo(stackTrace.getLine(13).toString)
            stackLines(3) must beEqualTo(stackTrace.getLine(14).toString)
        }

        "Be able to parse stack traces with unknown source or native lines" in {

            val stackLines = new Array[String](4)
            stackLines(0)  = "NewEx01: Thrown from meth02"
            stackLines(1)  = "at Class01.meth02(StackTr01.java:92)"
            stackLines(2)  = "at TestDebug.main(Unknown Source)"
            stackLines(3)  = "at java.lang.System.arraycopy(Native Method)"

            val stackTrace : StackTrace = new JavaStackParser().parse(stackLines)

            stackTrace.sizeAllLines must beEqualTo(4)

            for(i <- 0 to 3)
                stackLines(i) must beEqualTo(stackTrace.getLine(i).toString)
        }
    }
}