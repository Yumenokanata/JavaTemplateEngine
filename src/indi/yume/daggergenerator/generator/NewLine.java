package indi.yume.daggergenerator.generator;

/**
 * Created by yume on 15/9/26.
 */
public class NewLine {
    public static final String FourSpace = "    ";
    public static final String EightSpace = "        ";

    private String NEW_LINE_SPACE = "    ";
    private String prefix = "";

    public NewLine(){}

    private NewLine(String prefix){
        this.prefix = prefix;
    }

    public StringBuilder startNewLine(StringBuilder stringBuilder){
        return stringBuilder.append(prefix);
    }

    public StringBuilder changeToNewLine(StringBuilder stringBuilder){
        return stringBuilder.append("\n")
                .append(prefix);
    }

    public NewLine addPrefix(){
        prefix = NEW_LINE_SPACE + prefix;
        return this;
    }

    public NewLine addPrefix(String addPrefix){
        prefix = addPrefix + prefix;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public NewLine clone() {
        return new NewLine(prefix);
    }
}
