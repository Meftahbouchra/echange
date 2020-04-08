package com.bouchra.myapplicationechange.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Message;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mcontext;
    private ArrayList<Message> mChat;
    private String imageurl;
    Dialog MyDialog;
    FirebaseUser fuser;
    // private ArrayList<Annonce> annonces = new ArrayList<>();


    public MessageAdapter(Context mcontext, ArrayList<Message> mChat, String imageurl) {
        this.mcontext = mcontext;
        this.mChat = mChat;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View vi = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(vi);

        } else {
            View vi = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(vi);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Message message = mChat.get(position);
// holder.show_message.setText(message.getTextMessage());////////////// hadi hiya ta3 message
        Glide.with(mcontext).load(imageurl).into(holder.profile_image);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy  \n kk:mm ");
        String str = simpleDateFormat.format(message.getDateMessage());
        holder.dateh.setText(str);
        String msg = message.getTextMessage();
        if (!isUrl(msg)) {//return false
            // affiche txt message
            holder.show_message.setVisibility(View.VISIBLE);
            holder.messageIv.setVisibility(View.GONE);
            holder.show_message.setText(msg);////////////// hadi hiya ta3 message
        } else {
            //affiche photo message
            holder.show_message.setVisibility(View.GONE);
            holder.messageIv.setVisibility(View.VISIBLE);
            Picasso.get().load(msg).placeholder(R.drawable.ic_image_black).into(holder.messageIv);
            holder.messageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImage(msg);
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_image, messageIv;
        public TextView show_message;
        private TextView dateh;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            show_message = itemView.findViewById(R.id.show_message);
            dateh = itemView.findViewById(R.id.datH);
            messageIv = itemView.findViewById(R.id.messageIv);


        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getIdsender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
            // return MSG_TYPE_LEFT;
        } else {
            return MSG_TYPE_LEFT;
            //return MSG_TYPE_RIGHT;
        }
    }

    private boolean isUrl(String urlString) {
        // URL url =new URL(urlString);
        if (URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()) {
            return true;
        } else {
            return false;
        }

    }

    private void showImage(String url) {


        View view = LayoutInflater.from(mcontext).inflate(R.layout.showimage, null);
        ImageView imageView = view.findViewById(R.id.imgclik_annonce);

        Glide.with(mcontext)
                .asBitmap()
                .load(url)
                .into(imageView);


        TextView close = view.findViewById(R.id.retour);
        close.setOnClickListener(v -> {
            MyDialog.cancel();

        });

        //full screen
        MyDialog = new Dialog(mcontext, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        MyDialog.setContentView(view);
        MyDialog.show();// hadi nkd ndirha ghir f fct

    }
}
