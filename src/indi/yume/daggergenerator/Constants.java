package indi.yume.daggergenerator;

import indi.yume.daggergenerator.model.AnnotationInfo;
import indi.yume.daggergenerator.model.ClazzInfo;
import indi.yume.daggergenerator.model.Type;

/**
 * Created by yume on 15/9/27.
 */
public interface Constants {
    AnnotationInfo inject = new AnnotationInfo("javax.inject", "Inject");
    AnnotationInfo provides = new AnnotationInfo("dagger", "Provides");
    AnnotationInfo override = new AnnotationInfo(null, "Override");

    ClazzInfo layoutInflater = new ClazzInfo("android.view", "LayoutInflater");
    ClazzInfo viewGroup = new ClazzInfo("android.view", "ViewGroup");
    ClazzInfo bundle = new ClazzInfo("android.os", "Bundle");
    ClazzInfo supportV4Fragment = new ClazzInfo("android.support.v4.app", "Fragment");
    ClazzInfo activity = new ClazzInfo("android.app", "Activity");
    ClazzInfo view = new ClazzInfo("android.view", "View");
    ClazzInfo voidClazz = new ClazzInfo(null, "void");
    ClazzInfo context = new ClazzInfo(Type.CLASS, "android.content", "Context");
    ClazzInfo butterknife = new ClazzInfo(Type.CLASS, "butterknife", "ButterKnife");

    AnnotationInfo activityScope = new AnnotationInfo("com.happy_bears.mybears.di", "ActivityScope");
    ClazzInfo appComponent = new ClazzInfo(Type.CLASS, "com.happy_bears.mybears.ui", "AppComponent");
    ClazzInfo R = new ClazzInfo(Type.CLASS, "com.happy_bears.mybears", "R");
}
