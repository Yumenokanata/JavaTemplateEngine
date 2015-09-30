package indi.yume.daggergenerator.model;

import java.util.*;

/**
 * Created by yume on 15/9/26.
 */
public class AnnotationInfo extends ClazzInfo {
    private Map<String, ClazzInfo[]> params = new HashMap<>();

    public AnnotationInfo(String packageName, String clazzName){
        super(Type.ANNOTATION, packageName, clazzName);
    }

    public AnnotationInfo addParam(String key, ClazzInfo... value){
        if(params.containsKey(key)) {
            List<ClazzInfo> list = Arrays.asList(params.get(key));
            list.addAll(Arrays.asList(value));
            value = list.toArray(new ClazzInfo[list.size()]);
        }
        params.put(key, value);
        return this;
    }

    @Override
    public List<String> getImportClazz() {
        List<String> list = new ArrayList<>();
        if(packageName != null)
            list.add(packageName + "." + clazzName);

        if(genericClazz != null)
            list.addAll(genericClazz.getImportClazz());
        for(String key : params.keySet())
            for(ClazzInfo ci : params.get(key))
                list.addAll(ci.getImportClazz());
        return list;
    }

    @Override
    public String toString() {
        String baseName = "@" + super.toString();
        StringBuilder name = new StringBuilder(baseName);
        if(params.size() != 0) {
            name.append("(");
            for (String key : params.keySet()) {
                name.append(key)
                        .append(" = ");
                ClazzInfo[] values = params.get(key);
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
            name.delete(name.length() - 10, name.length());
            name.append(")");
        }
        return name.toString();
    }
}
