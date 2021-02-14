# EH
---

## (Uncaught exception handler for android.)


Program to handle and view details of all uncaught exceptions in android applications. An easy way to handle, view, share, and report app crashes quickly on android.

### FEATURES
------------

View crash logs on android easily and...

* Instantly view crash log
* Share crash log.
* Copy crash log.
* Save crash log.
* Detailed information in crash log.
* User Friendly

#### CRASH LOG INFO

* Device Information
* App Information
* Activity logs/traces
* Stack trace
* Crash date

Download the from below.

> [EH.zip](https://github.com/jorexdeveloper/EH/archive/main.zip)

![Screenshot](https://github.com/jorexdeveloper/EH/blob/main/img/screenshot_00.jpg) ![Screenshot](https://github.com/jorexdeveloper/EH/blob/main/img/screenshot_01.jpg)

### INSTALLATION

All you have to do is download and extract the zip (from above), then add the library to your project. See [here](https://www.google.com/) how to add library to project.

### USAGE

Initialize EH with a new `EH.Builder` instance, passing to it the application context...

```java
public class MyApplication extends Application {
    @Override public void onCreate() {
  
    // Create new Builder passing application context
   
    new EH.Builder(this)
        .init();
    ...
    }
}
```

or in `Activity` as

```java
public class MyActivity extends Activity {
    @Override public void onCreate(Bundle savedInstanceState) {
   
    // Create new Builder passing application context
   
    new EH.Builder(getApplicationContext())
        .init();
    ...
    }
}
```

### Methods

`public static final class EH.Builder (Context context)`

Method                                         | Default Parameters | Description
-----------------------------------------------|--------------------|--------------------------------------------------------------------------------------------------------------------
`enable (boolean enable)`                      | `true`             | Enable/disable EH. Exceptions are forwarded to Default Uncaught Exception Handler present before call to `init ()`.
`runInBackground (boolean runInBackground)`    | `true`             | Whether EH should function when app is currently stopped. (After call to `onStop ()`)
`addSuppressed (boolean add)`                  | `true`             | Whether to add suppressed exceptions to crash log.
`setMaxActivityLogs (int max)`                 | `100`              | Maximum number of activity logs/traces to include in crash log. (Last `max` logs are included)
`setMaxStackTraceSize (int size)`              | `100`              | Maximum number of stack traces to include in crash log. (First `size` stack traces are included)
`addEmailAddresses (String... emailAddresses)` | `none`             | Email addresses included in `Intent` to forward/send crash log to developers.
`init ()`                                      |  N/A               | Initialize/set up EH with the configurations above.

### Example

```java
public class MyApplication extends Application {
    @Override public void onCreate() {
    
    // Create new Builder passing application context
    
    new EH.Builder (this)
        .enable (true)
        .runInBackground (true)
        .setMaxActivityLogs (50)
        .setMaxStackTraceSize (50)
        .addEmailAddresses ("email1@gmail.com", "email2@gmail.com", "email3@gmail.com")
        .init();
    ...
    }
}
```

#### Author : Jore-X

#### Version : 1.0

#### License

```
Copyright © 2021 Jore

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
