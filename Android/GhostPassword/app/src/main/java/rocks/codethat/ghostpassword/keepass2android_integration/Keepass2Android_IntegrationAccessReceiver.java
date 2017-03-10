package rocks.codethat.ghostpassword.keepass2android_integration;

/**
 * Created by Sparrow on 12/4/2016.
 */

        import keepass2android.pluginsdk.Strings;

        import java.util.ArrayList;

public class Keepass2Android_IntegrationAccessReceiver
        extends keepass2android.pluginsdk.PluginAccessBroadcastReceiver {

    @Override
    public ArrayList<String> getScopes() {
        ArrayList<String> scopes = new ArrayList<String>();
        scopes.add(Strings.SCOPE_DATABASE_ACTIONS);
        scopes.add(Strings.SCOPE_CURRENT_ENTRY);
        scopes.add(Strings.SCOPE_QUERY_CREDENTIALS_FOR_OWN_PACKAGE);
        return scopes;

    }
}