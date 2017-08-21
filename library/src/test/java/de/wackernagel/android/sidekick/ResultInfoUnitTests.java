package de.wackernagel.android.sidekick;

import android.app.Activity;
import android.support.annotation.NonNull;

import junit.framework.Assert;

import org.junit.Test;

import java.io.File;

import de.wackernagel.android.sidekick.medias.Medias;
import de.wackernagel.android.sidekick.medias.ResultInfo;

public class ResultInfoUnitTests {

    @Test
    public void resultInfoBuilder_mandatory() throws Exception {
        final ResultInfo resultInfo = new ResultInfo.Builder(Activity.RESULT_OK, null ).build();
        Assert.assertEquals( resultInfo.getResultCode(), Activity.RESULT_OK );
        Assert.assertNull( resultInfo.getData() );
        Assert.assertNull( resultInfo.getNamingStrategy() );
        Assert.assertNull( resultInfo.getCallback() );
    }

    @Test
    public void resultInfoBuilder_optionals() throws Exception {
        final String uniqueName = "foo";
        final ResultInfo resultInfo = new ResultInfo.Builder(Activity.RESULT_OK, null )
                .callback(new Medias.Callback() {
                    @Override
                    public void onMediaResult(@NonNull File media) {

                    }

                    @Override
                    public void onMediaCanceled() {

                    }

                    @Override
                    public void onMediaError(@NonNull Exception exception) {

                    }
                })
                .namingStrategy(new Medias.NamingStrategy() {
                    @NonNull
                    @Override
                    public String createUniqueName() {
                        return uniqueName;
                    }
                })
                .build();
        Assert.assertNotNull( resultInfo.getNamingStrategy() );
        Assert.assertEquals( uniqueName, resultInfo.getNamingStrategy().createUniqueName() );
        Assert.assertNotNull( resultInfo.getCallback() );
    }

}
