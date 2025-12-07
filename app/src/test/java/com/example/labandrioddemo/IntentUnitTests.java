package com.example.labandrioddemo;

import static com.example.labandrioddemo.CharacterCreationActivity.COMP_DOOM_ACTIVITY_SLOT;
import static com.example.labandrioddemo.CharacterSelectActivity.COMP_DOOM_ACTIVITY_USER_ID;
import static com.example.labandrioddemo.MainMenuActivity.COMP_DOOM_ACTIVITY_CHARACTER_ID;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IntentUnitTests {
    private final int testUserId = 1;
    private final int testCharacterId = 2;
    private final Context testContext = new Context() {
        @Override
        public boolean bindService(@NonNull Intent intent, @NonNull ServiceConnection serviceConnection, int i) {
            return false;
        }

        @Override
        public int checkCallingOrSelfPermission(@NonNull String s) {
            return 0;
        }

        @Override
        public int checkCallingOrSelfUriPermission(Uri uri, int i) {
            return 0;
        }

        @Override
        public int checkCallingPermission(@NonNull String s) {
            return 0;
        }

        @Override
        public int checkCallingUriPermission(Uri uri, int i) {
            return 0;
        }

        @Override
        public int checkPermission(@NonNull String s, int i, int i1) {
            return 0;
        }

        @Override
        public int checkSelfPermission(@NonNull String s) {
            return 0;
        }

        @Override
        public int checkUriPermission(Uri uri, int i, int i1, int i2) {
            return 0;
        }

        @Override
        public int checkUriPermission(@Nullable Uri uri, @Nullable String s, @Nullable String s1, int i, int i1, int i2) {
            return 0;
        }

        @Override
        public void clearWallpaper() throws IOException {

        }

        @Override
        public Context createConfigurationContext(@NonNull Configuration configuration) {
            return null;
        }

        @Override
        public Context createContextForSplit(String s) throws PackageManager.NameNotFoundException {
            return null;
        }

        @Override
        public Context createDeviceProtectedStorageContext() {
            return null;
        }

        @Override
        public Context createDisplayContext(@NonNull Display display) {
            return null;
        }

        @Override
        public Context createPackageContext(String s, int i) throws PackageManager.NameNotFoundException {
            return null;
        }

        @Override
        public String[] databaseList() {
            return new String[0];
        }

        @Override
        public boolean deleteDatabase(String s) {
            return false;
        }

        @Override
        public boolean deleteFile(String s) {
            return false;
        }

        @Override
        public boolean deleteSharedPreferences(String s) {
            return false;
        }

        @Override
        public void enforceCallingOrSelfPermission(@NonNull String s, @Nullable String s1) {

        }

        @Override
        public void enforceCallingOrSelfUriPermission(Uri uri, int i, String s) {

        }

        @Override
        public void enforceCallingPermission(@NonNull String s, @Nullable String s1) {

        }

        @Override
        public void enforceCallingUriPermission(Uri uri, int i, String s) {

        }

        @Override
        public void enforcePermission(@NonNull String s, int i, int i1, @Nullable String s1) {

        }

        @Override
        public void enforceUriPermission(Uri uri, int i, int i1, int i2, String s) {

        }

        @Override
        public void enforceUriPermission(@Nullable Uri uri, @Nullable String s, @Nullable String s1, int i, int i1, int i2, @Nullable String s2) {

        }

        @Override
        public String[] fileList() {
            return new String[0];
        }

        @Override
        public Context getApplicationContext() {
            return null;
        }

        @Override
        public ApplicationInfo getApplicationInfo() {
            return null;
        }

        @Override
        public AssetManager getAssets() {
            return null;
        }

        @Override
        public File getCacheDir() {
            return null;
        }

        @Override
        public ClassLoader getClassLoader() {
            return null;
        }

        @Override
        public File getCodeCacheDir() {
            return null;
        }

        @Override
        public ContentResolver getContentResolver() {
            return null;
        }

        @Override
        public File getDataDir() {
            return null;
        }

        @Override
        public File getDatabasePath(String s) {
            return null;
        }

        @Override
        public File getDir(String s, int i) {
            return null;
        }

        @Nullable
        @Override
        public File getExternalCacheDir() {
            return null;
        }

        @Override
        public File[] getExternalCacheDirs() {
            return new File[0];
        }

        @Nullable
        @Override
        public File getExternalFilesDir(@Nullable String s) {
            return null;
        }

        @Override
        public File[] getExternalFilesDirs(String s) {
            return new File[0];
        }

        @Override
        public File[] getExternalMediaDirs() {
            return new File[0];
        }

        @Override
        public File getFileStreamPath(String s) {
            return null;
        }

        @Override
        public File getFilesDir() {
            return null;
        }

        @Override
        public Looper getMainLooper() {
            return null;
        }

        @Override
        public File getNoBackupFilesDir() {
            return null;
        }

        @Override
        public File getObbDir() {
            return null;
        }

        @Override
        public File[] getObbDirs() {
            return new File[0];
        }

        @Override
        public String getPackageCodePath() {
            return "";
        }

        @Override
        public PackageManager getPackageManager() {
            return null;
        }

        @Override
        public String getPackageName() {
            return "";
        }

        @Override
        public String getPackageResourcePath() {
            return "";
        }

        @Override
        public Resources getResources() {
            return null;
        }

        @Override
        public SharedPreferences getSharedPreferences(String s, int i) {
            return null;
        }

        @Override
        public Object getSystemService(@NonNull String s) {
            return null;
        }

        @Nullable
        @Override
        public String getSystemServiceName(@NonNull Class<?> aClass) {
            return "";
        }

        @Override
        public Resources.Theme getTheme() {
            return null;
        }

        @Override
        public Drawable getWallpaper() {
            return null;
        }

        @Override
        public int getWallpaperDesiredMinimumHeight() {
            return 0;
        }

        @Override
        public int getWallpaperDesiredMinimumWidth() {
            return 0;
        }

        @Override
        public void grantUriPermission(String s, Uri uri, int i) {

        }

        @Override
        public boolean isDeviceProtectedStorage() {
            return false;
        }

        @Override
        public boolean moveDatabaseFrom(Context context, String s) {
            return false;
        }

        @Override
        public boolean moveSharedPreferencesFrom(Context context, String s) {
            return false;
        }

        @Override
        public FileInputStream openFileInput(String s) throws FileNotFoundException {
            return null;
        }

        @Override
        public FileOutputStream openFileOutput(String s, int i) throws FileNotFoundException {
            return null;
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory) {
            return null;
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory, @Nullable DatabaseErrorHandler databaseErrorHandler) {
            return null;
        }

        @Override
        public Drawable peekWallpaper() {
            return null;
        }

        @Nullable
        @Override
        public Intent registerReceiver(@Nullable BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
            return null;
        }

        @Nullable
        @Override
        public Intent registerReceiver(@Nullable BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, int i) {
            return null;
        }

        @Nullable
        @Override
        public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, @Nullable String s, @Nullable Handler handler) {
            return null;
        }

        @Nullable
        @Override
        public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, @Nullable String s, @Nullable Handler handler, int i) {
            return null;
        }

        @Override
        public void removeStickyBroadcast(Intent intent) {

        }

        @Override
        public void removeStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

        }

        @Override
        public void revokeUriPermission(Uri uri, int i) {

        }

        @Override
        public void revokeUriPermission(String s, Uri uri, int i) {

        }

        @Override
        public void sendBroadcast(Intent intent) {

        }

        @Override
        public void sendBroadcast(Intent intent, @Nullable String s) {

        }

        @Override
        public void sendBroadcastAsUser(Intent intent, UserHandle userHandle) {

        }

        @Override
        public void sendBroadcastAsUser(Intent intent, UserHandle userHandle, @Nullable String s) {

        }

        @Override
        public void sendOrderedBroadcast(Intent intent, @Nullable String s) {

        }

        @Override
        public void sendOrderedBroadcast(@NonNull Intent intent, @Nullable String s, @Nullable BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s1, @Nullable Bundle bundle) {

        }

        @Override
        public void sendOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, @Nullable String s, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s1, @Nullable Bundle bundle) {

        }

        @Override
        public void sendStickyBroadcast(Intent intent) {

        }

        @Override
        public void sendStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

        }

        @Override
        public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s, @Nullable Bundle bundle) {

        }

        @Override
        public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, BroadcastReceiver broadcastReceiver, @Nullable Handler handler, int i, @Nullable String s, @Nullable Bundle bundle) {

        }

        @Override
        public void setTheme(int i) {

        }

        @Override
        public void setWallpaper(Bitmap bitmap) throws IOException {

        }

        @Override
        public void setWallpaper(InputStream inputStream) throws IOException {

        }

        @Override
        public void startActivities(Intent[] intents) {

        }

        @Override
        public void startActivities(Intent[] intents, Bundle bundle) {

        }

        @Override
        public void startActivity(Intent intent) {

        }

        @Override
        public void startActivity(Intent intent, @Nullable Bundle bundle) {

        }

        @Nullable
        @Override
        public ComponentName startForegroundService(Intent intent) {
            return null;
        }

        @Override
        public boolean startInstrumentation(@NonNull ComponentName componentName, @Nullable String s, @Nullable Bundle bundle) {
            return false;
        }

        @Override
        public void startIntentSender(IntentSender intentSender, @Nullable Intent intent, int i, int i1, int i2) throws IntentSender.SendIntentException {

        }

        @Override
        public void startIntentSender(IntentSender intentSender, @Nullable Intent intent, int i, int i1, int i2, @Nullable Bundle bundle) throws IntentSender.SendIntentException {

        }

        @Nullable
        @Override
        public ComponentName startService(Intent intent) {
            return null;
        }

        @Override
        public boolean stopService(Intent intent) {
            return false;
        }

        @Override
        public void unbindService(@NonNull ServiceConnection serviceConnection) {

        }

        @Override
        public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {

        }
    };
    private final int testSlot = 3;
    private final int DEFAULT_VALUE = -1;
    private Intent intent;

    @After
    public void resetIntentVariable() {
        intent = null;
    }

    @Test
    public void AccountCreationIntentTest() {
        intent = AccountCreationActivity.accountCreationIntentFactory(testContext);

        assertFalse(intent.hasExtra(COMP_DOOM_ACTIVITY_USER_ID));
        assertFalse(intent.hasExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID));
    }

    @Test
    public void AdminPowersIntentTest() {
        intent = AdminPowersActivity.adminPowersIntentFactory(testContext, testUserId);

        assertEquals(testUserId, intent.getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, DEFAULT_VALUE));
        assertFalse(intent.hasExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID));
    }

    @Test
    public void BattleHistoryIntentTest() {
        intent = BattleHistoryActivity.battleHistoryIntentFactory(testContext, testUserId, testCharacterId);

        assertEquals(testUserId, intent.getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, DEFAULT_VALUE));
        assertEquals(testCharacterId, intent.getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, DEFAULT_VALUE));
    }

    @Test
    public void BattleScreenIntentTest() {
        intent = BattleScreenActivity.battleScreenIntentFactory(testContext, testCharacterId);

        assertFalse(intent.hasExtra(COMP_DOOM_ACTIVITY_USER_ID));
        assertEquals(testCharacterId, intent.getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, DEFAULT_VALUE));
    }

    @Test
    public void CharacterCreationIntentTest() {
        intent = CharacterCreationActivity.characterCreationIntentFactory(testContext, testUserId, testSlot);

        assertEquals(testUserId, intent.getIntExtra(COMP_DOOM_ACTIVITY_USER_ID, DEFAULT_VALUE));
        assertEquals(testCharacterId, intent.getIntExtra(COMP_DOOM_ACTIVITY_CHARACTER_ID, DEFAULT_VALUE));
        assertEquals(testSlot, intent.getIntExtra(COMP_DOOM_ACTIVITY_SLOT, DEFAULT_VALUE));
    }
}
