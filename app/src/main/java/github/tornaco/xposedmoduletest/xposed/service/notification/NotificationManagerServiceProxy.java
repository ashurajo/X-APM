package github.tornaco.xposedmoduletest.xposed.service.notification;

import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.google.common.collect.Lists;

import java.util.ArrayList;

import de.robv.android.xposed.XposedHelpers;
import github.tornaco.xposedmoduletest.xposed.util.XposedLog;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * Created by guohao4 on 2018/1/4.
 * Email: Tornaco@163.com
 */
@AllArgsConstructor
@ToString
public class NotificationManagerServiceProxy {

    private final ArrayList<StatusBarNotification> EMPTY_SBN = Lists.newArrayListWithCapacity(0);

    private Object notificationManagerService;

    public ArrayList<StatusBarNotification> getStatusBarNotifications() {
        try {
            Object listObj = XposedHelpers.getObjectField(notificationManagerService, "mNotificationList");
            XposedLog.debug("getStatusBarNotifications, listObj: " + listObj);
            if (listObj == null) return EMPTY_SBN;
            ArrayList notificationRecordList = (ArrayList) listObj;
            XposedLog.debug("getStatusBarNotifications, notificationRecordList: " + notificationRecordList);
            if (notificationRecordList.size() == 0) return EMPTY_SBN;

            ArrayList<StatusBarNotification> res = new ArrayList<>();

            for (Object nr : notificationRecordList) {
                try {
                    StatusBarNotification sbn = (StatusBarNotification) XposedHelpers.getObjectField(nr, "sbn");
                    XposedLog.debug("getStatusBarNotifications, sbn: " + sbn);
                    if (sbn != null) {
                        res.add(sbn);
                    }
                } catch (Throwable e) {
                    XposedLog.wtf("Fail retrieve sbn from nr:" + Log.getStackTraceString(e));
                }
            }

            return res;
        } catch (Throwable e) {
            XposedLog.wtf("Fail getStatusBarNotifications:" + Log.getStackTraceString(e));
            return EMPTY_SBN;
        }
    }
}
