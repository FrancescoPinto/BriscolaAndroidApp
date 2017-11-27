package it.ma.polimi.briscola.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import it.ma.polimi.briscola.MatchMenuActivity;
import it.ma.polimi.briscola.R;

/**
 * Created by utente on 25/11/17.
 */

public class DrawerListAdapter extends BaseAdapter {

        /** the main activity */
        private MatchMenuActivity activity;
        /** the elements lists */
        private ArrayList<String> texts;
        private ArrayList<Drawable> icons;

        public DrawerListAdapter(MatchMenuActivity _act, ArrayList<String> _elemText,
                                 ArrayList<Drawable> _icons) {
            this.activity = _act;
            this.texts = _elemText;
            this.icons = _icons;
        }

        @Override
        public int getCount() {
            return texts.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            // initialize the Image view
            ViewHolder holder = new ViewHolder();
            // define the layout to retrieve elements
            if (view == null)
                view = LayoutInflater.from(activity).inflate(R.layout.drawer_list_element_layout, viewGroup, false);
            // the icon
            holder.icon = view.findViewById(R.id.id_icon_item);
            // the text
            holder.text = view.findViewById(R.id.id_text_item);
            // set elements
            holder.icon.setImageDrawable(icons.get(i));
            holder.text.setText(texts.get(i));

            //todo, devi modificare questo AFFINCHE' NEL CASO IN CUI UN'OPZIONE SIA STATA SELEZIOANTA, ESSA POSSA ESSERE TOGGLATA
            //todo devi modificarlo anche per aggiungere delle dropdown list (presumibilmente, dovrai fare un inflate di un'altro tipo di layout (magari quello che c'è ora lo rinomini "drawer_..._toggle_layout",
            //todo l'altro invece lo chiamit drawer_..._dropdown, e i suoi figli dropdown_sublist_element

            return view;
        }

        private class ViewHolder {
            ImageView icon;
            TextView text;
        }

}



