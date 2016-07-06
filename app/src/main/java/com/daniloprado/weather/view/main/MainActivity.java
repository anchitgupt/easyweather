package com.daniloprado.weather.view.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.daniloprado.weather.R;
import com.daniloprado.weather.model.City;
import com.daniloprado.weather.view.base.BaseActivity;
import com.daniloprado.weather.view.base.ContractFragment;
import com.daniloprado.weather.view.cityadd.CityAddDialogFragment;
import com.daniloprado.weather.view.cityforecast.CityForecastFragment;
import com.daniloprado.weather.view.citylist.CityListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity
        implements CityListFragment.Contract, FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getDiComponent().inject(this);
        init();

        if (savedInstanceState == null) {
            setupCityListFragment();
        }
    }

    private void init() {
        initToolbar();
        setupUi();
    }

    private void setupUi() {
        fab.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            CityAddDialogFragment newFragment = new CityAddDialogFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        });
    }

    private void setupCityListFragment() {
        addContentFragment(new CityListFragment());
    }

    private void setupCityFragment(City city) {
        CityForecastFragment cityForecastFragment = new CityForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        cityForecastFragment.setArguments(args);
        replaceContentFragment(cityForecastFragment);
    }

    private void replaceContentFragment(ContractFragment contractFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_content, contractFragment)
                .addToBackStack(null)
                .commit();
    }

    private void addContentFragment(ContractFragment contractFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.root_content, contractFragment)
                .commit();
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.app_name));
            setSupportActionBar(toolbar);
            ActionBar bar = getSupportActionBar();
            if (bar != null) {
                bar.setDisplayShowTitleEnabled(true);
            }
            shouldDisplayHomeUp();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onCitySelected(City city) {
        setupCityFragment(city);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    @Override
    public boolean onNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    public void shouldDisplayHomeUp(){
        boolean enableHomeUp = getSupportFragmentManager().getBackStackEntryCount() > 0;
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(enableHomeUp);
        }
    }
}
