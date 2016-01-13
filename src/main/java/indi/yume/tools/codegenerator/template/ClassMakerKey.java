package indi.yume.tools.codegenerator.template;

/**
 * Created by yume on 15/11/24.
 */
public interface ClassMakerKey {
    String KEY = "classMaker";

    String CLASS_VAR_NAME_ATTR = "classVarName";

    String TYPE = "type";
    String PACKAGE_NAME = "packageName";
    String CLASS_NAME = "className";
    String MODIFIER = "modifier";

    interface EXTENDS{
        String KEY = "extends";
        String NAME_ATTR = "classVarName";
    }

    interface IMPLEMENTS{
        String KEY = "implements";
        String NAME_ATTR = "classVarName";

        interface ITEM{
            String KEY = "item";
            String NAME_ATTR = "classVarName";
        }
    }

    interface NOTE{
        String KEY = "note";
    }

    interface PROPERTY{
        String KEY = "property";

        String CLASS_VAR_NAME_ATTR = "classVarName";
        String VALUE_NAME_ATTR = "valueName";
        String MODIFIER_ATTR = "modifier";
    }

    interface ANNO{
        String KEY = "anno";
        String CLASS_VAR_NAME_ATTR = "classVarName";

        interface PARAMS{
            String KEY = "param";

            String KEY_ATTR = "key";
            String VALUE_ATTR = "value";
            String CLASS_VALUE_ATTR = "classVarName";

            interface VALUE{
                String KEY = "value";
            }

            interface CLASS_VALUE{
                String KEY = "classVar";
            }
        }
    }

    interface METHOD{
        String KEY = "method";

        String RETURN_CLASS_NAME_ATTR = "returnClassName";
        String METHOD_NAME_ATTR = "methodName";
        String MODIFIER_ATTR = "modifier";

        interface PARAM{
            String KEY = "methodParam";

            String CLASS_VAR_NAME_ATTR = "classVarName";
            String VALUE_NAME_ATTR = "valueName";
        }

        interface BODY{
            String KEY = "body";
        }

        interface INCLUDE{
            String KEY = "include";

            String CLASS_VAR_NAME_ATTR = "classVarName";
        }
    }
}
