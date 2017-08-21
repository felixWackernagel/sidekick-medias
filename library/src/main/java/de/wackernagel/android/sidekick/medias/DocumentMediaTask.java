package de.wackernagel.android.sidekick.medias;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

class DocumentMediaTask extends MediaTask {

    DocumentMediaTask(@NonNull Context context, @NonNull ResultInfo resultInfo) {
        super(context, resultInfo);
    }

    @NonNull
    @Override
    File performTaskInBackground(@Nullable Intent data) throws Exception {
        if( data == null ) {
            throw new Exception( "No result data to create media." );
        }

        final Uri contentUri = data.getData();
        final Bitmap mediaBitmap = getBitmap( getContext(), contentUri );
        if( mediaBitmap == null ) {
            throw new Exception( "The media from storage can not be found or decoded." );
        }

        final File media = createNewMedia();
        if( saveBitmapInFile( mediaBitmap, media) ) {
            return media;
        } else {
            throw new Exception( "File was not found or compression failed." );
        }
    }

}
