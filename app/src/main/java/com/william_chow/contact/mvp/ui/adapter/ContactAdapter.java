package com.william_chow.contact.mvp.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.william_chow.contact.R;
import com.william_chow.contact.mvp.model.entity.Contact;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by William Chow on 14/04/2019.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private List<Contact> contactList;
    private Activity activity;

    private AdapterOnClickListener onClickListener;

    public void setOnClickListener(AdapterOnClickListener _onClickListener) {
        this.onClickListener = _onClickListener;
    }

    public ContactAdapter(Activity _activity, List<Contact> _contactList) {
        activity = _activity;
        contactList = _contactList;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_contact_item, parent, false);
        return new ContactHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact contact = contactList.get(position);
        Glide.with(activity).load("").apply(RequestOptions.bitmapTransform(new CircleCrop()).format(DecodeFormat.PREFER_ARGB_8888).placeholder(new ColorDrawable(ContextCompat.getColor(activity, R.color.orange)))).into(holder.ivContact);
        holder.tvContact.setText(contact.getFirstName() + " " + contact.getLastName());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    private void add(Contact r) {
        contactList.add(r);
        notifyItemInserted(contactList.size() - 1);
    }

    public void addContact(List<Contact> moveResults) {
        for (Contact result : moveResults) {
            add(result);
        }
    }

    public void clear() {
        int size = this.contactList.size();
        this.contactList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView ivContact;
        TextView tvContact;

        ContactHolder(View itemView) {
            super(itemView);
            ivContact = itemView.findViewById(R.id.ivContact);
            tvContact = itemView.findViewById(R.id.tvContact);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClickListener(getAdapterPosition(), contactList);
        }
    }

    public interface AdapterOnClickListener {
        void onClickListener(int _position, List<Contact> contactList);
    }
}
