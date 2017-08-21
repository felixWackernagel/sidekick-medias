package de.wackernagel.android.sidekick.medias;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * A MediaStrategy is a simplified Interface to check the existence of a special external image
 * application, its launch {@link Intent} and result processing to receive an image.
 */
public interface MediaStrategy {

    /**
     * @return <tt>true</tt> if an application exist which handles the {@link Intent} from
     * {@link MediaStrategy#createIntent()} to launch an external image application.
     */
    boolean supported();

    /**
     * Build a {@link Intent} for a special external application.
     *
     * @return A {@link Intent} to launch a external image application.
     */
    @NonNull
    Intent createIntent();

    /**
     * This method handles the result from {@link android.app.Activity#onActivityResult(int, int, Intent)}.
     * It can only create a valid result when the {@link MediaStrategy#createIntent()} of the
     * same {@link MediaStrategy} was used.
     *
     * @param resultInfo A configuration object for the result processing.
     */
    void result( @NonNull ResultInfo resultInfo );

}
