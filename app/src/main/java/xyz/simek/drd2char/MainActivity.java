package xyz.simek.drd2char;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CharacterSheetFragment.OnFragmentInteractionListener,
        CharactersListFragment.OnFragmentInteractionListener {

    public ArrayList<Race> races;
    private JsonReader reader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //toolbar.setNavigationIcon(R.drawable.logo_transparent_sm);

        // Dragon icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_transparent_sm);

        races = new ArrayList<>();
        loadRaces();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String title;

        switch (id) {
            case R.id.nav_list_characters:
                switchFragment(getResources().getString(R.string.fragment_character_list), new CharactersListFragment());
                break;
            case R.id.nav_new_character:
                switchFragment(getResources().getString(R.string.menu_new_character), new CharacterSheetFragment());
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchFragment(String title, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        getSupportActionBar().setTitle(title);
    }

    public void onFragmentInteraction(boolean close)
    {
        // TODO: ...
    }

    public ArrayList<Race> getRaces() {
        return this.races;
    }

    public void onNewCharacterInteraction() {
        switchFragment(getResources().getString(R.string.menu_new_character), new CharacterSheetFragment());
    }

    public void loadRaces() {
        try {
            reader = new JsonReader(new InputStreamReader(getAssets().open("races.json"), "UTF-8"));

            reader.beginArray();
            while(reader.hasNext()) {
                reader.beginObject();

                Race race = new Race();
                while (reader.hasNext()) {
                    if (reader.peek() ==  JsonToken.END_OBJECT)
                    {
                        break;
                    }
                    if(reader.peek() == JsonToken.BEGIN_ARRAY)
                    {
                        reader.skipValue();
                        continue;
                    }
                    if(reader.peek() == JsonToken.NAME)
                    {
                        String name = reader.nextName();
                        if (name.equals("name")) {
                            race.setName(reader.nextString());
                        } else if (name.equals("description")) {
                            race.setDescription(reader.nextString());
                        } else if (name.equals("cultures")) {
                            reader.beginArray();
                            while(reader.hasNext())
                            {
                                race.addCulture(new Ability(reader.nextString(), ""));
                            }
                            reader.endArray();
                        } else if (name.equals("abilities")) {
                            reader.beginArray();
                            while(reader.hasNext())
                            {
                                reader.beginObject();
                                String ability_name = "", ability_desc = "";
                                while(reader.hasNext())
                                {
                                    String name2 = reader.nextName();
                                    if(name2.equals("name")) {
                                        ability_name = reader.nextString();
                                    } else if (name2.equals("description")) {
                                        ability_desc = reader.nextString();
                                        race.addAbility(new Ability(ability_name, ability_desc));
                                    }
                                }
                                reader.endObject();
                            }
                            reader.endArray();
                        }
                    }
                }
                reader.endObject();
                races.add(race);
            }
            reader.endArray();

        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        }

    }
}
