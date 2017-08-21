package de.wackernagel.android.sidekick.medias;

import android.content.Context;
import android.os.FileObserver;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import java.io.File;

class MediasLoader extends AsyncTaskLoader<File[]> {
    private File[] cachedMedias;
    private FileObserver observer;

    MediasLoader( @NonNull final Context context ) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if( cachedMedias != null) {
            deliverResult( cachedMedias );
        }

        if( observer == null ) {
            observer = new FileObserver( Medias.of( getContext() ).getMediasDirectory().getPath(),
                    FileObserver.CLOSE_WRITE | FileObserver.DELETE | FileObserver.MOVED_TO ) {
                @Override
                public void onEvent(int event, String path) {
                    onContentChanged();
                }
            };
            observer.startWatching();
        }

        if( takeContentChanged() || cachedMedias == null ) {
            forceLoad();
        }
    }

    @Override
    public File[] loadInBackground() {
        return Medias.of( getContext() ).getMedias();
    }

    @Override
    public void deliverResult(File[] medias) {
        cachedMedias = medias;

        if( isStarted() ) {
            super.deliverResult( medias );
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if( cachedMedias != null ) {
            cachedMedias = null;
        }

        if( observer != null ) {
            observer.stopWatching();
            observer = null;
        }
    }
}
