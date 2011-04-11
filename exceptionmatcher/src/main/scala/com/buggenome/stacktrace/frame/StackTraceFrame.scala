package com.buggenome.stacktrace.frame

/*
 * Created by IntelliJ IDEA.
 * User: Paulo Sérgio
 * Date: 13/02/11
 * Time: 12:02
 */
/**
 * An element in a stack trace, as returned by {@link Throwable#getStackTrace()}.
 * Each element represents a single stack frame.
 * All stack frames except for the one at the top of the stack represent
 * a method invocation.  The frame at the top of the stack represents the
 * execution point at which the stack trace was generated.  Typically,
 * this is the point at which the throwable corresponding to the stack trace
 * was created.
 *
 * @param declaringClass the fully qualified name of the class containing
 *        the execution point represented by the stack trace element
 */
abstract class StackTraceFrame {

    /**
     * Returns a string representation of this stack trace element.  The
     * format of this string depends on the implementation.
     */
    def toString() : String
}
