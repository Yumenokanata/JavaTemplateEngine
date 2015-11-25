package indi.yume.daggergenerator.template;

/**
 * Created by yume on 15/11/23.
 */
public interface ConfigItemsKey {
    interface CLASS_ITEM extends BASE_CLASS_ITEM{
        String KEY = "class";
    }

    interface ANNOTATION_ITEM extends BASE_CLASS_ITEM{
        String KEY = "annotation";
    }

    interface INTERFACE_ITEM extends BASE_CLASS_ITEM{
        String KEY = "interface";
    }

    public interface BASE_CLASS_ITEM{
        String NAME = "name";
        String PACKAGE_NAME = "packageName";
        String CLASS_NAME = "className";
        String MODIFIER = "modifier";

        String IS_INNER = "isInner";
    }

    interface GENERIC_ITEM{
        String KEY = "generic";
        String NAME = "classVarName";

        interface ITEM{
            String KEY = "item";

            String NAME = "classVarName";
        }
    }
}
