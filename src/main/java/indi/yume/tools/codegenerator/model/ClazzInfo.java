package indi.yume.tools.codegenerator.model;

import indi.yume.tools.codegenerator.generator.NewLine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yume on 15/9/26.
 */
public class ClazzInfo extends BaseInfo{
    protected ModifierInfo modifierInfo = new ModifierInfo();
    protected Type type;
    protected String packageName;
    protected String clazzName;
    protected List<ClazzInfo> genericClazzList = new ArrayList<>();

    public ClazzInfo(){

    }

    public ClazzInfo(String packageName, String clazzName) {
        this.type = Type.CLASS;
        this.packageName = packageName;
        this.clazzName = clazzName;
    }

    public ClazzInfo(Type type, String packageName, String clazzName) {
        this.type = type;
        this.packageName = packageName;
        this.clazzName = clazzName;
    }

    public ClazzInfo(String packageName, Type type, String clazzName, ClazzInfo genericClazz){
        this.packageName = packageName;
        this.clazzName = clazzName;
        this.genericClazzList.add(genericClazz);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setModifierInfo(ModifierInfo modifierInfo) {
        this.modifierInfo = modifierInfo;
    }

    public ModifierInfo getModifierInfo() {
        return modifierInfo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClazzOriginName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public List<ClazzInfo> getGenericClazz() {
        return genericClazzList;
    }

    public void addGenericClazz(ClazzInfo genericClazz) {
        this.genericClazzList.add(genericClazz);
    }

    public String toString(){
        StringBuilder clazz = new StringBuilder(clazzName);
        if(genericClazzList.size() != 0) {
            clazz.append("<");
            for (ClazzInfo ci : genericClazzList)
                clazz.append(ci.toString())
                        .append(", ");
            clazz.deleteCharAt(clazz.length() - 1)
                    .deleteCharAt(clazz.length() - 1)
                    .append(">");
        }
        return clazz.toString();
    }

    public String toString(NewLine newline){
        StringBuilder clazz = new StringBuilder(getModifierInfo() + type.get() + clazzName);
        if(genericClazzList.size() != 0) {
            clazz.append("<");
            for (ClazzInfo ci : genericClazzList)
                clazz.append(ci.toString())
                        .append(", ");
            clazz.deleteCharAt(clazz.length() - 1)
                    .deleteCharAt(clazz.length() - 1)
                    .append(">");
        }
        return clazz.toString();
    }

    @Override
    public List<String> getImportClazz() {
        List<String> list = new ArrayList<>();
        if(packageName != null && !"".equals(packageName))
            list.add(packageName + "." + clazzName);

        if(genericClazzList.size() != 0)
            for(ClazzInfo ci : genericClazzList)
                list.addAll(ci.getImportClazz());
        list = getAnnoImportClazz(list);
        return list;
    }
}
