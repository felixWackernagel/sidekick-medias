package de.wackernagel.android.sidekick.medias;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

class GalleryMediaStrategy extends AbstractMediaStrategy {

    GalleryMediaStrategy(@NonNull final Context context ) {
        super(context);
    }

    @NonNull
    @Override
    public Intent createIntent() {
        return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    @Override
    void processResult(@NonNull ResultInfo resultInfo) {
        new GalleryMediaTask( getContext(), resultInfo ).execute();
    }

}
