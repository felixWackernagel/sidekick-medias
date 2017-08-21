package de.wackernagel.android.sidekick.medias.sample;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private File[] medias;

    private LayoutInflater layoutInflater;
    private FragmentManager supportFragmentManager;

    MediaAdapter(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    public void setData( @Nullable File[] medias ) {
        this.medias = medias;
        notifyDataSetChanged();
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MediaViewHolder( getLayoutInflater( parent ).inflate( R.layout.item_media, parent, false ) );
    }

    private LayoutInflater getLayoutInflater( View v ) {
        if( layoutInflater == null ) {
            layoutInflater = LayoutInflater.from( v.getContext() );
        }
        return layoutInflater;
    }

    @Override
    public void onBindViewHolder(final MediaViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int adapterPosition = holder.getAdapterPosition();
                if( adapterPosition != RecyclerView.NO_POSITION ) {
                    final File media = medias[ adapterPosition ];
                    ContextMenuBottomSheetDialog.newInstance(media).show(supportFragmentManager, "");
                }
            }
        });

        Glide.with( holder.itemView.getContext() )
             .fromFile()
             .load( medias[position] )
             .into( holder.image );
    }

    @Override
    public int getItemCount() {
        return medias != null ? medias.length : 0;
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        MediaViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById( R.id.image );
        }
    }

}
