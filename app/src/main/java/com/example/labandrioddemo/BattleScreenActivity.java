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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.example.labandrioddemo.database.AccountRepository;
import com.example.labandrioddemo.database.entities.BattleHistory;
import com.example.labandrioddemo.database.entities.ProjectCharacter;
import com.example.labandrioddemo.databinding.ActivityBattleScreenBinding;
import java.util.Random;

public class BattleScreenActivity extends AppCompatActivity {

    private ActivityBattleScreenBinding binding;
    private AccountRepository repository;
    private int monsterMaxHp;
    private int monsterCurHp;
    private static final String CHANNEL_ID = "MESSAGE_CHANNEL";
    private static final int NOTIFICATION_ID = 1;
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
                    monsterMaxHp = (character.getBattleNum()- 1) * 2 + 10;
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
                // Monster attacks player
                int monsterDmg = random.nextInt(character.getBattleNum() - 1,3 + character.getBattleNum());
                if (monsterCurHp > 0) {
                    if(character.getCurrHp() - monsterDmg >= 0) {
                        character.setCurrHp(character.getCurrHp() - monsterDmg);
                    } else {
                        character.setCurrHp(0);
                    }

                    binding.hpTextView.setText("HP: " + character.getCurrHp() + "/" + character.getMaxHp());
                }

                // Player attacks monster
                int playerDmg = random.nextInt(3,6) + character.getAtkMod();
                monsterCurHp = monsterCurHp - (playerDmg);
                if(monsterCurHp < 0) {
                    monsterCurHp = 0;
                }
                binding.enemyHpTextView.setText("Enemy HP: " + monsterCurHp + "/" + monsterMaxHp);
                if (character.getCurrHp() > 0) {
                    if (monsterCurHp > 0) {
                        binding.currentSituationTextView.setText(character.getCharacterName() + " did "  + playerDmg + " and took " + monsterDmg + " damage.");
                    } else {
                        binding.currentSituationTextView.setText("You beat the monster!");
                        binding.fleeButton.setText("Return Home");
                    }
                } else {
                    binding.currentSituationTextView.setText("You died!");
                    binding.fleeButton.setText("Game Over");
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
                if (monsterCurHp <= 0) {
                    repository.updateCharacter(character);
                    showBattleNotification("You Ween");
                    startActivity(VictoryScreenActivity.VictoryScreenIntentFactory(getApplicationContext(), character.getUserID(), character.getCharacterID()));
                } else if (character.getCurrHp() <= 0) {
                    repository.updateCharacter(character);
                    showBattleNotification("Skill Issue");
                    startActivity(GameOverScreenActivity.GameOverScreenIntentFactory(getApplicationContext(), character.getCharacterID()));
                } else {
                    int fleeRoll = random.nextInt(1,101);

                    if (fleeRoll <= character.getFleeChance()) {
                        repository.updateCharacter(character);

                        BattleHistory battleRecord = new BattleHistory(character.getCharacterID(), character.getBattleNum(), character.getCurrHp(), false);
                        repository.insertBattleHistory(battleRecord);

                        makeToast("Flee successful!");
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
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, MainActivity.mainIntentFactory(getApplicationContext()), PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Battle Decision")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }


    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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