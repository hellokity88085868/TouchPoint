#ifeq ($(strip $(CONFIG_TINNO_GESTURE)), yes)
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_STATIC_JAVA_LIBRARIES := gson v4 v7

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := TouchPoint

LOCAL_CERTIFICATE := platform
include $(BUILD_PACKAGE)

##########################
include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := gson:libs/gson-2.2.4.jar \
					v4:libs/android-support-v4.jar \
 					v7:libs/android-support-v7-appcompat.jar

include $(BUILD_MULTI_PREBUILT)

include $(call all-makefiles-under,$(LOCAL_PATH))
#endif
