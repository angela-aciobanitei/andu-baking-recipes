# Baking App

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0d6eab1bb62144908463f03a4dcdffb4)](https://app.codacy.com/manual/angela-aciobanitei/andu-baking-recipes?utm_source=github.com&utm_medium=referral&utm_content=angela-aciobanitei/andu-baking-recipes&utm_campaign=Badge_Grade_Settings)

An app that displays recipes from a network resource. When user selects a recipe, its ingredients and video-guided steps are shown.

## What Will I Learn?
*   Use [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/) to implement the [MVVM](https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b) architecture pattern.
*   Handle network requests with [Retrofit](https://github.com/square/retrofit) and [OkHttp](https://github.com/square/okhttp).
*   Persist app data with [Room](https://developer.android.com/topic/libraries/architecture/room).
*   Display videos with [ExoPlayer](https://exoplayer.dev/hello-world.html). 
*   Properly initialize and release video assets.
*   Create a home screen [widget](https://developer.android.com/guide/topics/appwidgets).
*   Use Fragments to create a responsive design that works on phones and tablets.
*   Make use of [Espresso](https://developer.android.com/training/testing/espresso/) to test aspects of the UI.
*   Handle dependency injection with [Dagger](https://github.com/google/dagger).

## Libraries
*   [Room](https://developer.android.com/topic/libraries/architecture/room) for data persistence.
*   [Retrofit 2](https://github.com/square/retrofit) and [OkHttp](https://github.com/square/okhttp) for networking.
*   [Gson](https://github.com/google/gson) for parsing JSON.
*   [Glide](https://github.com/bumptech/glide) for image loading.
*   [ExoPlayer](https://github.com/google/ExoPlayer) for streaming videos.
*   [Timber](https://github.com/JakeWharton/timber) for logging.
*   [Dagger 2](https://github.com/google/dagger) for dependency injection.
*   [Mockito](https://github.com/mockito/mockito), a mocking framework for Java.
*   [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver), a scriptable web server for testing HTTP clients.
*   [Espresso](https://developer.android.com/training/testing/espresso/), a testing framework for Android UI tests.

## Screenshots Phone
<img src="/screenshots/phone_recipe_list.png" width="250"/> <img src="/screenshots/phone_recipe_details.png" width="250"/> 
<img src="/screenshots/phone_step_details.png" width="250"/> 

On landscape mode recipe video takes up the full screen:

<img src="/screenshots/phone_step_details_land.png" width="500"/>

## Screenshots Tablet
<img src="/screenshots/tablet_details_port2.png" width="400"/>
