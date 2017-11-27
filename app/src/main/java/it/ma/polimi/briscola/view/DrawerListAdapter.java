package it.ma.polimi.briscola.view;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import it.ma.polimi.briscola.MatchMenuActivity;
import it.ma.polimi.briscola.R;

/**
 * Created by utente on 25/11/17.
 */


public class DrawerListAdapter extends BaseAdapter {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;
        private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

        private MatchMenuActivity activity;
        private List<String> texts = new ArrayList<>();
        private List<Drawable> icons = new ArrayList<>();
        private Set<Integer> separatorsSet = new TreeSet<>();

        public DrawerListAdapter(MatchMenuActivity activity, Map<String,Map<String,Drawable>> drawerContent) {
            this.activity = activity;
            Set<String> separators = drawerContent.keySet();
            for(String s1 : separators){
                addSeparatorItem(s1);
                for(String s2: drawerContent.get(s1).keySet()){
                    addItem(s2, drawerContent.get(s1).get(s2));

                }
            }
        }


        private void addItem(String item, Drawable icon) {
            texts.add(item);
            icons.add(icon);
        }

        private void addSeparatorItem(String item) {
            texts.add(item);
            // save separator position
            separatorsSet.add(texts.size() - 1);
        }


        @Override
        public int getItemViewType(int position) {
            return separatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
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

            int type = getItemViewType(i);
            ItemViewHolder holderItem = null;
            SeparatorViewHolder holderSeparator =null;
            if (view == null) {

                switch (type) {
                    case TYPE_ITEM:
                        holderItem = new ItemViewHolder();
                        view = LayoutInflater.from(activity).inflate(R.layout.drawer_list_item_layout, null);
                        holderItem.text= (TextView) view.findViewById(R.id.id_text_item);
                        holderItem.icon= (ImageView) view.findViewById(R.id.id_icon_item);
                        view.setTag(holderItem);
                        break;
                    case TYPE_SEPARATOR:
                        holderSeparator = new SeparatorViewHolder();
                        view = LayoutInflater.from(activity).inflate(R.layout.drawer_list_separator_layout, null);
                        holderSeparator.text = (TextView) view.findViewById(R.id.id_text_item);
                        view.setTag(holderSeparator);
                        break;
                }

            } else {
                switch (type) {
                    case TYPE_ITEM:
                        holderItem = (ItemViewHolder) view.getTag();
                        break;
                    case TYPE_SEPARATOR:
                        holderSeparator = (SeparatorViewHolder) view.getTag();
                        break;
                }
            }

            switch (type) {
                case TYPE_ITEM:
                    holderItem.text.setText(texts.get(i));
                    holderItem.icon.setImageDrawable(icons.get(i));
                    break;
                case TYPE_SEPARATOR:
                    holderSeparator.text.setText(texts.get(i));
                    break;

            }

            return view;
        }

            //TODO IMPORTANT REMARK: the holder.icon/holder.text is a POINTER to the element in the view, hence updating it means updating the view
            //TODO KEEP IN MIND THAT IN THE CASE OF THIS SIMPLE LIST, THERE IS NO PROBLEM IN THROWING THE HOLDER AWAY ... however,
            //TODO: ho usato anche questo tutorial http://android.amberfog.com/?p=296

          /*  // initialize the Image view
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
*/
            //todo, devi modificare questo AFFINCHE' NEL CASO IN CUI UN'OPZIONE SIA STATA SELEZIOANTA, ESSA POSSA ESSERE TOGGLATA
            //todo devi modificarlo anche per aggiungere delle dropdown list (presumibilmente, dovrai fare un inflate di un'altro tipo di layout (magari quello che c'è ora lo rinomini "drawer_..._toggle_layout",
            //todo l'altro invece lo chiamit drawer_..._dropdown, e i suoi figli dropdown_sublist_element

            //return view;


        private class ItemViewHolder {
            ImageView icon;
            TextView text;
        }


        private class SeparatorViewHolder {
            TextView text;
        }

}



