package xyz.simek.drd2char;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jansimek on 07.05.16.
 */
public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.MyViewHolder> {

    private List<Character> charactersList;

    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, level;
        public ImageView race;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            level = (TextView) view.findViewById(R.id.level);
            description = (TextView) view.findViewById(R.id.description);
            race = (ImageView) view.findViewById(R.id.race);
        }
    }

    public CharactersAdapter(List<Character> charactersList, Context context) {
        this.charactersList = charactersList;

        // This has to be here in order to have access to resource images
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.character_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Character character = charactersList.get(position);
        holder.name.setText(character.getName());
        holder.level.setText("úr. 1");
        holder.description.setText(character.getDescription());
        //holder.race.setText(character.getRace());

        String race_icon;

        switch (character.getRace()) {
            case ELF:
                race_icon = "ic_race_elf";
                break;
            case HOBBIT:
                race_icon = "ic_race_hobbit";
                break;
            case KROLL:
                race_icon = "ic_race_kroll";
                break;
            case DWARF:
                race_icon = "ic_race_dwarf";
                break;
            case HUMAN:
            default:
                race_icon = "ic_race_human";
                break;
        }
        int image = context.getResources().getIdentifier(race_icon, "drawable", context.getPackageName());
        holder.race.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return charactersList.size();
    }
}
