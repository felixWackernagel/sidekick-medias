package de.wackernagel.android.sidekick.medias.sample;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import de.wackernagel.android.sidekick.medias.Medias;

public class ContextMenuBottomSheetDialog extends BottomSheetDialogFragment {

    public static ContextMenuBottomSheetDialog newInstance(@NonNull  final File media ) {
        final ContextMenuBottomSheetDialog dialog = new ContextMenuBottomSheetDialog();
        final Bundle arguments = new Bundle( 1 );
        arguments.putString( "media", media.getAbsolutePath() );
        dialog.setArguments( arguments );
        return dialog;
    }

    @Override
    public void setupDialog(final Dialog sheetDialog, int style) {
        @SuppressLint("InflateParams")
        final View sheet = sheetDialog.getLayoutInflater().inflate( R.layout.dialog_context_menu, null );
        sheetDialog.setContentView( sheet );

        final File media = getMedia();
        if( media != null ) {
            final TextView header = ( TextView ) sheet.findViewById(R.id.header);
            header.setText( sheet.getContext().getString( R.string.file_headline, media.getName(), media.length() / 1024 ) );

            sheet.findViewById( R.id.action_share ).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(Intent.createChooser( Medias.of( v.getContext() ).share( media ), "Open with" ) );
                    sheetDialog.dismiss();
                }
            });

            sheet.findViewById( R.id.action_delete ).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();
                    showOkCancelDialog(context, "Delete", "Should this file be deleted?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if( media.delete() ) {
                                Toast.makeText( context, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                            sheetDialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    @Nullable
    private File getMedia() {
        final Bundle arguments = getArguments();
        if( arguments == null ) {
            return null;
        }
        final String mediaPath = arguments.getString( "media" );
        if( mediaPath == null ) {
            return null;
        }
        return new File( mediaPath );
    }

    private void showOkCancelDialog(final Context context, final String okLabel, final String message, final DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder( context )
                .setMessage(message)
                .setPositiveButton( okLabel, okListener )
                .setNegativeButton( "Cancel", null )
                .create()
                .show();
    }

}
