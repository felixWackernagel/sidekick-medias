package de.wackernagel.android.sidekick;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import de.wackernagel.android.sidekick.medias.Medias;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith( AndroidJUnit4.class )
public class MediasInstrumentedTest {

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals( "de.wackernagel.android.sidekick.medias.test", appContext.getPackageName() );
    }

    @Test
    public void mediasDirectory() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        final Medias medias = Medias.of( appContext );
        assertNotNull( medias );

        final File directory = medias.getMediasDirectory();
        assertNotNull( directory );
        assertEquals( appContext.getFilesDir().getAbsolutePath() + "/medias", directory.getAbsolutePath() );
        assertEquals( medias.getMedias().length, directory.listFiles().length );
    }

    @Test
    public void mediasIntents() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        final Medias medias = Medias.of( appContext );
    }
}
