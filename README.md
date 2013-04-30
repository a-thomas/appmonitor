# AppMonitor

Display in every given seconds on top of all apps the following values:

* the x top processes
* the battery level
* the native size and alloc for a given app
* the Dalvik size and alloc for a given app
* the number of activities in memory for a given app
* the number of views in memory for a given app

Modifiable parameters from UI:

* Package name of the monitored application
* Max number of top processes
* Refresh interval


![Screenshot3](https://raw.github.com/a-thomas/appmonitor/master/screenshot.png)

AppMonitor get these information by using `dumpsys` service commands. Therefore it needs the `android.permission.DUMP` only settable through the package manager by typing `adb shell pm grant com.athomas.appmonitor android.permission.DUMP`.

**Dumpsys commands used:**

* `dumpsys meminfo <package_name>`
* `dumpsys cpuinfo`
* `dumpsys battery

## Important


The `grant` command seems to be unavailable on certain devices/versions. For example, on HTC and Samsung versions of Android, entering the above command will result to `Error: unknown command grant`. In this case, your device has to be rooted.


## TODO :

* Allow to change the filename/path
* Add more info
* Find a way to generate the diagramm automatically
