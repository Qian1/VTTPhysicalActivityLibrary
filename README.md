# VTT Physical Activity Library for Android 
 
 
## 1. Introduction 
 
VTT Physical Activity Library for Android provides the following detection methods: 
 
* Run and walk detection 
* Potential fall detection 
* Stability detection 
* Orientation detection 
 
 
## 2. Installation and configuration 
 
* PhysicalActivityClient/ - contains a simple example application that uses the library. 
* PhysicalActivityLibrary/ - contains the library code. 
 
1. If not installed already, install [Eclipse](http://www.eclipse.org/downloads/) and [Android tools](http://developer.android.com/sdk/installing.html). Android API level 10 and higher are supported, so it might be a good idea to start with API level 10 (2.3.3). 
 
2. If not created already, create a new workspace in Eclipse. 
 
3. Import PhysicalActivityClient into the workspace: 
 
        "File" -> "Import..." -> "General" -> "Existing Projects into Workspace" -> click "Next >" 
 
        "Select root directory:" -> click "Browse..." -> navigate to "PhysicalActivityClient" -> press "OK" -> press "Finish" 
 
4. Import PhysicalActivityLibrary into the workspace, do the same as in the previous step, but select PhysicalActivityLibrary instead. 
 
5. Connect your Android device to the computer (if the device does not show up, you might need to install device specific debug drivers, for example, for HTC devices [HTC Sync](http://www.htc.com/www/support/) needs to be installed). 
 
6. Clean and build both projects. 
 
7. Run PhysicalActivityClient in the mobile device (emulator not supported): 
 
        Right click on the "PhysicalActivityClient" -> "Run As" (or "Debug As", if you prefer) -> "Android Application" 
 
 
## 3. Documentation 
 
Usage, see PhysicalActivityClient: 
 
* fi.vtt.physicalactivityclient.PhysicalActivityClientActivity.java 
* fi.vtt.physicalactivityclient.PhysicalActivityClientService.java 
 
PhysicalActivityClientActivity is the main entry point in this example. 
 
For more description, see the documentation: 
 
	Javadoc - see Documentation/Javadoc/index.html 
 
 
## 4. Known problems and solutions 
 
If you get messages, such as: 
 
	"Must override a super class." 
 
	"Android requires compiler compliance level 5.0 or 6.0. Found '1.4' instead. Please use Android Tools > Fix Project Properties." 
 
Set compliance level to, for example, 1.6: 
 
	"Project" -> "Properties" -> "Java Compiler" -> "Compiler compliance level: 1.6" 
 
Clean and build both projects again. 
 
 
## 5. License 
 
VTT Physical Activity Library is available under the BSD License. 
 
 
## 6. Contact 
 
No particular support is provided, but questions, suggestions etc. can be emailed to [cauit@vtt.fi](mailto:cauit@vtt.fi). 
 
 