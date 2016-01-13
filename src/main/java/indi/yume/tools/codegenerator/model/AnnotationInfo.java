package indi.yume.tools.codegenerator.model;

import java.util.*;

/**
 * Created by yume on 15/9/26.
 */
public class AnnotationInfo extends ClazzInfo {
    private Map<String, ClazzInfo[]> clazzParams = new HashMap<>();
    private Map<String, String[]> valueParams = new HashMap<>();

    public AnnotationInfo(String packageName, String clazzName){
        super(Type.ANNOTATION, packageName, clazzName);
    }

    public AnnotationInfo addClazzParam(String key, ClazzInfo... value){
        if(clazzParams.containsKey(key)) {
            List<ClazzInfo> list = Arrays.asList(clazzParams.get(key));
            list.addAll(Arrays.asList(value));
            value = list.toArray(new ClazzInfo[list.size()]);
        }
        clazzParams.put(key, value);
        return this;
    }

    public AnnotationInfo addValueParam(String key, String... value){
        if(valueParams.containsKey(key)) {
            List<String> list = Arrays.asList(valueParams.get(key));
            list.addAll(Arrays.asList(value));
            value = list.toArray(new String[list.size()]);
        }
        valueParams.put(key, value);
        return this;
    }

    @Override
    public List<String> getImportClazz() {
        List<String> list = new ArrayList<>();
        if(packageName != null && !"".equals(packageName))
            list.add(packageName + "." + clazzName);

        if(genericClazzList.size() != 0)
            for(ClazzInfo ci : genericClazzList)
                list.addAll(ci.getImportClazz());
        for(String key : clazzParams.keySet())
            for(ClazzInfo ci : clazzParams.get(key))
                list.addAll(ci.getImportClazz());
        return list;
    }

    @Override
    public String toString() {
        String baseName = "@" + super.toString();
        StringBuilder name = new StringBuilder(baseName);
        if(clazzParams.size() != 0 || valueParams.size() != 0) {
            name.append("(");

            for (String key : clazzParams.keySet()) {
                name.append(key)
                        .append(" = ");
                ClazzInfo[] values = clazzParams.get(key);
                if(values.length > 1){
                    name.append("{");
                    for(ClazzInfo v : values)
                        name.append(v.toString())
                                .append(".class")
                                .append(", ");
                    name.deleteCharAt(name.length() - 1);
                    name.deleteCharAt(name.length() - 1);
                    name.append("}");
                } else{
                    name.append(values[0])
                            .append(".class");
                }
                name.append(",\n        ");
            }

            for (String key : valueParams.keySet()) {
                name.append(key)
                        .append(" = ");
                String[] values = valueParams.get(key);
                if(values.length > 1){
                    name.append("{");
                    for(String v : values)
                        name.append(v)
                                .append(", ");
                    name.deleteCharAt(name.length() - 1);
                    name.deleteCharAt(name.length() - 1);
                    name.append("}");
                } else{
                    name.append(values[0]);
                }
                name.append(",\n        ");
            }

            name.delete(name.length() - 10, name.length());
            name.append(")");
        }
        return name.toString();
    }
}
