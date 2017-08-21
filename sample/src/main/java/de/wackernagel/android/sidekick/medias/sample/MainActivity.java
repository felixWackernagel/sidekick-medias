package de.wackernagel.android.sidekick.medias.sample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import de.wackernagel.android.sidekick.medias.Medias;
import de.wackernagel.android.sidekick.medias.ResultInfo;
import de.wackernagel.android.sidekick.medias.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements Medias.Callback, LoaderManager.LoaderCallbacks<File[]> {

    private static final int REQUEST_CODE_CAMERA = 0;
    private static final int REQUEST_CODE_GALLERY = 1;
    private static final int REQUEST_CODE_DOCUMENT = 2;

    private MediaAdapter adapter;

    @Override
    protected void onCreate( @Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);

        binding.cameraButton.setEnabled( Medias.of( this ).fromCamera().supported() );
        binding.galleryButton.setEnabled( Medias.of( this ).fromGallery().supported() );
        binding.documentButton.setEnabled( Medias.of( this ).fromDocument().supported() );

        binding.recyclerView.setHasFixedSize( true );
        binding.recyclerView.setLayoutManager( new GridLayoutManager(this, 3) );
        binding.recyclerView.setAdapter( adapter = new MediaAdapter( getSupportFragmentManager() ) );

        getSupportLoaderManager().initLoader( 0, null, this );
    }

    public void openCamera( View v ) {
        startActivityForResult( Medias.of( this ).fromCamera().createIntent(), REQUEST_CODE_CAMERA );
    }

    public void openGallery( View v ) {
        startActivityForResult( Medias.of( this ).fromGallery().createIntent(), REQUEST_CODE_GALLERY );
    }

    public void openDocument( View v ) {
        startActivityForResult( Medias.of( this ).fromDocument().createIntent(), REQUEST_CODE_DOCUMENT );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final ResultInfo resultInfo = new ResultInfo.Builder( resultCode, data )
                .callback( this )
                .build();

        switch( requestCode ) {
            case REQUEST_CODE_CAMERA:
                Medias.of( this ).fromCamera().result( resultInfo );
                break;

            case REQUEST_CODE_GALLERY:
                Medias.of( this ).fromGallery().result( resultInfo );
                break;

            case REQUEST_CODE_DOCUMENT:
                Medias.of( this ).fromDocument().result( resultInfo );
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onMediaResult( @NonNull File media ) {
        Toast.makeText( this, "Media saved", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onMediaCanceled() {
        Toast.makeText( this, "Action canceled", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onMediaError( @NonNull Exception exception ) {
        Toast.makeText( this, "Error during processing", Toast.LENGTH_SHORT ).show();
        Log.e( "Sidekick-Medias Sample", "Media result error.", exception );
    }

    @Override
    public Loader<File[]> onCreateLoader(int id, Bundle args) {
        return Medias.of( this ).getMediasLoader();
    }

    @Override
    public void onLoadFinished(Loader<File[]> loader, File[] data) {
        adapter.setData( data );
    }

    @Override
    public void onLoaderReset(Loader<File[]> loader) {
        adapter.setData( null );
    }
}
