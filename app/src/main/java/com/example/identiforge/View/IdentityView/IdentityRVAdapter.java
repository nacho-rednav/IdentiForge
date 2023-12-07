package com.example.identiforge.View.IdentityView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.identiforge.Model.Helper;
import com.example.identiforge.Model.Identity;
import com.example.identiforge.R;

public class IdentityRVAdapter extends ListAdapter<Identity, IdentityRVAdapter.ViewHolder> {

    private IdentityListView parent;

    protected IdentityRVAdapter(IdentityListView parent) {
        super(DIFF_CALLBACK);
        this.parent = parent;
    }

    private static final DiffUtil.ItemCallback<Identity> DIFF_CALLBACK = new DiffUtil.ItemCallback<Identity>() {
        @Override
        public boolean areItemsTheSame(Identity oldItem, Identity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Identity oldItem, Identity newItem) {
            // below line is to check the course name, description and course duration.
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    newItem.getLevelUp() == oldItem.getLevelUp() &&
                    newItem.getLevel() == oldItem.getLevel() &&
                    newItem.getPoints() == oldItem.getPoints();
        }
    };



    @NonNull
    @Override
    public IdentityRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.identity_row, parent, false);

        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull IdentityRVAdapter.ViewHolder holder, int position) {
        Identity identity = getItem(holder.getAdapterPosition());

        holder.titleTV.setText(identity.getTitle());
        holder.levelTV.setText("Level: " + identity.getLevel());
        holder.pointsTV.setText(Helper.formatPoints(identity.getPoints(), identity.getLevelUp()));
        if(identity.canLevelUp()){
            holder.imageView.setImageResource(R.drawable.green_upgrade);
        }
        else{
            holder.imageView.setImageResource(R.drawable.upgrade_circle);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Identity i = getItem(holder.getAdapterPosition());
                parent.levelUp(getItem(holder.getAdapterPosition()));
                if(i.getPoints() - i.getLevelUp() < i.getLevelUp())
                    holder.imageView.setImageResource(R.drawable.upgrade_circle);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                parent.launchEdit(getItem(position));
                return true;
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV, levelTV, pointsTV;
        ImageView imageView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing each view of our recycler view.

            titleTV = itemView.findViewById(R.id.idrow_title);
            levelTV = itemView.findViewById(R.id.idrow_level);
            pointsTV = itemView.findViewById(R.id.idrow_points);
            imageView = itemView.findViewById(R.id.idrow_img);

            // adding on click listener for each item of recycler view.

        }
    }

    public Identity getIdentityOn(int position){
        return getItem(position);
    }
}
