# Sidekick - Medias
Take and pick photos from camera, gallery or documents provider. Each photo is stored as a copy inside the application.
This has the advantage that you can easy delete images and you need no additional permissions.

## Installation
```gradle
dependencies {
    ...
    compile 'de.wackernagel.android:sidekick-medias:1.0.0'
    ...
}
```

## Usage
```java
Medias.of( this ).fromCamera().supported()
Medias.of( this ).fromCamera().createIntent()
Medias.of( this ).fromGallery().supported()
Medias.of( this ).fromGallery().createIntent()
Medias.of( this ).fromDocument().supported()
Medias.of( this ).fromDocument().createIntent()

final ResultInfo resultInfo = new ResultInfo.Builder( resultCode, data )
                .callback( this )
                .build();
Medias.of( this ).fromCamera().result( resultInfo );
Medias.of( this ).fromGallery().result( resultInfo );
Medias.of( this ).fromDocument().result( resultInfo );
Medias.of( this ).getMediasLoader();
Medias.of( this ).getMedias();
Medias.of( this ).getMediasDirectory();

```

## How it works

## For Development
Following gradle task can be executed:
```gradle
# create a zip archive which can be uploaded at bintray
./gradlew bintray

# create a code coverage HTML file
./gradlew jacoco
```
