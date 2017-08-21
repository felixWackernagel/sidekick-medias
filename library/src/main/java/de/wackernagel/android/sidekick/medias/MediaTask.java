package de.wackernagel.android.sidekick.medias;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

abstract class MediaTask extends AsyncTask<Void, Void, MediaTask.MediaTaskResult> {

    static class MediaTaskResult {

        private final File media;
        private final Exception exception;

        MediaTaskResult( final File media ) {
            this.media = media;
            this.exception = null;
        }

        MediaTaskResult( final Exception exception ) {
            this.media = null;
            this.exception = exception;
        }

        @Nullable
        File getMedia() {
            return media;
        }

        @Nullable
        Exception getException() {
            return exception;
        }
    }

    @NonNull
    private final Context context;
    @Nullable
    private final Intent data;
    @NonNull
    private final WeakReference<Medias.Callback> callback;
    @Nullable
    private final Medias.NamingStrategy namingStrategy;

    private final Medias.NamingStrategy timestampNamingStrategy = new Medias.NamingStrategy() {
        @NonNull
        @Override
        public String createUniqueName() {
            final Date now = new Date();
            final String dateTimePattern = "yyyy-MM-dd_HH-mm-ss";
            return new SimpleDateFormat( dateTimePattern, Locale.getDefault() ).format( now );
        }
    };

    MediaTask( @NonNull final Context context, @NonNull final ResultInfo resultInfo ) {
        this.context = context;
        this.data = resultInfo.getData();
        this.callback = new WeakReference<>( resultInfo.getCallback() );
        this.namingStrategy = resultInfo.getNamingStrategy();
    }

    @Override
    final protected MediaTaskResult doInBackground(Void... params) {
        try {
            final File media = performTaskInBackground( data );
            return success( media );
        } catch( Exception exception ) {
            return error( exception );
        }
    }

    @NonNull
    abstract File performTaskInBackground( @Nullable Intent data ) throws Exception;

    @Override
    final protected void onPostExecute( MediaTaskResult result ) {
        final Medias.Callback callback = this.callback.get();
        if( callback == null ) {
            return;
        }

        if( result.getMedia() != null ) {
            callback.onMediaResult( result.getMedia() );
        } else if( result.getException() != null ) {
            callback.onMediaError( result.getException() );
        }
    }

    @NonNull
    Context getContext() {
        return context;
    }

    @NonNull
    private MediaTaskResult error( @NonNull final Exception exception ) {
        return new MediaTaskResult( exception );
    }

    @NonNull
    private MediaTaskResult success(@NonNull final File media ) {
        return new MediaTaskResult( media );
    }

    File createNewMedia() throws Exception {
        final String mediaName = getNamingStrategy().createUniqueName();
        final File media = new File( Medias.of( context ).getMediasDirectory(), mediaName.concat( ".jpg" ) );
        if( media.exists() ) {
            throw new Exception( "A file with name '" + mediaName + "' already exists. The file name must be unique." );
        }
        return media;
    }

    private Medias.NamingStrategy getNamingStrategy() {
        if( namingStrategy != null ) {
            return namingStrategy;
        } else {
            return timestampNamingStrategy;
        }
    }

    boolean saveBitmapInFile(@NonNull final Bitmap bitmap, @NonNull final File media ) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream( media );
            return bitmap.compress( Bitmap.CompressFormat.JPEG, 75, fileOutputStream );
        } catch( FileNotFoundException e ) {
            return false;
        } finally {
            closeStream( fileOutputStream );
        }
    }

    boolean saveInputStream(final InputStream inputStream, final File media) {
        OutputStream outputStream = null;
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            outputStream = new FileOutputStream(media);
            outputStream.write(buffer);

            return true;
        } catch( IOException e ) {
            return false;
        } finally {
            closeStream(inputStream);
            closeStream(outputStream);
        }
    }

    /**
     * Close a stream silently.
     *
     * @param stream to close
     */
    private static void closeStream( @Nullable final Closeable stream ) {
        if( stream != null ) {
            try {
                stream.close();
            } catch( IOException e ) {
                // catch exception silently
            }
        }
    }

    /**
     * @param context of the application
     * @param contentUri to a file
     * @return A Bitmap behind the contentUri or null when the file does not exist or a decoding error occurred.
     */
    @Nullable
    static Bitmap getBitmap(@NonNull final Context context, @NonNull final Uri contentUri) {
        final InputStream inputStream = getInputStream( context, contentUri );
        if( inputStream == null )
            return null;

        final Bitmap bitmap = BitmapFactory.decodeStream( inputStream );
        closeStream( inputStream );
        return bitmap;
    }

    /**
     * @param context of the application.
     * @param contentUri to the file
     * @return A InputStream to the given contentUri or null if file was not found.
     */
    @Nullable
    static InputStream getInputStream( @NonNull final Context context, @NonNull final Uri contentUri) {
        try {
            return context.getContentResolver().openInputStream( contentUri );
        } catch( FileNotFoundException e ) {
            return null;
        }
    }
}
