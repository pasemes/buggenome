package com.buggenome.exceptionfixtures;

import com.buggenome.exceptionfixtures.exceptiongenerators.ExceptionGeneratorL2;
import com.buggenome.exceptionfixtures.sampleexceptions.CheckedExceptionL2;

/**
 * @author Paulo Sérgio
 * @version 1.0, 15/02/11
 */
public class ChainedException {

    public static void main(String args[]) throws CheckedExceptionL2 {
        new ExceptionGeneratorL2().generateExceptionL2();
    }
}
