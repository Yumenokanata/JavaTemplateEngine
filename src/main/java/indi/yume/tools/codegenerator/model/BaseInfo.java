package indi.yume.tools.codegenerator.model;

import indi.yume.tools.codegenerator.generator.NewLine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yume on 15/9/26.
 */
public abstract class BaseInfo implements ImportInfo, HasAnnotationInfo {
    private List<AnnotationInfo> annotationList = new ArrayList<>();
    private Set<String> annoKey = new HashSet<>();

    public abstract String toString(NewLine newline);

    public List<String> getAnnoImportClazz(List<String> list) {
        for(AnnotationInfo ci : annotationList)
            list.addAll(ci.getImportClazz());
        return list;
    }

    @Override
    public void addAnnotation(AnnotationInfo annotation) {
        annotationList.add(annotation);
    }

    @Override
    public String generatorAnnotation(NewLine newLine) {
        StringBuilder stringBuilder = new StringBuilder();
        for(AnnotationInfo ci : annotationList)
            stringBuilder.append(newLine.getPrefix())
                    .append(ci.toString().replace("\n", "\n" + newLine.getPrefix()))
                    .append("\n");
        return stringBuilder.toString();
    }
}
