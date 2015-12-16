package indi.yume.tools.codegenerator.model;

import java.lang.reflect.Modifier;

/**
 * Created by yume on 15/9/26.
 */
public class ModifierInfo {
    private int modifier = 0;

    public ModifierInfo(){
        resetModifier();
    }

    public ModifierInfo(int mod){
        setModifier(mod);
    }

    public void setModifier(int mod){
        modifier = modifier | mod;
    }

    public String getModifier(){
        return Modifier.toString(modifier);
    }

    public void resetModifier() {
        modifier = 0;
        modifier = modifier | Modifier.PUBLIC;
    }

    @Override
    public String toString() {
        return Modifier.toString(modifier);
    }
}
