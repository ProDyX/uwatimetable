package com.github.marco9999.uwatimetable;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Marco on 4/07/2015.
 * Headless fragment for keeping some data which is 'large'
 */
public class HDataFragment extends Fragment {

    HClassesHttpsOlcrImporter olcr_https_client;
    DHttpsOLCRStatus olcr_https_status;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }
}


