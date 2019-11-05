package ateneatech.room4u;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class chat extends Fragment {
    View v;
    String email,correov;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chat, container, false);

        if(getArguments() != null) {
            email = getArguments().getString("email");
            correov = getArguments().getString("correov");

        }
        Toast.makeText(getContext(),email + correov,Toast.LENGTH_SHORT).show();
        return v;
    }

}

