package com.buggenome.exceptionfixtures.exceptiongenerators;

import com.buggenome.exceptionfixtures.sampleexceptions.CheckedExceptionL2;
import com.buggenome.exceptionfixtures.sampleexceptions.CheckedExceptionL1;

import java.io.*;

/**
 * Autor: Paulo Sérgio
 * Data: 20/01/11
 * Hora: 21:58
 * Descrição:
 */
public class ExceptionGeneratorL2 {

    public ExceptionGeneratorL2() {}

    public void generateExceptionL2() throws CheckedExceptionL2 {
        throw new CheckedExceptionL2(new CheckedExceptionL1());
    }

    public String getGeneratedExceptionStack() {
        try {
            new ExceptionGeneratorL2().generateExceptionL2();
            return "";
        } catch(Exception e) {
            StringWriter stackTraceWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTraceWriter));
            return stackTraceWriter.toString();
        }
    }
}
