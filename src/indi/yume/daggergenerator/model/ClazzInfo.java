package indi.yume.daggergenerator.model;

import indi.yume.daggergenerator.generator.NewLine;

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
    protected ClazzInfo genericClazz;

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
        this.genericClazz = genericClazz;
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

    public ClazzInfo getGenericClazz() {
        return genericClazz;
    }

    public void setGenericClazz(ClazzInfo genericClazz) {
        this.genericClazz = genericClazz;
    }

    public String toString(){
        if(genericClazz != null)
            return clazzName + "<" + genericClazz.toString() + ">";
        return clazzName;
    }

    public String toString(NewLine newline){
        String s = getModifierInfo() + type.get() + clazzName;
        if(genericClazz != null)
            return s + "<" + genericClazz.toString() + ">";
        return s;
    }

    @Override
    public List<String> getImportClazz() {
        List<String> list = new ArrayList<>();
        if(packageName != null)
            list.add(packageName + "." + clazzName);

        if(genericClazz != null)
            list.addAll(genericClazz.getImportClazz());
        list = getAnnoImportClazz(list);
        return list;
    }
}
