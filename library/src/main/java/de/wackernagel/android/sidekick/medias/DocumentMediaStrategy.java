package de.wackernagel.android.sidekick.medias;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

class DocumentMediaStrategy extends AbstractMediaStrategy {

    DocumentMediaStrategy(@NonNull final Context context ) {
        super(context);
    }

    @NonNull
    @Override
    public Intent createIntent() {
        final Intent imagePickerIntent = new Intent( Intent.ACTION_GET_CONTENT);
        imagePickerIntent.setType( "image/*" );
        return imagePickerIntent;
    }

    @Override
    void processResult(@NonNull ResultInfo resultInfo) {
        new DocumentMediaTask( getContext(), resultInfo ).execute();
    }
}
