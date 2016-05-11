package xyz.simek.drd2char;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CharacterSheetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CharacterSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterSheetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ArrayList<Integer> mAbilitiesSelected = new ArrayList<Integer>();

    public CharacterSheetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CharacterSheetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CharacterSheetFragment newInstance(String param1, String param2) {
        CharacterSheetFragment fragment = new CharacterSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public class AbilityAdapter extends ArrayAdapter<Ability> {
        List<Boolean> checkboxState;

        public AbilityAdapter(Context context, ArrayList<Ability> abilities) {
            super(context, 0, abilities);
            this.checkboxState = new ArrayList<Boolean>(Collections.nCopies(abilities.size(), true));

        }

        public AbilityAdapter(Context context) {
            super(context, 0);
            this.checkboxState = new ArrayList<Boolean>();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data for this position
            Ability ability = getItem(position);
            // Check if an existing view is being reused otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ability, parent, false);
            }
            TextView ability_name = (TextView) convertView.findViewById(R.id.ability_name);
            TextView ability_description = (TextView) convertView.findViewById(R.id.ability_description);
            final CheckBox ability_check = (CheckBox) convertView.findViewById(R.id.ability_check);

            if(!checkboxState.isEmpty()) {
                ability_check.setChecked(checkboxState.get(position));
            }

            ability_name.setText(ability.getName());
            ability_description.setText(ability.getDescription());
            ability_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(checkboxState.isEmpty()) {
                        checkboxState = new ArrayList<Boolean>(Collections.nCopies(getCount(), false));
                    }
                    checkboxState.set(position, !checkboxState.get(position));
                }
            });

            // Return the completed view to be rendered to the screen
            return convertView;
        }

        public Boolean isChecked(int position) {
            return checkboxState.get(position);
        }

    }

    private ArrayAdapter<CharSequence> race_adapter;
    private ArrayAdapter<CharSequence> culture_adapter;
    private AbilityAdapter ability_adapter;

    private ArrayList<Race> races;

    private Spinner race_spinner;
    private Spinner culture_spinner;
    private EditText race_special_ability;

    public void refreshRacialAbilities() {
        // Initially just show cultures for the first race
        for (Race race : races) {
            if(race.getName().equals(race_spinner.getSelectedItem().toString())) {
                culture_adapter.clear();
                for(Ability culture : race.getCultures()) {
                    culture_adapter.add(culture.getName());
                }
                ability_adapter.clear();
                for(Ability ability : race.getAbilities()) {
                    ability_adapter.add(ability);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = (View) inflater.inflate(R.layout.fragment_character_sheet, container, false);

        ImageButton gender = (ImageButton) view.findViewById(R.id.gender);
        gender.setTag("male");
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton gender = (ImageButton) v.findViewById(R.id.gender);
                int image;
                if(gender.getTag() == "male")
                {
                    gender.setTag("female");
                    image = R.drawable.ic_gender_female;
                } else {
                    gender.setTag("male");
                    image = R.drawable.ic_gender_male;
                }
                gender.setImageResource(image);
            }
        });

        race_spinner = (Spinner) view.findViewById(R.id.race);
        culture_spinner = (Spinner) view.findViewById(R.id.culture);
        race_special_ability = (EditText) view.findViewById(R.id.race_special_ability);

        races = mListener.getRaces();

        race_adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);
        culture_adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);
        ability_adapter = new AbilityAdapter(getContext());

        for(Race race : races)
        {
            race_adapter.add(race.getName());
        }

        race_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        race_spinner.setAdapter(race_adapter);

        race_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (Race race : races) {
                    if(race.getName().equals(race_spinner.getSelectedItem().toString())) {
                        refreshRacialAbilities();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        culture_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        culture_spinner.setAdapter(culture_adapter);

        refreshRacialAbilities();

        final ImageButton edit_race = (ImageButton) view.findViewById(R.id.edit_race);
        edit_race.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText new_race = new EditText(getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.create_character_add_race))
                        .setView(new_race)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!new_race.getText().toString().equals("")) {
                                    race_adapter.add(new_race.getText().toString());

                                    Spinner race = (Spinner) view.findViewById(R.id.race);
                                    race.setSelection(race_adapter.getPosition(new_race.getText().toString()));
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        });
                builder.show();
            }
        });

        final ImageButton edit_culture = (ImageButton) view.findViewById(R.id.edit_culture);
        edit_culture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText new_culture = new EditText(getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.create_character_add_culture))
                        .setView(new_culture)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!new_culture.getText().toString().equals("")) {
                                    culture_adapter.add(new_culture.getText().toString());

                                    Spinner culture = (Spinner) view.findViewById(R.id.culture);
                                    culture.setSelection(culture_adapter.getPosition(new_culture.getText().toString()));
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        });
                builder.show();
            }
        });

        ImageButton add_ability = (ImageButton) view.findViewById(R.id.add_special_ability);
        add_ability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.create_character_special_ability) + " (" + race_spinner.getSelectedItem().toString()  + ")")
                        .setAdapter(ability_adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                StringBuffer responseText = new StringBuffer();
                                race_special_ability.setText("");
                                responseText.append("The following were selected...\n");
                                for (int i = 0; i < ability_adapter.getCount(); i++) {
                                    if (ability_adapter.isChecked(i)) {
                                        responseText.append("\n" + ability_adapter.getItem(i).getName());
                                        if(!race_special_ability.getText().toString().equals("")) {
                                            race_special_ability.append("\n");
                                        }
                                        race_special_ability.append(ability_adapter.getItem(i).getName());
                                    }
                                }

                                Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();

                            /*
                            for (int ability : mAbilitiesSelected) {
                                abilities += getResources().getStringArray(R.array.abilities_human)[ability] + ", ";
                            }
                            abilities = abilities.substring(0, abilities.length()-2); // get rid of comma at the end
                            special_ability.setText(abilities);*/
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.getListView().setItemsCanFocus(false);
                ColorDrawable black = new ColorDrawable(Color.BLACK);
                dialog.getListView().setDivider(black);
                dialog.getListView().setDividerHeight(1);
                dialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });

                dialog.show();
            }
        });


        NumberPicker body = (NumberPicker) view.findViewById(R.id.pickBody);
        body.setMinValue(0);
        body.setMaxValue(100);
        body.setWrapSelectorWheel(true);

        NumberPicker soul = (NumberPicker) view.findViewById(R.id.pickSoul);
        soul.setMinValue(0);
        soul.setMaxValue(100);
        soul.setWrapSelectorWheel(true);

        NumberPicker influence = (NumberPicker) view.findViewById(R.id.pickInfluence);
        influence.setMinValue(0);
        influence.setMaxValue(100);
        influence.setWrapSelectorWheel(true);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(boolean close);
        ArrayList<Race> getRaces();
    }
}
