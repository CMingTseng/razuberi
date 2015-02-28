Razuberi v0.5
=======

Simple, flexible and synchronous analogue of Android fragments.


Short info
=======

In Razuberi the application's UI is composed of `Screen`-objects. They are managed with `ScreensManager` or its subclasses.

`ScreensManager` is the most basic one, it supports only `add()`, `restoreAndAdd()`, `remove()`, `getStateAndRemove()` operations.
Methods `getStateAndRemove()` and `restoreAndAdd()` work with `ScreenState`-objects, `ScreenState` is a snapshot of a `Screen` at some point of time. It makes possible to remove `Screen` and add it later without loss its state. `ScreenState` implements `Parcelable` so it can be retained across `Activity` instances. There's a `Bundle` for any specific data that you want to retain, it can be obtained by calling `getPersistentData()` inside of the screen.

There's also `HistoryScreensManager` that supports flow of screens by keeping transitions history and using operations `replace()` and `popHistory()`. However it uses only public methods of basic `ScreensManager` so you can easily implement your own subclass of `ScreensManager` which would be the most suitable for your purposes.


Quick start
=======

At first subclass your `Activity` from `ScreensActivity<T extends ScreensManager>`, where `T` is an appropriate subclass of `ScreensManager`. Then you must implement 2 abstract methods:

1. `createScreensManager()` - to instantiate an appropriate subclass of  `ScreensManager`;
2. `setContentView()` - call `Activity`'s method `setContentView(..)` here instead of doing it in `onCreate()`, It lets ScreensManager to be re-instantiated synchronously when needed.

To obtain an instance of a created `ScreensManager` call `getScreensManager()`. Now you can start creating your screens. Subclass your screen from `Screen` and implement its abstract method `onAdd()`, there you must create and return your screen's `View`.

Also, take a look at [the sample](https://github.com/shchurov/razuberi/tree/master/razuberisamples/src/main/java/com/shchurov/razuberisamples/basic_sample). It shows how to work with animations.


License
--------

    Copyright 2015 Mykhailo Shchurov.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
