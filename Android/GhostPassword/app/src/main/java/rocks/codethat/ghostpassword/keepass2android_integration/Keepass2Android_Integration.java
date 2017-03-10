package rocks.codethat.ghostpassword.keepass2android_integration;

/**
 * Created by Sparrow on 12/4/2016.
 */

        import java.util.ArrayList;
        import java.util.HashMap;

        import keepass2android.pluginsdk.KeepassDefs;
        import keepass2android.pluginsdk.Kp2aControl;
        import android.app.Activity;
        import android.app.Fragment;
        import android.content.ActivityNotFoundException;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.Toast;

        import rocks.codethat.ghostpassword.GhostPassword;
        import rocks.codethat.ghostpassword.R;

public class Keepass2Android_Integration extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.act);

        if (savedInstanceState == null) {
            // getFragmentManager().beginTransaction()
            //        .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String GHOST_PASSWORD_PASSPHRASE = "Keepass2Android_Integration passphrase";

        public PlaceholderFragment() {
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode,
                                     Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if ((requestCode == 1) //queryEntry for own package
                    && (resultCode == RESULT_OK)) // ensure user granted access and selected something
            {
                HashMap<String, String> credentials = Kp2aControl.getEntryFieldsFromIntent(data);
                if (!credentials.isEmpty()) {
                    //here we go!
                    //Toast.makeText(getActivity(), "retrieved credenitals! Username=" + credentials.get(KeepassDefs.UserNameField), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}