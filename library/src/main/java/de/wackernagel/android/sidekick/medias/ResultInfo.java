package de.wackernagel.android.sidekick.medias;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A configuration class in builder style to change the image result processing.
 */
public class ResultInfo {

    /**
     * A annotation to make the <tt>resultCode</tt> type save.
     */
    @Retention( RetentionPolicy.SOURCE)
    @IntDef({Activity.RESULT_OK, Activity.RESULT_CANCELED, Activity.RESULT_FIRST_USER})
    public @interface Result {}

    @Result
    private final int resultCode;
    @Nullable
    private final Intent data;
    @Nullable
    private Medias.Callback callback;
    @Nullable
    private Medias.NamingStrategy namingStrategy;

    private ResultInfo( @Result final int resultCode,
                        @Nullable final Intent data,
                        @Nullable final Medias.Callback callback,
                        @Nullable final Medias.NamingStrategy namingStrategy ) {
        this.resultCode = resultCode;
        this.data = data;
        this.callback = callback;
        this.namingStrategy = namingStrategy;
    }

    /**
     * The <tt>resultCode</tt> can only be {@link Activity#RESULT_OK}, {@link Activity#RESULT_CANCELED} or {@link Activity#RESULT_FIRST_USER}.
     *
     * @return The resultCode after the {@link MediaStrategy#createIntent()} was launched.
     */
    @Result
    public int getResultCode() {
        return resultCode;
    }

    /**
     * @return A {@link Intent} which can contain result data.
     */
    @Nullable
    public Intent getData() {
        return data;
    }

    /**
     * @return A {@link Medias.Callback} to return information about success, error or cancellation.
     */
    @Nullable
    public Medias.Callback getCallback() {
        return callback;
    }

    /**
     * @return A generator for custom file names.
     */
    @Nullable
    public Medias.NamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    /**
     * Builder class to create a {@link ResultInfo}.
     */
    public static class Builder {

        @Result
        private final int resultCode;
        @Nullable
        private final Intent data;
        @Nullable
        private Medias.Callback callback;
        @Nullable
        private Medias.NamingStrategy namingStrategy;

        /**
         * Create a {@link ResultInfo} builder with mandatory fields.
         *
         * @param resultCode The <tt>resultCode</tt> argument from {@link Activity#onActivityResult(int, int, Intent)}.
         * @param data The <tt>data</tt> argument from {@link Activity#onActivityResult(int, int, Intent)}.
         */
        public Builder( @Result final int resultCode, @Nullable final Intent data ) {
            this.resultCode = resultCode;
            this.data = data;
        }

        /**
         * @param callback For errors, cancellations and results
         * @return this builder object
         */
        public Builder callback( Medias.Callback callback ) {
            this.callback = callback;
            return this;
        }

        /**
         * Change the art of media naming. The default strategy generates names by current timestamp.
         *
         * @param strategy A <tt>strategy</tt> for custom media names.
         * @return this builder object
         */
        public Builder namingStrategy( Medias.NamingStrategy strategy ) {
            this.namingStrategy = strategy;
            return this;
        }

        /**
         * @return A immutable {@link ResultInfo} object.
         */
        public ResultInfo build() {
            return new ResultInfo( resultCode, data, callback, namingStrategy );
        }

    }
}
