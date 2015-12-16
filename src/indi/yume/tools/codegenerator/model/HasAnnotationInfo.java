package indi.yume.tools.codegenerator.model;

import indi.yume.tools.codegenerator.generator.NewLine;

/**
 * Created by yume on 15/9/26.
 */
public interface HasAnnotationInfo {
    void addAnnotation(AnnotationInfo annotation);
    String generatorAnnotation(NewLine newLine);
}
