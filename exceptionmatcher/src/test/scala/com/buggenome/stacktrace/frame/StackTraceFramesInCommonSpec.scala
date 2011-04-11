package com.buggenome.stacktrace.frame

/*
 * Created by IntelliJ IDEA.
 * User: Paulo Sérgio
 * Date: 27/02/11
 * Time: 23:05
 */
import org.specs.Specification
import com.buggenome.stacktrace.StackTrace
import com.weiglewilczek.slf4s.Logging

class StackTraceFramesInCommonSpec extends Specification {

    "A stack frames in common line" should {

        "Return the number of frames in common from a well formed line" in {
            val stackLine : String = "... 3 more"
            stackLine must beLike {//the boolean values inside cases are captured by the "must beLike" Steps matcher
                case StackTraceFramesInCommon(numberFramesInCommon) =>
                    numberFramesInCommon must beEqualTo("3")
                    true
                case _ =>
                    false
            }
        }

        "Not match when the stack line isn't well formed" in {
            val stackLine : String = "... 3 moreasd)"
            stackLine must beLike {//the boolean values inside cases are captured by the "must beLike" Steps matcher
                case StackTraceFramesInCommon(numberFramesInCommon) =>
                    false //shouldn't match this string
                case StackTraceMethodInvocationLine(clazz, method, file, language, line) =>
                    false //shouldn't match this string
                case StackTraceTopLine(chained, exception, message) =>
                    false //shouldn't match this string
                case _ =>
                    true //doesn't matched any case
            }
        }

        "Keep the number of frames in common and the StackTrace which has the common frames" in {

            val framesInCommonStack = new StackTrace()

            framesInCommonStack.addFrame(new StackTraceTopLine(false, "com.FooClass", new Some("Some exception message")))
            framesInCommonStack.addFrame(new StackTraceMethodInvocationLine("com.FooClass", "fooMethod", new Some("FooFile"), new Some("java"), 18))
            val stackLine = new StackTraceMethodInvocationLine("com.FooClass2", "fooMethod2", new Some("FooFile2"), new Some("java"), 18)
            framesInCommonStack.addFrame(stackLine)

            val framesInCommon = new StackTraceFramesInCommon(1, framesInCommonStack)
            val framesInCommonList = framesInCommon.getFramesInCommon

            framesInCommonList.size must beEqualTo(1)
            framesInCommonList(0) must be(stackLine)
        }

        "Not accept an instance to be created when there are more frames in common than the StackTrace which has the frames" in {

            val framesInCommonStack = new StackTrace()

            framesInCommonStack.addFrame(new StackTraceTopLine(false, "com.FooClass", new Some("Some exception message")))
            framesInCommonStack.addFrame(new StackTraceMethodInvocationLine("com.FooClass", "fooMethod", new Some("FooFile"), new Some("java"), 18))

            new StackTraceFramesInCommon(1, null) must throwAn[IllegalArgumentException]
            new StackTraceFramesInCommon(2, framesInCommonStack) must throwAn[IllegalArgumentException]
        }
    }
}