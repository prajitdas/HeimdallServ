package edu.umbc.cs.ebiquity.android.mitrhil.hma;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class ShowAppsInstalledActivityFragment extends Fragment {

    public ShowAppsInstalledActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_apps_installed, container, false);
    }
}
