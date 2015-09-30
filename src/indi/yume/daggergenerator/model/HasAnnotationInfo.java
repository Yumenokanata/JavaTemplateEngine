package indi.yume.daggergenerator.model;

import indi.yume.daggergenerator.generator.NewLine;

/**
 * Created by yume on 15/9/26.
 */
public interface HasAnnotationInfo {
    void addAnnotation(AnnotationInfo annotation);
    String generatorAnnotation(NewLine newLine);
}
