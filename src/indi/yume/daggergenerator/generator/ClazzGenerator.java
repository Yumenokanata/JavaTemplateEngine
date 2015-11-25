package indi.yume.daggergenerator.generator;

import indi.yume.daggergenerator.model.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yume on 15/9/26.
 */
public class ClazzGenerator {
    private ClazzInfo baseClazzInfo;
    private String note;

    private ClazzInfo extendsClazzInfo;
    private List<ClazzInfo> interfaceList = new ArrayList<>();

    private List<PropertyInfo> propertyList = new ArrayList<>();
    private List<MethodInfo> methodList = new ArrayList<>();

    private List<ClazzGenerator> innerClazzList = new ArrayList<>();

    private List<ClazzInfo> extraImportClazz = new ArrayList<>();

    public ClazzGenerator(ClazzInfo baseClazzInfo){
        this.baseClazzInfo = baseClazzInfo;
    }

    public ClazzInfo getBaseClazzInfo() {
        return baseClazzInfo;
    }

    public void addAnnotation(AnnotationInfo annotation){
        baseClazzInfo.addAnnotation(annotation);
    }

    public void setNote(String note){
        this.note = note;
    }

    public void setExtendsClazzInfo(ClazzInfo clazzInfo){
        this.extendsClazzInfo = clazzInfo;
    }

    public void addInterface(ClazzInfo interfaceInfo){
        interfaceList.add(interfaceInfo);
    }

    public void addProperty(PropertyInfo property){
        propertyList.add(property);
    }

    public void addMethod(MethodInfo method){
        method.setType(baseClazzInfo.getType());
        methodList.add(method);
    }

    public void addInnerClazz(ClazzGenerator clazz){
        innerClazzList.add(clazz);
    }

    public String render(){
        return toString(new NewLine(), true);
    }

    protected String toString(NewLine newLine, boolean generatorHead){
        StringBuilder stringBuilder = new StringBuilder();

        if(generatorHead) {
            generatorPackage(stringBuilder, newLine);
            generatorImport(stringBuilder, newLine);
            generatorNote(stringBuilder, newLine);
        }

        generatorHead(stringBuilder, newLine);

        stringBuilder.append("{");

        stringBuilder.append("\n");

        generatorBody(stringBuilder, newLine.clone().addPrefix());

        stringBuilder.append(newLine.getPrefix())
                .append("}");

        return stringBuilder.toString();
    }

    private StringBuilder generatorPackage(StringBuilder stringBuilder, NewLine newLine){
        stringBuilder.append(newLine.getPrefix())
                .append("package ")
                .append(baseClazzInfo.getPackageName())
                .append(";\n\n");
        return stringBuilder;
    }

    private StringBuilder generatorImport(StringBuilder stringBuilder, NewLine newLine){
        Set<String> importSet = new HashSet<>();
        importSet.addAll(baseClazzInfo.getImportClazz());
        for(ImportInfo ci : extraImportClazz)
            importSet.addAll(ci.getImportClazz());
        if(extendsClazzInfo != null)
            importSet.addAll(extendsClazzInfo.getImportClazz());
        for(ImportInfo ci : interfaceList)
            importSet.addAll(ci.getImportClazz());
        for(ImportInfo ci : propertyList)
            importSet.addAll(ci.getImportClazz());
        for(ImportInfo ci : methodList)
            importSet.addAll(ci.getImportClazz());

        List<String> importList = new LinkedList<>(importSet);
        Collections.sort(importList);
        String packageFirst = null;
        for(String importPackage : importList)
            if(isInSamePackage(importPackage, baseClazzInfo.getPackageName())) {
                if(packageFirst != null && !packageFirst.equals(importPackage.split("\\.")[0]))
                    stringBuilder.append("\n");
                stringBuilder.append(newLine.getPrefix())
                        .append("import ")
                        .append(importPackage)
                        .append(";\n");
                packageFirst = importPackage.split("\\.")[0];
            }
        return stringBuilder;
    }

    private boolean isInSamePackage(String package1, String basePackage){
        if(package1 == null || "".equals(package1))
            return false;
        int index = package1.lastIndexOf('.');
        String package_ = package1.substring(0, index);
        return !package_.equals(basePackage);
    }

    private StringBuilder generatorNote(StringBuilder stringBuilder, NewLine newLine){
        stringBuilder.append("\n")
                .append(note)
                .append("\n");
        return stringBuilder;
    }

    private StringBuilder generatorHead(StringBuilder stringBuilder, NewLine newLine){
        stringBuilder.append(baseClazzInfo.generatorAnnotation(newLine.clone()))
                .append(newLine.getPrefix())
                .append(baseClazzInfo.getModifierInfo().toString())
                .append(" ")
                .append(baseClazzInfo.getType().get())
                .append(" ")
                .append(baseClazzInfo.toString());

        if(extendsClazzInfo != null)
            stringBuilder.append(" extends ")
                    .append(extendsClazzInfo.toString());

        if(interfaceList.size() != 0){
            stringBuilder.append(" implements ");
            for(ClazzInfo ci : interfaceList)
                stringBuilder.append(ci.toString())
                .append(",");
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder;
    }

    private StringBuilder generatorBody(StringBuilder stringBuilder, NewLine newLine){
        for(PropertyInfo pi : propertyList)
            stringBuilder.append(pi.toString(newLine.clone()))
                    .append(";\n");

        stringBuilder.append("\n");

        for(MethodInfo mi : methodList)
            stringBuilder.append(mi.toString(newLine.clone()))
                    .append("\n");

        for(ClazzGenerator cg : innerClazzList)
            stringBuilder.append(cg.toString(newLine.clone(), false))
                    .append("\n");

        return stringBuilder;
    }
}
