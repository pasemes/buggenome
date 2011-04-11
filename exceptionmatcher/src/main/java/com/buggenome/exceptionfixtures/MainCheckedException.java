package com.buggenome.exceptionfixtures;

import com.buggenome.exceptionfixtures.exceptiongenerators.ExceptionGeneratorL1;
import com.buggenome.exceptionfixtures.sampleexceptions.CheckedExceptionL1;

/**
 * @author Paulo Sérgio
 * @version 1.0, 13/02/11
 */
public class MainCheckedException {

    public static void main(String args[]) throws CheckedExceptionL1 {
        new ExceptionGeneratorL1().generateExceptionL1();
    }
}
