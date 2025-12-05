package com.example.labandrioddemo;

import static com.example.labandrioddemo.MainMenuActivity.COMP_DOOM_ACTIVITY_CHARACTER_ID;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.databinding.ActivityBattleScreenBinding;
import java.util.Random;

public class BattleScreenActivity extends AppCompatActivity {

    private ActivityBattleScreenBinding binding;
    private AccountRepository repository;
    private int battleNum;
    private int monsterMaxHp;
    private int monsterCurHp;
    private static final String CHANNEL_ID = "MESSAGE_CHANNEL";
    private static final int NOTIFICATION_ID= 1;

    Random random = new Random();

    private ProjectCharacter character;
    private BattleScreenActivity thisHolder = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBattleScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = AccountRepository.getRepository(getApplication());
        createNotificationChannel();
        LiveData<ProjectCharacter> characterLiveData = repository.getCharacterByCharacterId(getIntent().getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, -1));
        characterLiveData.observe(this, new Observer<ProjectCharacter>() {
            /**
             * Creates the initial layout of the screen by pulling data related to character
             */
            @Override
            public void onChanged(ProjectCharacter character) {
                if (character != null) {
                    characterLiveData.removeObserver(this);
                    thisHolder.character = character;
                    binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                    binding.battleName.setText("Battle " + character.getBattleNum());
                    monsterMaxHp = character.getBattleNum()*2 + 10;
                    monsterCurHp = monsterMaxHp;
                    binding.enemyHpTextView.setText("Enemy HP: " + monsterCurHp + "/" + monsterMaxHp);
                    binding.currentSituationTextView.setText("A Monster Appears!");
                }
            }
        });
        //Don't put stuff here, too slow ^

        /**
         * On click listener for attack, does damage to both player and monster, checks if either are dead.
         */
        binding.attackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monsterCurHp > 0) {
                    character.setCurrHp(character.getCurrHp() - random.nextInt(character.getBattleNum(),5 + character.getBattleNum()));
                    binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                }
                monsterCurHp = monsterCurHp - ( 5 + character.getAtkMod());
                binding.enemyHpTextView.setText("Enemy HP: " + monsterCurHp + "/" + monsterMaxHp);
                if (character.getCurrHp() > 0) {
                    if (monsterCurHp > 0) {
                        binding.currentSituationTextView.setText(character.getCharacterName() + " did and took damage.");
                    } else {
                        binding.currentSituationTextView.setText("You beat the monster!");
                        showBattleNotification("You Ween");
                    }
                } else {
                    binding.currentSituationTextView.setText("You died!");
                    showBattleNotification("Skill Issue");
                }
            }
        });

        /**
         * Flee button rolls to see if you escape and tells you if you did, also move you to victory or loss
         * based on game state.
         */
        binding.fleeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chance = random.nextInt(1 + character.getFleeChance(),10);
                if (monsterCurHp <= 0) {
                    //go to victory screen
                    showBattleNotification("You Ween");
                } else if (character.getCurrHp() <= 0) {
                    //go to loss screen
                    showBattleNotification("Skill Issue");
                } else {
                    if (chance > 5) {
                        startActivity(MainMenuActivity.mainMenuIntentFactory(getApplicationContext(), character.getUserID(), character.getCharacterID()));
                    } else {
                        binding.currentSituationTextView.setText("You tried to flee and failed!");
                        character.setCurrHp(character.getCurrHp() - random.nextInt(character.getBattleNum(),5 + character.getBattleNum()));
                        binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                    }
                }
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.character_name);
            String description = "Notifications about battle outcomes";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showBattleNotification(String message) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(this, BattleScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Battle Decision")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * BattleScreenIntentFactory takes in context and characterId for accessing all the related data
     * @param context the screen
     * @param characterId the id for the selected character
     * @return the intent
     */
    static Intent BattleScreenIntentFactory(Context context, int characterId) {
        Intent intent = new Intent(context, BattleScreenActivity.class);
        intent.putExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, characterId);
        return intent;
    }
}