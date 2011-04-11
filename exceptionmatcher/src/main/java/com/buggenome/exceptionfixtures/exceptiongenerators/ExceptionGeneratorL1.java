package com.buggenome.exceptionfixtures.exceptiongenerators;

import com.buggenome.exceptionfixtures.sampleexceptions.CheckedExceptionL1;

/**
 * Autor: Paulo Sérgio
 * Data: 20/01/11
 * Hora: 21:58
 * Descrição:
 */
public class ExceptionGeneratorL1 {

    public ExceptionGeneratorL1() {}

    public void generateExceptionL1() throws CheckedExceptionL1 {
        throw new CheckedExceptionL1();
    }
}
