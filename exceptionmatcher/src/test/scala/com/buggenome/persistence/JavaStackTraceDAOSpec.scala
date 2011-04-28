package com.buggenome.persistence

import org.specs.Specification
import com.buggenome.parser.JavaStackParser

/**
 * User: pasemes
 * Date: 03/04/11
 * Time: 23:48
 */

class JavaStackTraceDAOSpec extends Specification {

    val stackTraceColl = MongoDriverManager.javaStackTraceColl

    def cleanCollection() { stackTraceColl.drop() }

    "The JavaStackTraceDAO" should {

        cleanCollection().before

        //this isn't an unitary test, since we test insert and retrieve at the same time
        //but it easier to do that because we can use domain classes (e.g., StackTrace) instead of
        //checking directly in the database
        "insert and retrieve a stack in the database" in {

            //we insert a complete (chained stack), since it tests most of the cases (code coverage)
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

            //getting a stack trace fron the above array
            val stackTrace = new JavaStackParser().parse(stackLines)

            //inserting the stack
            JavaStackTraceDAO.insert(stackTrace)

            //retrieving it again from the database for asserting some properties
            val dbResult = JavaStackTraceDAO.findByStackTopLine(stackTrace.getTopLine, 0)


            println("RESULTADO " + dbResult.toString)
            dbResult.hasNext must be(true)
            println("RESULTADO " + dbResult.toString)
            val stackTraceFromDB = dbResult.next
            println("RESULTADO " + dbResult.toString)
            //TODO testar este apenas quando o teste equals da classe StackTrace estiver pronto
            stackTraceFromDB must beEqualTo(stackTrace)






        }

//         "testar unknown source|native" in { //TODO testar
//
////            stackLines(2) = "at TestDebug.main(Unknown Source)"
////            stackLines(3) = "at java.lang.System.arraycopy(Native Method)"
//        }
    }
}