package francescozoccheddu.lockassist;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.widget.Toast;

class AssistSession extends VoiceInteractionSession {

    AssistSession(Context context) {
        super(context);
    }

    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        Log.d(AssistSession.class.getName(), "Handle assist");
        super.onHandleAssist(data, structure, content);
        if (AccessService.isEnabled(getContext())) {
            AccessService.fire(getContext());
        } else {
            openSettings();
        }
        finish();
    }

    private void openSettings() {
        Log.d(AssistSession.class.getName(), "Open accessibility settings");
        Intent goToSettings = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        goToSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        getContext().startActivity(goToSettings);
        Toast.makeText(getContext(), R.string.accessibility_enable_hint, Toast.LENGTH_SHORT).show();
    }

}
