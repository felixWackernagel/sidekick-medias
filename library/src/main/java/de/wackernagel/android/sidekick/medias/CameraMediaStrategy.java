package de.wackernagel.android.sidekick.medias;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import java.io.File;

class CameraMediaStrategy extends AbstractMediaStrategy {

    CameraMediaStrategy( @NonNull final Context context ) {
        super(context);
    }

    @NonNull
    @Override
    public Intent createIntent() {
        final File media = new File( getContext().getCacheDir(), "reserved.jpg" );
        if( media.exists() ) {
            media.delete();
        }

        final String authority = getContext().getPackageName().concat( ".medias" );
        final Uri mediaUri = FileProvider.getUriForFile( getContext(), authority, media );

        final Intent cameraIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, mediaUri );
        cameraIntent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
        cameraIntent.addFlags( Intent.FLAG_GRANT_WRITE_URI_PERMISSION );
        return cameraIntent;
    }

    @NonNull
    @Override
    Intent createdSupportedIntent() {
        return new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
    }

    @Override
    void processResult(@NonNull ResultInfo resultInfo) {
        new CameraMediaTask( getContext(), resultInfo ).execute();
    }
}
