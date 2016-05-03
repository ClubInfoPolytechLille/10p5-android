package com.example.app_10p5;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class main_tab_frag extends Fragment {


    public main_tab_frag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_main_tab_frag, container, false);

        final MainActivite parent = (MainActivite) getActivity();

        TabLayout tabLayout = (TabLayout) ret.findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addTab(tabLayout.newTab().setText("Connexion"));
        tabLayout.addTab(tabLayout.newTab().setText("Commande"));
        tabLayout.addTab(tabLayout.newTab().setText("Rechargement"));
        tabLayout.addTab(tabLayout.newTab().setText("Création"));
        tabLayout.addTab(tabLayout.newTab().setText("Vidange"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) ret.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(parent.getToken() != "" && System.currentTimeMillis() - parent.getTimeToken() < MainActivite.EXPIRATION){
                    viewPager.setCurrentItem(tab.getPosition());
                }
                else{
                    viewPager.setCurrentItem(tab.getPosition());    //Empeche un bug graphique
                    viewPager.setCurrentItem(0);
                    Toast.makeText(parent, "Veuillez vous connecter.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return ret;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String s);
    }
}
