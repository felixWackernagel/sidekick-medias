package de.wackernagel.android.sidekick.medias;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

abstract class AbstractMediaStrategy implements MediaStrategy {

    @NonNull
    private final Context context;

    AbstractMediaStrategy( @NonNull final Context context) {
        this.context = context;
    }

    @NonNull
    Context getContext() {
        return context;
    }

    @Override
    public boolean supported() {
        return createdSupportedIntent().resolveActivity( context.getPackageManager() ) != null;
    }

    @NonNull
    Intent createdSupportedIntent() {
        return createIntent();
    }

    @Override
    public void result( @NonNull ResultInfo resultInfo ) {
        if( resultInfo.getResultCode() == Activity.RESULT_CANCELED ) {
            final Medias.Callback callback = resultInfo.getCallback();
            if( callback != null ) {
                callback.onMediaCanceled();
            }
        } else {
            processResult( resultInfo );
        }
    }

    abstract void processResult( @NonNull ResultInfo resultInfo );
}
