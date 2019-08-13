package com.github.cdflynn.android.scrollsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.cdflynn.android.scrollsample.mock.Contact;
import com.github.cdflynn.android.scrollsample.mock.Section;

import java.util.List;
import java.util.Locale;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private static final String NAME_FORMAT = "%s %s";

    private List<Contact> mContacts;
    private LayoutInflater mInflater;
//    private ContactScrollerAdapter mContactScrollerAdapter;

    public ContactAdapter(Context c, List<Contact> contacts) {
        mContacts = contacts;
        mInflater = LayoutInflater.from(c);
//        mContactScrollerAdapter = contactScrollerAdapter;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contact = mInflater.inflate(R.layout.view_contact, parent, false);
        return new ContactViewHolder(contact);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.pic.setImageDrawable(contact.getProfileImage());
        holder.mName.setText(String.format(Locale.US, NAME_FORMAT, contact.getFirstName(), contact.getLastName()));
//        Section s = mContactScrollerAdapter.fromItemIndex(position);
        String catalog = mContacts.get(position).getFirstName().substring(0,1);
        if (position == getPositionForSection(catalog)) {
            holder.title.setText(catalog);
        } else {
            holder.title.setText("");
        }
    }
    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mContacts.get(i).getFirstName().substring(0,1);
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView pic;
        private TextView mName;

        public ContactViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_index);
            pic = (ImageView) itemView.findViewById(R.id.contact_img);
            mName = (TextView) itemView.findViewById(R.id.contact_name);
        }
    }
}
