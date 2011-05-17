package com.buggenome.stacktrace.frame

/**
 * Represents an stack trace line.
 * @param declaringClass the fully qualified name of the <tt>Class</tt> containing
 *         the execution point represented by this stack trace element.
 */
abstract class StackTraceLine(val declaringClass : String) extends StackTraceFrame {

    require(declaringClass != null, "Declaring class cannot be null")

}