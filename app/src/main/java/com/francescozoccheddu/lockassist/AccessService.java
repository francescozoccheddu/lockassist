package com.francescozoccheddu.lockassist;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

public class AccessService extends AccessibilityService {

    private static final String ACTION = "lock";

    public static boolean isEnabled(Context context) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName())
                    && enabledServiceInfo.name.equals(AccessService.class.getName()))
                return true;
        }
        return false;
    }

    public static void fire(Context context) {
        Log.d(AccessService.class.getName(), "Fire");
        AccessibilityManager manager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (manager.isEnabled()) {
            AccessibilityEvent event = AccessibilityEvent.obtain();
            event.setEventType(AccessibilityEvent.TYPE_ANNOUNCEMENT);
            event.setPackageName(context.getPackageName());
            event.setClassName(AccessService.class.getName());
            event.getText().add(ACTION);
            event.setEnabled(true);
            manager.sendAccessibilityEvent(event);
        } else {
            Log.e(AccessService.class.getName(), "Cannot fire");
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (getApplicationContext().getPackageName().equals(event.getPackageName())) {
            if (AccessService.class.getName().equals(event.getClassName()) && event.getText().contains(ACTION)) {
                lock();
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    private void lock() {
        Log.d(AccessService.class.getName(), "Lock");
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN);
    }

}
