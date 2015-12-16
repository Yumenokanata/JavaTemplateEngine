package indi.yume.tools.codegenerator.model;

import indi.yume.tools.codegenerator.generator.NewLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yume on 15/9/26.
 */
public class MethodInfo extends BaseInfo {
    private List<ClazzInfo> importClazz = new ArrayList<>();

    private Type type = Type.CLASS;

    private ModifierInfo modifierInfo = new ModifierInfo();
    private ClazzInfo returnClazz;
    private String methodName;
    private ParamInfo[] params = new ParamInfo[]{};
    private MethodBodyGenerator methodBodyGenerator;

    public MethodInfo(Type type, ClazzInfo returnClazz, String methodName, ParamInfo[] params) {
        this.type = type;
        this.returnClazz = returnClazz;
        this.methodName = methodName;
        if(params != null)
            this.params = params;

        if(returnClazz != null)
            importClazz.add(returnClazz);

        for(ParamInfo pi : params)
            importClazz.add(pi.getClazzInfo());
    }

    public MethodInfo(ClazzInfo returnClazz, String methodName, ParamInfo[] params) {
        this.returnClazz = returnClazz;
        this.methodName = methodName;
        if(params != null && params.length != 0)
            this.params = params;

        if(returnClazz != null)
            importClazz.add(returnClazz);

        if(params != null && params.length != 0)
            for(ParamInfo pi : params)
                importClazz.add(pi.getClazzInfo());
    }

    public void addAnnotation(AnnotationInfo annotationClazz) {
        super.addAnnotation(annotationClazz);
        importClazz.add(annotationClazz);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ModifierInfo getModifierInfo() {
        return modifierInfo;
    }

    public void setModifierInfo(ModifierInfo modifierInfo) {
        this.modifierInfo = modifierInfo;
    }

    public ClazzInfo getReturnClazz() {
        return returnClazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public ParamInfo[] getParams() {
        return params;
    }

    public MethodBodyGenerator getMethodBodyGenerator() {
        return methodBodyGenerator;
    }

    public void setMethodBodyGenerator(MethodBodyGenerator methodBodyGenerator, ClazzInfo... importClazz) {
        this.methodBodyGenerator = methodBodyGenerator;
        Collections.addAll(this.importClazz, importClazz);
    }

    public String toString(NewLine newline){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(generatorAnnotation(newline))
                .append(newline.getPrefix())
                .append(modifierInfo.toString());

        if(returnClazz != null)
            stringBuilder.append(" ")
                    .append(returnClazz.toString());

        stringBuilder.append(" ")
                .append(methodName)
                .append("(");

        if(params.length > 0) {
            for (ParamInfo pi : params)
                stringBuilder.append(pi.toString())
                        .append(", ");
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        if(type == Type.INTERFACE){
            stringBuilder.append(");\n");
        } else {
            stringBuilder.append("){\n");

            newline.clone().addPrefix();
            String body = methodBodyGenerator.generatorMethodBody(newline.getTab());
            String tab = newline.clone().addPrefix().getPrefix();
            body = body.replace("\n", "\n" + tab);
            body = tab + body;
            stringBuilder.append(body);
            stringBuilder.append("\n")
                    .append(newline.getPrefix())
                    .append("}\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public List<String> getImportClazz() {
        List<String> list = new ArrayList<>();
        for(ClazzInfo ci : importClazz)
            list.addAll(ci.getImportClazz());
        return getAnnoImportClazz(list);
    }

    public static class ParamInfo{
        private ClazzInfo clazzInfo;
        private String name;

        public ParamInfo(ClazzInfo clazzInfo, String name) {
            this.clazzInfo = clazzInfo;
            this.name = name;
        }

        @Override
        public String toString(){
            return clazzInfo.toString() + " " + name;
        }

        public ClazzInfo getClazzInfo() {
            return clazzInfo;
        }

        public void setClazzInfo(ClazzInfo clazzInfo) {
            this.clazzInfo = clazzInfo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
