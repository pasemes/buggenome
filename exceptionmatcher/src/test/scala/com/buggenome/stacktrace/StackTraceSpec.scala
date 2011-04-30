package com.buggenome.stacktrace

import frame.{StackTraceMethodInvocationLine, StackTraceTopLine, StackTraceFramesInCommon}
import org.specs.Specification
import com.buggenome.parser.JavaStackParser

/*
* Created by IntelliJ IDEA.
* User: Paulo Sérgio
* Date: 15/02/11
* Time: 20:47
*/
class StackTraceSpec extends Specification {

    "A stack trace" should {
        "be dynamically increased with a stack line" in {
            val stackTrace = new StackTrace()
            stackTrace.addFrame(new StackTraceTopLine(false, "com.FooClass", new Some("Some exception message")))
            stackTrace.sizeAllLines mustBe 1
        }

        "be dynamically increased with frames in common" in {

            //first, creating the stack trace
            val stackTrace = new StackTrace()
            stackTrace.addFrame(new StackTraceTopLine(false, "NewEx01", new Some("Thrown from meth01")))
            val frameInCommon = new StackTraceMethodInvocationLine("com.Class01", "meth01", new Some("Class01"), new Some("java"), 10)
            stackTrace.addFrame(frameInCommon)

            //second, creating the stack which has frames in common with the first
            val framesInCommonStack = new StackTrace()
            framesInCommonStack.addFrame(new StackTraceTopLine(true, "NewEx02", new Some("Thrown from meth02")))
            framesInCommonStack.addFrame(new StackTraceMethodInvocationLine("com.Class01", "meth02", new Some("Class01"), new Some("java"), 20))
            framesInCommonStack.addFrame(new StackTraceFramesInCommon(1, stackTrace))

            framesInCommonStack.sizeAllLines mustBe 3 // the first two frames plus the one frame in common added in the previous line
            framesInCommonStack.hasFramesInCommon must be(true)
            framesInCommonStack.getAllLines.last must be(frameInCommon)
        }

        "allow to be caused by another StackTrace (previous chained stack)" in {
            //first, creating the stack trace which caused the next stack
            val causeStack = new StackTrace()
            causeStack.addFrame(new StackTraceTopLine(false, "NewEx01", new Some("Thrown from meth01")))
            val frameInCommon = new StackTraceMethodInvocationLine("com.Class01", "meth01", new Some("Class01"), new Some("java"), 10)
            causeStack.addFrame(frameInCommon)

            //second, creating the stack which was caused by the previous
            val stackTrace = new StackTrace()
            stackTrace.addFrame(new StackTraceTopLine(true, "NewEx02", new Some("Thrown from meth02")))
            stackTrace.addFrame(new StackTraceMethodInvocationLine("com.Class01", "meth02", new Some("Class01"), new Some("java"), 20))
            stackTrace.addFrame(new StackTraceFramesInCommon(1, causeStack))

            stackTrace.causedBy(causeStack)

            stackTrace.wasCausedBy must be(true)
            stackTrace.getCauseStack.get must be(causeStack)

            causeStack.sizeAllLines mustBe 2 //should still be two because it was not linked with the stack that it cause (the second stack)
            causeStack.getAllLines.last must be(frameInCommon)
        }

        "allow to be the cause of another StackTrace (next chained stack)" in {
            //first, creating the stack trace which caused the next stack
            val causeStack = new StackTrace()
            causeStack.addFrame(new StackTraceTopLine(false, "NewEx01", new Some("Thrown from meth01")))
            val frameInCommon = new StackTraceMethodInvocationLine("com.Class01", "meth01", new Some("Class01"), new Some("java"), 10)
            causeStack.addFrame(frameInCommon)

            //second, creating the stack which was caused by the previous
            val stackTrace = new StackTrace()
            stackTrace.addFrame(new StackTraceTopLine(true, "NewEx02", new Some("Thrown from meth02")))
            stackTrace.addFrame(new StackTraceMethodInvocationLine("com.Class01", "meth02", new Some("Class01"), new Some("java"), 20))
            stackTrace.addFrame(new StackTraceFramesInCommon(1, causeStack))

            causeStack.resultedIn(stackTrace)

            causeStack.hasResultedIn must be(true)
            causeStack.getResultingStack.get must be(stackTrace)

            causeStack.sizeAllLines mustBe 5
            causeStack.getAllLines.last must be(frameInCommon)
        }

        "allow to get only the stack lines excluding the chained stack lines" in {
            //first, creating the stack trace which caused the next stack
            val causeStack = new StackTrace()
            causeStack.addFrame(new StackTraceTopLine(false, "NewEx01", new Some("Thrown from meth01")))
            val frameInCommon = new StackTraceMethodInvocationLine("com.Class01", "meth01", new Some("Class01"), new Some("java"), 10)
            causeStack.addFrame(frameInCommon)

            //second, creating the stack which was caused by the previous
            val stackTrace = new StackTrace()
            stackTrace.addFrame(new StackTraceTopLine(true, "NewEx02", new Some("Thrown from meth02")))
            stackTrace.addFrame(new StackTraceMethodInvocationLine("com.Class01", "meth02", new Some("Class01"), new Some("java"), 20))
            stackTrace.addFrame(new StackTraceFramesInCommon(1, causeStack))

            causeStack.resultedIn(stackTrace)

            causeStack.hasResultedIn must be(true)
            causeStack.getResultingStack.get must be(stackTrace)

            causeStack.getLinesNotChained.size must be(2)
        }

        "have only one StackTraceTopLine" in {
            val stackTrace = new StackTrace()
            stackTrace.addFrame(new StackTraceTopLine(false, "com.FooClass", new Some("Some exception message")))
            stackTrace.addFrame(new StackTraceTopLine(false, "com.FooClass", new Some("Some exception message"))) must throwAn[IllegalArgumentException]
        }

        "not accept the first stack line to be different than a StackTraceTopLine" in {
            val stackTrace = new StackTrace()
            stackTrace.addFrame(new StackTraceMethodInvocationLine("com.FooClass", "fooMethod", new Some("FooFile"), new Some("java"), 18)) must throwAn[IllegalArgumentException]
        }

        "allow that a specific StackTraceLine be retrieved" in {
            val stackTrace = new StackTrace()
            val stackLine = new StackTraceTopLine(false, "com.FooClass", new Some("Some exception message"))
            stackTrace.addFrame(stackLine)
            stackTrace.getLine(0) must be(stackLine)
        }

        "throw an exception when the index of the StackTraceLine to be retrieved is out of bounds" in {
            val stackTrace = new StackTrace()
            stackTrace.addFrame(new StackTraceMethodInvocationLine("com.FooClass", "fooMethod", new Some("FooFile"), new Some("java"), 18)) must throwAn[IllegalArgumentException]

            stackTrace.getLine(1) must throwAn[IndexOutOfBoundsException]
        }

        "Be equal to another when they have same content" in {
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

            stackTrace1 must beEqualTo(stackTrace2)
        }

        "Be different to another when they have different content" in {

            var stackLines = new Array[String](12)
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


            stackLines = new Array[String](12)
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
            val stackTrace2 = new JavaStackParser().parse(stackLines)

            stackTrace1 must beDifferentFrom(stackTrace2)
        }
    }
}