package rocks.codethat.ghostpassword.keepass2android_integration;

/**
 * Created by Sparrow on 12/4/2016.
 */

import keepass2android.pluginsdk.PluginAccessException;
import keepass2android.pluginsdk.Strings;
import rocks.codethat.ghostpassword.BluetoothChatService;
import rocks.codethat.ghostpassword.GhostPassword;
import rocks.codethat.ghostpassword.GhostPasswordException;
import rocks.codethat.ghostpassword.R;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by jeffrey on 11/23/15.
 */
public class Keepass2Android_IntegrationActionReceiver
        extends keepass2android.pluginsdk.PluginActionBroadcastReceiver {

    private BluetoothChatService mChatService = null;
    protected void startChat() throws GhostPasswordException{
        try {
            //BlueToothDao dao = new BlueToothDao(getApplicationContext());
            mChatService = BluetoothChatService.getInstance();
            if(mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
                mChatService.connect(mChatService.device, true);
            }
        }
        catch(GhostPasswordException e){
            e.printStackTrace();
            Log.i("GhostPass","Failed to create chat service");
            Toast.makeText(GhostPassword.getContext(),"Chat service fail:"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
            throw(e);
        }
    }
    @Override
    protected void actionSelected(ActionSelectedAction actionSelected) {
        if (actionSelected.isEntryAction()) {
            Toast.makeText(actionSelected.getContext(), "Keepass2Android_Integration rocks!", Toast.LENGTH_SHORT).show();
        } else {

            String fieldId = actionSelected.getFieldId().substring(Strings.PREFIX_STRING.length());
            // This shows the field you just sent. Probably a bad thing.
            //Toast.makeText(actionSelected.getContext(), actionSelected.getEntryFields().get(fieldId), Toast.LENGTH_SHORT).show();

            String password = actionSelected.getEntryFields().get(fieldId);

            Context context = actionSelected.getContext().getApplicationContext();
            try {
                startChat();
                mChatService.writeString(password);
            } catch (IOException|GhostPasswordException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void openEntry(OpenEntryAction oe) {
        try {
            Bundle bField = new Bundle();
            String password = oe.getEntryFields().get("Password");
            bField.putString("text", oe.getEntryFields().get("Password"));
            oe.addEntryFieldAction("keepass2android.plugina.bla", Strings.PREFIX_STRING + "Password", "Type through GhostPassword", R.mipmap.ic_launcher, bField);
            oe.addEntryFieldAction("keepass2android.plugina.bla", Strings.PREFIX_STRING + "UserName", "Type through GhostPassword", R.mipmap.ic_launcher, null);
            oe.addEntryFieldAction("keepass2android.plugina.bla", Strings.PREFIX_STRING + "URL", "Type through GhostPassword", R.mipmap.ic_launcher, null);
        } catch (PluginAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void dbAction(DatabaseAction db) {
        Log.d("GhostPass", db.getAction() + " in file " + db.getFileDisplayName() + " (" + db.getFilePath() + ")");
    }
}