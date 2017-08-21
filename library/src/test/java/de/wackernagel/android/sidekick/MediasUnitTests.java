package de.wackernagel.android.sidekick;

import android.content.Context;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import de.wackernagel.android.sidekick.medias.Medias;

@RunWith(MockitoJUnitRunner.class)
public class MediasUnitTests {

    @Mock
    Context mMockContext;

    @Before
    public void before_test() throws Exception {
        final File appDirectory = new File( "testApp" );
        Mockito.when( mMockContext.getApplicationContext() ).thenReturn( mMockContext );
        Mockito.when( mMockContext.getPackageName() ).thenReturn( "com.android.sample" );
        Mockito.when( mMockContext.getFilesDir() ).thenReturn( appDirectory );
    }

    @After
    public void after_test() {
        final File medias = new File( "testApp/medias" );
        if( medias.listFiles() != null )
            for( File media : medias.listFiles() )
                media.delete();

        if( medias.exists() )
            medias.delete();
    }

    @Test
    public void medias_directory() throws Exception {
        final File mediasDirectory = Medias.of( mMockContext ).getMediasDirectory();
        Assert.assertTrue( mediasDirectory.getAbsolutePath().endsWith( "/testApp/medias" ) );
    }

    @Test
    public void medias_empty() throws Exception {
        final File[] medias = Medias.of( mMockContext ).getMedias();
        Assert.assertEquals( 0, medias.length );
    }

    @Test
    public void medias_one() throws Exception {
        final Medias medias = Medias.of( mMockContext );
        sampleFile( medias.getMediasDirectory(), "foo" );

        final File[] mediaFiles = medias.getMedias();
        Assert.assertEquals( 1, mediaFiles.length );
        Assert.assertEquals( "foo.jpg", mediaFiles[0].getName() );
    }

    @Test
    public void medias_source() throws Exception {
        final Medias medias = Medias.of( mMockContext );
        Assert.assertNotNull( medias.fromCamera() );
        Assert.assertNotNull( medias.fromDocument() );
        Assert.assertNotNull( medias.fromGallery() );
    }

    private File sampleFile( File directory, String name ) throws Exception {
        final File mediaFile = new File( directory, name + ".jpg" );
        mediaFile.createNewFile();
        return mediaFile;
    }
}
