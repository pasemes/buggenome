package com.buggenome.exceptionfixtures.sampleexceptions;

/**
 * Autor: Paulo Sérgio
 * Data: 20/01/11
 * Hora: 17:49
 * Descrição:
 */
public class CheckedExceptionL2 extends Exception {

    public CheckedExceptionL2(Throwable cause) {
        super("Level 2 exception.", cause);
    }
}
