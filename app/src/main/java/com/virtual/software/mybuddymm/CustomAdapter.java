package com.virtual.software.mybuddymm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private int selectedPosition = -1;
    private Context context;
    private List<ListItemMoneymanagement> itemList;

    public CustomAdapter(Context context, List<ListItemMoneymanagement> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public ListItemMoneymanagement  getItem(int position) {

        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView fieldNameTextView = convertView.findViewById(R.id.fieldNameTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        RadioButton radioButton  = convertView.findViewById(R.id.radioButton);


        ListItemMoneymanagement currentItem = itemList.get(position);

//        radioButton.setChecked(position == selectedPosition);
        // Find the initially selected position (if any)

        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).isSelected()) {
                selectedPosition = i;
                break;
            }
        }

        radioButton.setChecked(position == selectedPosition);
//        radioButton.setChecked(currentItem.isSelected());
//
        fieldNameTextView.setText(currentItem.getFieldName());
        descriptionTextView.setText(currentItem.getDescription());

//        radioButton.setOnClickListener(v -> {
//            // Update the selected state of the item
//            currentItem.setSelected(true);
//            if (selectedPosition != -1 && selectedPosition != position) {
//                // Uncheck the previously selected item
//                getItem(selectedPosition).setSelected(false);
//            }
//            selectedPosition = position;
//            notifyDataSetChanged();
//        });


        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }
    public int getSelectedPosition() {
        return selectedPosition;
    }
}
