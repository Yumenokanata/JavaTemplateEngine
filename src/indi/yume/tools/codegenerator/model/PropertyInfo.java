package indi.yume.tools.codegenerator.model;

import indi.yume.tools.codegenerator.generator.NewLine;

import java.util.List;

/**
 * Created by yume on 15/9/26.
 */
public class PropertyInfo extends BaseInfo{
    private ModifierInfo modifier = new ModifierInfo(0);
    private ClazzInfo clazzInfo;
    private String name;

    public PropertyInfo(ClazzInfo clazzInfo, String name) {
        this.clazzInfo = clazzInfo;
        this.name = name;
    }

    public PropertyInfo(ModifierInfo modifier, ClazzInfo clazzInfo, String name) {
        this.modifier = modifier;
        this.clazzInfo = clazzInfo;
        this.name = name;
    }

    public String toString(NewLine newline){
        String mod = modifier.toString();
        if(!"".equals(mod))
            mod += " ";
        return generatorAnnotation(newline)
                + newline.getPrefix()
                + mod
                + clazzInfo.toString()
                + " " + name;
    }

    public ModifierInfo getModifier() {
        return modifier;
    }

    public void setModifier(ModifierInfo modifier) {
        this.modifier = modifier;
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

    @Override
    public List<String> getImportClazz() {
        return getAnnoImportClazz(clazzInfo.getImportClazz());
    }
}
