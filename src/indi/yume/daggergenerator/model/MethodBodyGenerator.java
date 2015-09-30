package indi.yume.daggergenerator.model;


import indi.yume.daggergenerator.generator.NewLine;

/**
 * Created by yume on 15/9/26.
 */
public interface MethodBodyGenerator {
    StringBuilder generatorMethodBody(StringBuilder stringBuilder, NewLine newline);
}
