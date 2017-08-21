package de.wackernagel.android.sidekick.medias;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.InputStream;

class GalleryMediaTask extends MediaTask {

    GalleryMediaTask(@NonNull Context context, @NonNull ResultInfo resultInfo) {
        super(context, resultInfo);
    }

    @NonNull
    @Override
    File performTaskInBackground(@Nullable Intent data) throws Exception {
        if( data == null ) {
            throw new Exception( "No media can be found because the result intent is null." );
        }

        final Uri contentUri = data.getData();
        final InputStream stream = getInputStream( getContext(), contentUri );
        if( stream == null ) {
            throw new Exception( "The media from gallery can not be found." );
        }

        final File media = createNewMedia();
        if( saveInputStream( stream, media ) ) {
            return media;
        } else {
            throw new Exception( "It was not possible to save the selected image." );
        }
    }

}
