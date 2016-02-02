# BoundLayout

A lightweight FrameLayout which can have a maximum / minimum width and height, simple as that.

[![Download](https://api.bintray.com/packages/flschweiger/maven/boundlayout/images/download.svg) ](https://bintray.com/flschweiger/maven/boundlayout/_latestVersion) 
[![License Apache](https://img.shields.io/badge/license-Apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

![Demo screen](https://github.com/flschweiger/BoundLayout/blob/master/art/screen1.jpg)  

## Attributes ##

*All attributes are optional.*

`maxWidth` specifies the maximum width of the layout.

`maxHeight` specifies the maximum height of the layout.

`minWidth` specifies the minimum width of the layout.

`minHeight` specifies the minimum height of the layout.

## QuickStart ##
### Include the Gradle dependency ###

```java
dependencies {
    compile 'link.fls:boundlayout:1.0.0'
}
```

### Use it in your layout file ###
1. Use the `link.fls.boundlayout` view in your XML layout file 
2. That's it!

*Example:*

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <link.fls.BoundLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="center"
        app:maxWidth="450dp"
        app:maxHeight="400dp">
        
        <!-- The content in here does not get larger than 450dp * 400dp -->

    </link.fls.BoundLayout>

</FrameLayout>
```

## Copyright Notice ##
``` 
Copyright (C) 2016 Frederik Schweiger

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
