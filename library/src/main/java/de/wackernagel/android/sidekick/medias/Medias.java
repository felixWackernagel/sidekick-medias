package de.wackernagel.android.sidekick.medias;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;

import java.io.File;

/**
 * Medias is a central access point to different image sources like camera or gallery.
 * It copies a selected image in the internal application files directory.
 * In this way is no permission required.
 */
public class Medias {

    @NonNull
    private final Context applicationContext;

    private Medias( @NonNull final Context context ) {
        this.applicationContext = context;
    }

    /**
     * Create a {@link Medias} object to access application images or copy external images.
     *
     * @param context A {@link Context} for storage and system access.
     * @return A {@link Medias} object to access image sources.
     */
    @NonNull
    public static Medias of( @NonNull final Context context ) {
        return new Medias( context.getApplicationContext() );
    }

    /**
     * Creates a {@link MediaStrategy} to access a camera as image source.
     *
     * @return A {@link MediaStrategy} to access a camera application.
     */
    @NonNull
    public MediaStrategy fromCamera() {
        return new CameraMediaStrategy( applicationContext );
    }

    /**
     * Creates a {@link MediaStrategy} to access a gallery as image source.
     *
     * @return A {@link MediaStrategy} to access a gallery application.
     */
    @NonNull
    public MediaStrategy fromGallery() {
        return new GalleryMediaStrategy( applicationContext );
    }

    /**
     * Creates a {@link MediaStrategy} to access a documents provider as image source.
     *
     * @return A {@link MediaStrategy} to access a documents provider application.
     */
    @NonNull
    public MediaStrategy fromDocument() {
        return new DocumentMediaStrategy( applicationContext );
    }

    /**
     * Creates a ACTION_SEND {@link Intent} which contains information about a given media {@link File}.
     * The {@link File} can be accessed through a {@link FileProvider} with read permission.
     *
     * @param media A {@link File} which is shared.
     * @return A share {@link Intent} with information about the media.
     */
    @NonNull
    public Intent share( @NonNull final File media ) {
        final String authority = applicationContext.getPackageName().concat( ".medias" );
        final Uri mediaUri = FileProvider.getUriForFile( applicationContext, authority, media );

        final Intent shareIntent = new Intent( Intent.ACTION_SEND );
        shareIntent.setDataAndType( mediaUri, applicationContext.getContentResolver().getType( mediaUri ) );
        shareIntent.putExtra( Intent.EXTRA_STREAM, mediaUri );
        shareIntent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
        return shareIntent;
    }

    /**
     * Creates a directory inside the application files which contain all image files from different image sources.
     *
     * @return A {@link File} as directory which contain all images from different sources.
     */
    @NonNull
    public File getMediasDirectory() {
        final File directory = new File( applicationContext.getFilesDir(), "medias" );
        if( !directory.exists() ) {
            directory.mkdirs();
        }
        return directory;
    }

    /**
     * Get all image files from medias directory.
     * @return A {@link File} array of images.
     */
    @NonNull
    public File[] getMedias() {
        return getMediasDirectory().listFiles();
    }

    /**
     * Create a {@link Loader} to get all medias with {@link Medias#getMedias()} but asynchronous.
     * This {@link Loader} observes the medias directory and reloads all files if something has changed.
     *
     * @return A {@link Loader} with an array of {@link File} as result.
     */
    @NonNull
    public Loader<File[]> getMediasLoader() {
        return new MediasLoader( applicationContext );
    }

    /**
     * The Callback interface should be implemented to receive a {@link File} as successful result,
     * an {@link Exception} in case of a processing error or a cancellation by the user.
     */
    @MainThread
    public interface Callback {

        /**
         * This method is called if everything was successful.
         *
         * @param media A {@link File} which was successful copied from a external source.
         */
        void onMediaResult( @NonNull File media );

        /**
         * This method is called if the user cancelled the external media application.
         */
        void onMediaCanceled();

        /**
         * This method is called if something goes wrong. Check {@link Exception#getMessage()} to
         * get more details about the error.
         *
         * @param exception A {@link Exception} which was thrown during image processing.
         */
        void onMediaError( @NonNull Exception exception );

    }

    /**
     * A configuration class to create a custom {@link File} name for the copied external images.
     */
    public interface NamingStrategy {

        /**
         * Create a unique file name for a copied image file of a external image source.
         * The image is stored in a central directory which requires a unique name.
         *
         * @return A name without file extension or path information ie. picture_01.
         */
        @NonNull
        String createUniqueName();
    }
}
