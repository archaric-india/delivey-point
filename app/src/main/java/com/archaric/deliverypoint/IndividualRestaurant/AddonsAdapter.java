package com.archaric.deliverypoint.IndividualRestaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.archaric.deliverypoint.R;
import com.archaric.deliverypoint.SendItemData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddonsAdapter extends RecyclerView.Adapter<AddonsAdapter.ViewHolder> {

    Map<String, Double> mapForGetValues = new HashMap<>();
    AddonsValues addonsValues;
    AddonsKey addonsKey;
    Double hereDoubleValue = 0.0;
    List<String> addonsKeyList = new ArrayList<>();

    public void setMapForGetValues(Map<String, Double> mapForGetValues) {
        this.mapForGetValues = mapForGetValues;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addons_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> keyList = new ArrayList<String>(mapForGetValues.keySet());
        ArrayList<Double> valueList = new ArrayList<Double>(mapForGetValues.values());

        holder.checkBox.setText(keyList.get(position));
        holder.valueOfCheckBox.setText(String.valueOf(valueList.get(position)));

       holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


               try {
                   addonsValues = (AddonsValues) holder.valueOfCheckBox.getContext();
               } catch (ClassCastException e) {
                   addonsValues = null;
                   e.printStackTrace();
               }

               try {
                   addonsKey = (AddonsKey) holder.valueOfCheckBox.getContext();
               } catch (ClassCastException e) {
                   addonsKey = null;
                   e.printStackTrace();
               }





               if(isChecked){
                   if (addonsValues != null & addonsKey != null) {
                       addonsKeyList.add(keyList.get(position));
                       addonsKey.sendAddonsKey(addonsKeyList);
                       hereDoubleValue += valueList.get(position);
                       addonsValues.sendAddonValue(hereDoubleValue);
                   }

               }else {
                   if (addonsValues != null & addonsKey != null) {
                       addonsKeyList.remove(keyList.get(position));
                       addonsKey.sendAddonsKey(addonsKeyList);
                       hereDoubleValue -= valueList.get(position);
                       addonsValues.sendAddonValue(hereDoubleValue);
                   }
               }
           }
       });

    }

    @Override
    public int getItemCount() {
        return mapForGetValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView valueOfCheckBox;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            valueOfCheckBox = itemView.findViewById(R.id.valueOfCheckBox);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }
}
