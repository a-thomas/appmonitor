## AppMonitor

Display in every given seconds on top of all apps the following values:

* the x top processes
* the battery level
* the native size and alloc for a given app
* the Dalvik size and alloc for a given app
* the number of activities in memory for a given app
* the number of views in memory for a given

AppMonitor get these information by using `dumpsys` service commands. Therefore it needs the `android.permission.DUMP` only settable through the package manager by typing `adb shell pm grant com.athomas.appmonitor android.permission.DUMP`.

### Features :

* Display top processes and their cpu usage
* Display battery level
* Save theses values in a file (easy to create a diagramm)


### TODO :

* Allow to change the filename/path
* Allow to modify the position of the monitor window


