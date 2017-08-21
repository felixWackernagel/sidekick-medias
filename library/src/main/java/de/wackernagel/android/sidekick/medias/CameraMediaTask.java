package de.wackernagel.android.sidekick.medias;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

class CameraMediaTask extends MediaTask {

    CameraMediaTask( @NonNull final Context context, @NonNull final ResultInfo resultInfo ) {
        super( context, resultInfo );
    }

    @NonNull
    @Override
    File performTaskInBackground( @Nullable final Intent data ) throws Exception {
        final File media = createNewMedia();
        final File reservedMedia = new File( getContext().getCacheDir(), "reserved.jpg" );
        if( !reservedMedia.renameTo( media ) ) {
            throw new Exception( "It was not possible to apply the file name '" + media.getName() + "' to the temporary camera picture." );
        }
        return media;
    }

}
