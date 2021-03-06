package github.tornaco.xposedmoduletest.xposed.submodules;

import android.os.Build;

import java.util.HashSet;
import java.util.Set;

import github.tornaco.xposedmoduletest.xposed.XAppBuildVar;
import github.tornaco.xposedmoduletest.xposed.util.XposedLog;
import lombok.Synchronized;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

public class AppGuardSubModuleManager {

    private static AppGuardSubModuleManager sMe;

    private final Set<SubModule> SUBS = new HashSet<>();


    private void addToSubsChecked(SubModule subModule) {
        String var = subModule.needBuildVar();
        if (var == null || XAppBuildVar.BUILD_VARS.contains(var)) {
            int minSDK = subModule.needMinSdk();
            if (Build.VERSION.SDK_INT >= minSDK) {
                SUBS.add(subModule);
            } else {
                XposedLog.boot("Skip submodule for min sdk not match: " + subModule.name());
            }
        } else {
            XposedLog.boot("Skip submodule for var not match: " + subModule.name());
        }
    }

    private AppGuardSubModuleManager() {
        addToSubsChecked(new FPSubModule());
        addToSubsChecked(new ScreenshotApplicationsSubModule());
        addToSubsChecked(new PackageInstallerSubModule());
        addToSubsChecked(new PMSSubModule());
        addToSubsChecked(new TaskMoverSubModuleDelegate());
        addToSubsChecked(new ActivityStartSubModuleDelegate());

        addToSubsChecked(new AMSSubModule12());
        addToSubsChecked(new AMSSubModule5());
        addToSubsChecked(new AMSSubModule4());
        addToSubsChecked(new AMSSubModule3());
        addToSubsChecked(new AMSSubModule2());
        addToSubsChecked(new AMSSubModule());
    }

    @Synchronized
    public
    static AppGuardSubModuleManager getInstance() {
        if (sMe == null) sMe = new AppGuardSubModuleManager();
        return sMe;
    }

    public Set<SubModule> getAllSubModules() {
        return SUBS;
    }
}
