package ateneatech.room4u.Request;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ateneatech.room4u.R;

public class BottomDelete extends BottomSheetDialogFragment {
    View v;
    private BottomSheetListener bottomSheetListener;
    public BottomDelete(){}

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){

            v= inflater.inflate(R.layout.fragment_bottom_delete, container, false);
            final Button delete = v.findViewById(R.id.deleteBtn);
            Button edit = v.findViewById(R.id.EditBtn);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetListener.onOptionClick("Delete clicked...");
                    dismiss();
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetListener.onOptionClick("Edit Clicked");
                    dismiss();
                }
            });
            return v;
        }
        public interface BottomSheetListener{
            void onOptionClick(String text);
        }

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        try {
            bottomSheetListener = (BottomSheetListener) getContext();
        }
        catch (ClassCastException e){
            throw  new ClassCastException(getContext().toString());
        }
    }
}

